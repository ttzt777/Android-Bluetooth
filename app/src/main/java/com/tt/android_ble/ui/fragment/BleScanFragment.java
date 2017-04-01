package com.tt.android_ble.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.tt.android_ble.R;
import com.tt.android_ble.app.Constant;
import com.tt.android_ble.ui.adapter.BleScanResultAdapter;
import com.tt.android_ble.ui.contract.BleScanContract;
import com.tt.android_ble.ui.decoration.BleScanResultItemDecoration;
import com.tt.android_ble.ui.presenter.BleScanPresenter;
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
public class BleScanFragment extends BaseFragment
        implements BleScanContract.View, BleScanResultAdapter.Callback, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = BleScanFragment.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    @BindView(R.id.ll_ble_scanning_layout)
    LinearLayout mScanningLayout;

    @BindView(R.id.srl_ble_scan_refresh)
    SwipeRefreshLayout mResultLayout;

    @BindView(R.id.ll_no_result_layout)
    LinearLayout mNoResultLayout;

    private BleScanContract.Presenter presenter;

    private BleScanResultAdapter adapter;

    public BleScanFragment() {
    }

    public static BleScanFragment newInstance() {
        BleScanFragment fragment = new BleScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        adapter = new BleScanResultAdapter(this);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_ble_scan_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new BleScanResultItemDecoration());
        mRecyclerView.setAdapter(adapter);

        mResultLayout.setOnRefreshListener(this);

        displayScanningLayout();

        presenter = new BleScanPresenter(this, navigator);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
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
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        presenter.stopScan();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ble_scan_layout;
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

    @OnClick(R.id.bt_ble_scan_again)
    public void onScanAgainClick() {
        presenter.startScan(true);
    }

    @OnClick(R.id.bt_ble_exit)
    public void onExitClick() {
        navigator.onBackPressed();
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
