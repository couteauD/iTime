package com.example.itime;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

public class newScheduleActivity extends AppCompatActivity{

    private Context context;
    private LinearLayout linearLayoutDate, linearLayoutcycle;
    private TextView dateInstuction, timeInstruction,cycleInstruction;
    private int year, month, day, hour, minute;
    //在TextView上显示的字符
    private StringBuffer date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        ActionBar actionBar = getSupportActionBar();
        //隐藏标题栏
        if (actionBar != null) {
            actionBar.hide();
        }

        //设置日期计算器
        context = this;
        date = new StringBuffer();
        time = new StringBuffer();
        initDateView();
        initDateTime();

        //设置周期
        //初始化控件
        linearLayoutcycle= findViewById(R.id.setcycle);
        cycleInstruction=findViewById(R.id.text_view_cycleInstruction);
        //为组件注册上下文菜单
        registerForContextMenu(linearLayoutcycle);
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
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        timeInstruction.setText(time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
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
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        dateInstuction.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
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
     * 重写与ContextMenu相关方法
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflator = new MenuInflater(context);
        inflator.inflate(R.menu.cycle_menu, menu);
        menu.setHeaderTitle("周期");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * 重复设置上下文菜单点击事件
     */

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_weekly:
                cycleInstruction.setText("每周");
                break;
            case R.id.item_monthly:
                cycleInstruction.setText("每月");
                break;
            case R.id.item_yearly:
                cycleInstruction.setText("每年");
                break;
            case R.id.item_defined:
                final EditText cycle_editText = new EditText(this);
                cycle_editText.setFocusable(true);
                cycle_editText.setHint("输入周期（天）");
                new AlertDialog.Builder(this).setTitle("周期")
                        .setView(cycle_editText)
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               cycleInstruction.setText(cycle_editText.getText()+"天");
                            }
                        }).show();
        }
        return true;
    }

}
