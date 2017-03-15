package com.tt.android_ble.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.tt.android_ble.R;
import com.tt.android_ble.ui.adapter.HomeFuncAdapter;
import com.tt.android_ble.ui.decoration.HomeFuncItemDecoration;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements HomeFuncAdapter.Callback{
    private static final String TAG = HomeFragment.class.getCanonicalName();

    @BindView(R.id.tl_home_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_home_menu)
    RecyclerView homeMenuRecyclerView;

    private String[] functions;

    private HomeFuncAdapter adapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();
        functions = getResources().getStringArray(R.array.home_functions);
        adapter = new HomeFuncAdapter(functions, this);

        homeMenuRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        homeMenuRecyclerView.addItemDecoration(new HomeFuncItemDecoration(40));
        homeMenuRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }

    @Override
    public void onFuncClick(int position) {
        navigator.openFunctionScreen(position);
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
}
