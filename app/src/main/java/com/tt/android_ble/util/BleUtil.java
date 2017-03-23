package com.tt.android_ble.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sse-zhaotao on 2017/3/23.
 */

public class BleUtil {
    public static HashMap<Integer, String> serviceTypes = new HashMap<>();
    static {
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_PRIMARY, "PRIMARY");
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_SECONDARY, "SECONDARY");
    }
    public static String getServiceType(int type) {
        return serviceTypes.get(type);
    }

    public static HashMap<Integer, String> charPermissions = new HashMap<>();
    static {
        charPermissions.put(0, "UNKNOWN");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ, "PERMISSION_READ");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED, "PERMISSION_READ_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM, "PERMISSION_READ_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE, "PERMISSION_WRITE");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED, "PERMISSION_WRITE_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM, "PERMISSION_WRITE_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED, "PERMISSION_WRITE_SIGNED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM, "PERMISSION_WRITE_SIGNED_MITM");
    }
    public static String getCharPermission(int permission) {
        return getHashMapValue(charPermissions, permission);
    }

    public static HashMap<Integer, String> charProperties = new HashMap<>();
    static {
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_BROADCAST, "PROPERTY_BROADCAST");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_READ, "PROPERTY_READ");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "PROPERTY_WRITE_NO_RESPONSE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE, "PROPERTY_WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_NOTIFY, "PROPERTY_NOTIFY");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_INDICATE, "PROPERTY_INDICATE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, "PROPERTY_SIGNED_WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS, "PROPERTY_EXTENDED_PROPS");
    }
    public static String getCharProperty(int property) {
        return getHashMapValue(charPermissions, property);
    }

    public static HashMap<Integer, String> descPermissions = new HashMap<>();
    static {
        descPermissions.put(0, "UNKNOWN");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ, "PERMISSION_READ");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED, "PERMISSION_READ_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM, "PERMISSION_READ_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE, "PERMISSION_WRITE");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED, "PERMISSION_WRITE_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM, "PERMISSION_WRITE_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED, "PERMISSION_WRITE_SIGNED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM, "PERMISSION_WRITE_SIGNED_MITM");
    }
    public static String getDescPermission(int permission) {
        return getHashMapValue(descPermissions, permission);
    }

    private static String getHashMapValue(HashMap<Integer, String> hashMap, int number) {
        String result = hashMap.get(number);
        if (TextUtils.isEmpty(result)) {
            List<Integer> numbers = getElement(number);
            result = "";
            for(int i = 0; i < numbers.size(); i++){
                result += hashMap.get(numbers.get(i)) + " | ";
            }
        }
        return result;
    }

    /**
     * 位运算结果的反推函数10 -> 2 | 8;
     */
    static private List<Integer> getElement(int number){
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++){
            int b = 1 << i;
            if ((number & b) > 0)
                result.add(b);
        }

        return result;
    }
}
