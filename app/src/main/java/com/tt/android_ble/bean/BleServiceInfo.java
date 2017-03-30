package com.tt.android_ble.bean;

import java.util.List;

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
public class BleServiceInfo {
    private String serviceType;
    private String uuid;
    private List<BleCharacteristicInfo> characteristicInfoList;

    public BleServiceInfo(String uuid, String serviceType, List<BleCharacteristicInfo> characteristicInfoList) {
        this.serviceType = serviceType;
        this.uuid = uuid;
        this.characteristicInfoList = characteristicInfoList;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<BleCharacteristicInfo> getCharacteristicInfoList() {
        return characteristicInfoList;
    }

    public void setCharacteristicInfoList(List<BleCharacteristicInfo> characteristicInfoList) {
        this.characteristicInfoList = characteristicInfoList;
    }

    public BleCharacteristicInfo getCharacteristicInfo(int index) {
        return characteristicInfoList.get(index);
    }

    public int getCharacteristicSize() {
        return (null == characteristicInfoList) ? 0 : characteristicInfoList.size();
    }
}
