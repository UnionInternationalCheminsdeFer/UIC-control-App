package org.open918.lib.signature;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.util.Arrays;

/**
 * Created by joelhaasnoot on 24/10/2016.
 */
public class SignatureVerifier {

    public static boolean verify(String key, Ticket918Dash3 s) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return verifySignature(key, s.getSignature(), s.getCompressedMessage());
    }

    private static boolean verifySignature(String key, byte[] signature, byte[] compressedMessage) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        DSAPublicKey publicKey;
        try {
            publicKey = getDsaPublicKey(key);
        } catch (CertificateException e) {
            return false;
        }
        int j = signature.length;
        for (int i = signature.length - 1; (i > 0) && (signature[i] == 0); i--) {
            j = i;
        }
        signature = Arrays.copyOfRange(signature, 0, j);

        try {
            Signature dsa = Signature.getInstance("DSA");
            dsa.initVerify(publicKey);
            dsa.update(compressedMessage);
            return dsa.verify(signature);
        } catch (Exception e) {
            throw e;
        }
    }

    private static DSAPublicKey getDsaPublicKey(String key) throws CertificateException {
        PEMParser parser = new PEMParser(new StringReader(key));
        DSAPublicKey publicKey = null;
        try {
            X509CertificateHolder holder = (X509CertificateHolder) parser.readObject();
            X509Certificate cert = new JcaX509CertificateConverter().getCertificate(holder);
            publicKey = ((DSAPublicKey)cert.getPublicKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }
}
