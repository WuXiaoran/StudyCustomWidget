package com.customview.android.learn_the_custom_control.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.customview.android.learn_the_custom_control.R;
import com.customview.android.learn_the_custom_control.widget.EighthView;
import com.customview.android.learn_the_custom_control.widgetAdapter.EighthViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class EighthActivity extends AppCompatActivity {
    private EighthView mEighthView;
    private EighthViewAdapter mAdapter;
    private ImageView mImg;
    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h,
            R.drawable.i));

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_eighth);

        mImg = (ImageView) findViewById(R.id.id_content);

        mEighthView = (EighthView) findViewById(R.id.id_eighthview);
        mAdapter = new EighthViewAdapter(this, mDatas);
        //添加滚动回调
        mEighthView
                .setCurrentImageChangeListener(new EighthView.CurrentImageChangeListener() {
                    @Override
                    public void onCurrentImageChanged(int position, View viewIndicator) {
                        mImg.setImageResource(mDatas.get(position));
                        viewIndicator.setBackgroundColor(Color
                                .parseColor("#AA024DA4"));
                    }
                });
        //添加点击回调
        mEighthView.setOnItemClickListener(new EighthView.OnItemClickListener()
        {

            @Override
            public void onClick(View view, int position)
            {
                mImg.setImageResource(mDatas.get(position));
                view.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });
        //设置适配器
        mEighthView.initDatas(mAdapter);
    }
}
