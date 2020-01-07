package nl.ns.barcode_tester.activity.scanner.scanners;

/**
 * Created by joelhaasnoot on 04/11/2016.
 */

public interface TicketResultCallback {

    void onNewTicketScanned(byte[] bytes, String text, boolean isZxing);

}
