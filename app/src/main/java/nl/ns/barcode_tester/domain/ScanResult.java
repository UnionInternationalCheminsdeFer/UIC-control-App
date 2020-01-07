package nl.ns.barcode_tester.domain;

import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.TicketStandard;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;

/**
 * Created by Joel Haasnoot on 01/05/15.
 */
public class ScanResult {

    private String contents;
    private byte[] raw;
    private Ticket ticket;

    public ScanResult(byte[] raw, String contents, Ticket ticket) {
        this.raw = raw;
        this.contents = contents;
        this.ticket = ticket;
    }

    public byte[] getRaw() {
        return raw;
    }

    public String getContents() {
        return contents;
    }

    public boolean isUicTicket() {
        return contents.startsWith("#UT");
    }

    public boolean isReadable() {
        return ticket != null;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public boolean hasOrderNumber() {
        return ticket.getStandard() == TicketStandard.TICKET918_3 &&
                ((Ticket918Dash3) ticket).getHeader() != null &&
                ((Ticket918Dash3) ticket).getHeader().getOrderNumber() != null;
    }
}
