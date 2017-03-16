package com.tt.android_ble.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        StatusBarUtil.setTranslucentForImageView(this, 0x00, null);

        navigableStateContext = new NavigableStateContext(this);

        navigableStateContext.initMainLayout();
    }

    @Override
    public Activity getActivity() {
        return this;
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
    public void bleShowDetailFragment() {
        navigableStateContext.bleShowDevicesDetailFragment();
    }
}
