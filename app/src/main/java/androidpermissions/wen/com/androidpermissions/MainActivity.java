package androidpermissions.wen.com.androidpermissions;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.camera_but).setOnClickListener(this);
        findViewById(R.id.gps_but).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_but:
                MainActivityPermissionsDispatcher.showMainWithCheck(this);
                break;
            case R.id.gps_but:
                MainActivityPermissionsDispatcher.showGPSWithCheck(this);
                break;
        }

    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void showMain() {
        // NOTE: Perform action that requires the permission. If this is run by PermissionsDispatcher, the permission will hae been granted
        Intent i = new Intent();
        i.setClass(this, CaptureActivity.class);
        i.putExtra("type", "camera");
        startActivity(i);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForMain(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog(R.string.permission_camer_rationale, request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onMainDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality

    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void onMainNeverAskAgain() {

    }


    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void showGPS() {
        Intent i = new Intent();
        i.setClass(this, CaptureActivity.class);
        i.putExtra("type", "gps");
        startActivity(i);
    }

    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void showRationaleForGPS(PermissionRequest request) {
        showRationaleDialog(R.string.permission_gps_rationale, request);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void onGPSDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
//        Toast.makeText(this, R.string.permission_gps_denied, Toast.LENGTH_SHORT).show();

    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void onGPSNeverAskAgain() {
//        Toast.makeText(this, R.string.permission_gps_never_askagain, Toast.LENGTH_SHORT).show();

    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.refuse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
