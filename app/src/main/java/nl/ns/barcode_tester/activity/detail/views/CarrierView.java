package nl.ns.barcode_tester.activity.detail.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.open918.lib.domain.Carrier;
import org.open918.lib.services.CarrierService;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.keys.CarrierDialogFactory;
import nl.ns.barcode_tester.domain.Country;

/**
 * Created by joelhaasnoot on 02/12/2016.
 */

public class CarrierView extends RelativeLayout {
    private static final String TAG = CarrierView.class.getSimpleName();

    private View carrierCell;
    private ImageView barcodeCarrierFlag;
    private static CarrierService carrierService = new CarrierService();
    private TextView shortLabel;

    public CarrierView(Context context) {
        super(context);
        init();
    }

    public CarrierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CarrierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CarrierView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        carrierCell = LayoutInflater.from(getContext()).inflate(R.layout.view_carrier, this, true);
        barcodeCarrierFlag = (ImageView) carrierCell.findViewById(R.id.image_flag);
        shortLabel = (TextView) carrierCell.findViewById(R.id.barcode_carrier);
    }


    public void setCarrier(int ricsCode) {
        final Carrier c = carrierService.getCarrier(ricsCode);
        if (c != null) {
            shortLabel.setText(c.getLabelShort());
            try {
                Country cntry = Country.valueOf(c.getCountry());
                if (cntry.getFlagResource() != 0) {
                    barcodeCarrierFlag.setImageDrawable(getResources().getDrawable(cntry.getFlagResource()));
                }
            } catch (IllegalArgumentException ignore) {
                Log.w(TAG, "Failed to find flag for country "+c.getCountry());
            }
            carrierCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CarrierDialogFactory.createDialog(getContext(), c);
                }
            });
        } else {
            shortLabel.setText(ricsCode + " " + getResources().getString(R.string.error_unknown_carrier_short));
            Log.w(TAG, "Failed to find carrier for RICS code "+ricsCode);
        }
    }


}
