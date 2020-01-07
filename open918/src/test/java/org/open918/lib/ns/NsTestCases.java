package org.open918.lib.ns;

import org.junit.Assert;
import org.junit.Test;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;
import org.open918.lib.signature.SignatureVerifier;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Test cases for NMBS tickets
 */
public class NsTestCases extends TicketTestCase {

    public static String getNSProductionKey() {
        // 1184 - NS002 (Prod)
        String keyNS = "MIID0zCCA5OgAwIBAgIJAJ09f3Y6kCnjMAkGByqGSM44BAMwgaMxCzAJBgNVBAYT\n" +
                "Ak5MMRAwDgYDVQQIDAdVdHJlY2h0MRAwDgYDVQQHDAdVdHJlY2h0MRUwEwYDVQQK\n" +
                "DAxOUyBSZWl6aWdlcnMxGjAYBgNVBAsMEUJ1c2luZXNzIFN5c3RlbWVuMRowGAYD\n" +
                "VQQDDBFFdGlja2V0IHByb2R1Y3RpZTEhMB8GCSqGSIb3DQEJARYSbnNjLmludGVy\n" +
                "bmV0QG5zLm5sMB4XDTE0MDEwODA3MjcyM1oXDTI0MDEwNjA3MjcyM1owgaMxCzAJ\n" +
                "BgNVBAYTAk5MMRAwDgYDVQQIDAdVdHJlY2h0MRAwDgYDVQQHDAdVdHJlY2h0MRUw\n" +
                "EwYDVQQKDAxOUyBSZWl6aWdlcnMxGjAYBgNVBAsMEUJ1c2luZXNzIFN5c3RlbWVu\n" +
                "MRowGAYDVQQDDBFFdGlja2V0IHByb2R1Y3RpZTEhMB8GCSqGSIb3DQEJARYSbnNj\n" +
                "LmludGVybmV0QG5zLm5sMIIBtjCCASsGByqGSM44BAEwggEeAoGBAO03OW4DOP8/\n" +
                "KQj9FADbhMysF6sd3TYxWPRpJ4pHbjl5/ogHx2yGVhZ7rEUj2YUqPWKwim2nDOum\n" +
                "gqSQfDnOZc9z3Wrj2cR0hyqtRTF5Qk1gyc+Q00Q0QyHd3XKfnc3NHoE+7Dsxoxm9\n" +
                "FO5zY0BSA3zt7JnO5pxnXVtkDj5IXjXRAhUAqCl7L9zECf9siJGoCzjo1K0HNLcC\n" +
                "gYAOm6Dr4ITWCEL1ABU78/FTFbetQSqA42E6bADy69knRlW1Vh4fzex/DbzXI9xU\n" +
                "431dj/NrLyTmOygyJWGJR7VI5o96UbRFe1vqfE13mxTc9VlYRHketVMlrP54Mo20\n" +
                "hijLvJWHlWj54OhqVGNUV8Mj29PYfE2KYq7EOXUIK6oMxQOBhAACgYBVmRP9ZruJ\n" +
                "9frYGZy/Das70KcQhe9G79ifjhxjjAgiBzegqlv92IKaQl7ai2XhKfxFRQSSnsYI\n" +
                "8TcW7ARrYJP4eddyr2WNk594uaASE8X54PIekcToAMC/vI9ZI2/T4lHXmn4d/5zE\n" +
                "XyxDxyIF6gtnXUsrjyVXAYiw3Y3Tx+ldnKNQME4wHQYDVR0OBBYEFIBmg4W69s09\n" +
                "i8N1qyXyd1RPvsewMB8GA1UdIwQYMBaAFIBmg4W69s09i8N1qyXyd1RPvsewMAwG\n" +
                "A1UdEwQFMAMBAf8wCQYHKoZIzjgEAwMvADAsAhQ8xUvztzfGu24cJm+mgmPuxbA9\n" +
                "iAIUOqTpCO+e4He8b+7bT+laaQ+kkL8=";
        keyNS = "-----BEGIN CERTIFICATE-----\n" + keyNS + "\n-----END CERTIFICATE-----";
        return keyNS;
    }

    @Test
    public void test33GroepsretourAmsterdamCRotterdamZuid() {
        String ticket = "I1VUMDExMTg0TlMwMDIwLQIUTsKCw5jDkFnCr3nDuRZewqzDo8OdRMOxBsKHwqvChH0CFQDCnHx/\n" +
                "dAZmw60SRMKhwrV5woXDlhLDknrCgcOlAQAAADAyNjV4wpxlUMOBbsKDMAzDvcKVwpw5wrR2Egd2\n" +
                "RMKUwq1owoxKDMKQwrpLwrXCqTl0UsOFFMOow7bDu8KLMwZoe8K5w7jDhX5Pw49uT8O7PMOdAQLC\n" +
                "kEJMwrTCmMKhdEzCpGkDIAlQAhoJBMKmKsOzwqo9NWV6w7QiLU3CnTXDksKPAADDngEqLiR0w5Z9\n" +
                "w7bDlsK9w5nCr8OLw7sgajvDtjfDhx5eYiBAw7rCh8KgwrhUWcOReWbDmMKPwrnDphzCnCcoPMOi\n" +
                "RztYJ8O2woDDix/CosKPNTNvw4J+FCrDqsOSwrLDmMKJw7vDusOwJMKkw5kCbjnCvGgOKwYGwrVP\n" +
                "G8KcdMOdwo/Co3XDp8OXwqt4wrldw45gFMO9wrZkeh3CplbCtsKBeMORw7xpLMKKf2Ymw7F7w6HC\n" +
                "jwjCksOVw5ZdwpHCosKieMOmeVvCozI8wpvChCvDhBvCtgPCmsKOwopUPcKLB8OXw5vCj8OBwoV7\n" +
                "wqJewonCoyhCwr3CiMKTaMOCNzIUcQI=";

        Ticket918Dash3 t = (Ticket918Dash3) checkTicket(ticket);

        try {
            Assert.assertTrue(SignatureVerifier.verify(getNSProductionKey(), t));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }




}
