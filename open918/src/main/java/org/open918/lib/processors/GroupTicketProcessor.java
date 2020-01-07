package org.open918.lib.processors;

import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.util.BytesUtil;

/**
 * Created by joelhaasnoot on 16/03/2017.
 */

public class GroupTicketProcessor implements Ticket918Dash2Processor {
    @Override
    public void process(BytesUtil bytes, Ticket918Dash2 ticket) {
        ticket.setReturn(bytes.decode(145, 146) == 1);
        ticket.setValidityFirst(bytes.decode(146, 155));
        ticket.setValidityLast(bytes.decode(155, 164));
        ticket.setStationCodeNumOrAlpha(bytes.decode(164, 165));
        int offset = 165;
        if (ticket.getStationCodeNumOrAlpha() == 0) {
            ticket.setStationCodeListType(bytes.decode(165, 169));
            ticket.setDepartureStation(bytes.decode(169, 169+28));
            ticket.setArrivalStation(bytes.decode(197, 197+28));
            offset=197+28;
        } else if (ticket.getStationCodeNumOrAlpha() == 1) {
            ticket.setDepartureStationString(bytes.decodeString(165, 6));
            ticket.setArrivalStationString(bytes.decodeString(195, 6));
            offset=195+30;
        }

        ticket.setGroupLeaderName(bytes.decodeString(offset, 12));
        ticket.setCountermarkNumber(bytes.decode(offset+72, offset+80));

        ticket.setInformationMessages(bytes.decode(offset+80, offset+94));
        ticket.setOpenText(bytes.decodeString(offset+94, 24));
    }
}
