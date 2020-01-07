package nl.ns.barcode_tester.activity.detail;

import android.support.v4.app.Fragment;

import nl.ns.barcode_tester.domain.ScanResult;
import nl.ns.barcode_tester.util.Base64Util;

import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.TicketStandard;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;
import org.open918.lib.domain.uic918_3.TicketContents;
import org.open918.lib.encodings.ConverterFactory;
import org.open918.lib.encodings.TicketConverter;


public class BaseTicketFragment extends Fragment {

    protected ScanResult result;
    protected TicketConverter converter;

    protected void getTicket() {
        if (getActivity() != null && getActivity() instanceof TicketDetailActivity) {
            result = ((TicketDetailActivity) getActivity()).getScanResult();
            converter = getTicketConverter();
        }
    }

    private TicketConverter getTicketConverter() {
        if (result.getTicket().getStandard() == TicketStandard.TICKET918_3) {
            TicketContents contents = ((Ticket918Dash3) result.getTicket()).getContents();
            if (contents.getStandard() != null) {
                return ConverterFactory.getInstance(contents.getStandard(), contents.getFields());
            }
        }
        return null;
    }

    public static String getResultAsBase64(ScanResult result) {
        return Base64Util.getAsBase64(result);
    }



}
