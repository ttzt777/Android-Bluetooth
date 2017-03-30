/**
 * RecyclerView实现ExpandListView的Adapter
 * 泛型K -- 为数据类型，该数据类型结构为属性加List
 * List中装载了次级数据类型
 * 举例 K表示班级，List中装载班级中的学生bean
 */
package com.tt.android_ble.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tt on 2017/3/30.
 */
public abstract class ExpandAdapter<K> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int VIEW_TYPE_UNKNOWN = -1;
    private final static int VIEW_TYPE_GROUP = 0;
    private final static int VIEW_TYPE_ITEM = 1;

    private List<K> dataList;                           // 数据集
    private List<Boolean> expandStatusList;             // Group展开状态

    public ExpandAdapter() {
        dataList = new ArrayList<>();
        expandStatusList = new ArrayList<>();
    }

    /**
     * 数据更新
     * @param dataList 泛型数据集
     */
    public void update(List<K> dataList) {
        update(dataList, false);
    }

    /**
     * 数据更新
     * @param dataList 泛型数据集
     * @param expand 初始状态
     */
    public void update(@NonNull List<K> dataList, boolean expand) {
        this.dataList = dataList;
        expandStatusList.clear();

        for (int i = 0, size = dataList.size(); i < size; i++) {
            expandStatusList.add(expand);
        }

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_GROUP:
                viewHolder = onCreateGroupViewHolder(parent);
                break;

            case VIEW_TYPE_ITEM:
                viewHolder = onCreateItemViewHolder(parent);
                break;

            default:
                throw new RuntimeException(ExpandAdapter.class.getSimpleName() + " can not createViewHolder, because unknown view type.");
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int groupPosition = getGroupPosition(position);
        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_TYPE_GROUP:
                onBindGroupViewHolder(holder, groupPosition);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 此时的position为之前传入监听时的position而非当前position，需重新获取
                        int newPosition = holder.getAdapterPosition();
                        int newGroupPosition = getGroupPosition(newPosition);
                        Boolean newExpandStatus = !expandStatusList.get(newGroupPosition); // 状态置反
                        onGroupExpandStatusChanged(holder, newExpandStatus, groupPosition);
                        expandStatusList.set(newGroupPosition, newExpandStatus);
                        expandGroup(newPosition, newGroupPosition, newExpandStatus);
                    }
                });
                break;

            case VIEW_TYPE_ITEM:
                final int itemPosition = getItemPosition(position);
                onBindItemViewHolder(holder, groupPosition, itemPosition);
                break;

            default:
                throw new RuntimeException(ExpandAdapter.class.getSimpleName() + " can not createViewHolder, because unknown view type.");
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0, size = dataList.size(); i < size; i++) {
            if (expandStatusList.get(i)) {
                count += getItemSizeFromGroup(i);
            }

            count += 1;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;      // 当前位置ServiceGroup前面显示的计数
        for (int i = 0, size = dataList.size(); i < size; i++) {
            if (position == count) {
                return VIEW_TYPE_GROUP;
            } else if (expandStatusList.get(i) && position <= getItemSizeFromGroup(i) + count) {
                return VIEW_TYPE_ITEM;
            }
            count += expandStatusList.get(i)? getItemSizeFromGroup(i) + 1 : 1;
        }
        return VIEW_TYPE_UNKNOWN;
    }

    /**
     * 创建Group的ViewHolder
     * @param parent 父容器
     * @return ViewHolder
     */
    public abstract RecyclerView.ViewHolder onCreateGroupViewHolder(ViewGroup parent);

    /**
     * 创建Item的ViewHolder
     * @param parent 父容器
     * @return ViewHolder
     */
    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent);

    /**
     * 显示Group中的信息
     * @param viewHolder
     * @param groupPosition
     */
    public abstract void onBindGroupViewHolder(RecyclerView.ViewHolder viewHolder, int groupPosition);

    /**
     * 显示Item中的信息
     * @param viewHolder
     * @param groupPosition
     * @param itemPosition
     */
    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int groupPosition, int itemPosition);

    /**
     * 获取泛型K中的List size
     * @param groupPosition
     * @return
     */
    public abstract int getItemSizeFromGroup(int groupPosition);

    /**
     * Group的Expand状态改变
     * @param viewHolder
     * @param newExpandStatus
     * @param groupPosition
     */
    protected void onGroupExpandStatusChanged(RecyclerView.ViewHolder viewHolder, boolean newExpandStatus, int groupPosition) {

    }

    /**
     * 获取泛型数据
     * @param groupPosition
     * @return
     */
    protected K getGroupInfo(int groupPosition) {
        return dataList.get(groupPosition);
    }

    /**
     * 获取Group的Expand状态
     * @param groupPosition
     * @return
     */
    protected boolean getGroupExpandStatus(int groupPosition) {
        return expandStatusList.get(groupPosition);
    }

    /**
     * 根据当前项的位置确定属于的Group位置
     * @param position
     * @return
     */
    private int getGroupPosition(int position) {
        int count = 0;      // 当前位置ServiceGroup前面显示的计数
        for (int i = 0, size = dataList.size(); i < size; i++) {
            if (position == count || (expandStatusList.get(i) && position <= getItemSizeFromGroup(i) + count)) {
                return i;
            }
            count += expandStatusList.get(i)? getItemSizeFromGroup(i) + 1 : 1;
        }

        return 0;
    }

    /**
     * 根据当前项的位置确定Group中Item的位置
     * @param position
     * @return
     */
    private int getItemPosition(int position) {
        int count = 0;      // 当前位置ServiceGroup前面显示的计数
        for (int i = 0, size = dataList.size(); i < size; i++) {
            if (position > count && expandStatusList.get(i) && position <= getItemSizeFromGroup(i) + count) {
                return position - count - 1;
            }
            count += expandStatusList.get(i)? getItemSizeFromGroup(i) + 1 : 1;
        }

        return 0;
    }

    /**
     * Group状态改变，增加或者减少RecyclerView中的项
     * @param position
     * @param groupPosition
     * @param newExpandStatus
     */
    private void expandGroup(int position, int groupPosition, boolean newExpandStatus) {
        int itemSize = getItemSizeFromGroup(groupPosition);
        if (newExpandStatus) {
            notifyItemRangeInserted(position + 1, itemSize);
        } else {
            notifyItemRangeRemoved(position + 1, itemSize);
        }
    }
}
