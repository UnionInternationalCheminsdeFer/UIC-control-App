package nl.ns.barcode_tester.activity.keys;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.database.KeyDatabase;

public class AddKeyActivity extends AppCompatActivity {

    private EditText editCarrier;
    private EditText editKey;
    private EditText editCertificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key);

        editCarrier = (EditText) findViewById(R.id.edit_carrier);
        editKey = (EditText) findViewById(R.id.edit_key);
        editCertificate = (EditText) findViewById(R.id.edit_certificate);

        Button button = (Button) findViewById(R.id.button_add);
        button.setOnClickListener(new OnAddClickListener());
    }

    private boolean isValid() {
        String carrier = editCarrier.getText().toString();
        String key = editKey.getText().toString();
        String cert = editCertificate.getText().toString();
        // TODO: Check Cert
        return carrier.length() == 4 && key.length() == 5 && cert.length() > 5;
    }

    private class OnAddClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isValid()) {
                KeyDatabase db = new KeyDatabase(AddKeyActivity.this);
                db.open();
                try {
                    db.insertKey(editCarrier.getText().toString(), editKey.getText().toString(), editCertificate.getText().toString());
                    AddKeyActivity.this.finish();
                } catch (SQLiteConstraintException ex) {
                    Snackbar.make(v, R.string.error_snack_key_exists, Snackbar.LENGTH_LONG)
                            .setAction(R.string.label_ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {}
                            })
                            .show();
                } finally {
                    db.close();
                }
            } else {
                Snackbar.make(v, R.string.error_snack_not_valid, Snackbar.LENGTH_LONG)
                        .setAction(R.string.label_ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {}
                        })
                        .show();
            }
        }
    }
}
