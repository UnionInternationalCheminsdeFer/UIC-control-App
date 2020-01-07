package nl.ns.barcode_tester.domain;

/**
 * Created by joelhaasnoot on 24/10/2016.
 */

public class Key {

    private int id;
    private int carrier;
    private String keyIdentifier;
    private String certificate;

    public Key(int id, int carrier, String keyIdentifier, String certificate) {
        this.id = id;
        this.carrier = carrier;
        this.keyIdentifier = keyIdentifier;
        this.certificate = certificate;
    }

    public int getId() {
        return id;
    }

    public int getCarrier() {
        return carrier;
    }

    public String getKeyIdentifier() {
        return keyIdentifier;
    }

    public String getCertificate() {
        return certificate;
    }
}
