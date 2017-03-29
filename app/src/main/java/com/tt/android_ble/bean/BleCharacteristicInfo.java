package com.tt.android_ble.bean;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/29
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class BleCharacteristicInfo {

    private String uuid;
    private int permission;
    private int property;


    public BleCharacteristicInfo(String uuid, int permission, int property) {
        this.uuid = uuid;
        this.permission = permission;
        this.property = property;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property = property;
    }
}
