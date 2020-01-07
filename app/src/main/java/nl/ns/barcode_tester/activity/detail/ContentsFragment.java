package nl.ns.barcode_tester.activity.detail;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.ShareClickListener;
import nl.ns.barcode_tester.activity.table.FieldsTableActivity;

/**
 * Created by Joel Haasnoot on 18/10/15.
 */
public class ContentsFragment extends BaseTicketFragment implements TabFragment {

    public static Fragment getInstance() {
        return new ContentsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getTicket();

        View frg = inflater.inflate(R.layout.fragment_contents, container, false);

        TextView ticketContents = (TextView) frg.findViewById(R.id.label_ticket_contents);
        try {
            ticketContents.setText(converter.toText());
            ticketContents.setTypeface(Typeface.MONOSPACE);
        } catch (Exception e) {
            ticketContents.setText(R.string.text_error_recreating_ticket);
            ticketContents.setTextColor(getResources().getColor(R.color.colorError));
            frg.findViewById(R.id.label_swipe_tip).setVisibility(View.GONE);
        }

        return frg;
    }


    @Override
    public int getTitleResource() {
        return R.string.tab_contents;
    }
}
