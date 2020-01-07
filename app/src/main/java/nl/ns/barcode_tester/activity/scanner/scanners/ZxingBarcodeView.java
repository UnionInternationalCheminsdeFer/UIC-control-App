package nl.ns.barcode_tester.activity.scanner.scanners;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.Collection;
import java.util.List;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 04/11/2016.
 */
public class ZxingBarcodeView extends FrameLayout implements BarcodeScannerView {

    private BarcodeView barcodeView;
    private ViewfinderView viewFinder;
    private Button viewfinderButton;
    private boolean isRunning;
    private TicketResultCallback ticketCallback;

    public ZxingBarcodeView(Context context) {
        super(context);
        init();
    }

    public ZxingBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_barcode_zxing, this, true);
        barcodeView = (BarcodeView) findViewById(R.id.zxing_barcode_surface);

        viewFinder = (ViewfinderView) findViewById(R.id.zxing_viewfinder_view);
        viewfinderButton = (Button) findViewById(R.id.button_viewfinder);

        viewFinder.setCameraPreview(barcodeView);
    }

    public void pause() {
        barcodeView.stopDecoding();
        barcodeView.pause();
        isRunning = false;
        viewfinderButton.setText(getContext().getString(R.string.label_pause));
        viewfinderButton.setBackgroundColor((getResources().getColor(R.color.slightly_transparent)));
    }

    public void resume() {
        barcodeView.decodeSingle(barcodeCallback);
        barcodeView.resume();
        isRunning = true;
        viewfinderButton.setText(null);
        viewfinderButton.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setOnClickListener(OnClickListener l) {
        viewfinderButton.setOnClickListener(l);
    }

    public void startDecoding(Collection<BarcodeFormat> formats, TicketResultCallback callback) {
        barcodeView.setDecoderFactory(new DefaultDecoderFactory(formats, null, null));
        this.ticketCallback = callback;
        barcodeView.decodeSingle(barcodeCallback);
    }

    public void setTorch(boolean on) {
        barcodeView.setTorch(on);
    }

    private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            ticketCallback.onNewTicketScanned(result.getRawBytes(), result.getText(), true);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            for (ResultPoint point : resultPoints) {
                viewFinder.addPossibleResultPoint(point);
            }
        }
    };

}
