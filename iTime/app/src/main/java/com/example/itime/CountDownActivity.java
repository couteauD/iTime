package com.example.itime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CountDownActivity extends AppCompatActivity implements View.OnClickListener {

    private appThemeSaver appthemeSaver;
    private ImageView imageViewCountdownBackground;
    private TextView textViewCountdown,textViewTitle,textViewDate;

    private MyCount myCount;
    private long difference,from,to;
    private int position;
    private String title,date,time,remark,cycle,mark;
    private byte[] bitmapByte;
    private ImageButton buttonBack,buttonDelete,buttonShare,buttonUpdate;

    private static final int UPDATE_CODE=203;
    private static final int RESULT_DELETE=1;

    private int themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appthemeSaver=new appThemeSaver(this);
        themeColor=appthemeSaver.load();

        setContentView(R.layout.activity_count_down);

        //状态栏设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(themeColor!=0)
                getWindow().setStatusBarColor(themeColor);
        }

        ActionBar actionBar = getSupportActionBar();
        //隐藏标题栏
        if (actionBar != null) {
            actionBar.hide();
        }
        //初始化日程表基本信息
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        remark = getIntent().getStringExtra("remark");
        cycle=getIntent().getStringExtra("cycle");
        mark=getIntent().getStringExtra("mark");
        bitmapByte= getIntent().getByteArrayExtra("bitmap");
        position=getIntent().getIntExtra("position",-1);

        imageViewCountdownBackground = findViewById(R.id.image_view_countDownBackground);
        textViewCountdown = findViewById(R.id.text_view_countdown);
        textViewTitle=findViewById(R.id.text_view_title);
        textViewDate=findViewById(R.id.text_view_date);

        //获取图片数据设置背景
        byte[] url = getIntent().getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(url, 0, url.length);
        imageViewCountdownBackground.setImageBitmap(bitmap);

        //获取日程信息
        String title=getIntent().getStringExtra("title");
        textViewTitle.setText(title);
        String date=getIntent().getStringExtra("date")+getIntent().getStringExtra("time");
        textViewDate.setText(date);

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
            long days = millisUntilFinished / (1000 * 60 * 60 * 24);
            long hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (millisUntilFinished % (1000 * 60)) / 1000;
            textViewCountdown.setText(days + " 天 " + hours + "小时 " + minutes + " 分钟 " + seconds + "秒");
        }
    }


    /**
     * 获取给定时间与当前系统时间的差值（以毫秒为单位）
     */
    public long getTimeDifference() {
        try {
            //获取当前系统时间
        Calendar calendar = Calendar.getInstance();
        from=calendar.getTimeInMillis();

        //获取给定时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");//24小时制
            String toDate=textViewDate.getText().toString();
            to = simpleDateFormat.parse(toDate).getTime();

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
                Intent intentBack=new Intent();
                intentBack.putExtra("title",title);
                intentBack.putExtra("date",date);
                intentBack.putExtra("time",time);
                intentBack.putExtra("remark",remark);
                intentBack.putExtra("cycle",cycle);
                intentBack.putExtra("mark",mark);
                intentBack.putExtra("bitmap",bitmapByte);
                intentBack.putExtra("position",position);
                setResult(RESULT_OK,intentBack);
                CountDownActivity.this.finish();
                break;

            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setTitle("是否删除该计时？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                position= getIntent().getIntExtra("position",-1);
                                Intent intentDelete=new Intent();
                                intentDelete.putExtra("position",position);
                                setResult(RESULT_DELETE,intentDelete);
                                CountDownActivity.this.finish();
                            }
                        }).show();
                break;

            case R.id.update:
                Intent intentUpdate=new Intent(CountDownActivity.this,newScheduleActivity.class);
                intentUpdate.putExtra("title",title);
                intentUpdate.putExtra("date",date);
                intentUpdate.putExtra("time",time);
                intentUpdate.putExtra("remark",remark);
                intentUpdate.putExtra("cycle",cycle);
                intentUpdate.putExtra("mark",mark);
                intentUpdate.putExtra("bitmap",bitmapByte);

                startActivityForResult(intentUpdate,UPDATE_CODE);
                myCount.cancel();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==UPDATE_CODE && resultCode==RESULT_OK){
            title = data.getStringExtra("title");
            date = data.getStringExtra("date");
            time = data.getStringExtra("time");
            remark = data.getStringExtra("remark");
            cycle=data.getStringExtra("cycle");
            mark=data.getStringExtra("mark");
            bitmapByte= data.getByteArrayExtra("bitmap");

            textViewTitle.setText(title);
            textViewDate.setText(date+time);
            byte[] url = getIntent().getByteArrayExtra("bitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(url, 0, url.length);
            imageViewCountdownBackground.setImageBitmap(bitmap);
            myCount = new MyCount(getTimeDifference(), 1000);
            myCount.start();
        }
    }
}
