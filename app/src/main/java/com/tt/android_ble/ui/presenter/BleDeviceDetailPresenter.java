package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothAdapter;
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

    private static final String BT05_UUID_STRING = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private static final boolean DEBUG = true;

    private BleDeviceDetailContract.View view;

    private BleController bleController;

    private INavigator navigator;
    private String deviceAddress;

    private List<BluetoothGattService> supportServices;

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
    public void onCharacteristicClick(int serviceIndex, int characteristicIndex) {
        if (supportServices == null) {
            Log.e(TAG, "supportServices is null.");
            return;
        }

        Log.i(TAG, "characteristic uuid: " + supportServices.get(serviceIndex)
                .getCharacteristics().get(characteristicIndex).getUuid());

        BluetoothGattCharacteristic characteristic = supportServices.get(serviceIndex).getCharacteristics().get(characteristicIndex);
        int charProperties = characteristic.getProperties();

        if ((charProperties | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            // 该特征可读，读取该特征数据
            bleController.readCharateristis(characteristic);
        }

        if ((charProperties | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            // 可以通知，设置通知
            bleController.setCharacteristicNotifacation(characteristic, true);
        }
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
        supportServices = bleController.getSupportServices();
        if (supportServices == null || supportServices.size() == 0) {
            return;
        }

        List<BleServiceInfo> bleServiceInfoList = getServiceInfoFromGattServices();
        view.showDeviceServicesInfo(bleServiceInfoList);
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

    private List<BleServiceInfo> getServiceInfoFromGattServices() {
        List<BleServiceInfo> bleServiceInfoList = new ArrayList<>();

        for (BluetoothGattService gattService : supportServices) {
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
            bleServiceInfoList.add(bleServiceInfo);
        }

        return bleServiceInfoList;
    }
}
