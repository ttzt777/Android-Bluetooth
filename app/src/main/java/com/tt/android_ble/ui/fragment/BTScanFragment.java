package com.tt.android_ble.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tt.android_ble.R;
import com.tt.android_ble.app.Constant;
import com.tt.android_ble.ui.adapter.BleScanResultAdapter;
import com.tt.android_ble.ui.contract.BtScanContract;
import com.tt.android_ble.ui.decoration.BleScanResultItemDecoration;
import com.tt.android_ble.ui.presenter.BleScanPresenter;
import com.tt.android_ble.ui.presenter.BtScanPresenter;
import com.tt.android_ble.util.AppUtil;
import com.tt.android_ble.util.DialogUtil;
import com.tt.android_ble.util.PermissionHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
public class BTScanFragment extends BaseFragment
        implements BtScanContract.View, BleScanResultAdapter.Callback, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = BTScanFragment.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    @BindView(R.id.ll_bt_scanning_layout)
    LinearLayout mScanningLayout;

    @BindView(R.id.tv_scan_text)
    TextView mScanText;

    @BindView(R.id.srl_bt_scan_refresh)
    SwipeRefreshLayout mResultLayout;

    @BindView(R.id.ll_no_result_layout)
    LinearLayout mNoResultLayout;

    @BindView(R.id.tv_scan_no_result)
    TextView mNoResultText;

    private BtScanContract.Presenter presenter;

    private BleScanResultAdapter adapter;

    public BTScanFragment() {
    }

    public static BTScanFragment newInstance(Bundle args) {
        BTScanFragment fragment = new BTScanFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        adapter = new BleScanResultAdapter(this);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_bt_scan_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new BleScanResultItemDecoration());
        mRecyclerView.setAdapter(adapter);

        mResultLayout.setOnRefreshListener(this);

        displayScanningLayout();

        init();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!presenter.isBluetoothEnable()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!PermissionHandler.checkPermissions(getActivity(), Constant.PERMISSION_LOCATION)) {
                showOpenSettingDialog();
                return;
            }
        }

        presenter.startScan(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            navigator.onBackPressed();
            return;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.stopScan();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bt_scan_layout;
    }

    @Override
    public void finish() {
        navigator.onBackPressed();
    }

    @Override
    public void displayScanningLayout() {
        mScanningLayout.setVisibility(View.VISIBLE);
        mResultLayout.setVisibility(View.GONE);
        mNoResultLayout.setVisibility(View.GONE);

        mResultLayout.setRefreshing(false);
    }

    @Override
    public void displayNoResultLayout() {
        mScanningLayout.setVisibility(View.GONE);
        mResultLayout.setVisibility(View.GONE);
        mNoResultLayout.setVisibility(View.VISIBLE);

        mResultLayout.setRefreshing(false);
    }

    @Override
    public void displayResultLayout() {
        mScanningLayout.setVisibility(View.GONE);
        mResultLayout.setVisibility(View.VISIBLE);
        mNoResultLayout.setVisibility(View.GONE);

        mResultLayout.setRefreshing(false);
    }

    @Override
    public void updateData(List<BluetoothDevice> deviceList) {
        adapter.updateData(deviceList);
    }

    @Override
    public void onRefresh() {
        presenter.startScan(true);
    }

    @Override
    public void onBleDeviceClick(int position) {
        presenter.clickDevice(position);
    }

    @Override
    public void onBleDeviceMoreFunc(int position) {

    }

    @OnClick(R.id.bt_bt_scan_again)
    public void onScanAgainClick() {
        presenter.startScan(true);
    }

    @OnClick(R.id.bt_bt_exit)
    public void onExitClick() {
        navigator.onBackPressed();
    }

    private void init() {
        String scanningString = getResources().getString(R.string.bt_ble_scan_index);
        String noResultString = getResources().getString(R.string.bt_ble_scan_noResult);

        if (getArguments() != null) {
            if (getArguments().getInt(BluetoothFragment.BT_TYPE) == BluetoothFragment.TYPE_SPP) {
                scanningString = getResources().getString(R.string.bt_device_scan_index);
                noResultString = getResources().getString(R.string.bt_device_scan_noResult);
                presenter = new BtScanPresenter(this, navigator);
            } else if (getArguments().getInt(BluetoothFragment.BT_TYPE) == BluetoothFragment.TYPE_AUDIO) {
                scanningString = getResources().getString(R.string.bt_device_scan_index);
                noResultString = getResources().getString(R.string.bt_device_scan_noResult);
                presenter = new BleScanPresenter(this, navigator);
            } else {
                presenter = new BleScanPresenter(this, navigator);
            }
        }  else {
            presenter = new BleScanPresenter(this, navigator);
        }

        mScanText.setText(scanningString);
        mNoResultText.setText(noResultString);
    }

    private void showOpenSettingDialog() {
        DialogUtil.showOpenSettingDialog(getContext(), getResources().getString(R.string.bt_need_location_permission), new DialogUtil.DialogListener() {
            @Override
            public void onPositiveButtonClick() {
                AppUtil.openAppSystemSetting(getContext());
            }

            @Override
            public void onNegativeButtonClick() {
                navigator.onBackPressed();
            }
        });
    }
}
