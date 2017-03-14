package com.customview.android.learn_the_custom_control.widgetAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.customview.android.learn_the_custom_control.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class EighthViewAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Integer> mDatas;

    public EighthViewAdapter(Context mContext, List<Integer> mDatas) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
    }
    public int getCount(){
        return mDatas.size();
    }

    public Object getItem(int position){
        return mDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.activity_eighth_item, parent, false);
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.id_eighth_item_imageview);
            viewHolder.mText = (TextView) convertView
                    .findViewById(R.id.id_eighth_item_textview);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mImg.setImageResource(mDatas.get(position));
        viewHolder.mText.setText("some info ");

        return convertView;
    }
    private class ViewHolder
    {
        ImageView mImg;
        TextView mText;
    }
}
