package org.open918.lib.util;

/**
 * Created by joelhaasnoot on 17/03/2017.
 */

public class AsciiSixBits {

    /*
    This translates between decimal (index) and value in 6 bit ASCII
     */
    private static char[] TABLE = {' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',',
            '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>',
            '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_'};


    public static char translate(int in) {
        return TABLE[in];
    }

}
