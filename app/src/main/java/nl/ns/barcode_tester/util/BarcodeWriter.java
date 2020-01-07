package nl.ns.barcode_tester.util;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joel Haasnoot on 18/10/15.
 */
public class BarcodeWriter {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    public static final int AZTEC_LAYERS = 17;
    public static final int ERROR_CORRECTION = 23;

    public static Bitmap createBarcode(String input) {
        return matrixToBitmap(stringToMatrix(input));
    }

    private static BitMatrix stringToMatrix(String input) {
        return stringToMatrix(input, AZTEC_LAYERS, ERROR_CORRECTION);
    }

    private static BitMatrix stringToMatrix(String input, int aztec_layers, int error_correction) {
        AztecWriter writer = new AztecWriter();
        Map<EncodeHintType, Integer> hints = new HashMap<>();
        hints.put(EncodeHintType.AZTEC_LAYERS, aztec_layers);
        hints.put(EncodeHintType.ERROR_CORRECTION, error_correction);
        return writer.encode(input, BarcodeFormat.AZTEC, 900, 900, hints);
    }

    private static Bitmap matrixToBitmap(BitMatrix input) {
        int width = input.getWidth();
        int height = input.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = input.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        output.setPixels(pixels, 0, width, 0, 0, width, height);
        return output;
    }
}
