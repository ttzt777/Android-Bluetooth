package com.tt.android_ble.ui.contract;

import com.tt.android_ble.ui.manager.INavigator;

/**
 * Created by zhaotao on 2017/3/21.
 */

public interface BleDeviceDetailContract {
    interface View {
        void showConnecting();

        void showConnectedLayout();

        void showDisConnectLayout();
    }

    interface Presenter extends IPresenter<INavigator> {
        boolean isBluetoothEnable();

        void connect();

        void disConnect();
    }
}
