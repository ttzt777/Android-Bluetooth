package com.tt.android_ble.ui.fragment;

import com.tt.android_ble.R;

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
public class BleDeviceDetailFragment extends BaseFragment {

    public static BleDeviceDetailFragment newInstance() {
        BleDeviceDetailFragment fragment = new BleDeviceDetailFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ble_device_detail_layout;

    }
}
