package nl.ns.barcode_tester.activity.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.open918.lib.domain.uic918_2.Ticket918Dash2;

import java.util.Calendar;
import java.util.Date;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.detail.domain.TicketType;
import nl.ns.barcode_tester.activity.detail.views.CarrierView;

/**
 * Created by Joel Haasnoot on 18/10/15.
 */
public class Properties9182CreditCardTicketFragment extends BaseTicketFragment implements TabFragment {

    private static final String TAG = Properties9182CreditCardTicketFragment.class.getSimpleName();

    public static Fragment getInstance() {
        return new Properties9182CreditCardTicketFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getTicket();

        View v = inflater.inflate(R.layout.fragment_properties9182, container, false);
        setupTicket918_2Fields(v);

        return v;
    }

    private void setupTicket918_2Fields(View v) {
        Ticket918Dash2 ticket = (Ticket918Dash2) result.getTicket();
        CardView card = (CardView) v.findViewById(R.id.card_ticket_9182);
        card.setVisibility(View.VISIBLE);

        CarrierView carrier = (CarrierView) v.findViewById(R.id.view_carrier);
        if (ticket.getIssuerCode() != 0) {
            carrier.setCarrier(ticket.getIssuerCode());
        } else {
            carrier.setVisibility(View.GONE);
        }

        setText(v, R.id.text_barcode_version, String.valueOf(ticket.getVersion()));
        setText(v, R.id.text_barcode_key_id, String.valueOf(ticket.getId()));
        String label = String.format("%s (%s)", getString(TicketType.fromType(ticket.getType()).getLabel()), ticket.getTicketTypeCode());
        setText(v, R.id.text_ticket_type, label);

        setText(v, R.id.text_ticket_adults, ticket.getNumberOfAdults());
        setText(v, R.id.text_ticket_children, ticket.getNumberOfChildren());
        setTextResource(v, R.id.text_specimen, (ticket.isSpecimen() ? R.string.label_yes : R.string.label_no));
        setText(v, R.id.text_travel_class, ticket.getTravelClass());
        setText(v, R.id.text_ticket_number, ticket.getTicketNumber());

        String issued = String.format(getString(R.string.format_date), ticket.getIssuedDay(), ticket.getIssuedYear());
        issued += " ("+ getDate(ticket.getIssuedDay(), ticket.getIssuedYear()) +")";
        setText(v, R.id.text_issued_date, issued);

        setTextResource(v, R.id.text_return, (ticket.isSpecimen() ? R.string.label_yes : R.string.label_no));

        String base = String.format(getString(R.string.format_validity), ticket.getValidityFirst(), ticket.getValidityLast());
        String validFrom = getDate(ticket.getIssuedDay() + ticket.getValidityFirst(), ticket.getIssuedYear());
        String validTo = getDate(ticket.getIssuedDay() + ticket.getValidityLast(), ticket.getIssuedYear());
        base += " (" + validFrom + " - "+validTo+ ")";
        setText(v, R.id.text_validity, base);

        if (ticket.getStationCodeListType() == 1) {
            setText(v, R.id.text_departure, ticket.getDepartureStation());
            setText(v, R.id.text_departure, ticket.getArrivalStation());
        } else {
            setText(v, R.id.text_departure, ticket.getDepartureStationString());
            setText(v, R.id.text_arrival, ticket.getArrivalStationString());
        }

        setText(v, R.id.text_info_messages, ticket.getInformationMessages());

//       TODO: int stationCodeListType;
    }

    private String getDate(int issuedDay, int issuedYear) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, issuedDay);
        calendar.set(Calendar.YEAR, 2010+issuedYear);
        Date d = new Date(calendar.getTimeInMillis());
        return DateFormat.getDateFormat(getContext()).format(d);
    }

    private boolean isNotEmpty(String field) {
        return field != null && !field.equalsIgnoreCase("") && !field.contains("**");
    }

    private void setFieldUnknown(int grey, TextView text) {
        text.setText(R.string.label_field_unknown);
        text.setTextColor(grey);
    }

    private void setText(View v, int id, String text) {
        ((TextView) v.findViewById(id)).setText(text);
    }

    private void setText(View v, int id, int number) {
        ((TextView) v.findViewById(id)).setText(String.valueOf(number));
    }

    private void setTextResource(View v, int id, int resource) {
        ((TextView) v.findViewById(id)).setText(getString(resource));
    }

    @Override
    public int getTitleResource() {
        return R.string.tab_properties;
    }
}
