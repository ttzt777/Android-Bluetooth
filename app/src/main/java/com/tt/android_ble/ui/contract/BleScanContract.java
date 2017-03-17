package com.tt.android_ble.ui.contract;

import android.bluetooth.BluetoothDevice;

import com.tt.android_ble.ui.manager.INavigator;

import java.util.List;

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
public interface BleScanContract {
    interface View {
        void finish();

        void displayScanningLayout();

        void displayNoResultLayout();

        void displayResultLayout();

        void updateData(List<BluetoothDevice> deviceList);
    }

    interface Presenter extends IPresenter<INavigator> {
        void startScan(boolean reScan);

        boolean isBluetoothEnable();
    }
}
