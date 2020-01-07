package org.open918.lib.services;

import org.open918.lib.domain.Carrier;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joelhaasnoot on 18/11/2016.
 */
public class CarrierService {

    private Map<Integer, Carrier> ricsCodeCarriers = new HashMap<>();

    public CarrierService() {
        InputStream in = CarrierService.class.getClassLoader().getResourceAsStream("uic_company_codes.csv");
        if (in != null) {
            for (String line : convertStreamToString(in).split("\\r")) {
                String[] columns = line.split(";");
                Carrier c = new Carrier(Integer.valueOf(columns[0]), columns[1], columns[2], columns[3], (columns.length == 5) ? columns[4] : null);
                ricsCodeCarriers.put(c.getRics(), c);
            }
            try {
                in.close();
            } catch (IOException ignore) { }
        }
    }

    private static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public Carrier getCarrier(int ricsCode) {
        if (ricsCodeCarriers.containsKey(ricsCode)) {
            return ricsCodeCarriers.get(ricsCode);
        }
        return null;
    }
}
