package com.customview.android.learn_the_custom_control.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/3/14.
 */

public class NinthView extends RecyclerView{

    /**
     * 记录当前第一个View
     */
    private View mCurrentView;
    /**
     * 滑动回调接口
     */
    private OnItemScrollChangeListener mItemScrollChangeListener;

    public void setOnItemScrollChangeListener(OnItemScrollChangeListener mItemScrollChangeListener){
        this.mItemScrollChangeListener = mItemScrollChangeListener;
    }

    /**
     * 定义接口
     */
    public interface OnItemScrollChangeListener{
        void onChange(View view,int position);
    }

    public NinthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addOnScrollListener(new OnScrollListener() {
            /**
             * 滚动时，判断当前第一个View是否发生变化，发生才回调
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View newView = getChildAt(0);
                if (mItemScrollChangeListener != null){
                    if (newView != null && newView != mCurrentView){
                        mCurrentView = newView;
                        mItemScrollChangeListener.onChange(mCurrentView,getChildLayoutPosition(mCurrentView));
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mCurrentView = getChildAt(0);
        if (mItemScrollChangeListener != null){
            mItemScrollChangeListener.onChange(mCurrentView,getChildLayoutPosition(mCurrentView));
        }
    }

}
