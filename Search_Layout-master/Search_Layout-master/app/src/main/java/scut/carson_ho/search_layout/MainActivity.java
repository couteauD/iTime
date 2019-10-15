package scut.carson_ho.search_layout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.app.AlertDialog;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener{

    private Context context;
    private TextView tvDate;
    private int year, month, day;
    //在TextView上显示的字符
    private StringBuffer date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchDemo.class));
            }

});
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        date = new StringBuffer();
        initView();
        initDateTime();
    }


        /**
         * 初始化控件
         */
        private void initView() {

            tvDate = (TextView) findViewById(R.id.tv_date);
            tvDate.setOnClickListener(this);
        }

        /**
         * 获取当前的日期和时间
         */
        private void initDateTime() {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (date.length() > 0) { //清除上次记录的日期
                                date.delete(0, date.length());
                            }
                            tvDate.setText(date.append(year).append("年").append(month).append("月").append(day).append("日"));
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
                    datePicker.init(year, month - 1, day, this);
               }


        /**
         * 日期改变的监听事件
         *
         */
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;
        }

}
