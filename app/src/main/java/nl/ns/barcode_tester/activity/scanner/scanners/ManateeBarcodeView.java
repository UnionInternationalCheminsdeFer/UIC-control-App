package nl.ns.barcode_tester.activity.scanner.scanners;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.manateeworks.BarcodeScanner;
import com.manateeworks.CameraManager;
import com.manateeworks.MWOverlay;

import org.open918.lib.UicTicketParser;
import org.open918.lib.domain.Ticket;

import java.io.IOException;
import java.util.Collection;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 04/11/2016.
 */
public class ManateeBarcodeView extends FrameLayout implements BarcodeScannerView, SurfaceHolder.Callback {

    private static final String TAG = ManateeBarcodeView.class.getSimpleName();
    public static final String API_KEY = "<enter key here>";

    public static int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    public static final OverlayMode OVERLAY_MODE = OverlayMode.OM_MWOVERLAY;
    public static final int USE_RESULT_TYPE = BarcodeScanner.MWB_RESULT_TYPE_MW;
    // !!! Rects are in format: x, y, width, height !!!
    public static final Rect RECT_FULL_2D = new Rect(20, 5, 60, 90);

    public static final int ID_AUTO_FOCUS = 0x01;
    public static final int ID_DECODE = 0x02;
    public static final int ID_RESTART_PREVIEW = 0x04;
    public static final int ID_DECODE_SUCCEED = 0x08;
    public static final int ID_DECODE_FAILED = 0x10;

    private Handler decodeHandler;

    private boolean isRunning; // TODO: remove
    State state = State.STOPPED;

    private int activeThreads = 0;

    private static int zoomLevel = 0;
    private int firstZoom = 150;
    private int secondZoom = 300;
    boolean flashOn = false;

    private TicketResultCallback barcodeCallback;

    private ImageButton zoomButton;
    private ImageButton flashButton;
    private SurfaceHolder surfaceHolder;
    private Button pauseButton;

    private boolean hasSurface;
    private boolean cameraIsInit = false;
    private SurfaceView surfaceView;

    private enum OverlayMode {
        OM_IMAGE, OM_MWOVERLAY, OM_NONE
    }

    private enum State {
        STOPPED, PREVIEW, DECODING
    }

    public Handler getHandler() {
        return decodeHandler;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("Init Camera", "On Surface changed");
        initCamera();
        cameraIsInit = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
        cameraIsInit = false;
    }


    public ManateeBarcodeView(Context context) {
        super(context);
        init();
    }

    public ManateeBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_barcode_manatee, this, true);
        registerSdk();
        setupScanning();

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);

        zoomButton = (ImageButton) findViewById(R.id.zoomButton);
        zoomButton.setOnClickListener(new ZoomClickListener());
        pauseButton = (Button) findViewById(R.id.pauseButton);

    }

    private void setupScanning() {
        // Our sample app is configured by default to search both
        // directions...
        BarcodeScanner.MWBsetDirection(BarcodeScanner.MWB_SCANDIRECTION_HORIZONTAL | BarcodeScanner.MWB_SCANDIRECTION_VERTICAL);
        // Our sample app is configured by default to search all supported
        // barcodes...
        BarcodeScanner.MWBsetActiveCodes(BarcodeScanner.MWB_CODE_MASK_AZTEC); /*  | BarcodeScanner.MWB_CODE_MASK_PDF*/

        // set the scanning rectangle based on scan direction(format in pct:
        // x, y, width, height)
        BarcodeScanner.MWBsetScanningRect(BarcodeScanner.MWB_CODE_MASK_AZTEC, RECT_FULL_2D);


        if (OVERLAY_MODE == OverlayMode.OM_IMAGE) {
            ImageView imageOverlay = (ImageView) findViewById(R.id.imageOverlay);
            imageOverlay.setVisibility(View.VISIBLE);
        }

        // set decoder effort level (1 - 5)
        // for live scanning scenarios, a setting between 1 to 3 will suffice
        // levels 4 and 5 are typically reserved for batch scanning
        BarcodeScanner.MWBsetLevel(5);
        BarcodeScanner.MWBsetResultType(USE_RESULT_TYPE);

        CameraManager.init(getContext());

        hasSurface = false;
        state = State.STOPPED;
        decodeHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what) {
                    case ID_DECODE:
                        decode((byte[]) msg.obj, msg.arg1, msg.arg2);
                        break;

                    case ID_AUTO_FOCUS:
                        // When one auto focus pass finishes, start another. This isthe
                        // closest thing to continuous AF. It does seem to hunt a bit, but I'm not
                        // sure what else to do.
                        if (state == State.PREVIEW || state == State.DECODING) {
                            CameraManager.get().requestAutoFocus(decodeHandler, ID_AUTO_FOCUS);
                        }
                        break;
                    case ID_RESTART_PREVIEW:
                        restartPreviewAndDecode();
                        break;
                    case ID_DECODE_SUCCEED:
                        state = State.STOPPED;
                        handleDecode((BarcodeScanner.MWResult) msg.obj);

                        break;
                    case ID_DECODE_FAILED:
                        // We're decoding as fast as possible, so when one decode
                        // fails,
                        // start another.
                        break;

                }

                return false;
            }
        });
    }

    public void pause() {
        isRunning = false;
        if (OVERLAY_MODE == OverlayMode.OM_MWOVERLAY) {
            MWOverlay.removeOverlay();
        }

        if (hasSurface && cameraIsInit) {
            CameraManager.get().stopPreview();
            CameraManager.get().closeDriver();
        }
        state = State.STOPPED;

        pauseButton.setVisibility(VISIBLE);

        flashOn = false;
        updateFlash();
    }

    public void resume() {
        isRunning = true;
        if (surfaceView != null) {
            surfaceHolder = surfaceView.getHolder();
        }

        pauseButton.setVisibility(GONE);

        if (OVERLAY_MODE == OverlayMode.OM_MWOVERLAY) {
            MWOverlay.addOverlay(getContext(), surfaceView);
        }

        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore surfaceCreated() won't be called, so init the camera here.
            Log.i("Init Camera", "On resume");
            initCamera();
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setOnClickListener(OnClickListener l) {
        surfaceView.setOnClickListener(l);
        pauseButton.setOnClickListener(l);
    }

    public void startDecoding(Collection<BarcodeFormat> formats, TicketResultCallback callback) {
        this.barcodeCallback = callback;
        resume();
    }

    public void setTorch(boolean on) {
        CameraManager.get().setTorch(flashOn);
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void registerSdk() {
        int registerResult = BarcodeScanner.MWBregisterSDK(API_KEY, getActivity());

        switch (registerResult) {
            case BarcodeScanner.MWB_RTREG_OK:
                Log.i("MWBregisterSDK", "Registration OK");
                break;
            case BarcodeScanner.MWB_RTREG_INVALID_KEY:
                Log.e("MWBregisterSDK", "Registration Invalid Key");
                break;
            case BarcodeScanner.MWB_RTREG_INVALID_CHECKSUM:
                Log.e("MWBregisterSDK", "Registration Invalid Checksum");
                break;
            case BarcodeScanner.MWB_RTREG_INVALID_APPLICATION:
                Log.e("MWBregisterSDK", "Registration Invalid Application");
                break;
            case BarcodeScanner.MWB_RTREG_INVALID_SDK_VERSION:
                Log.e("MWBregisterSDK", "Registration Invalid SDK Version");
                break;
            case BarcodeScanner.MWB_RTREG_INVALID_KEY_VERSION:
                Log.e("MWBregisterSDK", "Registration Invalid Key Version");
                break;
            case BarcodeScanner.MWB_RTREG_INVALID_PLATFORM:
                Log.e("MWBregisterSDK", "Registration Invalid Platform");
                break;
            case BarcodeScanner.MWB_RTREG_KEY_EXPIRED:
                Log.e("MWBregisterSDK", "Registration Key Expired");
                break;

            default:
                Log.e("MWBregisterSDK", "Registration Unknown Error");
                break;
        }
    }

    private void initCamera() {
        try {
            // Select desired camera resolution. Not all devices supports all
            // resolutions, closest available will be chosen.
            // If not selected, closest match to screen resolution will be chosen
            // High resolutions will slow down scanning proccess on slower devices

            if (MAX_THREADS > 2) {
                CameraManager.setDesiredPreviewSize(1280, 720);
            } else {
                CameraManager.setDesiredPreviewSize(800, 480);
            }

            CameraManager.get().openDriver(surfaceHolder,
                    (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT));

            int maxZoom = CameraManager.get().getMaxZoom();
            if (maxZoom < 100) {
                zoomButton.setVisibility(View.GONE);
            } else {
                zoomButton.setVisibility(View.VISIBLE);
                if (maxZoom < 300) {
                    secondZoom = maxZoom;
                    firstZoom = (maxZoom - 100) / 2 + 100;
                }

            }
        } catch (IOException | RuntimeException ioe) {
            Log.e(TAG, ioe.getMessage());
            return;
        }
        Log.i("preview", "start preview.");

        flashOn = false;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                switch (zoomLevel) {
                    case 0:
                        CameraManager.get().setZoom(100);
                        break;
                    case 1:
                        CameraManager.get().setZoom(firstZoom);
                        break;
                    case 2:
                        CameraManager.get().setZoom(secondZoom);
                        break;
                    default:
                        break;
                }
            }
        }, 300);
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
        updateFlash();

    }

    private void decode(final byte[] data, final int width, final int height) {

        if (activeThreads >= MAX_THREADS || state == State.STOPPED) {
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                activeThreads++;
                // Log.i("Active threads", String.valueOf(activeThreads));
                long start = System.currentTimeMillis();
                byte[] rawResult = null;

                rawResult = BarcodeScanner.MWBscanGrayscaleImage(data, width, height);

                if (state == State.STOPPED) {
                    activeThreads--;
                    return;
                }

                BarcodeScanner.MWResult mwResult = null;

                if (rawResult != null
                        && BarcodeScanner.MWBgetResultType() == BarcodeScanner.MWB_RESULT_TYPE_MW) {

                    BarcodeScanner.MWResults results = new BarcodeScanner.MWResults(
                            rawResult);

                    if (results.count > 0) {
                        mwResult = results.getResult(0);
                        rawResult = mwResult.bytes;
                    }

                } else if (rawResult != null
                        && BarcodeScanner.MWBgetResultType() == BarcodeScanner.MWB_RESULT_TYPE_RAW) {
                    mwResult = new BarcodeScanner.MWResult();
                    mwResult.bytes = rawResult;
                    mwResult.text = rawResult.toString();
                    mwResult.type = BarcodeScanner.MWBgetLastType();
                    mwResult.bytesLength = rawResult.length;
                }

                if (mwResult != null) {
                    state = State.STOPPED;

                    Message message = Message.obtain(ManateeBarcodeView.this.getHandler(), ID_DECODE_SUCCEED, mwResult);
                    message.arg1 = mwResult.type;
                    message.sendToTarget();

                } else {
                    Message message = Message.obtain(ManateeBarcodeView.this.getHandler(), ID_DECODE_FAILED);
                    message.sendToTarget();
                }
                activeThreads--;
            }
        }).start();
    }

    public void handleDecode(BarcodeScanner.MWResult result) {

        if (result.locationPoints != null && CameraManager.get().getCurrentResolution() != null
                && OVERLAY_MODE == OverlayMode.OM_MWOVERLAY) {
            MWOverlay.showLocation(result.locationPoints.points, result.imageWidth, result.imageHeight);
        }

        if (result != null && barcodeCallback != null && result.type >= 0 && result.bytes != null) {
            barcodeCallback.onNewTicketScanned(result.bytes, result.text, false);
        } else {
            decodeHandler.sendEmptyMessage(ID_RESTART_PREVIEW);
        }
    }

    private void restartPreviewAndDecode() {
        if (state == State.STOPPED) {
            state = State.PREVIEW;
            Log.i("preview", "requestPreviewFrame.");
            CameraManager.get().requestPreviewFrame(getHandler(), ID_DECODE);
            CameraManager.get().requestAutoFocus(getHandler(), ID_AUTO_FOCUS);
        }
    }

    private void updateFlash() {

        CameraManager.get().setTorch(flashOn);
    }

    private static class ZoomClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            zoomLevel++;
            if (zoomLevel > 2) {
                zoomLevel = 0;
            }

            switch (zoomLevel) {
                case 0:
                    CameraManager.get().setZoom(100);
                    break;
                case 1:
                    CameraManager.get().setZoom(150);
                    break;
                case 2:
                    CameraManager.get().setZoom(300);
                    break;

                default:
                    break;
            }

        }
    }
}
