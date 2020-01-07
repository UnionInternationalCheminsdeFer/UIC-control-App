package org.open918.lib.processors;

import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.util.BytesUtil;

/**
 * Created by joelhaasnoot on 16/03/2017.
 */

public class RailPassProcessor implements Ticket918Dash2Processor {
    @Override
    public void process(BytesUtil bytes, Ticket918Dash2 ticket) {
        ticket.setSubType(bytes.decode(145, 147)); // 1=INTERAIL, 2=EURAIL EUROPE, 3=EURAIL OVERSEAS
        ticket.setValidityFirst(bytes.decode(147, 156));
        ticket.setValidityLast(bytes.decode(156, 165)); // Or max duration
        ticket.setDaysOfTravel(bytes.decode(165, 172));
        ticket.setCountry1(bytes.decode(172, 179)); // 100 = All Countries
        ticket.setCountry2(bytes.decode(179, 186));
        ticket.setCountry3(bytes.decode(186, 193));
        ticket.setCountry4(bytes.decode(193, 200));
        ticket.setCountry5(bytes.decode(200, 207));
        ticket.hadSecondPage(bytes.decode(207, 208) == 1);

        ticket.setInformationMessages(bytes.decode(208, 222));
        ticket.setOpenText(bytes.decodeString(222, 40));
    }
}
