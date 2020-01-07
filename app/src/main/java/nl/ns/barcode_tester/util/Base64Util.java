package nl.ns.barcode_tester.util;

import android.util.Base64;

import nl.ns.barcode_tester.domain.ScanResult;

/**
 * Created by joelhaasnoot on 21/10/2016.
 */

public class Base64Util {
    public static String getAsBase64(ScanResult result) {
        return Base64.encodeToString(result.getRaw(), Base64.DEFAULT);
    }

    public static String getAsBase64(byte[] raw) {
        return Base64.encodeToString(raw, Base64.DEFAULT);
    }

    public static byte[] getBytes(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }
}
