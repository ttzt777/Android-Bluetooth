package com.tt.android_ble.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tt.android_ble.R;
import com.tt.android_ble.app.Constant;
import com.tt.android_ble.util.AppUtil;
import com.tt.android_ble.util.DialogUtil;
import com.tt.android_ble.util.PermissionHandler;
import com.tt.android_ble.util.StatusBarUtil;

public class SplashActivity extends AppCompatActivity implements PermissionHandler.Callback{
    public static final String[] PERMISSIONS = {Constant.PERMISSION_LOCATION};

    private PermissionHandler permissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        StatusBarUtil.setTranslucent(this, 0x00);

        permissionHandler = new PermissionHandler();
        permissionHandler.setCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!permissionHandler.checkPermissions(this, PERMISSIONS)) {
            permissionHandler.requestPermissions(this, PERMISSIONS);
            return;
        }

        timerToStartNextActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied() {
        DialogUtil.showOpenSettingDialog(this, getResources().getString(R.string.bt_need_location_permission), new DialogUtil.DialogListener() {
            @Override
            public void onPositiveButtonClick() {
                AppUtil.openAppSystemSetting(SplashActivity.this);
            }

            @Override
            public void onNegativeButtonClick() {
                finish();
            }
        });
    }

    private void timerToStartNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}