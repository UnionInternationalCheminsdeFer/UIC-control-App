package nl.ns.barcode_tester.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.domain.ScanResult;
import nl.ns.barcode_tester.util.Base64Util;

/**
 * Created by Joel Haasnoot on 25/08/16.
 */
public class ShareClickListener implements View.OnClickListener, DialogInterface.OnClickListener {

    private final String base64Contents;
    private final Context context;

    public ShareClickListener(Context ctx, String base64Contents) {
        this.context = ctx;
        this.base64Contents = base64Contents;
    }

    public ShareClickListener(Context ctx, ScanResult result) {
        this.context = ctx;
        this.base64Contents = Base64Util.getAsBase64(result);
    }

    @Override
    public void onClick(View v) {
        shareBarcode();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        shareBarcode();
    }

    private void shareBarcode() {
        if (base64Contents == null) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ context.getString(R.string.email_barcode)});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject));

        String emailBody = context.getString(R.string.email_content)
                + base64Contents;
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);

        context.startActivity(Intent.createChooser(intent, context.getText(R.string.label_send_email)));
    }
}
