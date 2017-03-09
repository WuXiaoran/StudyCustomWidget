package com.customview.android.learn_the_custom_control.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.android.learn_the_custom_control.R;

/**
 * Created by Administrator on 2017/3/7.
 */

public class ThirdView extends View {

    /**
     * 第一圈的颜色
     */
    private int mFirstColor;
    /**
     * 第二圈的颜色
     */
    private int mSecondColor;
    /**
     * 圈的宽度
     */
    private int mCircleWidth;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前进度
     */
    private int mProgress;
    /**
     * 速度
     */
    private int mSpeed;
    /**
     * 是否应该开始下一个
     */
    private boolean isNext = false;

    public ThirdView(Context context) {
        this(context,null);
    }

    public ThirdView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 必要的初始化，获得自定义的值
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ThirdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ThirdView,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i = 0;i < n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.ThirdView_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.ThirdView_secondColor:
                    mSecondColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.ThirdView_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20
                                            ,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ThirdView_speed:
                    mSpeed = a.getInt(attr,20);//默认20
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        //绘图线程
        new Thread(){
            public void run(){
                while (true){
                    mProgress++;
                    if (mProgress == 360){
                        mProgress = 0;
                        if (!isNext){
                            isNext = true;
                        }else{
                            isNext = false;
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;//获取圆心的x坐标
        int radius = center - mCircleWidth /2;//半径
        mPaint.setStrokeWidth(mCircleWidth);//设置圆环的宽度
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        RectF oval = new RectF(center - radius,center - radius,center + radius,center + radius);//设置圆环的形状及大小
        if (!isNext){
            //第一种颜色的圈跑完整，第二种颜色接着跑
            mPaint.setColor(mFirstColor);//设置圆环的颜色
            canvas.drawCircle(center,center,radius,mPaint);//画出圆环
            mPaint.setColor(mSecondColor);//设置圆环的颜色
            canvas.drawArc(oval,-90,mProgress,false,mPaint);//根据进度画圆弧
        }else{
            mPaint.setColor(mSecondColor);//设置圆环的颜色
            canvas.drawCircle(center,center,radius,mPaint);//画出圆环
            mPaint.setColor(mFirstColor);//设置圆环的颜色
            canvas.drawArc(oval,-90,mProgress,false,mPaint);//根据进度画圆弧
        }
    }
}