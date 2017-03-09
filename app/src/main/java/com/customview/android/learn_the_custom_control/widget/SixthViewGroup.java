package com.customview.android.learn_the_custom_control.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.customview.android.learn_the_custom_control.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体包含n*n个SixthView,每个SixthView间间隔mMarginBetweenLockView，
 * 最外层的SixthView与容器存在mMarginBetweenLockView的外边距
 *
 * 关于SixthView的边长（n*n）： n * mSixthViewWidth + ( n + 1 ) *
 * mMarginBetweenLockView = mWidth ; 得：mSixthViewWidth = 4 * mWidth / ( 5
 * * mCount + 1 ) 注：mMarginBetweenLockView = mSixthViewWidth * 0.25 ;
 *
 *
 */

public class SixthViewGroup extends RelativeLayout {

    private static final String TAG = "SixthViewGroup";
    /**
     * 保存所有的SixthView
     */
    private SixthView[] mSixthViews;
    /**
     * 每个边上的SixthView的个数
     */
    private int mCount = 4;
    /**
     * 存储答案
     */
    private int[] mAnswer = { 0, 1, 2, 5, 8 };
    /**
     * 保存用户选中的SixthView的id
     */
    private List<Integer> mChoose = new ArrayList<Integer>();

    private Paint mPaint;
    /**
     * 每个SixthView中间的间距 设置为：mSixthViewWidth * 25%
     */
    private int mMarginBetweenLockView = 30;
    /**
     * SixthView的边长 4 * mWidth / ( 5 * mCount + 1 )
     */
    private int mSixthViewWidth;

    /**
     * SixthView无手指触摸的状态下内圆的颜色
     */
    private int mNoFingerInnerCircleColor = 0xFF939090;
    /**
     * SixthView无手指触摸的状态下外圆的颜色
     */
    private int mNoFingerOuterCircleColor = 0xFFE0DBDB;
    /**
     * SixthView手指触摸的状态下内圆和外圆的颜色
     */
    private int mFingerOnColor = 0xFF378FC9;
    /**
     * SixthView手指抬起的状态下内圆和外圆的颜色
     */
    private int mFingerUpColor = 0xFFFF0000;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;

    private Path mPath;
    /**
     * 指引线的开始位置x
     */
    private int mLastPathX;
    /**
     * 指引线的开始位置y
     */
    private int mLastPathY;
    /**
     * 指引下的结束位置
     */
    private Point mTmpTarget = new Point();

    /**
     * 最多尝试次数
     */
    private int mTryTimes = 4;

    /**
     * 回调接口
     */
    private OnSixthViewListener mOnSixthViewListener;

    public SixthViewGroup(Context context) {
        this(context,null);
    }

    public SixthViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SixthViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获取自定义的参数值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SixthView,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i = 0;i < n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.SixthView_color_no_finger_inner_circle:
                    mNoFingerInnerCircleColor = a.getColor(attr,mNoFingerInnerCircleColor);
                    break;
                case R.styleable.SixthView_color_no_finger_outer_circle:
                    mNoFingerOuterCircleColor = a.getColor(attr,mNoFingerOuterCircleColor);
                    break;
                case R.styleable.SixthView_color_finger_on:
                    mFingerOnColor = a.getColor(attr,mFingerOnColor);
                    break;
                case R.styleable.SixthView_color_finger_up:
                    mFingerUpColor = a.getColor(attr,mFingerUpColor);
                    break;
                case R.styleable.SixthView_count:
                    mCount = a.getInt(attr,3);
                    break;
                case R.styleable.SixthView_tryTimes:
                    mTryTimes = a.getInt(attr,5);
                    break;
            }
        }
        a.recycle();
        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        //纠正成正方形
        mHeight = mWidth = mWidth < mHeight ? mWidth : mHeight;
        //初始化mSixthViews
        if (mSixthViews == null){
            mSixthViews = new SixthView[mCount * mCount];
            //计算每个SixthView的宽度
            mSixthViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
            //计算每个SixthView的间距
            mMarginBetweenLockView = (int) (mSixthViewWidth * 0.25);
            //设置画笔的宽度为SixthView的内圆直径稍微小点
            mPaint.setStrokeWidth(mSixthViewWidth * 0.29f);

            for (int i = 0;i < mSixthViews.length;i++){
                //初始化每个SixthView
                mSixthViews[i] = new SixthView(getContext(),mNoFingerInnerCircleColor,mNoFingerOuterCircleColor,
                        mFingerOnColor,mFingerUpColor);
                mSixthViews[i].setId(i + 1);
                //设置参数，主要是定位SixthView间的位置
                RelativeLayout.LayoutParams lockerParams = new RelativeLayout.LayoutParams(mSixthViewWidth,
                        mSixthViewWidth);
                //不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF,mSixthViews[i - 1].getId());
                }
                //从第二行开始，设置为上一行同一位置view的下面
                if (i > mCount - 1){
                    lockerParams.addRule(RelativeLayout.BELOW,mSixthViews[i - mCount].getId());
                }
                //设置右下左上的边距
                int rightMargin = mMarginBetweenLockView;
                int bottomMargin = mMarginBetweenLockView;
                int leftMargin = 0;
                int topMargin = 0;
                /**
                 * 每个view都有右外边距的底外边距，第一行的有上外边距，第一列的有左外边距
                 */
                if (i >= 0 && i < mCount){//第一行
                    topMargin = mMarginBetweenLockView;
                }
                if (i % mCount == 0){//第一列
                    leftMargin = mMarginBetweenLockView;
                }
                lockerParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
                mSixthViews[i].setMode(SixthView.Mode.STATUS_NO_FINGER);
                addView(mSixthViews[i],lockerParams);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                //重置
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                mPaint.setColor(mFingerOnColor);
                mPaint.setAlpha(50);
                SixthView child = getChildIdByPos(x,y);
                if (child != null){
                    int cId = child.getId();
                    if (!mChoose.contains(cId)){
                        mChoose.add(cId);
                        child.setMode(SixthView.Mode.STATUS_FINGER_ON);
                        if (mOnSixthViewListener != null){
                            mOnSixthViewListener.onBlockSelected(cId);
                            //设置指引线的起点
                            mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
                            mLastPathY = child.getTop() / 2 + child.getBottom() / 2;
                            if (mChoose.size() == 1){//当前添加为第一个
                                mPath.moveTo(mLastPathX,mLastPathY);
                            }else{
                                //非第一个，将两者使用线连上
                                mPath.lineTo(mLastPathX,mLastPathY);
                            }
                        }
                    }
                }
                //指引线的终点
                mTmpTarget.x = x;
                mTmpTarget.y = y;
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(mFingerUpColor);
                mPaint.setAlpha(50);
                this.mTryTimes--;
                //回调是否成功
                if (mOnSixthViewListener != null && mChoose.size() > 0){
                    mOnSixthViewListener.onGestureEvent(checkAnswer());
                    if (this.mTryTimes == 0){
                        mOnSixthViewListener.onUnmatchedExceedBoundary();
                    }
                }

                //将终点设置位置为起点，即取消指引线
                mTmpTarget.x = mLastPathX;
                mTmpTarget.y = mLastPathY;
                //改变子元素的状态为UP
                changeItemMode();
                //计算每个元素中箭头需要旋转的角度
                for(int i = 0;i < mChoose.size();i++){
                    Log.e("i",i + "");
                    Log.e("i",i+1 + "");
                    int childId = mChoose.get(i);
                    SixthView startChild = (SixthView) findViewById(childId);
                    if (i != mChoose.size() - 1){
                        int nextChildId = mChoose.get(i + 1);
                        SixthView nextChild = (SixthView) findViewById(nextChildId);
                        int dx = nextChild.getLeft() - startChild.getLeft();
                        int dy = nextChild.getTop() - startChild.getTop();
                        //计算角度      Math.atan2(dy,dx)计算两个点的连线与X坐标轴的夹角
                        //              Math.toDegrees(x)传回将angrad径度转换成角度
                        int angle = (int) (Math.toDegrees(Math.atan2(dy,dx)) + 90);
                        startChild.setArrowDegree(angle);
                    }else{
                        //如果是最后一个,就不连线了
                        startChild.setArrowDegree(-1);
                    }
                }
                break;
        }
        invalidate();
        return true;
    }

    private void changeItemMode(){
        for (SixthView sixthView : mSixthViews){
            if (mChoose.contains(sixthView.getId())){
                sixthView.setMode(SixthView.Mode.STATUS_FINGER_UP);
            }
        }
    }

    /**
     * 做一些必要的重置
     */
    private void reset(){
        mChoose.clear();
        mPath.reset();
        for (SixthView sixthView : mSixthViews){
            sixthView.setMode(SixthView.Mode.STATUS_NO_FINGER);
            sixthView.setArrowDegree(-1);
        }
    }

    /**
     * 检查用户绘制的手势是否正确
     * @return
     */
    private boolean checkAnswer(){
        if (mAnswer.length != mChoose.size()) {
            return false;
        }
        for(int i = 0;i < mAnswer.length;i++){
            if (mAnswer[i] != mChoose.get(i)){
                return false;
            }
        }
        return true;
    }
    /**
     * 检查当前左边是否在child中
     * @param child
     * @param x
     * @param y
     * @return
     */
    private boolean checkPositionInChild(View child,int x,int y){
        //设置了内边距，即x,y必须落入下SixthView的内部中间的小区域中，
        //可以通过调整padding使得x,y落入范围不变大，或者不设置padding
        int padding = (int) (mSixthViewWidth * 0.15);
        if (x >= child.getLeft() + padding && x <= child.getRight() - padding
                && y >= child.getTop() + padding
                && y <= child.getBottom() - padding){
            return true;
        }
        return false;
    }

    /**
     * 通过x，y获得落入的SixthView
     * @param x
     * @param y
     * @return
     */
    private SixthView getChildIdByPos(int x,int y){
        for (SixthView sixthView : mSixthViews){
            if (checkPositionInChild(sixthView,x,y)){
                return sixthView;
            }
        }
        return null;
    }

    /**
     * 设置回调接口
     * @param listener
     */
    public void setOnSixthViewListener(OnSixthViewListener listener){
        this.mOnSixthViewListener = listener;
    }

    /**
     * 对外公布设置答案的方法
     * @param answer
     */
    public void setmAnswer(int[] answer){
        this.mAnswer = answer;
    }

    /**
     * 设置最大实验次数
     * @param boundary
     */
    public void setUnMatchExceedBoundary(int boundary){
        this.mTryTimes = boundary;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制SixthView间的连线
        if (mPath != null){
            canvas.drawPath(mPath,mPaint);
        }
        //绘制指引线
        if (mChoose.size() > 0){
            if (mLastPathX != 0 && mLastPathY != 0){
                canvas.drawLine(mLastPathX,mLastPathY,mTmpTarget.x,mTmpTarget.y,mPaint);
            }
        }
    }

    public interface OnSixthViewListener{
        /**
         * 单独选中元素的Id
         * @param cId
         */
        public void onBlockSelected(int cId);

        /**
         * 是否匹配
         * @param matched
         */
        public void onGestureEvent(boolean matched);

        /**
         * 超过尝试次数
         */
        public void onUnmatchedExceedBoundary();

    }

}
