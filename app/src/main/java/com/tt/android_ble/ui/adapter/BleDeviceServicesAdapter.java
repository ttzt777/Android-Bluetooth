package com.tt.android_ble.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tt.android_ble.R;
import com.tt.android_ble.bean.BleCharacteristicInfo;
import com.tt.android_ble.bean.BleServiceInfo;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhaotao on 2017/3/21.
 */

public class BleDeviceServicesAdapter extends ExpandAdapter<String, BleCharacteristicInfo>{


    public BleDeviceServicesAdapter(Context context, List<Entry<String, List<BleCharacteristicInfo>>> items) {
        super(context, items);
    }

    @Override
    public BaseViewHolder createGroupHolder(ViewGroup parent) {
        return new ServiceViewHolder(inflateView(parent, R.layout.item_ble_info_service));
    }

    @Override
    public BaseViewHolder createChildHolder(ViewGroup parent) {
        return new ServiceViewHolder(inflateView(parent, R.layout.item_ble_info_characteristic));
    }

    @Override
    public void onBindGroupHolder(BaseViewHolder holder, int groupPosition) {
        ServiceViewHolder serviceViewHolder = (ServiceViewHolder) holder;
        serviceViewHolder.mExpand.setSelected(getGroupExpand(groupPosition));
        serviceViewHolder.mUuid.setText(((BleServiceInfo)getGroup(groupPosition)).getUuid());
    }

    @Override
    public void onBindChildHolder(BaseViewHolder holder, int groupPosition, int position) {
        CharacteristicViewHolder characteristicViewHolder = (CharacteristicViewHolder) holder;
        characteristicViewHolder.mUuid.setText(getChild(groupPosition, position).getUuid());
    }

    @Override
    protected void onGroupExpand(BaseViewHolder holder, boolean expand, int groupPosition) {
        super.onGroupExpand(holder, expand, groupPosition);
        ServiceViewHolder serviceViewHolder = (ServiceViewHolder) holder;
        serviceViewHolder.mExpand.setSelected(expand);
    }

    class ServiceViewHolder extends BaseViewHolder {
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

    class CharacteristicViewHolder extends BaseViewHolder {
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
