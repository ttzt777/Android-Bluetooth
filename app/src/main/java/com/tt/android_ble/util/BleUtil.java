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
 * 通用UUID参考：http://blog.csdn.net/chenxh515/article/details/45723299
 */

public class BleUtil {
    private static boolean CN_ENABLE = true;

    private static final String[] nameDefault = {
            "Generic Access Profile", "Generic Attribute Profile", "Device Information Service",
            "Device Name", "Appearance", "Peripheral Privacy Flag", "Peripheral Preferred Connection Parameters",
            "Service Change",
            "Manufacturer Name", "Mode Number", "Software Revision", "System ID", "PNP ID"
    };

    private static final String[] nameChinese = {
            "通用访问", "通用属性", "设备信息服务",                               // 0 - 2
            "设备名称", "外围、外观", "外围隐藏标志", "外围优先连接参数",         // 3 - 6
            "服务变更",                                                           // 7
            "制造商名称", "型号参数", "软件版本", "系统识别ID", "三极管ID"        // 8 - 12
    };

    private static String getString(int index) {
        if (nameDefault.length == 0 || nameChinese.length == 0 || nameDefault.length != nameChinese.length) {
            throw new RuntimeException(BleUtil.class.getSimpleName() + " name array init error.");
        }

        if (index >= nameDefault.length || index < 0) {
            throw new RuntimeException(BleUtil.class.getSimpleName() + " size = " + nameDefault.length + " index = " + index);
        }

        if (CN_ENABLE) {
            return nameChinese[index];
        } else {
            return nameDefault[index];
        }
    }

    // UUID 通用定义
    private static HashMap<String, String> attributes = new HashMap<>();
    static {
        /* ----------------------- Service uuid ------------------------------------ */
        // 通用
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", getString(0));       // 通用访问
        attributes.put("00001801-0000-1000-8000-00805f9b34fb", getString(1));       // 通用属性
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", getString(2));       // 设备信息服务

        // 特有 FIXME 未曾验证
        attributes.put("00001803-0000-1000-8000-00805f9b34fb", "Link Loss");                // 连接的损失
        attributes.put("00001802-0000-1000-8000-00805f9b34fb", "Immediate Alert Service");  // 立即提醒服务
        attributes.put("00001804-0000-1000-8000-00805f9b34fb", "Tx Power");                 // 发射功率
        attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");          // 电池服务
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");       // 心率服务

        /* ----------------------- Characteristic uuid ----------------------------- */
        //  in "Generic Access Profile"
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", getString(3));       // 设备名称
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", getString(4));       // 外围、外观
        attributes.put("00002a02-0000-1000-8000-00805f9b34fb", getString(5));       // 外围隐藏标志，只可写00或01
        attributes.put("00002a03-0000-1000-8000-00805f9b34fb", "Reconnection Address");     //
        attributes.put("00002a04-0000-1000-8000-00805f9b34fb", getString(6));       // 外围优先连接参数

       // in "Generic Attribute Profile"
        attributes.put("00002a05-0000-1000-8000-00805f9b34fb", getString(7));       // 服务变更

        // in "Device Information Service"
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", getString(8));       // 制造商名称
        attributes.put("00002a24-0000-1000-8000-00805f9b34fb", getString(9));       // 型号参数
        attributes.put("00002a28-0000-1000-8000-00805f9b34fb", getString(10));      // 软件版本
        attributes.put("00002a23-0000-1000-8000-00805f9b34fb", getString(11));      // 系统识别ID
        attributes.put("00002a50-0000-1000-8000-00805f9b34fb", getString(12));      // 三极管ID

        // FIXME 以下UUID均为验证
        // in "Link Loss"
        attributes.put("00002a06-0000-1000-8000-00805f9b34fb", "Alert Level rw");           // 警戒级别，读写权限

        // in "Immediate Alert Service"
        attributes.put("00002a06-0000-1000-8000-00805f9b34fb", "Alert Level w");            // 警戒级别，只写权限

        // in "Tx Power"
        attributes.put("00002a07-0000-1000-8000-00805f9b34fb", "Tx Power Level");           // 发射功率水平

        // in "Battery Service"
        attributes.put("00002a19-0000-1000-8000-00805f9b34fb", "Battery Level");            // 电池电量
    }
    public static String getNameViaUuid(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    private static HashMap<Integer, String> serviceTypes = new HashMap<>();
    static {
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_PRIMARY, "PRIMARY");
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_SECONDARY, "SECONDARY");
    }
    public static String getServiceType(int type) {
        return serviceTypes.get(type);
    }

    private static HashMap<Integer, String> charPermissions = new HashMap<>();
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

    private static HashMap<Integer, String> charProperties = new HashMap<>();
    static {
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_BROADCAST, "PROPERTY_BROADCAST");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_READ, "PROPERTY_READ");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "PROPERTY_WRITE_NO_RESPONSE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE, "PROPERTY_WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_NOTIFY, "PROPERTY_NOTIFY");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_INDICATE, "PROPERTY_INDICATE"); // 指示
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, "PROPERTY_SIGNED_WRITE");     // 写签名
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS, "PROPERTY_EXTENDED_PROPS"); // 扩展属性
    }
    public static String getCharProperty(int property) {
        return getHashMapValue(charProperties, property);
    }

    private static HashMap<Integer, String> descPermissions = new HashMap<>();
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
