package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.tt.android_ble.bluetooth.le.BleController;
import com.tt.android_ble.ui.contract.BleDeviceDetailContract;
import com.tt.android_ble.ui.manager.INavigator;

/**
 * Created by zhaotao on 2017/3/21.
 */

public class BleDeviceDetailPresenter implements BleDeviceDetailContract.Presenter, BleController.Callback {

    private BleDeviceDetailContract.View view;

    private BleController bleController;

    private INavigator navigator;
    private String deviceAddress;

    public BleDeviceDetailPresenter(BleDeviceDetailContract.View view, String deviceAddress, INavigator navigator) {
        this.view = view;
        this.deviceAddress = deviceAddress;
        this.navigator = navigator;

        initBleController();
    }

    @Override
    public boolean isBluetoothEnable() {
        return bleController.isBluetoothEnable();
    }

    @Override
    public void connect() {
        view.showConnecting();

        bleController.connect(deviceAddress);
    }

    @Override
    public void disConnect() {

    }

    @Override
    public void onConnect() {
        view.showConnectedLayout();
    }

    @Override
    public void onDisConnect() {

    }

    @Override
    public void onServicesDiscover(BluetoothGatt gatt) {

    }

    private void initBleController() {
        BluetoothManager bluetoothManager = (BluetoothManager) navigator.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            navigator.onBackPressed();
            return;
        }

        bleController = new BleController(bluetoothAdapter, this);
    }
}
