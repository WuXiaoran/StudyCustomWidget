package com.customview.android.learn_the_custom_control.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.android.learn_the_custom_control.R;

/**
 * Created by Administrator on 2017/3/6.
 */

public class SenondView extends View {

    /**
     * 文本
     */
    private String titleText;
    /**
     * 文本的颜色
     */
    private int titleTextColor;
    /**
     * 文本大小
     */
    private int titleTextSize;
    /**
     * 绘制时控制整个矩形范围
     */
    private Rect rect;

    /**
     * 控制文本绘制范围
     */
    private Rect rectText;

    /**
     * 设置画笔
     */
    private Paint paint;
    /**
     * 设置图片
     */
    private Bitmap bitmap;

    private int scale;
    /**
     * 图片缩放模式
     */
    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;
    //宽
    int width;
    //高
    int height;

    Typeface typeFace;

    public SenondView(Context context) {
        super(context);
    }

    public SenondView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 获取自定义控件属性
         * */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SenondView);

        int count = typedArray.getIndexCount();

        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SenondView_titleText:
                    titleText = typedArray.getString(attr);
                    break;
                case R.styleable.SenondView_titleTextColor:
                    titleTextColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.SenondView_titleTextSize:
                    titleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SenondView_image:
                    bitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.SenondView_imageScaleType:
                    scale = typedArray.getInt(attr, 0);
                    break;
            }
        }
        //资源回收
        typedArray.recycle();
        rect = new Rect();
        rectText = new Rect();
        paint = new Paint();
        paint.setTextSize(titleTextSize);
        paint.getTextBounds(titleText, 0, titleText.length(), rectText);

    }

    public SenondView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取MeasureSpec的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            paint.setTextSize(titleTextSize);
            paint.getTextBounds(titleText, 0, titleText.length(), rectText);
            //字体宽度
            int textWidth = rectText.width() + getPaddingLeft() + getPaddingRight();
            //图片宽度
            int bitmapWidth = bitmap.getWidth() + getPaddingRight() + getPaddingLeft();

            if (widthMode == MeasureSpec.AT_MOST)// wrap_content
            {
                int desire = Math.max(bitmapWidth, textWidth);
                width = Math.min(desire, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            paint.setTextSize(titleTextSize);
            paint.getTextBounds(titleText, 0, titleText.length(), rectText);
            //图片高度加上字体高度 == 总体高度
            int textHeight = rectText.height() + bitmap.getHeight() + getPaddingBottom() + getPaddingTop();
            height = textHeight;
            if (heightMode == MeasureSpec.AT_MOST)// wrap_content
            {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        /**
         * 边框
         * */
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
        //外矩形宽高
        rect.left = getPaddingLeft();
        rect.right = width - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = height - getPaddingBottom();

        paint.setColor(titleTextColor);
        paint.setStyle(Paint.Style.FILL);


        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        if (rectText.width() > width){
            TextPaint tvpaint = new TextPaint(paint);
            String msg = TextUtils.ellipsize(titleText,tvpaint,width - getPaddingLeft() -
                                    getPaddingRight(),TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),height - getPaddingBottom(),paint);
        }else{
            //正常情况，将字体居中
            canvas.drawText(titleText,width / 2 - rectText.width() * 1.0f/2,
                    height - getPaddingBottom(),paint);
        }

        //减去字体的高
        rect.bottom -= rectText.height();
        if (scale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(bitmap, null, rect, paint);
        } else {
            //计算居中的矩形范围
            rect.left = width / 2 - bitmap.getWidth() / 2;
            rect.right = width / 2 + bitmap.getWidth() / 2;
            rect.top = (height - rectText.height()) / 2 - bitmap.getHeight() / 2;
            rect.bottom = (height - rectText.height()) / 2 + bitmap.getHeight() / 2;

            canvas.drawBitmap(bitmap, null, rect, paint);
        }

    }
}
