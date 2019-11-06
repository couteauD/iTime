package com.example.itime;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

public class newScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        ActionBar actionBar = getSupportActionBar();
        //隐藏标题栏
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
