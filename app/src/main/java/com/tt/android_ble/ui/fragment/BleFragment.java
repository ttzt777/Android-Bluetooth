package com.tt.android_ble.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tt.android_ble.R;
import com.tt.android_ble.util.DensityUtil;
import com.tt.android_ble.util.PermissionHandler;

import butterknife.BindView;

public class BleFragment extends BaseFragment {
    private static final String TAG = BleFragment.class.getSimpleName();

    @BindView(R.id.view_status_bar)
    View mStatusBar;

    @BindView(R.id.tl_ble_toolbar)
    Toolbar toolbar;

    public BleFragment() {
        // Required empty public constructor
    }

    public static BleFragment newInstance() {
        BleFragment fragment = new BleFragment();
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

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mStatusBar.getLayoutParams();
        layoutParams.height = DensityUtil.getStatusBarHeight(getContext());
        mStatusBar.setLayoutParams(layoutParams);

        setupToolbar();

        if (!checkSavedFragment(savedInstanceState) && getChildFragmentManager().findFragmentById(R.id.fl_ble_content) == null) {
            navigator.bleInitScanFragment(getChildFragmentManager().beginTransaction());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getChildFragmentManager().findFragmentById(R.id.fl_ble_content) == null) {
            outState.putBoolean("fragmentAlreadyInit", true);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ble_layout;
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
        toolbar.setTitle("BLE");
    }

    private boolean checkSavedFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("fragmentAlreadyInit")) {
            return true;
        }

        return false;
    }
}
