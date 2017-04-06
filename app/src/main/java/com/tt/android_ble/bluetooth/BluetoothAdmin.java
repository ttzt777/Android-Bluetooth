package com.tt.android_ble.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * -------------------------------------------------
 * Description：获取当前连接的蓝牙设备，扫描附近蓝牙设备
 * Author：TT
 * Since：2017/4/5
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class BluetoothAdmin {
    private static final String TAG = BluetoothAdmin.class.getSimpleName();

    public static final int SCAN_PERIOD = 5000;

    private Context mContext;

    private BluetoothA2dp a2dp;

    private BluetoothAdapter bluetoothAdapter;

    private Handler mHandler;

    private List<BluetoothDevice> deviceList = new ArrayList<>();

    private Callback listener;

    public BluetoothAdmin(Context context) {
        this.mContext = context;
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void startScan() {
        startScan(SCAN_PERIOD);
    }

    public void startScan(int period) {
        if (mHandler != null) {
            return;
        }

        mHandler = new Handler();
        mHandler.postDelayed(scanCompletedRunnable, period);
        deviceList.clear();
        registerScanReceiver();
        bluetoothAdapter.startDiscovery();
    }

    public void stopScan() {
        if (mHandler == null) {
            return;
        }

        mHandler.removeCallbacks(scanCompletedRunnable);
        mHandler = null;
        bluetoothAdapter.cancelDiscovery();
        unregisterScanReceiver();

        if (listener != null) {
            listener.onScanCompleted(deviceList);
        }
    }

    public boolean isScanning() {
        if (mHandler == null) {
            return false;
        }

        return true;
    }

    private BluetoothDevice getConnectedDevice() {
        BluetoothDevice device = null;

        BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if (profile == BluetoothProfile.A2DP) {
                    a2dp = (BluetoothA2dp) proxy;
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {
                if (profile == BluetoothProfile.A2DP) {
                    a2dp = null;
                }
            }
        };

        bluetoothAdapter.getProfileProxy(mContext, mProfileListener, BluetoothProfile.A2DP);

        List<BluetoothDevice> devices = null;
        if (a2dp != null) {
            devices = a2dp.getConnectedDevices();
        }

        if (devices != null && devices.size() > 0) {
            device = devices.get(0);
        }

        return device;
    }

    private void registerScanReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(scanBroadcastReceiver, filter);
    }

    private void unregisterScanReceiver() {
        mContext.unregisterReceiver(scanBroadcastReceiver);
    }

    private Runnable scanCompletedRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };

    private BroadcastReceiver scanBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addDeviceToList(device);
            }
        }

        private void addDeviceToList(BluetoothDevice device) {
            if (deviceList.size() == 0) {
                deviceList.add(device);
                return;
            }

            for (BluetoothDevice btDevice : deviceList) {
                if (btDevice.getAddress().equals(device.getAddress())) {
                    return;
                }
            }

            deviceList.add(device);
        }
    };

    public interface Callback {
        void onScanCompleted(List<BluetoothDevice> devices);
    }
}
