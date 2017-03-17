package com.tt.android_ble.bluetooth.le;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public interface IBleScanner {

    int SCAN_PERIOD = 5000;

    boolean isBluetoothEnable();

    boolean isScanning();

    void startScan();

    void stopScan();

    interface Callback {
        void onScanFinish(List<BluetoothDevice> deviceList);
    }
}
