package com.tt.android_ble.ui.contract;

import com.tt.android_ble.ui.manager.INavigator;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/15
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public interface BleScanContract {
    interface View {
        void displayScanningLayout();

        void displayNoResultLayout();

        void displayResultLayout();
    }

    interface Presenter extends IPresenter<INavigator> {
        void startScan();
    }
}
