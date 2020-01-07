package nl.ns.barcode_tester.activity.keys;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.open918.lib.domain.Carrier;
import org.open918.lib.services.CarrierService;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.database.CursorRecyclerViewAdapter;
import nl.ns.barcode_tester.database.KeyDatabase;
import nl.ns.barcode_tester.domain.Country;
import nl.ns.barcode_tester.domain.Key;

/**
 * Created by joelhaasnoot on 21/10/2016.
 */

public class KeyAdapter extends CursorRecyclerViewAdapter<KeyAdapter.ViewHolder> {

    private static final String TAG = KeyAdapter.class.getSimpleName();

    private final Context ctx;
    private OnKeyClickListener onKeyClickListener;
    private CarrierService carrierService = new CarrierService();

    public KeyAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.ctx = context;
    }

    public void setOnKeyClickListener(OnKeyClickListener listener) {
        this.onKeyClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView carrier;
        TextView keyId;
        ImageView flag;
        View parent;

        public ViewHolder(View itemView) {
            super(itemView);
            carrier = (TextView) itemView.findViewById(R.id.field_key_carrier);
            keyId = (TextView) itemView.findViewById(R.id.field_key_id);
            flag = (ImageView) itemView.findViewById(R.id.image_flag);
            parent = itemView;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final Key key = KeyDatabase.keyFromCursor(cursor);
        viewHolder.carrier.setText(String.valueOf(key.getCarrier()) + "(?)");

        final Carrier c = carrierService.getCarrier(key.getCarrier());
        if (c != null) {
            viewHolder.carrier.setText(c.getLabelShort());
            try {
                Country cntry = Country.valueOf(c.getCountry());
                if (cntry.getFlagResource() != 0) {
                    viewHolder.flag.setImageDrawable(ctx.getResources().getDrawable(cntry.getFlagResource()));
                }
            } catch (IllegalArgumentException ignore) {
                Log.w(TAG, "Failed to find flag for country "+c.getCountry());
            }

            View.OnClickListener carrier = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CarrierDialogFactory.createDialog(ctx, c);
                }
            };
            viewHolder.flag.setOnClickListener(carrier);
            viewHolder.carrier.setOnClickListener(carrier);
        } else {
            Log.w(TAG, "Failed to find carrier for RICS code "+key.getCarrier());
        }


        viewHolder.keyId.setText(String.valueOf(key.getKeyIdentifier()));
        viewHolder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onKeyClickListener != null) {
                    onKeyClickListener.onKeyLongClickListener(key.getId(), key);
                }
                return true;
            }
        });
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_key, parent, false);
        return new ViewHolder(itemView);
    }

    public interface OnKeyClickListener {
        void onKeyClickListener(int id, Key k);
        void onKeyLongClickListener(int id, Key k);
    }
}
