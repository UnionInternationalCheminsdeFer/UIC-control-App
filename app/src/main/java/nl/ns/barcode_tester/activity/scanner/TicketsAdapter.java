package nl.ns.barcode_tester.activity.scanner;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.open918.lib.UicTicketParser;
import org.open918.lib.domain.Carrier;
import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.TicketStandard;
import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;
import org.open918.lib.services.CarrierService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.database.CursorRecyclerViewAdapter;
import nl.ns.barcode_tester.database.TicketDatabase;
import nl.ns.barcode_tester.domain.ScanResult;

/**
 * Created by joelhaasnoot on 21/10/2016.
 */

public class TicketsAdapter extends CursorRecyclerViewAdapter<TicketsAdapter.ViewHolder> {

    private static final String TAG = TicketsAdapter.class.getSimpleName();
    private final Context context;

    private OnTicketClickListener onTicketClickListener;

    public TicketsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    public void setOnTicketClickListener(OnTicketClickListener listener) {
        this.onTicketClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        TextView time;
        View parent;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.field_ticket_title);
            description = (TextView) itemView.findViewById(R.id.field_ticket_description);
            time = (TextView) itemView.findViewById(R.id.field_date_time);
            parent = itemView;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final int id = cursor.getInt(cursor.getColumnIndex("_id"));
        ScanResult sr = TicketDatabase.fromCursor(cursor);
        if (sr != null && sr.getTicket() != null) {
            if (sr.hasOrderNumber()) {
                viewHolder.title.setText(((Ticket918Dash3) sr.getTicket()).getHeader().getOrderNumber().replace(" ", ""));
            } else if (sr.getTicket().getStandard() == TicketStandard.TICKET918_2) {
                viewHolder.title.setText(R.string.label_ticket918_2);
            } else {
                viewHolder.title.setText(R.string.error_unknown);
            }

            if (sr.getTicket().getStandard() == TicketStandard.TICKET918_3) {
                Carrier c = new CarrierService().getCarrier(((Ticket918Dash3) sr.getTicket()).getRicsCode());
                viewHolder.description.setVisibility(View.VISIBLE);
                setCarrier(viewHolder, c);
            } else if (sr.getTicket().getStandard() == TicketStandard.TICKET918_2) {
                viewHolder.description.setVisibility(View.VISIBLE);
                Carrier c = new CarrierService().getCarrier(((Ticket918Dash2) sr.getTicket()).getIssuerCode());
                setCarrier(viewHolder, c);
            } else {
                viewHolder.description.setVisibility(View.GONE);
            }

            final Ticket ticketClick = sr.getTicket();
            setDate(viewHolder, cursor.getString(cursor.getColumnIndex(TicketDatabase.COLUMN_UPDATED)));

            viewHolder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTicketClickListener.onTicketClick(id, ticketClick);
                }
            });
        }
    }

    private void setCarrier(ViewHolder viewHolder, Carrier c) {
        if (c != null) {
            viewHolder.description.setText(c.getLabelShort());
        } else {
            viewHolder.description.setText(R.string.error_unknown_carrier);
        }
    }

    private void setDate(ViewHolder viewHolder, String dateTime) {
        Long millis = null;
        try {
            millis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (millis != null) {
            viewHolder.time.setText(DateUtils.getRelativeTimeSpanString(context, millis, false));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ticket, parent, false);
        return new ViewHolder(itemView);
    }

    public interface OnTicketClickListener {
        void onTicketClick(int id, Ticket t);
    }
}
