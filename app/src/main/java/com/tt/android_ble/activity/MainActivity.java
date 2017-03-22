package com.tt.android_ble.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tt.android_ble.R;
import com.tt.android_ble.ui.fragment.HomeFragment;
import com.tt.android_ble.ui.manager.INavigator;
import com.tt.android_ble.ui.manager.NavigableStateContext;
import com.tt.android_ble.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity implements INavigator{
    private static final String TAG = MainActivity.class.getSimpleName();

    private NavigableStateContext navigableStateContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        StatusBarUtil.setTranslucentForImageView(this, 0x00, null);

        navigableStateContext = new NavigableStateContext(this);

        if (getSupportFragmentManager().findFragmentById(R.id.fl_main_content) == null) {
            navigableStateContext.initMainLayout();
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        navigableStateContext = null;
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public void updateOptionsMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void openFunctionScreen(int navigator) {
        navigableStateContext.navigateToNextScreen(navigator);
    }

    @Override
    public void bleInitScanFragment(FragmentTransaction transaction) {
        navigableStateContext.bleInitScanFragment(transaction);
    }

    @Override
    public void bleShowDetailFragment(String name, String address) {
        navigableStateContext.bleShowDevicesDetailFragment(name, address);
    }
}
