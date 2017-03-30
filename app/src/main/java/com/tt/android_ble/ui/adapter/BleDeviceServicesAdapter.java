package com.tt.android_ble.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tt.android_ble.R;
import com.tt.android_ble.bean.BleCharacteristicInfo;
import com.tt.android_ble.bean.BleServiceInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sse-zhaotao on 2017/3/30.
 */

public class BleDeviceServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = BleDeviceServicesAdapter.class.getSimpleName();

    public static final int ITEM_SERVICE = 0;
    public static final int ITEM_CHARACTERISTIC = 1;

    private List<BleServiceInfo> serviceList;           // 数据集
    private List<Boolean> serviceExpandStatus;          // Service展开状态

    private Callback listener;

    public BleDeviceServicesAdapter() {
        serviceList = new ArrayList<>();
        serviceExpandStatus = new ArrayList<>();
    }

    public BleDeviceServicesAdapter(Callback listener) {
        serviceList = new ArrayList<>();
        serviceExpandStatus = new ArrayList<>();

        this.listener = listener;
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    public void update(List<BleServiceInfo> serviceList) {
        update(serviceList, false);
    }

    public void update(@NonNull List<BleServiceInfo> serviceList, boolean expand) {
        this.serviceList = serviceList;
        serviceExpandStatus.clear();

        for (int i = 0, size = serviceList.size(); i < size; i++) {
            serviceExpandStatus.add(expand);
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM_SERVICE:
                View view = inflater.inflate(R.layout.item_ble_info_service, parent, false);
                viewHolder = new ServiceViewHolder(view);
                break;

            case ITEM_CHARACTERISTIC:
                View itemView = inflater.inflate(R.layout.item_ble_info_characteristic, parent, false);
                viewHolder = new CharacteristicViewHolder(itemView);
                break;

            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int servicePosition = getServicePosition(position);
        int viewType = getItemViewType(position);

        switch (viewType) {
            case ITEM_SERVICE:
                onBindServiceViewHolder(holder, servicePosition);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int newPosition = holder.getAdapterPosition();
                        int newServicePosition = getServicePosition(newPosition);
                        Boolean expandStatus = serviceExpandStatus.get(newServicePosition);
                        ((ServiceViewHolder) holder).mExpand.setSelected(!expandStatus);
                        serviceExpandStatus.set(newServicePosition, !expandStatus);
                        expandService(newPosition, newServicePosition, !expandStatus);
                    }
                });
                break;

            case ITEM_CHARACTERISTIC:
                final int characteristicPosition = getCharacteristicPosition(position);
                onBindCharacteristicViewHolder(holder, servicePosition, characteristicPosition);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.onCharacteristicItemClick(servicePosition, characteristicPosition);
                        }
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0, size = serviceList.size(); i < size; i++) {
            if (serviceExpandStatus.get(i)) {
                count += serviceList.get(i).getCharacteristicSize();
            }

            count += 1;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;      // 当前位置ServiceGroup前面显示的计数
        for (int i = 0, size = serviceList.size(); i < size; i++) {
            if (position == count) {
                return ITEM_SERVICE;
            } else if (serviceExpandStatus.get(i) && position <= serviceList.get(i).getCharacteristicSize() + count) {
                return ITEM_CHARACTERISTIC;
            }
            count += serviceExpandStatus.get(i)? serviceList.get(i).getCharacteristicSize() + 1 : 1;
        }
        return ITEM_SERVICE;
    }

    /**
     * 根据指针位置返回当前处于Service index
     * @param position
     * @return
     */
    private int getServicePosition(int position) {
        int count = 0;      // 当前位置ServiceGroup前面显示的计数
        for (int i = 0, size = serviceList.size(); i < size; i++) {
            if (position == count || (serviceExpandStatus.get(i) && position <= serviceList.get(i).getCharacteristicSize() + count)) {
                return i;
            }
            count += serviceExpandStatus.get(i)? serviceList.get(i).getCharacteristicSize() + 1 : 1;
        }

        return 0;
    }

    private int getCharacteristicPosition(int position) {
        int count = 0;      // 当前位置ServiceGroup前面显示的计数
        for (int i = 0, size = serviceList.size(); i < size; i++) {
            if (position > count && serviceExpandStatus.get(i) && position <= serviceList.get(i).getCharacteristicSize() + count) {
                return position - count - 1;
            }
            count += serviceExpandStatus.get(i)? serviceList.get(i).getCharacteristicSize() + 1 : 1;
        }

        return 0;
    }

    private void onBindServiceViewHolder(RecyclerView.ViewHolder viewHolder, int servicePosition) {
        ServiceViewHolder serviceViewHolder = (ServiceViewHolder) viewHolder;
        serviceViewHolder.mExpand.setSelected(serviceExpandStatus.get(servicePosition));
        serviceViewHolder.mUuid.setText(serviceList.get(servicePosition).getUuid());
    }

    private void onBindCharacteristicViewHolder(RecyclerView.ViewHolder viewHolder, int servicePosition, int characteristicPosition) {
        CharacteristicViewHolder characteristicViewHolder = (CharacteristicViewHolder) viewHolder;
        characteristicViewHolder.mUuid.setText(serviceList.get(servicePosition).getCharacteristicInfo(characteristicPosition).getUuid());
    }

    private void expandService(int position, int servicePosition, boolean newExpandStatus) {
        List<BleCharacteristicInfo> characteristicInfoList = serviceList.get(servicePosition).getCharacteristicInfoList();
        int characteristicSize = (null == characteristicInfoList) ? 0 : characteristicInfoList.size();
        if (newExpandStatus) {
            notifyItemRangeInserted(position + 1, characteristicSize);
        } else {
            notifyItemRangeRemoved(position + 1, characteristicSize);
        }
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
