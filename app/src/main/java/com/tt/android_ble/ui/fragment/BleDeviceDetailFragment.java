package com.tt.android_ble.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tt.android_ble.R;
import com.tt.android_ble.ui.adapter.BleDeviceServicesAdapter;
import com.tt.android_ble.ui.contract.BleDeviceDetailContract;
import com.tt.android_ble.ui.decoration.BleScanResultItemDecoration;
import com.tt.android_ble.ui.presenter.BleDeviceDetailPresenter;

import butterknife.BindView;

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
public class BleDeviceDetailFragment extends BaseFragment implements BleDeviceDetailContract.View{
    private static final String TAG = BleDeviceDetailFragment.class.getSimpleName();

    private static final String DEVICE_NAME = "device_name";
    private static final String DEVICE_ADDRESS = "device_address";

    @BindView(R.id.tv_ble_detail_name)
    TextView mDeviceName;

    @BindView(R.id.tv_ble_detail_address)
    TextView mDeviceAddress;

    @BindView(R.id.sb_ble_detail_toggle)
    Switch mConnectStatus;

    @BindView(R.id.rv_ble_detail_list)
    RecyclerView mServiceList;

    private BleDeviceDetailContract.Presenter presenter;

    private BleDeviceServicesAdapter adapter;

    private AlertDialog connectingDialog;

    public static BleDeviceDetailFragment newInstance(String name, String address) {
        BleDeviceDetailFragment fragment = new BleDeviceDetailFragment();
        Bundle args = new Bundle();
        args.putString(DEVICE_NAME, name);
        args.putString(DEVICE_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(savedInstanceState);

        presenter = new BleDeviceDetailPresenter(this, mDeviceAddress.getText().toString(), navigator);

        presenter.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ble_device_detail_layout;
    }

    @Override
    public void showConnecting() {
        if (connectingDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setView(R.layout.dialog_connect_to_device);
            connectingDialog = builder.create();
        }

        connectingDialog.show();
    }

    @Override
    public void showConnectedLayout() {
        connectingDialog.dismiss();
        connectingDialog = null;

        mConnectStatus.setChecked(true);
    }

    @Override
    public void showDisConnectLayout() {

    }

    private void init(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mDeviceName.setText(getArguments().getString(DEVICE_NAME));
            mDeviceAddress.setText(getArguments().getString(DEVICE_ADDRESS));
        }

        toggleStatus(false);

        mConnectStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presenter.connect();
                } else {
                    presenter.disConnect();
                }
            }
        });

        adapter = new BleDeviceServicesAdapter();
        mServiceList.setLayoutManager(new LinearLayoutManager(getContext()));
        mServiceList.addItemDecoration(new BleScanResultItemDecoration());
        mServiceList.setAdapter(adapter);
    }

    private void toggleStatus(boolean isConnected) {
        mConnectStatus.setChecked(isConnected);
    }
}
