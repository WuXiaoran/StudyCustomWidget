package com.customview.android.learn_the_custom_control.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.android.learn_the_custom_control.R;

/**
 * Created by Administrator on 2017/3/7.
 */

public class FifthView extends View {

    /**
     * TYPE_CIRCLE / TYPE_ROUND
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    /**
     * 图片
     */
    private Bitmap mSrc;
    /**
     * 圆角的大小
     */
    private int mRadius;
    /**
     * 控件的宽度
     */
    private int mWidth;
    /**
     * 控件的高度
     */
    private int mHeight;

    public FifthView(Context context) {
        this(context,null);
    }

    public FifthView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 初始化自定义属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FifthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FifthView, defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i = 0;i < n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.FifthView_src:
                    mSrc = BitmapFactory.decodeResource(getResources(),a.getResourceId(attr,0));
                    break;
                case R.styleable.FifthView_type:
                    type = a.getInt(attr,0);//默认为Circle
                    break;
                case R.styleable.FifthView_borderRadius:
                    mRadius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20
                                        ,getResources().getDisplayMetrics()));//默认为10dp
                    break;
            }
        }
        a.recycle();
    }

    /**
     * 计算控件的高度和宽度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            mWidth = specSize;
        }else{
            //由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
            if (specMode == MeasureSpec.AT_MOST){
                mWidth = Math.min(desireByImg,specSize);
            }else{
                mWidth = desireByImg;
            }
        }
        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            mHeight = specSize;
        }else{
            //由图片决定的高
            int desire = getPaddingLeft() + getPaddingRight() + mSrc.getHeight();
            if (specMode == MeasureSpec.AT_MOST){
                mHeight = Math.min(desire,specSize);
            }else{
                mHeight = desire;
            }
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        switch (type){
            case TYPE_CIRCLE:
                //如果是TYPE_CIRCLE绘制圆形
                int min = Math.min(mWidth,mHeight);
                /**
                 * 长度如果不一致，按小的值进行压缩
                 */
                mSrc = Bitmap.createScaledBitmap(mSrc,min,min,false);
                canvas.drawBitmap(createCircleImage(mSrc,min),0,0,null);
                break;
            case TYPE_ROUND:
                canvas.drawBitmap(createRoundConerImage(mSrc),0,0,null);
                break;
        }
    }

    /**
     * 根据原图和变长绘制圆形图片
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source,int min){
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min,min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2,min / 2,min / 2,paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source,0,0,paint);
        return target;
    }

    private Bitmap createRoundConerImage(Bitmap source){
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0,0,source.getWidth(),source.getHeight());
        canvas.drawRoundRect(rect,mRadius,mRadius,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source,0,0,paint);
        return target;
    }
}
