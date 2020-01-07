package org.open918.lib.domain.uic918_3;

import java.util.ArrayList;
import java.util.List;

/**
 * Ticket contents for 918.3
 */
public class TicketContents extends TicketBlock {

    String standard;
    int numberOfFields;
    List<TicketField> fields = new ArrayList<>();

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public List<TicketField> getFields() {
        return fields;
    }

    public void setFields(List<TicketField> fields) {
        this.fields = fields;
    }
}
