package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import com.tt.android_ble.bluetooth.le.BleController;
import com.tt.android_ble.ui.contract.BleDeviceDetailContract;
import com.tt.android_ble.ui.manager.INavigator;
import com.tt.android_ble.util.BleUtil;

import java.util.List;

/**
 * Created by zhaotao on 2017/3/21.
 */

public class BleDeviceDetailPresenter implements BleDeviceDetailContract.Presenter, BleController.Callback {
    private static final String TAG = BleDeviceDetailPresenter.class.getSimpleName();

    private BleDeviceDetailContract.View view;

    private BleController bleController;

    private INavigator navigator;
    private String deviceAddress;

    public BleDeviceDetailPresenter(BleDeviceDetailContract.View view, String deviceAddress, INavigator navigator) {
        this.view = view;
        this.deviceAddress = deviceAddress;
        this.navigator = navigator;

        initBleController();
    }

    @Override
    public boolean isBluetoothEnable() {
        return bleController.isBluetoothEnable();
    }

    @Override
    public void connect() {
        if (bleController.getConnectionState() == BleController.STATE_CONNECTED
                || bleController.getConnectionState() == BleController.STATE_CONNECTING) {
            return;
        }
        view.showConnecting();

        bleController.connect(deviceAddress);
    }

    @Override
    public void disConnect() {
        if (bleController.getConnectionState() == BleController.STATE_DISCONNECTED) {
            return;
        }
        bleController.disConnect();
    }

    @Override
    public void onDestroy() {
        if (bleController.getConnectionState() == BleController.STATE_CONNECTED
                || bleController.getConnectionState() == BleController.STATE_CONNECTING) {
            bleController.disConnect();
        }
        bleController.close();
    }

    @Override
    public void onConnect() {
        view.showConnectedLayout();
    }

    @Override
    public void onDisConnect() {
        view.showDisConnectLayout();
    }

    @Override
    public void onServicesDiscover() {
        List<BluetoothGattService> supportServices = bleController.getSupportServices();
        if (supportServices != null && supportServices.size() != 0) {
            displaySupportServices(supportServices);
        }
    }

    private void initBleController() {
        BluetoothManager bluetoothManager = (BluetoothManager) navigator.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            navigator.onBackPressed();
            return;
        }

        bleController = new BleController(bluetoothAdapter, this);
    }

    private void displaySupportServices(List<BluetoothGattService> gattServices) {
        for (BluetoothGattService gattService : gattServices) {
            // Service 字段信息
            Log.i(TAG, "--> service type: " + BleUtil.getServiceType(gattService.getType()));
            Log.i(TAG, "--> include services size: " + gattService.getIncludedServices().size());
            Log.i(TAG, "--> service uuid: " + gattService.getUuid());

            // Characteristic 特征的字段信息
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.d(TAG, "----> char uuid: " + gattCharacteristic.getUuid());
                Log.d(TAG, "----> char permission: " + BleUtil.getCharPermission(gattCharacteristic.getPermissions()));
                Log.d(TAG, "----> char property: " + BleUtil.getCharProperty(gattCharacteristic.getProperties()));

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.d(TAG, "----> char data: " + new String(data));
                }

                // Descriptor 属性的字段信息
                List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                    Log.i(TAG, "------> desc uuid: " + gattDescriptor.getUuid());
                    Log.i(TAG, "------> desc permission: " + gattDescriptor.getPermissions());

                    byte[] desData = gattDescriptor.getValue();
                    if (desData != null && desData.length > 0) {
                        Log.i(TAG, "----> desc data: " + new String(desData));
                    }
                }
            }
        }
    }
}
