package org.open918.lib.ns;

import org.junit.Assert;
import org.open918.lib.TicketUtils;
import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;

/**
 * Created by joelhaasnoot on 19/10/2016.
 */
abstract class TicketTestCase {

    protected Ticket checkTicket(String ticket) {
        try {
            Ticket s = TicketUtils.getTicket(ticket);
            if (s == null) {
                Assert.fail();
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        return null;
    }
}
