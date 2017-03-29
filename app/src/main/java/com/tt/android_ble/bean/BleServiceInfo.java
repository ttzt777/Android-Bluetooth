package com.tt.android_ble.bean;

import com.tt.android_ble.ui.adapter.ExpandAdapter;

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
public class BleServiceInfo extends ExpandAdapter.Entry<String, List<BleCharacteristicInfo>>{
    private String serviceType;
    private String uuid;

    public List<BleCharacteristicInfo> getCharacteristicInfoList() {
        return characteristicInfoList;
    }

    public void setCharacteristicInfoList(List<BleCharacteristicInfo> characteristicInfoList) {
        this.characteristicInfoList = characteristicInfoList;
    }

    private List<BleCharacteristicInfo> characteristicInfoList;

    public BleServiceInfo(String uuid, String serviceType, List<BleCharacteristicInfo> characteristicInfoList) {
        super(uuid, characteristicInfoList);
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
}
