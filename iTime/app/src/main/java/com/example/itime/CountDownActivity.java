package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CountDownActivity extends AppCompatActivity {

    private ImageView imageViewCountdownBackground;
    private TextView textViewCountdown;

    private MyCount myCount;
    private long difference,from,to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        imageViewCountdownBackground = findViewById(R.id.image_view_countDownBackground);
        textViewCountdown = findViewById(R.id.text_view_countdown);

        //获取图片数据设置背景
        byte[] url = getIntent().getByteArrayExtra("url");
        Bitmap bitmap = BitmapFactory.decodeByteArray(url, 0, url.length);
        imageViewCountdownBackground.setImageBitmap(bitmap);

        myCount = new MyCount(getTimeDifference(), 1000);
        myCount.start();
    }


    /*
     * 定义一个倒计时的内部类
     *
     */
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            textViewCountdown.setText("finish");
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(millisUntilFinished);
            int year = calendar2.get(Calendar.YEAR);
            if(year==1970){
                year=0;
            }
            int month = calendar2.get(Calendar.MONTH);
            int day = calendar2.get(Calendar.DAY_OF_MONTH);
            int hour = calendar2.get(Calendar.HOUR_OF_DAY);//24小时制
            int minute = calendar2.get(Calendar.MINUTE);
            int second = calendar2.get(Calendar.SECOND);

           textViewCountdown.setText(year + "年" + month + "月" + day + "日"
                    + hour + "时" + minute + "分" + second + "秒");
        }
    }


    /**
     * 获取给定时间与当前系统时间的差值（以毫秒为单位）
     */
    public long getTimeDifference() {
        try {
            //获取当前系统时间
        Calendar calendar = Calendar.getInstance();
        int fromYear = calendar.get(Calendar.YEAR);
        int fromMonth = calendar.get(Calendar.MONTH) + 1;
        int fromDay = calendar.get(Calendar.DAY_OF_MONTH);
        int fromHour = calendar.get(Calendar.HOUR);
        int fromMinute = calendar.get(Calendar.MINUTE);

        Calendar fromdate = Calendar.getInstance();
        fromdate.set(fromYear, fromMonth, fromDay, fromHour, fromMinute, 0);
        from=fromdate.getTimeInMillis();

        //获取给定时间
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");//24小时制
            String date = getIntent().getStringExtra("date");
            to = simpleDateFormat.parse(date).getTime();

            //计算时间差
            difference =Math.abs(from-to);

            return difference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
