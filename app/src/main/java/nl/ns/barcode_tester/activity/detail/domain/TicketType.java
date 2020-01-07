package nl.ns.barcode_tester.activity.detail.domain;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 25/11/2016.
 */

public enum TicketType {

    RESERVED(org.open918.lib.domain.uic918_2.TicketType.IRT_RES_BOA, R.string.type_reserved),
    NON_RESERVED(org.open918.lib.domain.uic918_2.TicketType.NRT, R.string.type_non_reserved),
    GROUP(org.open918.lib.domain.uic918_2.TicketType.GRT, R.string.type_group),
    REGION(org.open918.lib.domain.uic918_2.TicketType.RPT, R.string.type_region),
    OTHER(org.open918.lib.domain.uic918_2.TicketType.OTHER_BILATERAL, R.string.type_other),
    UNKNOWN(org.open918.lib.domain.uic918_2.TicketType.UNKNOWN, R.string.type_unknown);

    org.open918.lib.domain.uic918_2.TicketType type;
    private int label;

    TicketType(org.open918.lib.domain.uic918_2.TicketType type, int label) {
        this.type = type;
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public static TicketType fromType(org.open918.lib.domain.uic918_2.TicketType type) {
        for (TicketType t : values()) {
            if (t.type == type) {
                return t;
            }
        }
        return UNKNOWN;
    }
}
