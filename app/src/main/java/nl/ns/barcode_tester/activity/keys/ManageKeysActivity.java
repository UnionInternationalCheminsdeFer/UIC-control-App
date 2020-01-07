package nl.ns.barcode_tester.activity.keys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.settings.SettingsActivity;
import nl.ns.barcode_tester.database.KeyDatabase;
import nl.ns.barcode_tester.domain.Key;

public class ManageKeysActivity extends AppCompatActivity {

    private KeyDatabase db;
    private RecyclerView keysList;
    private KeyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_keys);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new KeyDatabase(this);
        db.open();
        keysList = (RecyclerView) findViewById(R.id.list_keys);
        createAdapter();
        keysList.setAdapter(adapter);
        keysList.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageKeysActivity.this, AddKeyActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_keys, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download:
                startActivity(new Intent(this, DownloadKeysActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createAdapter() {
        adapter = new KeyAdapter(this, db.getKeys());
        adapter.setOnKeyClickListener(listener);
    }

    private KeyAdapter.OnKeyClickListener listener = new KeyAdapter.OnKeyClickListener() {
        @Override
        public void onKeyClickListener(int id, Key k) {

        }

        @Override
        public void onKeyLongClickListener(final int id, final Key k) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.label_snack_delete_key), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.label_yes), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.deleteKey(id);
                            createAdapter();
                            keysList.swapAdapter(adapter, true);
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorError))
                    .show();

        }
    };

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
            createAdapter();
            keysList.swapAdapter(adapter, true);
        }
    }
}
