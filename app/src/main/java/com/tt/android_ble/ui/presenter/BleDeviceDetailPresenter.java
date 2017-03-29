package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tt.android_ble.bean.BleCharacteristicInfo;
import com.tt.android_ble.bean.BleServiceInfo;
import com.tt.android_ble.bluetooth.le.BleController;
import com.tt.android_ble.ui.adapter.ExpandAdapter;
import com.tt.android_ble.ui.contract.BleDeviceDetailContract;
import com.tt.android_ble.ui.manager.INavigator;
import com.tt.android_ble.util.BleUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tt on 2017/3/21.
 */

public class BleDeviceDetailPresenter implements BleDeviceDetailContract.Presenter, BleController.Callback {
    private static final String TAG = BleDeviceDetailPresenter.class.getSimpleName();

    private static final boolean DEBUG = false;

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
        if (supportServices == null || supportServices.size() == 0) {
            return;
        }

        List<ExpandAdapter.Entry<String, List<BleCharacteristicInfo>>> bleEntryList = getServiceInfoFromGattServices(supportServices);
        view.showDeviceServicesInfo(bleEntryList);
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

    private List<ExpandAdapter.Entry<String, List<BleCharacteristicInfo>>> getServiceInfoFromGattServices(@NonNull List<BluetoothGattService> gattServices) {
        List<BleServiceInfo> bleServiceInfoList = new ArrayList<>();
        List<ExpandAdapter.Entry<String, List<BleCharacteristicInfo>>> bleEntryList = new ArrayList<>();

        for (BluetoothGattService gattService : gattServices) {
            String uuid = gattService.getUuid().toString();
            String type = BleUtil.getServiceType(gattService.getType());
            List<BleCharacteristicInfo> characteristicInfoList = new ArrayList<>();

            if (DEBUG) {
                // Service 字段信息
                Log.i(TAG, "--> service type: " + BleUtil.getServiceType(gattService.getType()));
                Log.i(TAG, "--> include services size: " + gattService.getIncludedServices().size());
                Log.i(TAG, "--> service uuid: " + gattService.getUuid());
            }

            // Characteristic 特征的字段信息
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                String charUuid = gattCharacteristic.getUuid().toString();
                int charPermission = gattCharacteristic.getPermissions();
                int charProperty = gattCharacteristic.getProperties();
                BleCharacteristicInfo characteristicInfo = new BleCharacteristicInfo(charUuid, charPermission, charProperty);
                characteristicInfoList.add(characteristicInfo);

                if (DEBUG) {
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

            BleServiceInfo bleServiceInfo = new BleServiceInfo(uuid, type, characteristicInfoList);
            ExpandAdapter.Entry<String, List<BleCharacteristicInfo>> entry = new ExpandAdapter.Entry<>(uuid, characteristicInfoList);
            bleEntryList.add(entry);
        }

        return bleEntryList;
    }
}
