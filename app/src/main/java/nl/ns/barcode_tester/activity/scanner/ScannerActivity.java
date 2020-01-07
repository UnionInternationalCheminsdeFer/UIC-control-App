package nl.ns.barcode_tester.activity.scanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.settings.SettingsActivity;
import nl.ns.barcode_tester.database.TicketDatabase;
import nl.ns.barcode_tester.util.CameraUtil;

import static nl.ns.barcode_tester.util.CameraUtil.REQUEST_CAMERA_PERMISSION;


public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = ScannerActivity.class.getSimpleName();
    private OnCameraPermission cameraPermissionListener;
    private OnScanEvent scanEventListener;

    private ViewPager viewPager;
    private ScannerFragmentPagerAdapter scannerPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!CameraUtil.hasPermission(this)) {
            CameraUtil.requestCameraPermission(this);
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        scannerPagerAdapter = new ScannerFragmentPagerAdapter(getSupportFragmentManager(), ScannerActivity.this);
        viewPager.setAdapter(scannerPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (cameraPermissionListener != null) {
                    cameraPermissionListener.onCameraPermission(requestCode, permissions, grantResults);
                }
            }
        }
    }

    private void navigateToTicketsTab() {
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scanner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_delete_scanned:
                showConfirmDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return cameraPermissionListener.onKey(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private void showConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_confirm_delete)
                .setMessage(R.string.text_confirm_delete)
                .setPositiveButton(R.string.label_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTickets();
                        scannerPagerAdapter.refreshTickets();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteTickets() {
        TicketDatabase db = new TicketDatabase(getBaseContext());
        db.open();
        db.deleteTickets();
        db.close();
    }

    public void onScanEvent() {
        if (scanEventListener != null) {
            scanEventListener.onScanEvent();
        }
        navigateToTicketsTab();
    }

    public void registerForCameraPermission(OnCameraPermission f) {
        this.cameraPermissionListener = f;
    }

    public void unregisterForCameraPermission() {
        this.cameraPermissionListener = null;
    }

    public void registerForScanEvent(OnScanEvent s) {
        this.scanEventListener = s;
    }

    public void unregisterForScanEvent() {
        this.scanEventListener = null;
    }

    public interface OnCameraPermission {
        void onCameraPermission(int requestCode, String permissions[], int[] grantResults);
        boolean onKey(int keyCode, KeyEvent event);
    }

    public interface OnScanEvent {
        void onScanEvent();
    }




}
