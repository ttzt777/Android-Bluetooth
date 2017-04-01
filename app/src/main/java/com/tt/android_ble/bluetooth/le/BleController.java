package com.tt.android_ble.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.tt.android_ble.bean.BleServiceInfo;

import java.util.List;

/**
 * Created by zhaotao on 2017/3/21.
 */

public class BleController {
    private static final String TAG = BleController.class.getSimpleName();

    public static final int STATE_DISCONNECTED = BluetoothProfile.STATE_DISCONNECTED;
    public static final int STATE_CONNECTING = BluetoothProfile.STATE_CONNECTING;
    public static final int STATE_CONNECTED = BluetoothProfile.STATE_CONNECTED;
    public static final int STATE_DISCONNECTING = BluetoothProfile.STATE_DISCONNECTING;

    private Context context;

    private BluetoothAdapter btAdapter;

    private BluetoothGatt btGatt;

    private Callback listener;

    private String previousDeviceAddress;   // 前一个连接的设备MAC地址

    private int mConnectionState = STATE_DISCONNECTED;           // 连接状态

    public BleController(BluetoothAdapter adapter, Callback listener) {
        this.btAdapter = adapter;
        this.listener = listener;
    }

    public int getConnectionState() {
        return mConnectionState;
    }

    public boolean isBluetoothEnable() {
        return btAdapter.isEnabled();
    }

    public boolean connect(String address) {
        if (btAdapter == null || address == null) {
            Log.e(TAG, "BluetoothAdapter not initialized or unspecified address");
            return false;
        }

        // 判断是否为刚才断开的设备，如果是可以直接连接
        if (previousDeviceAddress != null && previousDeviceAddress.equals(address) && btGatt != null) {
            Log.d(TAG, "Trying to use an existing btGatt for connection.");
            if (btGatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        // 创建一个新的连接
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.e(TAG, "Device not found. Unable to connect.");
            return false;
        }
        btGatt = device.connectGatt(context, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mConnectionState = STATE_CONNECTING;
        previousDeviceAddress = address;
        return true;
    }

    public void disConnect() {
        if (btAdapter == null || btGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized.");
            return;
        }

        btGatt.disconnect();
    }

    public List<BluetoothGattService> getSupportServices() {
        if (btGatt == null) {
            return null;
        }

        return btGatt.getServices();
    }

    /**
     * @BluetoothGattCallback$onCharacteristicRead
     * @param characteristic
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (btAdapter == null || btGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized.");
            return;
        }

        btGatt.readCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        if (btAdapter == null || btGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized.");
            return;
        }

        btGatt.setCharacteristicNotification(characteristic, enable);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (btAdapter == null || btGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized.");
            return;
        }

        btGatt.writeCharacteristic(characteristic);
    }

    /**
     * 使用完必须释放相应对象
     */
    public void close() {
        if (btGatt == null) {
            return;
        }

        btGatt.close();
        btGatt = null;
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "stateChange === status: " + status);
            mConnectionState = newState;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                listener.onConnect();
                Log.d(TAG, "Connected to GATT server.");
                Log.d(TAG, "Attempting to start service discovery: " + btGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                listener.onDisConnect();
                Log.d(TAG, "Disconnected from GATT server.");
            }
        }

        /**
         * 调用BluetoothGatt.discoverServices()后，发现服务后的回调
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                listener.onServicesDiscover();
            } else {
                Log.e(TAG, "onServicesDiscovered receive: " + status);
            }
        }

        /**
         * 发起读特性后的回调
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(TAG, "read " + characteristic.getUuid().toString() + " " + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                dataAvailable(characteristic);
            }
        }

        /**
         * 发送数据出去后的回调，特性为写的特性
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte[] data = characteristic.getValue();
            if (null != data && data.length > 0) {
                StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data) {
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                Log.i(TAG, "data receive: " + stringBuilder.toString());
            }
        }

        /**
         * 开始通知后，数据变化回调，及接收数据
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            if (null != data && data.length > 0) {
                StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data) {
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                Log.i(TAG, "data receive: " + stringBuilder.toString());
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    };

    private void dataAvailable(BluetoothGattCharacteristic characteristic){
        byte[] data = characteristic.getValue();
        if (null != data && data.length > 0) {
            StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data) {
                stringBuilder.append(String.format("%02X ", byteChar));
            }

            String availableData = new String(data) + "\n" + stringBuilder.toString();
            listener.onDataAvailable(availableData);
        }
    }

    public interface Callback {
        void onConnect();
        void onDisConnect();
        void onServicesDiscover();
        void onDataAvailable(String data);
    }
}
