package nl.ns.barcode_tester.activity.keys;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.open918.lib.domain.Carrier;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 18/11/2016.
 */

public class CarrierDialogFactory {

    public static void createDialog(Context ctx, Carrier c) {
        String msg = String.format(ctx.getString(R.string.label_carrier_dialog),
                c.getRics(), c.getLabelLong(), c.getCountry(), c.getWebsite(), c.getWebsite());

        TextView message = new TextView(ctx);
        message.setText(Html.fromHtml(msg));
        message.setMovementMethod(LinkMovementMethod.getInstance());
        message.setTextSize(18);
        message.setTextColor(ctx.getResources().getColor(R.color.black));
        message.setPadding(40, 16, 16, 16);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(c.getLabelShort())
                .setView(message)
                .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
