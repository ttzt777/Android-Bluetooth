package com.tt.android_ble.bluetooth.le;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.os.Build;

/**
 * -------------------------------------------------
 * Description：BLE设备扫描，特别备注：minSDkVersion>=21请使用BluetoothLeScanner
 * Author：TT
 * Since：2017/3/8
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class BleScannerV18 implements IBleScanner{
    public static final int SCAN_PERIOD = 5000;

    private boolean mScanning = false;

    private BluetoothAdapter mBluetoothAdapter;

    public BleScannerV18(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public boolean isBluetoothEnable() {
        return mBluetoothAdapter.isEnabled();
    }

    @SuppressWarnings("deprecation")
    public void startScan() {
        if (mScanning) {
            return;
        }

        mScanning = true;

        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    public void stopScan() {
        if (!mScanning) {
            return;
        }
    }



    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {

        }
    };

}
