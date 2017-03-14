package com.customview.android.learn_the_custom_control.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.customview.android.learn_the_custom_control.R;


/**
 * Created by Administrator on 2017/3/7.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Button widget_first;
    private Button widget_second;
    private Button widget_third;
    private Button widget_fourth;
    private Button widget_fifth;
    private Button widget_sixth;
    private Button widget_seven;
    private Button widget_eighth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){
        widget_first = (Button) findViewById(R.id.widget_first);
        widget_second = (Button) findViewById(R.id.widget_second);
        widget_third = (Button) findViewById(R.id.widget_third);
        widget_fourth = (Button) findViewById(R.id.widget_fourth);
        widget_fifth = (Button) findViewById(R.id.widget_fifth);
        widget_sixth = (Button) findViewById(R.id.widget_sixth);
        widget_seven = (Button) findViewById(R.id.widget_seven);
        widget_eighth = (Button) findViewById(R.id.widget_eighth);
        widget_first.setOnClickListener(this);
        widget_second.setOnClickListener(this);
        widget_third.setOnClickListener(this);
        widget_fourth.setOnClickListener(this);
        widget_fifth.setOnClickListener(this);
        widget_sixth.setOnClickListener(this);
        widget_seven.setOnClickListener(this);
        widget_eighth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.widget_first:
                startActivity(new Intent(MainActivity.this,FirstActivity.class));
                break;
            case R.id.widget_second:
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
                break;
            case R.id.widget_third:
                startActivity(new Intent(MainActivity.this,ThirdActivity.class));
                break;
            case R.id.widget_fourth:
                startActivity(new Intent(MainActivity.this,FourthActivity.class));
                break;
            case R.id.widget_fifth:
                startActivity(new Intent(MainActivity.this,FifthActivity.class));
                break;
            case R.id.widget_sixth:
                startActivity(new Intent(MainActivity.this,SixthActivity.class));
                break;
            case R.id.widget_seven:
                startActivity(new Intent(MainActivity.this,SeventhActivity.class));
                break;
            case R.id.widget_eighth:
                startActivity(new Intent(MainActivity.this,EighthActivity.class));
                break;
        }
    }
}
