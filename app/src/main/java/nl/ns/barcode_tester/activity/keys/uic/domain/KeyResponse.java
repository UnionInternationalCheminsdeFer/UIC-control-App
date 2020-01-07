package nl.ns.barcode_tester.activity.keys.uic.domain;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by joelhaasnoot on 17/11/2016.
 */
@Root
public class KeyResponse {

    @ElementList(inline=true)
    private List<Key> keys;


    public List<Key> getKeys() {
        return keys;
    }
}
