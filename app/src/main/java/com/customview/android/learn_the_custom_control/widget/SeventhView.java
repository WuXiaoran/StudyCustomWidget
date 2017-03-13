package com.customview.android.learn_the_custom_control.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.customview.android.learn_the_custom_control.R;

/**
 * Created by Administrator on 2017/3/9.
 */

public class SeventhView extends ViewGroup implements View.OnClickListener {

    private static final String TAG = "SenventhView";
    /**
     * 菜单的显示位置
     */
    private Position mPosition = Position.LEFT_TOP;
    /**
     * 菜单显示的半径，默认100dp
     */
    private int mRadius = 100;
    /**
     * 用户点击的按钮
     */
    private View mButton;
    /**
     * 当前SeventhView的状态
     */
    private Status mCurrentStatus = Status.CLOSE;
    /**
     * 回调接口
     */
    private OnMenuItemClickListener onMenuItemClickListener;

    /**
     * 状态的枚举类
     */
    public enum Status{
        OPEN,CLOSE;
    }

    /**
     * 设置菜单现实的位置，四选一，默认左上
     */
    public enum Position{
        LEFT_TOP,RIGHT_TOP,RIGHT_BOTTOM,LEFT_BOTTOM;

    }
    public interface OnMenuItemClickListener{
        void onClick(View view,int pos);
    }
    public SeventhView(Context context) {
        this(context,null);
    }

    public SeventhView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 初始化自定义属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SeventhView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mRadius,
                getResources().getDisplayMetrics());
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SeventhView,defStyleAttr,0);
        int n = a.getIndexCount();
        for(int i = 0;i < n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.SeventhView_position:
                    int val = a.getInt(attr,0);
                    switch (val){
                        case 0:
                            mPosition = Position.LEFT_TOP;
                            break;
                        case 1:
                            mPosition = Position.RIGHT_TOP;
                            break;
                        case 2:
                            mPosition = Position.RIGHT_BOTTOM;
                            break;
                        case 3:
                            mPosition = Position.LEFT_BOTTOM;
                            break;
                    }
                    break;
                case R.styleable.SeventhView_radius:
                    mRadius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100f,
                            getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i = 0;i < count;i++){
            //绘制子控件宽和高,让子控件自适应布局大小
            getChildAt(i).measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 为按钮添加点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        mButton = findViewById(R.id.id_button);
        if (mButton == null){
            mButton = getChildAt(0);
        }
        //为按钮mButton提供一个从0°到360°，维持300毫秒的动画
        rotateView(mButton,0f,360f,300);
        toggleMenu(300);
    }

    /**
     * 对子控件重新安排布局
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            layoutButton();
            int count = getChildCount();
            /**
             * 设置所有孩子的位置 例如(第一个为按钮)： 左上时，从左到右 ] 第2个：mRadius(sin0 , cos0)
             * 第3个：mRadius(sina ,cosa) 注：[a = Math.PI / 2 * (cCount - 1)]
             * 第4个：mRadius(sin2a ,cos2a) 第5个：mRadius(sin3a , cos3a) ...
             */
        for(int i = 0;i < count - 1;i++){
            //遍历子控件，将子控件方位固定并设置GONE
            View child = getChildAt(i + 1);
            child.setVisibility(View.GONE);
            int c1 = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
            //子控件的宽
            int cWidth = child.getMeasuredWidth();
            //子控件的高
            int cHeight = child.getMeasuredHeight();
            //左下，右下
            if (mPosition == Position.LEFT_BOTTOM ||
                    mPosition == Position.RIGHT_BOTTOM){
                ct = getMeasuredHeight() - cHeight - ct;
            }
            //右上，右下
            if (mPosition == Position.RIGHT_TOP ||
                    mPosition == Position.RIGHT_BOTTOM){
                c1 = getMeasuredWidth() - cWidth - c1;
            }
            //ViewGroup.layout(int l, int t, int r, int b)这个方法是确定View的大小和位置的，
            // 然后将其绘制出来，里面的四个参数分别是View的四个点的坐标，
            // 他的坐标不是相对屏幕的原点，而且相对于他的父布局来说的。
            //l 和 t 是控件左边缘和上边缘相对于父类控件左边缘和上边缘的距离
            //r 和 b是空间右边缘和下边缘相对于父类控件左边缘和上边缘的距离
            child.layout(c1,ct,c1 + cWidth,ct + cHeight);
        }
        }
    }

    /**
     * 第一个子元素为按钮，为按钮布局且初始化点击事件
     */
    private void layoutButton(){
        View cButton = getChildAt(0);
        cButton.setOnClickListener(this);
        int l = 0;
        int t = 0;
        int width = cButton.getMeasuredWidth();
        int height = cButton.getMeasuredHeight();
        switch (mPosition){
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        cButton.layout(l,t,l + width,t + height);
    }

    /**
     * 按钮的旋转动画
     * @param view
     * @param fromDegrees
     * @param toDegrees
     * @param durationMillis
     */
    public static void rotateView(View view,float fromDegrees,float toDegrees,int durationMillis){
        RotateAnimation rotate = new RotateAnimation(fromDegrees,toDegrees, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
    }

    /**
     * 菜单的旋转动画
     * @param durationMillis
     */
    public void toggleMenu(int durationMillis){
        int count =getChildCount();
        for(int i = 0;i < count - 1;i++){
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);
            int xflag = 1;
            int yflag = 1;
            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.LEFT_BOTTOM){
                xflag = -1;
            }
            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.RIGHT_TOP){
                yflag = -1;
            }
            // child left
            int c1 = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            // child top
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
            //构建动画集合
            AnimationSet animset = new AnimationSet(true);
            Animation animation = null;
            if (mCurrentStatus == Status.CLOSE){
                //开始动画
                //插入差值器
                animset.setInterpolator(new OvershootInterpolator(2F));
                animation = new TranslateAnimation(xflag * c1,0,yflag * ct,0);
                childView.setClickable(true);
                childView.setFocusable(true);
            }else{
                //关闭动画
                animation = new TranslateAnimation(0f,xflag * c1,0f,yflag * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE){
                        childView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setFillAfter(true);
            animation.setDuration(durationMillis);
            //为动画设置一个开始延迟事件
            animation.setStartOffset((i * 100) / (count - 1));
            RotateAnimation rotateAnimation = new RotateAnimation(0f,360f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(durationMillis);
            rotateAnimation.setFillAfter(true);
            animset.addAnimation(rotateAnimation);
            animset.addAnimation(animation);
            childView.startAnimation(animset);
            final int index = i + 1;
            childView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null){
                        onMenuItemClickListener.onClick(childView,index - 1);
                        menuItemAnin(index - 1);
                        changeStatus();
                    }
                }
            });

        }
        changeStatus();
    }
    private void changeStatus(){
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.CLOSE : Status.OPEN);
    }
    private void menuItemAnin(int item){
        for(int i = 0;i < getChildCount() - 1;i++){
            View childView = getChildAt(i + 1);
            if (i == item){
                childView.startAnimation(scaleBigAnim(300));
            }else{
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }
    }

    /**
     * 缩小消失
     * @param durationMillis
     * @return
     */
    private Animation scaleSmallAnim(int durationMillis){
        Animation anim = new ScaleAnimation(1.0f,0f,1.0f,0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(durationMillis);
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 放大，透明度降低
     * @param durationMillis
     * @return
     */
    private Animation scaleBigAnim(int durationMillis){
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new ScaleAnimation(1.0f,4.0f,1.0f,4.0f,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        Animation alphaAnimation = new AlphaAnimation(1,0);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(durationMillis);
        animationSet.setFillAfter(true);
        return animationSet;

    }
    public Position getmPosition()
    {
        return mPosition;
    }

    public void setmPosition(Position mPosition)
    {
        this.mPosition = mPosition;
    }

    public int getmRadius()
    {
        return mRadius;
    }

    public void setmRadius(int mRadius)
    {
        this.mRadius = mRadius;
    }

    public Status getmCurrentStatus()
    {
        return mCurrentStatus;
    }

    public void setmCurrentStatus(Status mCurrentStatus)
    {
        this.mCurrentStatus = mCurrentStatus;
    }

    public OnMenuItemClickListener getOnMenuItemClickListener()
    {
        return onMenuItemClickListener;
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener)
    {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

}
