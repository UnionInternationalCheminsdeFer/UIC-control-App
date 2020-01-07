package org.open918.lib.domain.uic918_3;

import java.util.EnumSet;
import java.util.HashSet;


/**
 * Ticket Flag for 918.3
 */
public enum TicketFlag {

    INTERNATIONAL(1),
    EDITED(2),
    SPECIMEN(4);

    private final int flagBit;

    TicketFlag(int bit) {
        this.flagBit = bit;
    }

    public static EnumSet<TicketFlag> getFlagsFromNumber(int value) {
        HashSet<TicketFlag> flags = new HashSet<>();
        for (TicketFlag f : values()) {
            if ((value & f.flagBit) == f.flagBit) {
                flags.add(f);
            }
        }
        if (flags.size() == 0) {
            return EnumSet.noneOf(TicketFlag.class);
        } else {
            return EnumSet.copyOf(flags);
        }
    }
}
