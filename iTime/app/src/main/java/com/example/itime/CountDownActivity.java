package com.example.itime;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itime.model.Schedule;
import com.example.itime.ui.mainpage.MainpageFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CountDownActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewCountdownBackground;
    private TextView textViewCountdown;

    private MyCount myCount;
    private long difference,from,to;

    private FloatingActionButton buttonBack,buttonDelete,buttonShare,buttonUpdate;

    private static final int COUNTDOWN=202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        ActionBar actionBar = getSupportActionBar();
        //隐藏标题栏
        if (actionBar != null) {
            actionBar.hide();
        }

        imageViewCountdownBackground = findViewById(R.id.image_view_countDownBackground);
        textViewCountdown = findViewById(R.id.text_view_countdown);

        //获取图片数据设置背景
        byte[] url = getIntent().getByteArrayExtra("url");
        Bitmap bitmap = BitmapFactory.decodeByteArray(url, 0, url.length);
        imageViewCountdownBackground.setImageBitmap(bitmap);

        myCount = new MyCount(getTimeDifference(), 1000);
        myCount.start();

        buttonBack=findViewById(R.id.back);
        buttonBack.setOnClickListener(this);
        buttonDelete=findViewById(R.id.delete);
        buttonDelete.setOnClickListener(this);
        buttonShare=findViewById(R.id.share);
        buttonShare.setOnClickListener(this);
        buttonUpdate=findViewById(R.id.update);
        buttonUpdate.setOnClickListener(this);

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

    /**
     * 设置顶部菜单栏的点击事件
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
               CountDownActivity.this.finish();
               break;

            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setTitle("是否删除该计时？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int position= getIntent().getIntExtra("position",-1);
                                Intent intent=new Intent(CountDownActivity.this,MainActivity.class);
                                intent.putExtra("position",position);
                                startActivity(intent);
                                CountDownActivity.this.finish();
                            }
                        }).show();

        }

    }

}
