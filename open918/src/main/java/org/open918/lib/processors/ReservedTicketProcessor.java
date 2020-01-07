package org.open918.lib.processors;

import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.util.BytesUtil;

/**
 * Created by joelhaasnoot on 16/03/2017.
 */

public class ReservedTicketProcessor implements Ticket918Dash2Processor {

    @Override
    public void process(BytesUtil bytes, Ticket918Dash2 ticket) {
        ticket.setSubType(bytes.decode(145, 147)); // 2=BOA; 1=IRT; 0=RES
        ticket.setStationCodeNumOrAlpha(bytes.decode(147, 148));
        int offset = 148;
        if (ticket.getStationCodeNumOrAlpha() == 0) {
            ticket.setStationCodeListType(bytes.decode(148, 152));
            ticket.setDepartureStation(bytes.decode(152, 152+28));
            ticket.setArrivalStation(bytes.decode(180, 180+28));
            offset=180+28;
        } else if (ticket.getStationCodeNumOrAlpha() == 1) {
            ticket.setDepartureStationString(bytes.decodeString(169, 6));
            ticket.setArrivalStationString(bytes.decodeString(199, 6));
            offset=199+30;
        }
        ticket.setDepartureDate(bytes.decode(offset, offset+9));
        ticket.setDepartureTime(bytes.decode(offset+9, offset+20));
        ticket.setTrainNumber(bytes.decodeString(offset+20, 5));
        ticket.setCoachNumber(bytes.decode(offset+50, offset+60));
        ticket.setSeatNumber(bytes.decodeString(offset+60, 3));
        ticket.setOverbookingIndicator(bytes.decode(offset+78, offset+79) == 1);

        ticket.setInformationMessages(bytes.decode(offset+79, offset+93));
        ticket.setOpenText(bytes.decodeString(offset+93, 27));
    }
}
