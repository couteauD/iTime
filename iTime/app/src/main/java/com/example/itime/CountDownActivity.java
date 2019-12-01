package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CountDownActivity extends AppCompatActivity {

    private ImageView imageViewCountdownBackground;
    private TextView textViewCountdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        imageViewCountdownBackground=findViewById(R.id.image_view_countDownBackground);
        textViewCountdown=findViewById(R.id.text_view_countdown);

        //获取图片数据设置背景
        byte[] url=getIntent().getByteArrayExtra("url");
        Bitmap bitmap=BitmapFactory.decodeByteArray(url,0, url.length);
        imageViewCountdownBackground.setImageBitmap(bitmap);

        //获取时间数据实现倒计时
        String string=getIntent().getStringExtra("date")+getIntent().getStringExtra("time");
        textViewCountdown.setText(string);

    }
}
