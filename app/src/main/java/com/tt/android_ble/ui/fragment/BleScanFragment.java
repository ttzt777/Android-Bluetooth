package com.tt.android_ble.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.tt.android_ble.R;
import com.tt.android_ble.ui.contract.BleScanContract;
import com.tt.android_ble.ui.presenter.BleScanPresenter;

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
public class BleScanFragment extends BaseFragment implements BleScanContract.View {
    private static final String TAG = BleFragment.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    @BindView(R.id.ll_ble_scanning_layout)
    LinearLayout mScanningLayout;

    @BindView(R.id.rv_ble_scan_result)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_no_result_layout)
    LinearLayout mNoResultLayout;

    private BleScanContract.Presenter presenter;

    public BleScanFragment() {
        // Required empty public constructor
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

        presenter = new BleScanPresenter(this, navigator);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!presenter.isBluetoothEnable()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ble_scan, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_ble_scan_refresh) {
            presenter.startScan();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            navigator.onBackPressed();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        mRecyclerView.setVisibility(View.GONE);
        mNoResultLayout.setVisibility(View.GONE);
    }

    @Override
    public void displayNoResultLayout() {
        mScanningLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mNoResultLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayResultLayout() {
        mScanningLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mNoResultLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.bt_ble_scan_again)
    public void onScanAgainClick() {
        presenter.startScan();
    }

    @OnClick(R.id.bt_ble_exit)
    public void onExitClick() {
        navigator.onBackPressed();
    }
}
