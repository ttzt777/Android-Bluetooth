package com.tt.android_ble.ui.contract;

import com.tt.android_ble.bean.BleCharacteristicInfo;
import com.tt.android_ble.bean.BleServiceInfo;
import com.tt.android_ble.ui.adapter.ExpandAdapter;
import com.tt.android_ble.ui.manager.INavigator;

import java.util.List;

/**
 * Created by zhaotao on 2017/3/21.
 */

public interface BleDeviceDetailContract {
    interface View {
        void showConnecting();

        void showConnectedLayout();

        void showDisConnectLayout();

        void showDeviceServicesInfo(List<ExpandAdapter.Entry<String, List<BleCharacteristicInfo>>> bleServiceInfoList);
    }

    interface Presenter extends IPresenter<INavigator> {
        boolean isBluetoothEnable();

        void connect();

        void disConnect();

        void onDestroy();
    }
}
