package com.tt.android_ble.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * -------------------------------------------------
 * Description：检查权限的工具类
 * Author：tt
 * Data：2016/12/8
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 -- 初步建立管理，可以查看权限状态，可以申请权限集合
 * -------------------------------------------------
 */
public class PermissionHandler {
    private static final String TAG = PermissionHandler.class.getSimpleName();

    public static final int REQUEST_CODE_ASK_PERMISSION = 0;

    private static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    private static final int PERMISSION_DENIED = PackageManager.PERMISSION_DENIED;

    /** 权限请求状态 */
    private boolean mPermissionRequestInProgress = false;

    /** 请求权限失败的权限集合，避免二次请求 */
    private List<String> mDeniedPermissions = new ArrayList<>();

    private Callback mCallback;

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    /**
     * 检查是否全部权限授权
     * @param activity  活动
     * @param permissions 权限集合
     * @return 全部权限授权状态 true -- 全部权限授权  false -- 非所有权限都授权
     */
    public boolean checkPermissions(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (!checkPermission(activity, permission)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查该权限是否授权
     * @param activity 活动
     * @param permission 权限
     * @return 权限授权状态 true -- 权限授权  false -- 权限拒绝
     */
    public boolean checkPermission(Activity activity, String permission){
        int permissionSystemStatus = ActivityCompat.checkSelfPermission(activity, permission);
        Log.d(TAG, "checkPermission: " + permission + " -- " + permissionSystemStatus);
        return permissionSystemStatus == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求权限
     * @param activity 活动
     * @param permissions 权限集合
     */
    public void requestPermissions(Activity activity, String[] permissions) {
        if (mPermissionRequestInProgress) {
            Log.d(TAG, "permission request in progress...");
            return;
        }

        /** 需要请求的权限集合 */
        List<String> mNeedRequestPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (checkPermissionInDeniedList(permission)) {
                continue;
            }

            if (!checkPermission(activity, permission)) {
                mNeedRequestPermissions.add(permission);
            }
        }

        if (mNeedRequestPermissions.isEmpty()) {
            Log.d(TAG, "requestPermissions: all permissions processed");
        } else {
            String[] needToRequestPermissionsArray = mNeedRequestPermissions.toArray(new String[mNeedRequestPermissions.size()]);
            ActivityCompat.requestPermissions(activity, needToRequestPermissionsArray, REQUEST_CODE_ASK_PERMISSION);
            mPermissionRequestInProgress = true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                Log.d(TAG, "onRequestPermissionsResult: " + permission + " -- " + grantResults[i]);

                checkDeniedPermissions(permission, grantResults[i]);
            }
        }

        notifyListener();
    }

    private void checkDeniedPermissions(String permission, int permissionStatus) {
        if (permissionStatus == PERMISSION_DENIED) {
            if (!checkPermissionInDeniedList(permission)) {
                mDeniedPermissions.add(permission);
            }
        }
    }

    private boolean checkPermissionInDeniedList(String permission) {
        if (permission.isEmpty()) {
            return false;
        } else {
            for (String str : mDeniedPermissions) {
                if (str.equals(permission)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void notifyListener() {
        mPermissionRequestInProgress = false;

        if (!mDeniedPermissions.isEmpty()) {
            if (mCallback != null) {
                mCallback.onDenied();
            }
        } else {
            if (mCallback != null) {
                mCallback.onGranted();
            }
        }
    }

    public interface Callback {
        void onGranted();               // 需要的权限全部获得
        void onDenied();                // 需要的权限未全获取
    }
}