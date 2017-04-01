package com.tt.android_ble.ui.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import com.tt.android_ble.R;
import com.tt.android_ble.bluetooth.le.BleScannerV18;
import com.tt.android_ble.bluetooth.le.BleScannerV21;
import com.tt.android_ble.bluetooth.le.IBleScanner;
import com.tt.android_ble.ui.contract.BleScanContract;
import com.tt.android_ble.ui.manager.INavigator;

import java.util.List;

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
public class BleScanPresenter implements BleScanContract.Presenter, IBleScanner.Callback {

    private BleScanContract.View view;
    private INavigator navigator;

    private IBleScanner bleScanner;

    private List<BluetoothDevice> deviceList;

    private BluetoothAdapter bluetoothAdapter;

    public BleScanPresenter(BleScanContract.View view, INavigator navigator) {
        this.view = view;
        this.navigator = navigator;

        initBluetoothScanner();
    }

    @Override
    public void startScan(boolean reScan) {
        if (bleScanner == null) {
            initBluetoothScanner();
        }


        if (!reScan && deviceList != null && !bleScanner.isScanning()) {
            return;
        }

        view.displayScanningLayout();
        bleScanner.startScan();
    }

    @Override
    public void stopScan() {
        if (bleScanner == null) {
            initBluetoothScanner();
        }

        bleScanner.stopScan();
    }

    public boolean isBluetoothEnable() {
        return bluetoothAdapter.isEnabled();
    }

    @Override
    public void onScanFinish(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;

        scanFinishProcess();
    }

    public void clickDevice(int position) {
        BluetoothDevice device = deviceList.get(position);
        String name = device.getName();
        if (name == null) {
            name = navigator.getActivity().getResources().getString(R.string.ble_unknown_device);
        }
        navigator.bleShowDetailFragment(name, device.getAddress());
    }

    private void initBluetoothScanner() {
        BluetoothManager btManager = (BluetoothManager) navigator.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = btManager.getAdapter();

        if (bluetoothAdapter == null) {
            view.finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (bluetoothAdapter.getBluetoothLeScanner() == null) {
                return;
            }
            bleScanner = new BleScannerV21(bluetoothAdapter, this);
        } else {
            bleScanner = new BleScannerV18(bluetoothAdapter, this);
        }
    }

    private void scanFinishProcess() {
        if (deviceList.size() == 0) {
            view.displayNoResultLayout();
        } else {
            view.displayResultLayout();
            view.updateData(deviceList);
        }
    }
}
