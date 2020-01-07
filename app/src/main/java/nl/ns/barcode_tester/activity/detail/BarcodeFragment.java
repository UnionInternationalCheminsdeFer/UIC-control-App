package nl.ns.barcode_tester.activity.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.util.BarcodeWriter;

/**
 * Created by Joel Haasnoot on 18/10/15.
 */
public class BarcodeFragment extends BaseTicketFragment implements TabFragment {

    private static final String TAG = BarcodeFragment.class.getSimpleName();

    public static Fragment getInstance() {
        return new BarcodeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getTicket();

        View fragment = inflater.inflate(R.layout.fragment_barcode, container, false);

        ImageView barcode = (ImageView) fragment.findViewById(R.id.image_barcode);
        Bitmap image = createBarcode(result.getContents());
        if (image == null) {
            TextView error = (TextView) fragment.findViewById(R.id.label_error_barcode);
            error.setVisibility(View.VISIBLE);
            error.setText(getString(R.string.text_error_creating_barcode));
        }
        barcode.setImageBitmap(image);

        return fragment;
    }

    private Bitmap createBarcode(String encode) {
        try {
            return BarcodeWriter.createBarcode(encode);
        } catch (Exception e) {
            Log.e(TAG, "Error creating barcode ", e);
        }
        return null;
    }


    @Override
    public int getTitleResource() {
        return R.string.tab_barcode;
    }
}
