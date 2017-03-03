package com.tt.android_ble.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Data：2017/3/3
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class HomeFuncItemDecoration extends RecyclerView.ItemDecoration{
    final private static int COLUMNS = 2;
    private int offset;

    public HomeFuncItemDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (position % COLUMNS == 0) {
            // left
            outRect.right = offset / 2;
        } else {
            // right
            outRect.left = offset / 2;
        }

        if (position / COLUMNS == 0) {
            // first line
            outRect.top = offset;
            outRect.bottom = offset / 2;
        } else if (position / COLUMNS == (parent.getAdapter().getItemCount() - 1) / COLUMNS) {
            // last line
            outRect.top = offset / 2;
            outRect.bottom = offset;
        } else {
            outRect.top = offset / 2;
            outRect.bottom = offset / 2;
        }
    }
}
