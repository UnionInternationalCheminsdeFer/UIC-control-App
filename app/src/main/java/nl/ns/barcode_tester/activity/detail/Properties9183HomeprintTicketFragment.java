package nl.ns.barcode_tester.activity.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.open918.lib.domain.GenericTicketDetails;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;
import org.open918.lib.domain.uic918_3.TicketFlag;
import org.open918.lib.services.CarrierService;
import org.open918.lib.signature.SignatureVerifier;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.detail.views.CarrierView;
import nl.ns.barcode_tester.database.KeyDatabase;
import nl.ns.barcode_tester.domain.Key;

/**
 * Created by Joel Haasnoot on 18/10/15.
 */
public class Properties9183HomeprintTicketFragment extends BaseTicketFragment implements TabFragment {

    private static final String TAG = Properties9183HomeprintTicketFragment.class.getSimpleName();

    public static Fragment getInstance() {
        return new Properties9183HomeprintTicketFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getTicket();

        View v = inflater.inflate(R.layout.fragment_properties9183, container, false);

        setupTicketFields(v);
        setupBarcodeFields(v);
        setupTicketDetailFields(v);

        return v;
    }


    private void setupTicketDetailFields(View v) {
        Ticket918Dash3 ticket = (Ticket918Dash3) result.getTicket();
        if (ticket.getHeader() == null) {
            return;
        }

        // Ticket details
        Date created = ticket.getHeader().getCreationDateAsDate();
        setText(v, R.id.text_barcode_creation, SimpleDateFormat.getDateTimeInstance().format(created));

        String order = ticket.getHeader().getOrderNumber();
        boolean orderIsEmpty = (order == null || order.trim().isEmpty() || order.replace(" ", "").isEmpty());
        setText(v, R.id.text_order_number, orderIsEmpty ? getString(R.string.label_empty) : order.trim());

        setText(v, R.id.text_languages, String.format("%s, %s", ticket.getHeader().getLanguage().toUpperCase(),
                ticket.getHeader().getLanguage2()).toUpperCase());

        TextView ticketFlags = (TextView) v.findViewById(R.id.text_ticket_flags);
        EnumSet<TicketFlag> flags = ticket.getHeader().getFlags();
        StringBuilder out = new StringBuilder();
        if (flags == null || flags.isEmpty()) {
            ticketFlags.setText(R.string.label_empty);
        } else {
            for (TicketFlag f : flags) {
                String flag = f.toString();
                flag = flag.substring(0, 1).toUpperCase() + flag.substring(1).toLowerCase();
                out.append(flag);
                out.append("\r\n");
            }
            ticketFlags.setText(out.toString());
        }
    }

    private void setupBarcodeFields(View v) {
        Ticket918Dash3 ticket = (Ticket918Dash3) result.getTicket();

        CarrierView carrier = (CarrierView) v.findViewById(R.id.view_carrier);
        carrier.setCarrier(ticket.getRicsCode());

        TextView barcodeSignatureId = (TextView) v.findViewById(R.id.barcode_signature_id);
        barcodeSignatureId.setText(ticket.getSignatureKeyId());

        KeyDatabase db = new KeyDatabase(getContext());
        db.open();
        Key k = null;
        if (ticket.getSignatureKeyId() != null) {
            k = db.getKey(ticket.getRicsCode(), ticket.getSignatureKeyId());
        }
        db.close();

        ImageView keyAvailableImage = (ImageView) v.findViewById(R.id.image_key_available);
        TextView keyAvailable = (TextView) v.findViewById(R.id.label_key_available);
        keyAvailableImage.setImageDrawable(getResources().getDrawable((k != null) ? R.drawable.ic_check_green : R.drawable.ic_cancel_red));
        keyAvailable.setText(getString((k != null) ? R.string.label_key_available : R.string.label_key_unavailable));

        boolean verify = false;
        if (ticket.getSignature() != null && k != null) {
            try {
                verify = SignatureVerifier.verify(k.getCertificate(), ticket);
            } catch (Exception ex) {
                Log.e(TAG, "Failed to validate signature ", ex);
            }
        }
        ImageView signatureValidImage = (ImageView) v.findViewById(R.id.image_signature_valid);
        TextView signatureValid = (TextView) v.findViewById(R.id.label_signature_valid);
        signatureValidImage.setImageDrawable(getResources().getDrawable((verify) ? R.drawable.ic_check_green : R.drawable.ic_cancel_red));
        signatureValid.setText((verify) ? R.string.label_signature_ticket_match : R.string.label_signature_ticket_mismatch);
    }

    private void setupTicketFields(View v) {
        if (converter == null) {
            v.findViewById(R.id.table_travel_details).setVisibility(View.GONE);
            v.findViewById(R.id.error_travel_details).setVisibility(View.VISIBLE);
            return;
        }
        int grey = ContextCompat.getColor(getActivity(), R.color.text_grey);

        // Travel details
        GenericTicketDetails details = converter.toDetails();
        TextView travelTitle = (TextView) v.findViewById(R.id.travel_ticket_name);
        if (!details.getTicketTitle().equalsIgnoreCase("")) {
            travelTitle.setText(details.getTicketTitle());
        } else {
            setFieldUnknown(grey, travelTitle);
        }

        TextView travelPassenger = (TextView) v.findViewById(R.id.travel_passenger_name);
        if (!details.getPassengerName().equalsIgnoreCase("")) {
            travelPassenger.setText(details.getPassengerName());
        } else {
            setFieldUnknown(grey, travelPassenger);
        }

        TextView travelOutward = (TextView) v.findViewById(R.id.travel_outward);
        if (isNotEmpty(details.getOutwardDeparture())) {
            travelOutward.setText(String.format("%s - %s", details.getOutwardDeparture(), details.getOutwardArrival()));
        } else {
            setFieldUnknown(grey, travelOutward);
        }

        TextView travelReturn = (TextView) v.findViewById(R.id.travel_return);
        if (isNotEmpty(details.getReturnDeparture())) {
            travelReturn.setText(String.format("%s - %s", details.getReturnDeparture(), details.getReturnArrival()));
        } else {
            setFieldUnknown(grey, travelReturn);
        }

        TextView travelPrice = (TextView) v.findViewById(R.id.travel_price);
        if (isNotEmpty(details.getPrice())) {
            travelPrice.setText(details.getPrice());
        } else {
            setFieldUnknown(grey, travelPrice);
        }

        TextView travelClass = (TextView) v.findViewById(R.id.travel_class);
        if (isNotEmpty(details.getTravelClass())) {
            travelClass.setText(details.getTravelClass());
        } else {
            setFieldUnknown(grey, travelClass);
        }

        TextView validity = (TextView) v.findViewById(R.id.validity);
        if (isNotEmpty(details.getValidity())) {
            validity.setText(details.getValidity());
        } else {
            setFieldUnknown(grey, validity);
        }
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
        ((TextView) v.findViewById(id)).setText(String.valueOf(resource));
    }

    @Override
    public int getTitleResource() {
        return R.string.tab_properties;
    }
}
