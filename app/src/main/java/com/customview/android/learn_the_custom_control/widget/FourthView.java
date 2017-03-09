package com.customview.android.learn_the_custom_control.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.customview.android.learn_the_custom_control.R;

/**
 * Created by Administrator on 2017/3/7.
 */

public class FourthView extends View {

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
    private int mCurrentCount = 3;
    /**
     * 中间的图片
     */
    private Bitmap mImage;
    /**
     * 每个块块间的间隙
     */
    private int mSplitSize;
    /**
     * 个数
     */
    private int mCount;
    private Rect mRect;

    /**
     * 设置移动最小距离
     */
    private static final int minMove = 130;

    public FourthView(Context context) {
        this(context,null);
    }

    public FourthView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 必要的初始化，获得一些自定义的值
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FourthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FourthView,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i = 0;i < n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.FourthView_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.FourthView_secondColor:
                    mSecondColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.FourthView_bg:
                    mImage = BitmapFactory.decodeResource(getResources(),a.getResourceId(attr,0));
                    break;
                case R.styleable.FourthView_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20,
                            getResources().getDisplayMetrics()));
                    break;
                case R.styleable.FourthView_dotCount:
                    mCount = a.getInt(attr,20);//默认20
                    break;
                case R.styleable.FourthView_splitSize:
                    mSplitSize = a.getInt(attr,20);//默认20
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStrokeWidth(mCircleWidth);//设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND);//定义线段断电形状为圆头
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        int center = getWidth() / 2;//获取圆心的x坐标
        int radius = center - mCircleWidth / 2;//半径
        /**
         * 画块
         */
        drawOval(canvas,center,radius);
        /**
         * 计算内切正方形的位置
         */
        int relRadius = radius - mCircleWidth / 2;//获得内圆的半径
        /**
         * 内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.top = (int) ((relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth);
        /**
         * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.left = (int) ((relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth);
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (mImage.getWidth() < Math.sqrt(2) * relRadius){
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2
                    - mImage.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2
                    - mImage.getHeight() * 1.0f / 2);
            mRect.right = (int) (mRect.left + mImage.getWidth());
            mRect.bottom = (int) (mRect.top + mImage.getHeight());
        }
        //绘图
        canvas.drawBitmap(mImage,null,mRect,mPaint);
    }

    /**
     * 根据参数画出每个小块
     * @param canvas
     * @param center
     * @param radius
     */
    private void drawOval(Canvas canvas,int center,int radius){
        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;
        //定义圆弧的形状及大小
        RectF oval = new RectF(center - radius,center - radius,center + radius,center + radius);
        mPaint.setColor(mFirstColor);//设置圆环的颜色
        for (int i = 0;i < mCount;i++){
            canvas.drawArc(oval,i * (itemSize + mSplitSize),itemSize,false,mPaint);//根据进度画圆弧
        }
        mPaint.setColor(mSecondColor);
        for (int i = 0;i < mCurrentCount;i++){
            canvas.drawArc(oval,i * (itemSize + mSplitSize),itemSize,false,mPaint);//根据进度画圆弧
        }
    }

    /**
     * 当前数量+xMove
     */
    public void up(){
        mCurrentCount += xMove;
        //如果mCurrentCount 大于mCount，就让他等于mCount
        if (mCurrentCount >= mCount){
            mCurrentCount = mCount;
        }
        postInvalidate();
    }
    /**
     * 当前数量-xMove
     */
    public void down(){
        mCurrentCount -= xMove;
        //如果mCurrentCount 小于1，就让他等于1
        if (mCurrentCount <= 0){
            mCurrentCount = 0;
        }
        postInvalidate();
    }

    private int xDown,xUp,xMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                xMove = Math.abs(xUp - xDown) / minMove;
                if (xUp > xDown){
                    //下滑
                    down();
                }else{
                    up();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }
}
