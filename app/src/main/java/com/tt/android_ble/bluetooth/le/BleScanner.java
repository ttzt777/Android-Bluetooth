package com.tt.android_ble.bluetooth.le;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/17
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public abstract class BleScanner implements IBleScanner {

    private boolean mScanning;

    private Handler mHandler = new Handler();

    private IBleScanner.Callback listener;

    protected List<BluetoothDevice> deviceList = new ArrayList<>();

    public BleScanner(IBleScanner.Callback listener) {
        this.listener = listener;
    }

    @Override
    public abstract boolean isBluetoothEnable();

    @Override
    public boolean isScanning() {
        return mScanning;
    }

    @Override
    public void startScan(){
        if (mScanning) {
            return;
        }

        mScanning = true;

        deviceList.clear();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 扫描完成
                stopScan();
            }
        }, IBleScanner.SCAN_PERIOD);

        startScanImpl();
    }

    @Override
    public void stopScan() {
        if (!mScanning) {
            return;
        }

        stopScanImpl();

        mScanning = false;

        listener.onScanFinish(deviceList);
    }

    public abstract void startScanImpl();

    public abstract void stopScanImpl();
}
