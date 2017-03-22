package com.tt.android_ble.ui.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tt.android_ble.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/17
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class BleScanResultAdapter extends RecyclerView.Adapter<BleScanResultAdapter.ViewHolder>{
    private List<BluetoothDevice> deviceList;

    private Callback listener;

    public BleScanResultAdapter(Callback listener) {
        this.listener = listener;
    }

    public void updateData(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ble_scan_result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BluetoothDevice device = deviceList.get(position);

        if (device.getName() == null) {
            holder.mName.setText(holder.mName.getContext().getResources().getString(R.string.ble_unknown_device));
        } else {
            holder.mName.setText(device.getName());
        }
        holder.mAddress.setText(device.getAddress());

        holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBleDeviceMoreFunc(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBleDeviceClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_ble_scan_name)
        TextView mName;

        @BindView(R.id.tv_item_ble_scan_address)
        TextView mAddress;

        @BindView(R.id.iv_item_ble_scan_more)
        ImageView mMore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Callback {
        void onBleDeviceClick(int position);
        void onBleDeviceMoreFunc(int position);
    }
}
