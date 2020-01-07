package nl.ns.barcode_tester.activity.keys;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.keys.uic.UicDownloadService;
import nl.ns.barcode_tester.activity.keys.uic.domain.Key;
import nl.ns.barcode_tester.activity.keys.uic.domain.KeyResponse;
import nl.ns.barcode_tester.database.KeyDatabase;
import nl.ns.barcode_tester.database.TicketDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class DownloadKeysActivity extends AppCompatActivity {

    private static final String TAG = DownloadKeysActivity.class.getSimpleName();

    private KeyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_keys);

        db = new KeyDatabase(this);
        db.open();

        downloadKeys();
    }

    private void downloadKeys() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railpublickey.uic.org/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        UicDownloadService service = retrofit.create(UicDownloadService.class);
        service.getKeys().enqueue(new DownloadKeysResponseCallback());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null) {
            db.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            db.open();
        }
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s).replace(' ', '0');
    }

    private class DownloadKeysResponseCallback implements Callback<KeyResponse> {
        @Override
        public void onResponse(Call<KeyResponse> call, Response<KeyResponse> response) {
            int added = 0, duplicate = 0;
            if (response.body() != null && response.body().getKeys().size() > 0) {
                for (Key k: response.body().getKeys()) {
                    String cert = String.format("-----BEGIN CERTIFICATE-----\n%s\n-----END CERTIFICATE-----", k.getPublicKey());
                    try {
                        db.insertKey(k.getIssuerCode(), padLeft(k.getId(), 6 - k.getId().length()), cert);
                        added += 1;
                    } catch(SQLiteConstraintException ex) {
                        Log.i(TAG, "Certificate with issuer "+k.getIssuerCode()+" and "+k.getId()+" already exists, ignoring.");
                        duplicate += 1;
                    }
                }
            }
            Log.i(TAG, "Added "+added+" keys, " + duplicate + " duplicate");
            Toast.makeText(DownloadKeysActivity.this, String.format(getString(R.string.message_keys_added), added), Toast.LENGTH_LONG).show();
            DownloadKeysActivity.this.finish();
        }

        @Override
        public void onFailure(Call<KeyResponse> call, Throwable t) {
            Snackbar.make(getCurrentFocus(), R.string.error_snack_download_failed, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.label_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadKeysActivity.this.finish();
                        }
                    })
                    .show();
        }
    }
}
