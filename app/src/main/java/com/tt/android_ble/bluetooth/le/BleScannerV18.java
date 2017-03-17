package com.tt.android_ble.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

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
public class BleScannerV18 extends BleScanner{
    private static final String TAG = BleScannerV18.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;

    public BleScannerV18(BluetoothAdapter mBluetoothAdapter, IBleScanner.Callback listener) {
        super(listener);
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public boolean isBluetoothEnable() {
        return mBluetoothAdapter.isEnabled();
    }

    @SuppressWarnings("deprecation")
    public void startScanImpl() {
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    @SuppressWarnings("deprecation")
    public void stopScanImpl() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Log.d(TAG, "device info: name -- " + bluetoothDevice.getName()
                    + "\r\n address -- " + bluetoothDevice.getAddress()
                    + "\r\n rssi == " + i);
            deviceList.add(bluetoothDevice);
        }
    };

}
