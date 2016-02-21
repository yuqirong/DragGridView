package com.yuqirong.draggridview.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Anyway on 2016/2/19.
 */
public abstract class DragGridAdapter<T> extends BaseAdapter {

    private static final String TAG = "DragGridAdapter";

    private boolean isMove = false;

    private int movePosition = -1;

    private final List<T> list;

    public List<T> getList() {
        return list;
    }

    public DragGridAdapter(List list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "-------------------------------");
        for (T t : list){
            Log.i(TAG, t.toString());
        }
        View view = getItemView(position, convertView, parent);
        if (position == movePosition && isMove) {
            view.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    protected abstract View getItemView(int position, View convertView, ViewGroup parent);

    /**
     * 给item交换位置
     *
     * @param originalPosition item原先位置
     * @param nowPosition      item现在位置
     */
    public void exchangePosition(int originalPosition, int nowPosition, boolean isMove) {
        T t = list.get(originalPosition);
        list.remove(originalPosition);
        list.add(nowPosition, t);
        movePosition = nowPosition;
        this.isMove = isMove;
        notifyDataSetChanged();
    }

//    public void exchange(int originalPosition, int nowPosition) {
//        T t = list.get(originalPosition);
//        list.remove(originalPosition);
//        list.add(nowPosition, t);
//    }

}
