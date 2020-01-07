package org.open918.lib;

import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.domain.uic918_2.TicketType;
import org.open918.lib.processors.GroupTicketProcessor;
import org.open918.lib.processors.NonReservedTicketProcessor;
import org.open918.lib.processors.RailPassProcessor;
import org.open918.lib.processors.ReservedTicketProcessor;
import org.open918.lib.util.BytesUtil;

/**
 * Created by joelhaasnoot on 22/11/2016.
 */
public class Uic918Dash2TicketParser {

    static Ticket918Dash2 parse(byte[] data) {
        Ticket918Dash2 ticket = new Ticket918Dash2();

        BytesUtil b = new BytesUtil(data);
        ticket.setVersion(b.decode(0, 4));
        ticket.setIssuerCode(b.decode(4, 18));
        ticket.setId(b.decode(18, 22));
        ticket.setTicketTypeCode(b.decode(22, 27));
        ticket.setType(TicketType.fromCode(ticket.getTicketTypeCode()));

        ticket.setNumberOfAdults(b.decode(27, 34));
        ticket.setNumberOfChildren(b.decode(34, 41));
        ticket.setSpecimen(b.decode(41, 42) == 1);
        ticket.setTravelClass(b.decode(42, 48));
        ticket.setTicketNumber(b.decodeString(48, 14));
        ticket.setIssuedYear(b.decode(132, 136));
        ticket.setIssuedDay(b.decode(136, 145));

        switch (ticket.getType()) {
            case IRT_RES_BOA: // 1
                new ReservedTicketProcessor().process(b, ticket);
                break;
            case NRT: // 2
                new NonReservedTicketProcessor().process(b, ticket);
                break;
            case GRT: // 3
                new GroupTicketProcessor().process(b, ticket);
                break;
            case RPT: // 4
                new RailPassProcessor().process(b, ticket);
                break;
        }

        return ticket;
    }


}
