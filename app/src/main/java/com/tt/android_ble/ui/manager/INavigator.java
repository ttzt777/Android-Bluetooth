package com.tt.android_ble.ui.manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/15
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public interface INavigator {

    Activity getActivity();

    void updateOptionsMenu();

    void onBackPressed();

    void openFunctionScreen(int navigator);

    void btInitScanFragment(FragmentTransaction transaction, Bundle args);

    void bleShowDetailFragment(String name, String address);
}
