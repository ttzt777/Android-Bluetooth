package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothDevice;

import com.tt.android_ble.bluetooth.BluetoothAdmin;
import com.tt.android_ble.ui.contract.BtScanContract;
import com.tt.android_ble.ui.manager.INavigator;

import java.util.List;

/**
 * Created by tt on 2017/4/6.
 */

public class BtScanPresenter extends BtScanBasePresenter implements BtScanContract.Presenter, BluetoothAdmin.Callback{
    private BluetoothAdmin btAdmin;

    public BtScanPresenter(BtScanContract.View view, INavigator navigator) {
        super(view, navigator);

        btAdmin = new BluetoothAdmin(navigator.getActivity());
        btAdmin.setListener(this);
    }

    @Override
    public void startScan(boolean reScan) {
        if (!reScan && deviceList != null && !btAdmin.isScanning()) {
            return;
        }

        view.displayScanningLayout();
        btAdmin.startScan();
    }

    @Override
    public void stopScan() {
        btAdmin.stopScan();
    }

    @Override
    public boolean isBluetoothEnable() {
        return btAdmin.isBluetoothEnabled();
    }

    @Override
    public void clickDevice(int position) {

    }

    @Override
    public void onScanCompleted(List<BluetoothDevice> devices) {
        deviceList = devices;

        scanFinished();
    }
}
