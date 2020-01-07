package nl.ns.barcode_tester.activity.scanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat; // TODO: Remove this reference to make replacing framework even easier

import org.open918.lib.UicTicketParser;
import org.open918.lib.domain.Ticket;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.ShareClickListener;
import nl.ns.barcode_tester.activity.scanner.scanners.BarcodeScannerView;
import nl.ns.barcode_tester.activity.scanner.scanners.ManateeBarcodeView;
import nl.ns.barcode_tester.activity.scanner.scanners.TicketResultCallback;
import nl.ns.barcode_tester.activity.scanner.scanners.ZxingBarcodeView;
import nl.ns.barcode_tester.database.TicketDatabase;
import nl.ns.barcode_tester.domain.ScanResult;
import nl.ns.barcode_tester.util.CameraUtil;


public class ScannerFragment extends Fragment implements ScannerActivity.OnCameraPermission {

    private static final String TAG = ScannerFragment.class.getSimpleName();

    private BarcodeScannerView barcodeView;

    private static final List<BarcodeFormat> FORMATS = Arrays.asList(BarcodeFormat.AZTEC, BarcodeFormat.PDF_417);
    private Vibrator vibrator;
    private TicketDatabase db;
    private SharedPreferences sharedPref;
    private RelativeLayout scannerHolder;

    private String currentScanner;
    private VolumeKeyListener keyListener;

    public static ScannerFragment getInstance() {
        return new ScannerFragment();
    }

    public ScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        db = new TicketDatabase(getContext());
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        keyListener = new ScannerFragment.VolumeKeyListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scanner, container, false);
        scannerHolder = (RelativeLayout) v.findViewById(R.id.layout_scanner);
        setupScanner(scannerHolder);

        return v;
    }

    private void setupScanner(RelativeLayout layout) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (barcodeView != null) {
            barcodeView.pause();
            layout.removeAllViews(); // Clear
        }
        if (sharedPref.getString("scanner", "MANATEE").equalsIgnoreCase("MANATEE")) {
            ManateeBarcodeView barcode = new ManateeBarcodeView(getActivity());
            layout.addView(barcode, params);
            barcodeView = barcode;
            currentScanner = "MANATEE";
        } else {
            ZxingBarcodeView barcode = new ZxingBarcodeView(getActivity());
            layout.addView(barcode, params);
            barcodeView = barcode;
            currentScanner = "ZXING";
        }

        // We'll likely have to repeat this if we setup a new scanner, so just put it here
        barcodeView.setOnClickListener(new ScannerFragment.ViewfinderClickListener());

        if (CameraUtil.hasPermission(getActivity())) {
            barcodeView.startDecoding(FORMATS, ticketCallback);
        }
        barcodeView.setOnClickListener(new ViewfinderClickListener());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof ScannerActivity) {
            ((ScannerActivity) getActivity()).registerForCameraPermission(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof ScannerActivity) {
            ((ScannerActivity) getActivity()).unregisterForCameraPermission();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!sharedPref.getString("scanner", "MANATEE").contentEquals(currentScanner)) {
            setupScanner(scannerHolder);
        }
        barcodeView.resume();
    }

    // Seperate method because this could also be called from other places
    private void handleBarcode(byte[] contents, boolean isZxing) {
        if (contents != null) {
            vibrator.vibrate(200);

            Ticket t = null;
            try {
                t = UicTicketParser.decode(contents, isZxing);
            } catch (Exception e) {
                Log.e(TAG, "Error reading data", e);
            }
            ScanResult r = new ScanResult(contents, new String(contents), t);

            if (r.isReadable()) {
                db.open();
                db.insertTicket(r.getRaw(), r.getTicket());
                db.close();

                if (getActivity() != null && getActivity() instanceof ScannerActivity) {
                    ((ScannerActivity) getActivity()).onScanEvent();
                }
            } else {
                showErrorDialog(r);
            }

        }
    }

    private TicketResultCallback ticketCallback = new TicketResultCallback() {
        @Override
        public void onNewTicketScanned(byte[] bytes, String text, boolean isZxing) {
            if (isZxing) {
                handleBarcode(text.getBytes(), true);
            } else {
                handleBarcode(bytes, false);
            }
            barcodeView.pause();
        }
    };

    private void showErrorDialog(ScanResult r) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_barcode_error)
                .setMessage(r.isUicTicket() ? R.string.text_barcode_error : R.string.text_barcode_error_not_uic)
                .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(R.string.label_share, new ShareClickListener(getActivity(), r))
                .show();
    }

    @Override
    public void onCameraPermission(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Starting camera after receiving permission");
            setupScanner(scannerHolder);
            barcodeView.resume();
        } else {
            Log.i(TAG, "Permission was denied to camera");
            barcodeView.pause();
        }
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (keyListener != null) {
            return keyListener.onKey(null, keyCode, event);
        }
        return false;
    }

    private class ViewfinderClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (barcodeView.isRunning()) {
                barcodeView.pause();
            } else {
                if (CameraUtil.hasPermission(getActivity())) {
                    barcodeView.resume();
                } else {
                    CameraUtil.requestCameraPermission(getActivity());
                }
            }
        }
    }

    private class VolumeKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_FOCUS:
                case KeyEvent.KEYCODE_CAMERA:
                    // Handle these events so they don't launch the Camera app
                    return true;
                // Use volume up/down to turn on light
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    barcodeView.setTorch(false);
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    barcodeView.setTorch(true);
                    return true;
            }
            return false;
        }
    }
}
