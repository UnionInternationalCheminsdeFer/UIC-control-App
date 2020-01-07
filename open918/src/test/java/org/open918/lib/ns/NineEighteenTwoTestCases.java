package org.open918.lib.ns;

import org.junit.Ignore;
import org.junit.Test;
import org.open918.lib.domain.TicketStandard;
import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.domain.uic918_2.TicketType;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for NMBS tickets
 */
public class NineEighteenTwoTestCases extends TicketTestCase {

    @Test
    public void testUitstelVanBetaling() {
        String ticket = "MSgGwqBAQF0EEEEWEE10w5EABgYHAAMCwoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAADAtAhUAwopFKm1pwpR2WcODw5fCt8Kiwp3CkUcCwqfDisKudAIUSsOzwq4tL13CnG3CnmjC\n" +
                "iMOAR8OUPcKkIE3DvD4AAAAAAAAAAAA=\n";

        Ticket918Dash2 t= (Ticket918Dash2) checkTicket(ticket);
        assertEquals(TicketStandard.TICKET918_2, t.getStandard());
        assertEquals(3, t.getVersion());
        assertEquals(1184, t.getIssuerCode());
        assertEquals(1, t.getId());
        assertEquals(21, t.getTicketTypeCode());
        assertEquals(TicketType.OTHER_BILATERAL, t.getType());
    }

    @Test
    public void testNmbsEssenRoosendaal() {
        String ticket = "MRAgQEACAATCmUFkw5BBdcKWVVYFAMKASMOPwo9zQ8KHUwcAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAAAAwLAIUHTnCmR4Nw5kbe20BPDjCm8OGeMOrcMKBw5rDpAIUen7CsMO3wovCjcODAMKI\n" +
                "wrF6d3nCucOQw5jDplHCmTYAAAAAAAAAAAAA\n";
        Ticket918Dash2 t = (Ticket918Dash2) checkTicket(ticket);
        assertEquals(TicketStandard.TICKET918_2, t.getStandard());
        assertEquals(3, t.getVersion());
        assertEquals(1088, t.getIssuerCode());
        assertEquals(8, t.getId());
        assertEquals(2, t.getTicketTypeCode());
        assertEquals(TicketType.NRT, t.getType());


    }


    @Test
    public void testEurail() {
        String ticket = "MSgGwqBAQEVEEEEEV0VkWUQFwojCjgALwpIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAwLAIURsO0wrXCrBnClGfDuMOjwpzCncODwqrCk8Oew793wpbDkcOXAhRLw58nTQHDlcKG\n" +
                "EcOIYTpMClbDjmwOw6zDosK4AAAAAAAAAAAAAA==\n";

        Ticket918Dash2 t = (Ticket918Dash2) checkTicket(ticket);
        assertEquals(TicketStandard.TICKET918_2, t.getStandard());
        assertEquals(3, t.getVersion());
        assertEquals(1184, t.getIssuerCode());
        assertEquals(1, t.getId());
        assertEquals(21, t.getTicketTypeCode());
        assertEquals(TicketType.OTHER_BILATERAL, t.getType());
    }

    @Test
    public void testInterrail() {
        String ticket = "MSgG77+9QEBFVBBBBFNhdBJIBe+/vQ8AA++/ve+/vQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAAAAAMCwCFGx+77+977+977+977+9N++/vVxWIwHvv73vlZgBQwQ+AhQv77+9Wh0oRFrv\n" +
                "v70S77+9Qxbvv70y77+9ak1qNwAAAAAAAAAAAAA=\n";

        Ticket918Dash2 t = (Ticket918Dash2) checkTicket(ticket);
        assertEquals(TicketStandard.TICKET918_2, t.getStandard());
        assertEquals(3, t.getVersion());
        assertEquals(1184, t.getIssuerCode());
        assertEquals(1, t.getId());
        assertEquals(23, t.getTicketTypeCode());
        assertEquals(TicketType.OTHER_BILATERAL, t.getType());
    }

    @Test
    public void testKeycard() {
        String ticket = "MSgGwqBAQEUEEEEEWEHClMOVAATCigEAG8KfAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAMCwCFHJiE31tJ8KxREA6w4pNEQ8ow6rCjkDDg8OwAhQtwrckd8KdNE0twrPDssO8ZMKv\n" +
                "Iyoyw43CscK5OAAAAAAAAAAAAAA=";

        Ticket918Dash2 t = (Ticket918Dash2) checkTicket(ticket);
        assertEquals(TicketStandard.TICKET918_2, t.getStandard());
        assertEquals(3, t.getVersion());
        assertEquals(1184, t.getIssuerCode());
        assertEquals(1, t.getId());
        assertEquals(21, t.getTicketTypeCode());
        assertEquals(TicketType.OTHER_BILATERAL, t.getType());
    }


    @Test
    public void testDbTestTickets() {
        String ticket = "MQ4EQO+/vQDvv70MGO+/vWNRAAAAAAbvv73vv70C77+9D0JNI++/vVIj77+9ATw6U3Tvv73vv711\n" +
                "77+977+9AAAAAAAAAAAAAAAAAAAAAAAAADAsAhQnWe+/ve+/ve+/vTpHQG5E77+9Y++/vTDvv70h\n" +
                "77+9Qi8OAhRp77+91qXvv73vv70477+9ZAvvv71tNEzvv71277+977+9aAAAAAAAAAAAAAA=";

        Ticket918Dash2 t = (Ticket918Dash2) checkTicket(ticket);
        assertEquals(TicketStandard.TICKET918_2, t.getStandard());
        assertEquals(3, t.getVersion());
        assertEquals(1080, t.getIssuerCode());
        assertEquals(1, t.getId());
        assertEquals(2, t.getTicketTypeCode());
        assertEquals(TicketType.NRT, t.getType());
    }

    @Test
    public void testNmbsProdTicket() {
        String ticket = "MSg8QEAC77+9KO+/vWnvv71AAAAABu+/vQAACO+/ve+/ve+/vWPvv71TBwABDHpTfe+/ve+/vQ5S\n" +
                "77+9AAAAAAAAAAAAAAAAAAAAAAAAADAsAhQVW1Hvv71H77+977+9xo3vv71BRRTvv71WaO+/vSdv\n" +
                "AhQF77+9BO+/ve+/vRbvv70eUcKy77+9JO+/vVQx77+9Se+/vWkAAAAAAAAAAAAA";

        Ticket918Dash2 t = (Ticket918Dash2) checkTicket(ticket);
        assertEquals(TicketStandard.TICKET918_2, t.getStandard());
        assertEquals(3, t.getVersion());
        assertEquals(1184, t.getIssuerCode());
        assertEquals(15, t.getId());
        assertEquals(2, t.getTicketTypeCode());
    }
}
