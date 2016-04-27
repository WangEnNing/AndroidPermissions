package androidpermissions.wen.com.androidpermissions;

import android.support.v4.app.ActivityCompat;


import java.lang.ref.WeakReference;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

//import dagger.Module;

/**
 * Created by wangenning on 16/3/14.
 */

public class MainActivityPermissionsDispatcher {
    private static final int REQUEST_SHOWMain = 0;

    private static final String[] PERMISSION_SHOWMain = new String[]{"android.permission.CAMERA"};
    private static final int REQUEST_SHOWGPS = 1;
    private static final String[] PERMISSION_SHOWGPS = new String[]{"android.permission.ACCESS_COARSE_LOCATION"};


    public MainActivityPermissionsDispatcher() {
    }

    public static void showMainWithCheck(MainActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWMain)) {
            target.showMain();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWMain)) {
                target.showRationaleForMain(new ShowMainPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_SHOWMain, REQUEST_SHOWMain);
            }
        }
    }

    public static void showGPSWithCheck(MainActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWGPS)) {
            target.showGPS();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWGPS)) {
                target.showRationaleForGPS(new ShowGPSPermissionRequest(target));
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_SHOWGPS, REQUEST_SHOWGPS);
            }
        }
    }

    public static void onRequestPermissionsResult(MainActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SHOWMain:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWMain)) {
                    target.onMainDenied();
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.showMain();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWMain)) {
                        target.onMainNeverAskAgain();
                    } else {
                        target.onMainDenied();
                    }
                }
                break;
            case REQUEST_SHOWGPS:
                if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWGPS)) {
                    target.onGPSDenied();
                    return;
                }
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.showGPS();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWGPS)) {
                        target.onGPSNeverAskAgain();
                    } else {
                        target.onGPSDenied();
                    }
                }
                break;
            default:
                break;
        }
    }

    private static final class ShowMainPermissionRequest implements PermissionRequest {
        private final WeakReference<MainActivity> weakTarget;

        private ShowMainPermissionRequest(MainActivity target) {
            this.weakTarget = new WeakReference<>(target);
        }

        @Override
        public void proceed() {
            MainActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWMain, REQUEST_SHOWMain);
        }

        @Override
        public void cancel() {
            MainActivity target = weakTarget.get();
            if (target == null) return;
            target.onMainDenied();
        }
    }

    private static final class ShowGPSPermissionRequest implements PermissionRequest {
        private final WeakReference<MainActivity> weakTarget;

        private ShowGPSPermissionRequest(MainActivity target) {
            this.weakTarget = new WeakReference<>(target);
        }

        @Override
        public void proceed() {
            MainActivity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWGPS, REQUEST_SHOWGPS);
        }

        @Override
        public void cancel() {
            MainActivity target = weakTarget.get();
            if (target == null) return;
            target.onGPSDenied();
        }
    }
}
