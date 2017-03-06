package com.tt.android_ble.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tt.android_ble.R;
import com.tt.android_ble.ui.adapter.HomeFuncAdapter;
import com.tt.android_ble.ui.decoration.HomeFuncItemDecoration;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements HomeFuncAdapter.Callback{
    private static final String TAG = HomeFragment.class.getCanonicalName();

    @BindView(R.id.tl_home_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_home_menu)
    RecyclerView homeMenuRecyclerView;

    private String[] functions;

    private HomeFuncAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        functions = getResources().getStringArray(R.array.home_functions);
        adapter = new HomeFuncAdapter(functions, this);

        homeMenuRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        homeMenuRecyclerView.addItemDecoration(new HomeFuncItemDecoration(40));
        homeMenuRecyclerView.setAdapter(adapter);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
              //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFuncClick(int position) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
