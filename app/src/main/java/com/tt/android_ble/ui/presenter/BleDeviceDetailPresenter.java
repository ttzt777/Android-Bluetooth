package com.tt.android_ble.ui.presenter;

import com.tt.android_ble.ui.contract.BleDeviceDetailContract;
import com.tt.android_ble.ui.manager.INavigator;

/**
 * Created by zhaotao on 2017/3/21.
 */

public class BleDeviceDetailPresenter implements BleDeviceDetailContract.Presenter {

    private BleDeviceDetailContract.View view;
    private INavigator navigator;
    private String deviceAddress;

    public BleDeviceDetailPresenter(BleDeviceDetailContract.View view, String deviceAddress, INavigator navigator) {
        this.view = view;
        this.deviceAddress = deviceAddress;
        this.navigator = navigator;
    }

    @Override
    public void connect() {
        view.showConnecting();

    }

    @Override
    public void disConnect() {

    }
}
