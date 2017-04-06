package com.tt.android_ble.ui.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tt.android_ble.R;
import com.tt.android_ble.util.DensityUtil;

import butterknife.BindView;

public class BluetoothFragment extends BaseFragment {
    private static final String TAG = BluetoothFragment.class.getSimpleName();

    public static final String BT_TYPE = "bt_type";
    public static final int TYPE_BLE = 0;
    public static final int TYPE_SPP = 1;
    public static final int TYPE_AUDIO = 2;

    @BindView(R.id.view_status_bar)
    View mStatusBar;

    @BindView(R.id.tl_bt_toolbar)
    Toolbar toolbar;

    private int currentType = TYPE_BLE;

    public BluetoothFragment() {
        // Required empty public constructor
    }

    public static BluetoothFragment newInstance(int type) {
        BluetoothFragment fragment = new BluetoothFragment();
        Bundle args = new Bundle();
        args.putInt(BT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentType = getArguments().getInt(BT_TYPE, TYPE_BLE);
            if (!(currentType >= TYPE_BLE && currentType <= TYPE_AUDIO)) {
                throw new RuntimeException(BluetoothFragment.class.getSimpleName() + "current bt type error.");
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mStatusBar.getLayoutParams();
        layoutParams.height = DensityUtil.getStatusBarHeight(getContext());
        mStatusBar.setLayoutParams(layoutParams);

        setupToolbar();

        if (!checkSavedFragment(savedInstanceState) && getChildFragmentManager().findFragmentById(R.id.fl_bt_content) == null) {
            navigator.btInitScanFragment(getChildFragmentManager().beginTransaction(), getArguments());
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.replace(R.id.fl_ble_content, BTScanFragment.newInstance(), BTScanFragment.class.getSimpleName());
//            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void onSaveInstanceState(Bundle outState) {
        if (getChildFragmentManager().findFragmentById(R.id.fl_bt_content) == null) {
            outState.putBoolean("fragmentAlreadyInit", true);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bt_layout;
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(android.support.design.R.drawable.abc_ic_ab_back_material);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.onBackPressed();
            }
        });

        if (currentType == TYPE_AUDIO) {
            toolbar.setTitle("音频");
        } else if (currentType == TYPE_SPP) {
            toolbar.setTitle("SPP");
        } else {
            toolbar.setTitle("BLE");
        }
    }

    private boolean checkSavedFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("fragmentAlreadyInit")) {
            return true;
        }

        return false;
    }
}
