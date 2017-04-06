package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.tt.android_ble.ui.contract.BtScanContract;
import com.tt.android_ble.ui.manager.INavigator;

import java.util.List;

/**
 * Created by sse-zhaotao on 2017/4/6.
 */

public abstract class BtScanBasePresenter {
    protected BtScanContract.View view;
    protected INavigator navigator;

    public List<BluetoothDevice> deviceList;

    public BtScanBasePresenter(BtScanContract.View view, INavigator navigator) {
        this.view = view;
        this.navigator = navigator;
    }

    protected void scanFinished() {
        if (deviceList.size() == 0) {
            view.displayNoResultLayout();
        } else {
            view.displayResultLayout();
            view.updateData(deviceList);
        }
    }
}
