package org.open918.lib.domain.uic918_2;

/**
 * Created by joelhaasnoot on 21/11/2016.
 */
public enum TicketType {
    IRT_RES_BOA(1),
    NRT(2),
    GRT(3),
    RPT(4),
    OTHER_BILATERAL(null),
    UNKNOWN(null);

    private final Integer code;

    TicketType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static TicketType fromCode(int code) {
        if (code > 20) {
            return OTHER_BILATERAL;
        }
        for (TicketType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
