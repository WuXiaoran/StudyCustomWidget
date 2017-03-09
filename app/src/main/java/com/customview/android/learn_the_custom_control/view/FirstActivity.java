package com.customview.android.learn_the_custom_control.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.customview.android.learn_the_custom_control.R;
import com.customview.android.learn_the_custom_control.widget.FirstView;

public class FirstActivity extends AppCompatActivity {

    private FirstView firstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        firstView = (FirstView) findViewById(R.id.firstview);
    }
}
