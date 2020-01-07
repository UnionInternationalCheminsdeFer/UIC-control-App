package nl.ns.barcode_tester.domain;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 18/11/2016.
 */

public enum Country {

    NL(R.drawable.flag_nl),
    FR(R.drawable.flag_fr),
    GB(R.drawable.flag_gb),
    BE(R.drawable.flag_be),
    MN(R.drawable.flag_mn),
    HU(R.drawable.flag_hu),
    CZ(R.drawable.flag_cz),
    IT(R.drawable.flag_it),
    LU(R.drawable.flag_lu),
    ME(R.drawable.flag_me),
    MK(R.drawable.flag_mk),
    GR(R.drawable.flag_gr),
    NO(R.drawable.flag_no),
    PT(R.drawable.flag_pt),
    RU(R.drawable.flag_ru),
    PL(R.drawable.flag_pl),
    BG(R.drawable.flag_bg),
    RO(R.drawable.flag_ro),
    SK(R.drawable.flag_sk),
    ES(R.drawable.flag_es),
    RS(R.drawable.flag_rs),
    SE(R.drawable.flag_se),
    HR(R.drawable.flag_hr),
    SI(R.drawable.flag_si),
    DE(R.drawable.flag_de),
    AT(R.drawable.flag_at),
    CH(R.drawable.flag_ch),
    DK(R.drawable.flag_dk),
    FI(R.drawable.flag_fi),
    IR(R.drawable.flag_ir),
    EE(R.drawable.flag_ee),
    US(R.drawable.flag_us),
    UA(R.drawable.flag_ua);

    private final int flagResource;

    Country(int flagResource) {
        this.flagResource = flagResource;
    }

    public int getFlagResource() {
        return flagResource;
    }
}
