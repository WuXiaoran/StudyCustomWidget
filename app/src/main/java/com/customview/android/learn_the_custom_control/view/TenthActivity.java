package com.customview.android.learn_the_custom_control.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.customview.android.learn_the_custom_control.R;
import com.customview.android.learn_the_custom_control.widget.TenthView;

/**
 * Created by Administrator on 2017/3/14.
 */

public class TenthActivity extends AppCompatActivity {

    private TenthView tenthview01;
    private TenthView tenthview02;
    private TenthView tenthview03;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenth);
        tenthview01 = (TenthView) findViewById(R.id.tenthview01);
        tenthview02 = (TenthView) findViewById(R.id.tenthview02);
        tenthview03 = (TenthView) findViewById(R.id.tenthview03);
        tenthview01.setOnItemClick(new TenthView.OnTenthViewItemClickListener() {
            @Override
            public void itemclick(View view, int position) {
                Toast.makeText(TenthActivity.this,"你点击了第" + position + "个item",Toast.LENGTH_SHORT).show();
            }
        });
        tenthview02.setOnItemClick(new TenthView.OnTenthViewItemClickListener() {
            @Override
            public void itemclick(View view, int position) {
                Toast.makeText(TenthActivity.this,"你点击了第" + position + "个item",Toast.LENGTH_SHORT).show();
            }
        });
        tenthview03.setOnItemClick(new TenthView.OnTenthViewItemClickListener() {
            @Override
            public void itemclick(View view, int position) {
                Toast.makeText(TenthActivity.this,"你点击了第" + position + "个item",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
