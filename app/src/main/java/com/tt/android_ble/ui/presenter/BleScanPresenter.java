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
import com.tt.android_ble.ui.contract.BtScanContract;
import com.tt.android_ble.ui.manager.INavigator;

import java.util.List;

/**
 *
 */
public class BleScanPresenter extends BtScanBasePresenter implements BtScanContract.Presenter, IBleScanner.Callback {
    private IBleScanner bleScanner;

    private BluetoothAdapter bluetoothAdapter;

    public BleScanPresenter(BtScanContract.View view, INavigator navigator) {
        super(view, navigator);

        initBluetoothScanner();
    }

    @Override
    public void startScan(boolean reScan) {
        if (bleScanner == null) {
            initBluetoothScanner();
        }

        if (reScan && deviceList != null && !bleScanner.isScanning()) {
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

    @Override
    public boolean isBluetoothEnable() {
        return bluetoothAdapter.isEnabled();
    }

    @Override
    protected void scanFinished() {
        super.scanFinished();
    }

    public void clickDevice(int position) {
        BluetoothDevice device = deviceList.get(position);
        String name = device.getName();
        if (name == null) {
            name = navigator.getActivity().getResources().getString(R.string.bt_unknown_device);
        }
        navigator.bleShowDetailFragment(name, device.getAddress());
    }

    @Override
    public void onScanFinish(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;

        scanFinished();
    }

    private void initBluetoothScanner() {
        BluetoothManager btManager = (BluetoothManager) navigator.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = btManager.getAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (bluetoothAdapter.getBluetoothLeScanner() == null) {
                return;
            }
            bleScanner = new BleScannerV21(bluetoothAdapter, this);
        } else {
            bleScanner = new BleScannerV18(bluetoothAdapter, this);
        }
    }
}
