package org.open918.lib;

import org.open918.lib.domain.Ticket;
import org.open918.lib.util.ZxingUtil;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by Joel Haasnoot on 22/04/15.
 * Created by Thomas on 23/04/15
 */

public class UicTicketParser {

    public static final Charset ASCII = Charset.forName("ASCII");
    public static final Charset UTF8 = Charset.forName("UTF-8");

    // OTI is not as per UIC specifications, but is used by certain DB tickets that otherwise conform
    private static final List<String> MESSAGE_TYPES = Arrays.asList("#UT", "OTI");

    public static Ticket decode(String contents, boolean isZxing) throws DataFormatException, ParseException {
        return decode(contents.getBytes(), isZxing);
    }

    public static Ticket decode(byte[] data, boolean isZxing) throws DataFormatException, ParseException {
        if (isZxing) {
            data = ZxingUtil.cleanupZXingData(data);
        }

        String contents = new String(data, UTF8);

        Ticket s;
        if (MESSAGE_TYPES.contains(contents.substring(0, 3).toUpperCase())) {
            s = Uic918Dash3TicketParser.parse(data);
        } else { // TODO: Fix this check
            s = Uic918Dash2TicketParser.parse(data);
        }

        return s;
    }



}

