package nl.ns.barcode_tester.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 20/10/2016.
 */

public class CameraUtil {

    private static final String TAG = CameraUtil.class.getSimpleName();

    public static final int REQUEST_CAMERA_PERMISSION = 100;


    public static boolean hasPermission(final Context ctx) {
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static void doPermissionRequest(final Activity act) {
        Log.i(TAG, "Requesting camera permission");
        ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    public static void requestCameraPermission(final Activity act) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.CAMERA)) {

            Log.e(TAG, "User needs explanation");

            AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setTitle(R.string.title_camera)
                    .setMessage(act.getString(R.string.text_camera))
                    .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            doPermissionRequest(act);
                        }
                    })
                    .show();

        } else {
            doPermissionRequest(act);
        }
    }

}
