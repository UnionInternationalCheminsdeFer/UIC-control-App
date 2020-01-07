package nl.ns.barcode_tester.activity.keys.uic.domain;

import org.simpleframework.xml.Element;

/**
 * Created by joelhaasnoot on 16/11/2016.
 */

public class Key
{
    @Element private String publicKey;
    @Element(required=false) private String startDate;
    @Element(required=false) private String commentForEncryptionType;
    @Element(required=false) private String allowedProductOwnerCodes;
    @Element private String issuerCode;
    @Element(required=false) private String endDate;
    @Element(required=false) private String keyForged;
    @Element private String id;
    @Element(required=false) private String barcodeXsd;
    @Element(required=false) private String signatureAlgorithm;
    @Element(required=false) private String issuerName;
    @Element(required=false) private String barcodeVersion;
    @Element(required=false) private String versionType;

    public String getPublicKey()
    {
        return publicKey;
    }

    public void setPublicKey(String publicKey)
    {
        this.publicKey = publicKey;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getCommentForEncryptionType()
    {
        return commentForEncryptionType;
    }

    public void setCommentForEncryptionType(String commentForEncryptionType)
    {
        this.commentForEncryptionType = commentForEncryptionType;
    }

    public String getAllowedProductOwnerCodes()
    {
        return allowedProductOwnerCodes;
    }

    public void setAllowedProductOwnerCodes(String allowedProductOwnerCodes)
    {
        this.allowedProductOwnerCodes = allowedProductOwnerCodes;
    }

    public String getIssuerCode()
    {
        return issuerCode;
    }

    public void setIssuerCode(String issuerCode)
    {
        this.issuerCode = issuerCode;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String getKeyForged()
    {
        return keyForged;
    }

    public void setKeyForged(String keyForged)
    {
        this.keyForged = keyForged;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getBarcodeXsd()
    {
        return barcodeXsd;
    }

    public void setBarcodeXsd(String barcodeXsd)
    {
        this.barcodeXsd = barcodeXsd;
    }

    public String getSignatureAlgorithm()
    {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm)
    {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getIssuerName()
    {
        return issuerName;
    }

    public void setIssuerName(String issuerName)
    {
        this.issuerName = issuerName;
    }

    public String getBarcodeVersion()
    {
        return barcodeVersion;
    }

    public void setBarcodeVersion(String barcodeVersion)
    {
        this.barcodeVersion = barcodeVersion;
    }

    public String getVersionType()
    {
        return versionType;
    }

    public void setVersionType(String versionType)
    {
        this.versionType = versionType;
    }

}

