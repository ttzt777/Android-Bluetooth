package com.tt.android_ble.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tt.android_ble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private Callback listener;

    public HomeFuncAdapter(String[] functions, Callback listener) {
        this.functions = functions;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_func_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String func = functions[position];
        holder.mFunc.setText(func);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFuncClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return functions.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_home_func)
        TextView mFunc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Callback {
        void onFuncClick(int position);
    }
}
