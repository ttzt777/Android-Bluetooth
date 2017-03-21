package com.tt.android_ble.bluetooth.le;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.util.Log;

import java.util.List;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/16
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BleScannerV21 extends BleScanner{
    private static final String TAG = BleScannerV21.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeScanner mScanner;

    public BleScannerV21(BluetoothAdapter mBluetoothAdapter, IBleScanner.Callback listener) {
        super(listener);
        this.mBluetoothAdapter = mBluetoothAdapter;
        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public boolean isBluetoothEnable() {
        return mBluetoothAdapter.isEnabled();
    }

    @Override
    public void startScanImpl() {
        mScanner.startScan(mLeCallback);
    }

    @Override
    public void stopScanImpl() {
        mScanner.stopScan(mLeCallback);
    }

    private ScanCallback mLeCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            addDevice(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (int i = 0; i < results.size(); i++) {
                BluetoothDevice bluetoothDevice = results.get(i).getDevice();
                Log.d(TAG, "device info: name -- " + bluetoothDevice.getName()
                        + "/r/n address -- " + bluetoothDevice.getAddress());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            stopScanByTimer();
        }
    };
}
