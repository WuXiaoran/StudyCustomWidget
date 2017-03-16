package com.customview.android.learn_the_custom_control.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.customview.android.learn_the_custom_control.R;
import com.customview.android.learn_the_custom_control.widget.NinthView;
import com.customview.android.learn_the_custom_control.widgetAdapter.NinthViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class NinthActivity extends AppCompatActivity {
    private NinthView mRecyclerView;
    private NinthViewAdapter mAdapter;
    private List<Integer> mDatas;
    private ImageView mImg ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ninth);

        mImg = (ImageView) findViewById(R.id.id_content);

        mDatas = new ArrayList<Integer>(Arrays.asList(R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d,
                R.mipmap.e, R.mipmap.f, R.mipmap.g, R.mipmap.h,
                R.mipmap.i));

        mRecyclerView = (NinthView) findViewById(R.id.id_recyclerview_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new NinthViewAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnItemScrollChangeListener(new NinthView.OnItemScrollChangeListener() {
            @Override
            public void onChange(View view, int position) {
                mImg.setImageResource(mDatas.get(position));
            }
        });

        mAdapter.setOnItemClickLitener(new NinthViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mImg.setImageResource(mDatas.get(position));
            }
        });

    }
}
