package com.tt.android_ble.ui.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.tt.android_ble.R;
import com.tt.android_ble.activity.MainActivity;
import com.tt.android_ble.ui.anim.Transitions;
import com.tt.android_ble.ui.fragment.BleDeviceDetailFragment;
import com.tt.android_ble.ui.fragment.BleFragment;
import com.tt.android_ble.ui.fragment.BleScanFragment;
import com.tt.android_ble.ui.fragment.HomeFragment;

/**
 * -------------------------------------------------
 * Description：管理Activity中Fragment的切换
 * Author：TT
 * Since：2017/3/14
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class NavigableStateContext {
    private static final String TAG = NavigableStateContext.class.getSimpleName();

    public static final int NAVIGATOR_TO_BLE = 0;
    public static final int NAVIGATOR_TO_SPP = 1;
    public static final int NAVIGATOR_TO_AUDIO = 2;
    public static final int NAVIGATOT_TO_DEVICES = 3;

    private final MainActivity mActivity;

    public NavigableStateContext (MainActivity activity) {
        mActivity = activity;
    }

    public void initMainLayout() {
        changeFragment(HomeFragment.newInstance());
    }

    public void navigateToNextScreen(int navigator) {
        switch (navigator) {
            case NAVIGATOR_TO_BLE:
                changeFragment(BleFragment.newInstance(), Transitions.ENTER_FORM_RIGHT, true);
                break;

            case NAVIGATOR_TO_SPP:

                break;

            case NAVIGATOR_TO_AUDIO:

                break;

            case NAVIGATOT_TO_DEVICES:

                break;

            default:
                break;
        }
    }

    public void bleInitScanFragment(FragmentTransaction transaction) {
        changeFragment(BleScanFragment.newInstance(), transaction,  R.id.fl_ble_content, null, true);
    }

    public void bleShowDevicesDetailFragment(String name, String address) {
        changeFragment(BleDeviceDetailFragment.newInstance(name, address), null, R.id.fl_ble_content, Transitions.ENTER_FORM_RIGHT, true);
    }

    private void changeFragment(Fragment fragment) {
        changeFragment(fragment, null, R.id.fl_main_content, null, false);
    }

    private void changeFragment(Fragment fragment, FragmentTransaction transaction, int layoutId) {
        changeFragment(fragment, null, layoutId, null, false);
    }

    private void changeFragment(Fragment fragment, Transitions transition, boolean addToBackStack) {
        changeFragment(fragment, null, R.id.fl_main_content, transition, addToBackStack);
    }

    private void changeFragment(Fragment fragment, FragmentTransaction transaction, int layoutId, Transitions transition, boolean addToBackStack) {
        Log.d(TAG, "changeFragment: " + fragment.getClass().getSimpleName());

        if (transaction == null) {
            transaction = mActivity.getSupportFragmentManager().beginTransaction();
        }
        if (transition != null) {
            Transitions.applyTransition(transaction, transition);
        }
        transaction.replace(layoutId, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commitAllowingStateLoss();
    }
}
