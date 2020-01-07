package nl.ns.barcode_tester.activity.scanner.scanners;

import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeCallback;

import java.util.Collection;

/**
 * Created by joelhaasnoot on 04/11/2016.
 */

public interface BarcodeScannerView {

    void startDecoding(Collection<BarcodeFormat> formats, TicketResultCallback callback);
    void setOnClickListener(View.OnClickListener l);
    void setTorch(boolean on);

    void pause();
    void resume();
    boolean isRunning();
}
