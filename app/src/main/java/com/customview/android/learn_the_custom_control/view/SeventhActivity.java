package com.customview.android.learn_the_custom_control.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.customview.android.learn_the_custom_control.R;
import com.customview.android.learn_the_custom_control.widget.SeventhView;

/**
 * Created by Administrator on 2017/3/13.
 */

public class SeventhActivity extends AppCompatActivity {

    private SeventhView seventhView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh);
        seventhView = (SeventhView) findViewById(R.id.id_arcmenu1);
        //动态添加一个MenuItem
        ImageView people = new ImageView(this);
        people.setImageResource(R.drawable.icon);
        people.setTag("People");
        seventhView.addView(people);


        seventhView.setOnMenuItemClickListener(new SeventhView.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(SeventhActivity.this,
                        pos + ":" + view.getTag(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
