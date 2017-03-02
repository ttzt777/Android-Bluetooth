package com.tt.android_ble.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Data：2017/3/2
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class HomeFuncAdapter extends RecyclerView.Adapter<HomeFuncAdapter.ViewHolder> {
    private String[] functions;

    public HomeFuncAdapter(String[] functions) {
        this.functions = functions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return functions.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
