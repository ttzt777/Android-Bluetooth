package com.tt.android_ble.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tt.android_ble.R;
import com.tt.android_ble.bean.BleServiceInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sse-zhaotao on 2017/3/30.
 */

public class BleDeviceServicesAdapter extends ExpandAdapter<BleServiceInfo> {
    private static final String TAG = BleDeviceServicesAdapter.class.getSimpleName();

    private Callback listener;

    public BleDeviceServicesAdapter() {
    }

    public BleDeviceServicesAdapter(Callback listener) {
        this.listener = listener;
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateGroupViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ble_info_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ble_info_characteristic, parent, false);
        return new CharacteristicViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(RecyclerView.ViewHolder viewHolder, int groupPosition) {
        ServiceViewHolder serviceViewHolder = (ServiceViewHolder) viewHolder;
        serviceViewHolder.mExpand.setSelected(getGroupExpandStatus(groupPosition));
        serviceViewHolder.mUuid.setText(getGroupInfo(groupPosition).getUuid());
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, final int groupPosition, final int itemPosition) {
        CharacteristicViewHolder characteristicViewHolder = (CharacteristicViewHolder) viewHolder;
        characteristicViewHolder.mUuid.setText(getGroupInfo(groupPosition).getCharacteristicInfo(itemPosition).getUuid());
        characteristicViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCharacteristicItemClick(groupPosition, itemPosition);
            }
        });
    }

    @Override
    public int getItemSizeFromGroup(int groupPosition) {
        return getGroupInfo(groupPosition).getCharacteristicSize();
    }

    @Override
    protected void onGroupExpandStatusChanged(RecyclerView.ViewHolder viewHolder, boolean newExpandStatus, int groupPosition) {
        super.onGroupExpandStatusChanged(viewHolder, newExpandStatus, groupPosition);
        ServiceViewHolder serviceViewHolder = (ServiceViewHolder) viewHolder;
        serviceViewHolder.mExpand.setSelected(newExpandStatus);
    }

    public interface Callback {
        void onCharacteristicItemClick(int servicePosition, int characteristicPosition);
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_ble_info_service_expand)
        ImageView mExpand;

        @BindView(R.id.tv_ble_info_service_name)
        TextView mName;

        @BindView(R.id.tv_ble_info_service_uuid)
        TextView mUuid;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CharacteristicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ble_info_characteristic_name)
        TextView mName;

        @BindView(R.id.tv_ble_info_characteristic_uuid)
        TextView mUuid;

        public CharacteristicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
