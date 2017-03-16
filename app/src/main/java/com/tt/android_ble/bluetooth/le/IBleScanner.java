package com.tt.android_ble.bluetooth.le;

import android.bluetooth.BluetoothDevice;

public interface IBleScanner {

    boolean isBluetoothEnable();

    void startScan();

    void stopScan();

    interface Callback {
        void onDeviceScan(BluetoothDevice device);
    }
}
