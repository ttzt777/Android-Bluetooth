package com.tt.android_ble.ui.presenter;

import com.tt.android_ble.ui.contract.BleScanContract;
import com.tt.android_ble.ui.manager.INavigator;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/8
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class BleScanPresenter implements BleScanContract.Presenter {

    private BleScanContract.View view;
    private INavigator navigator;

    public BleScanPresenter(BleScanContract.View view, INavigator navigator) {
        this.view = view;
        this.navigator = navigator;
    }

    @Override
    public void startScan() {
        view.displayScanningLayout();

    }
}
