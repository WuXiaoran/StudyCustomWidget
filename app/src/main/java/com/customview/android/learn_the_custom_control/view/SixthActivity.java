package com.customview.android.learn_the_custom_control.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.customview.android.learn_the_custom_control.R;
import com.customview.android.learn_the_custom_control.widget.SixthViewGroup;

/**
 * Created by Administrator on 2017/3/8.
 */

public class SixthActivity extends AppCompatActivity {

    private SixthViewGroup mSixthViewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);

        mSixthViewGroup = (SixthViewGroup) findViewById(R.id.viewGroup_Sixth);
        mSixthViewGroup.setmAnswer(new int[]{1,2,3,4,5});
        mSixthViewGroup.setOnSixthViewListener(new SixthViewGroup.OnSixthViewListener() {
            @Override
            public void onBlockSelected(int cId) {

            }

            @Override
            public void onGestureEvent(boolean matched) {
                Toast.makeText(SixthActivity.this,matched+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnmatchedExceedBoundary() {
                Toast.makeText(SixthActivity.this,"错误5次。。",Toast.LENGTH_SHORT).show();
                mSixthViewGroup.setUnMatchExceedBoundary(5);
            }
        });
    }
}
