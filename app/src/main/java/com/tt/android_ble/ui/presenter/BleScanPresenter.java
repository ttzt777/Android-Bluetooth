package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.tt.android_ble.bluetooth.le.BleScannerV18;
import com.tt.android_ble.ui.contract.BleScanContract;
import com.tt.android_ble.ui.manager.INavigator;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/8
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class BleScanPresenter implements BleScanContract.Presenter {

    private BleScanContract.View view;
    private INavigator navigator;

    private BleScannerV18 bleScanner;

    public BleScanPresenter(BleScanContract.View view, INavigator navigator) {
        this.view = view;
        this.navigator = navigator;

        initBluetoothScanner();
    }

    @Override
    public void startScan() {
        view.displayScanningLayout();

    }

    public boolean isBluetoothEnable() {
        return bleScanner.isBluetoothEnable();
    }

    private void initBluetoothScanner() {
        BluetoothManager btManager = (BluetoothManager) navigator.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = btManager.getAdapter();

        if (bluetoothAdapter == null) {
            view.finish();
            return;
        }

        bleScanner = new BleScannerV18(bluetoothAdapter);
    }
}
