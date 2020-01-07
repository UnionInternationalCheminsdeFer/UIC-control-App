package org.open918.lib.processors;

import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.util.BytesUtil;

/**
 * Created by joelhaasnoot on 16/03/2017.
 */

public interface Ticket918Dash2Processor {

    void process(BytesUtil input, Ticket918Dash2 ticket);
}
