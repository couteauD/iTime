package com.example.itime;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.itime.ui.mainpage.MainpageFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class newScheduleActivity extends AppCompatActivity{

    public static final int SET_MARK_CODE = 200; //设置标签
    public static final int TAKE_PHOTO_CODE = 1000; //拍照
    public static final int CHOOSE_PHOTO_CODE = 2000; //选择相册
    public static final int PICTURE_CROP_CODE = 3000;  //剪切图片

    private Context context;
    private LinearLayout linearLayoutDate, linearLayoutcycle,linearLayoutImg,linearLayouttitle,linearLayoutmark;
    private TextView dateInstuction, timeInstruction,cycleInstruction,markInstruction;
    private FloatingActionButton buttonNavOK,buttonNavBack;
    private EditText editTextTitle,editTextRemark;

    private int year, month, day, hour, minute;
    //在TextView上显示的字符
    private StringBuffer stringBuffer_date,stringBuffer_time;
    //拍照相册裁剪封装类
    private SelectPictureManager selectPictureManager;
    private Bitmap bitmap;
    private String title,date,time,cycle,mark,remark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        ActionBar actionBar = getSupportActionBar();
        //隐藏标题栏
        if (actionBar != null) {
            actionBar.hide();
        }


        //获取输入框内容
        //初始化控件
        editTextTitle=findViewById(R.id.edit_text_title);
        editTextRemark=findViewById(R.id.edit_text_remark);


        //设置日期计算器
        context = this;
        stringBuffer_date= new StringBuffer();
        stringBuffer_time = new StringBuffer();
        initDateView();
        initDateTime();

        //设置周期
        //初始化控件
        linearLayoutcycle= findViewById(R.id.setcycle);
        cycleInstruction=findViewById(R.id.text_view_cycleInstruction);
        //重复设置点击事件
        linearLayoutcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initcycle();
            }
        });

        //设置图片
        //初始化控件
        linearLayoutImg=findViewById(R.id.setimg);
        linearLayouttitle=findViewById(R.id.linearLayout_title);
        //图片点击事件
        linearLayoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectPictureManager();
            }
        });

        //添加标签
        //初始化控件
        linearLayoutmark=findViewById(R.id.setmark);
        markInstruction=findViewById(R.id.text_view_markInstruction);
        //添加标签点击事件
        linearLayoutmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(newScheduleActivity.this, markDialogActivity.class);
                startActivityForResult(intent, SET_MARK_CODE);
            }
        });

        //设置导航栏
        //初始化部件
        buttonNavBack=findViewById(R.id.nav_back);
        buttonNavOK=findViewById(R.id.nav_ok);
        //设置点击事件
        buttonNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newScheduleActivity.this.finish();
            }
        });
        buttonNavOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入内容
                title=editTextTitle.getText().toString();
                remark=editTextRemark.getText().toString();

                if(title==null){
                    Toast.makeText(newScheduleActivity.this,"请输入标题",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("title", title);
                            intent.putExtra("date", dateInstuction.getText().toString());
                            intent.putExtra("time",timeInstruction.getText().toString());
                            intent.putExtra("remark", remark);
                            intent.putExtra("cycle",cycleInstruction.getText().toString());
                            intent.putExtra("mark",markInstruction.getText().toString());

                            //把bitmap数据存储在btye[]数组中，然后再通过intent进行传递
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] bitmapByte = baos.toByteArray();
                            intent.putExtra("bitmap", bitmapByte);

                            setResult(RESULT_OK, intent);
                            newScheduleActivity.this.finish();
                        }
                    }).start();
                }

            }

        });
//更新编辑好的数据
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        if(getIntent()!=null){
            title=getIntent().getStringExtra("title");
            date=getIntent().getStringExtra("date");
            time=getIntent().getStringExtra("time");
            remark=getIntent().getStringExtra("remark");
            cycle=getIntent().getStringExtra("cycle");
            mark=getIntent().getStringExtra("mark");

            editTextTitle.setText(title);
            dateInstuction.setText(date);
            timeInstruction.setText(time);
            editTextRemark.setText(remark);
            cycleInstruction.setText(cycle);
            markInstruction.setText(mark);

            byte[] bitmapByte=getIntent().getByteArrayExtra("bitmap");
            bitmap = BitmapFactory.decodeByteArray(bitmapByte,0, bitmapByte.length);
            linearLayouttitle.setBackground( new BitmapDrawable(getResources(),bitmap));
        }
    }

    /**
     * 初始化日期控件
     */
    private void initDateView() {
        linearLayoutDate = (LinearLayout) findViewById(R.id.setdate);
        dateInstuction = (TextView) findViewById(R.id.text_view_dateInstruction);
        timeInstruction = (TextView) findViewById(R.id.text_view_timeInstruction);
        linearLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (stringBuffer_time.length() > 0) { //清除上次记录的日期
                            stringBuffer_time.delete(0, stringBuffer_time.length());
                        }
                        timeInstruction.setText(stringBuffer_time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
                timePicker.setIs24HourView(true); //设置24小时制
                //时间改变监听事件
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        newScheduleActivity.this.hour = hourOfDay;
                        newScheduleActivity.this.minute = minute;
                    }
                });
                dialog2.setTitle("设置时间");
                dialog2.setView(dialogView2);
                dialog2.show();


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (stringBuffer_date.length() > 0) { //清除上次记录的日期
                            stringBuffer_date.delete(0,stringBuffer_date.length());
                        }
                        dateInstuction.setText(stringBuffer_date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        newScheduleActivity.this.year = year;
                        newScheduleActivity.this.month = monthOfYear;
                        newScheduleActivity.this.day = dayOfMonth;
                    }
                });
            }
        });
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    /**
     * 实现重复设置点击弹出设置框
     */
    void initcycle(){
        //创建item
        final String[] itemsCycle = new String[]{"每周", "每月", "每年", "自定义"};
        new AlertDialog.Builder(context).setTitle("周期")
                .setItems(itemsCycle, new DialogInterface.OnClickListener() {//添加列表
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0)
                            cycleInstruction.setText("每周");
                        else if(i==1)
                            cycleInstruction.setText("每月");
                         else if(i==2)
                             cycleInstruction.setText("每年");
                         else if(i==3){
                                final EditText cycle_editText = new EditText(context);
                                cycle_editText.setFocusable(true);
                                cycle_editText.setHint("输入周期（天）");
                                new AlertDialog.Builder(context).setTitle("周期")
                                        .setView(cycle_editText)
                                        .setNegativeButton("取消",null)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                cycleInstruction.setText(cycle_editText.getText()+"天");
                                            }
                                        }).show();
                        }

                    }
                })
                .create().show();
    }

    /**
     * 实现拍照相册读取图片截图
     */
    void initSelectPictureManager() {
        selectPictureManager = new SelectPictureManager(this);
        selectPictureManager.setPictureSelectListner(new SelectPictureManager.PictureSelectListner() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPictureSelect(String imagePath) {
                if(imagePath != null){
                    bitmap = BitmapFactory.decodeFile(imagePath);
                    linearLayouttitle.setBackground( new BitmapDrawable(getResources(),bitmap));
                }else {
                    Toast.makeText(context,"获取图片失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void throwError(Exception e) {
                e.printStackTrace();
            }
        });
        selectPictureManager.setNeedCrop(true);//需要裁剪
        selectPictureManager.setOutPutSize(400, 400);//输入尺寸
        selectPictureManager.setContinuous(true);//设置连拍
        selectPictureManager.showSelectPicturePopupWindow(this.getWindow().getDecorView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SET_MARK_CODE:
                if(resultCode==RESULT_OK) {
                    ArrayList<String> marklist = data.getStringArrayListExtra("mark");
                    StringBuilder mark = new StringBuilder();
                    if (marklist.size() != 0) {
                        for (int i = 0; i < marklist.size() - 1; i++)
                            mark.append(marklist.get(i)+",");
                        mark.append(marklist.get(marklist.size() - 1));
                        markInstruction.setText("已选: " + mark);
                    }
                }
                break;
            case TAKE_PHOTO_CODE:
            case CHOOSE_PHOTO_CODE:
            case PICTURE_CROP_CODE:
                selectPictureManager.onActivityResult(requestCode, resultCode, data);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        selectPictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
