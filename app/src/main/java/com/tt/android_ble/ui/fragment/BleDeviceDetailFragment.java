package com.tt.android_ble.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tt.android_ble.R;

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
public class BleDeviceDetailFragment extends BaseFragment {
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

        init(savedInstanceState);
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

    public void toggleStatus(boolean isConnected) {
        mConnectStatus.setChecked(isConnected);
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

            }
        });
    }
}
