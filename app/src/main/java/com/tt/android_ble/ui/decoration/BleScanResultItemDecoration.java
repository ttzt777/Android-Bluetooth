package com.tt.android_ble.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tt.android_ble.util.DensityUtil;

/**
 * Created by tt on 2017/3/18.
 */
public class BleScanResultItemDecoration extends RecyclerView.ItemDecoration{

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(DensityUtil.dip2px(parent.getContext(), 5),
                DensityUtil.dip2px(parent.getContext(), 5),
                DensityUtil.dip2px(parent.getContext(), 5),
                DensityUtil.dip2px(parent.getContext(), 5));
    }
}
