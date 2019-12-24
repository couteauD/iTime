package com.example.itime.ui.mainpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.example.itime.CountDownActivity;
import com.example.itime.R;
import com.example.itime.ScheduleSaver;
import com.example.itime.appThemeSaver;
import com.example.itime.model.ImageFilter;
import com.example.itime.model.Schedule;
import com.example.itime.newScheduleActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MainpageFragment extends Fragment {

    private static final int SET_SCHEDULE=201;
    private static final int COUNTDOWN=202;
    private static final int RESULT_DELETE=1;

    private ListView listViewSchedule;
    private List<Schedule> schedules = new ArrayList<>();
    private ScheduleSaver scheduleSaver;
    private appThemeSaver appThemeSaver;
    private ScheduleAdapter adapter;
    private Bitmap bitmap;


    @Override
    public void  onStop(){
        super.onStop();
        scheduleSaver.save();
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        scheduleSaver=new ScheduleSaver(getContext());
        schedules=scheduleSaver.load();
        appThemeSaver=new appThemeSaver(getContext());
        int themeColor=appThemeSaver.load();

        final View root = inflater.inflate(R.layout.fragment_mainpage, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        if(themeColor!=0)
            fab.setBackgroundTintList(createColorStateList(themeColor, themeColor, themeColor, themeColor));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), newScheduleActivity.class);
                startActivityForResult(intent,SET_SCHEDULE);
            }
        });

        listViewSchedule=root.findViewById(R.id.list_view_schedule);
        listViewSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), CountDownActivity.class);
                intent.putExtra("title",schedules.get(i).getTitle());
                intent.putExtra("date",schedules.get(i).getDate());
                intent.putExtra("time",schedules.get(i).getTime());
                intent.putExtra("remark",schedules.get(i).getRemark());
                intent.putExtra("cycle",schedules.get(i).getCycle());
                intent.putExtra("mark",schedules.get(i).getMark());
                intent.putExtra("bitmap",schedules.get(i).getbitmapByte());
                intent.putExtra("position",i);
                startActivityForResult(intent,COUNTDOWN);

            }
        });

        if(schedules!=null) {
            adapter = new ScheduleAdapter(MainpageFragment.this.getContext(), R.layout.list_view_item_schedule, schedules);
            listViewSchedule.setAdapter(adapter);
        }

        return root;
    }

    public List<Schedule> getListSchedule(){
        return schedules;
    }

   class ScheduleAdapter extends ArrayAdapter<Schedule> {
        private int resourceId;
        public ScheduleAdapter(Context context, int resource, List<Schedule> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Schedule schedule = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            if(schedule.getbitmapByte()!= null && schedule.getbitmapByte().length!= 0) {
                bitmap = BitmapFactory.decodeByteArray(schedule.getbitmapByte(), 0, schedule.getbitmapByte().length);
                //选取图片颜色
                Palette palette = Palette.from(bitmap).generate();
                Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力的
                ((ImageView) view.findViewById(R.id.imageViewborder)).setBackgroundColor(vibrant.getRgb());
                //图片进行高斯模糊处理
                bitmap = ImageFilter.doBlur(bitmap, 30, false);
                ((ImageView) view.findViewById(R.id.image_view_img)).setImageBitmap(bitmap);
                ((TextView) view.findViewById(R.id.text_view_title)).setText(schedule.getTitle());
                ((TextView) view.findViewById(R.id.text_view_date)).setText(schedule.getDate());
                ((TextView) view.findViewById(R.id.text_view_remark)).setText(schedule.getRemark());
                long day = initTimeDifference(schedule.getDate(), schedule.getTime());
                ((TextView) view.findViewById(R.id.text_view_img)).setText(day + "天");
            }

            return view;
        }
    }


    /*
     * 设置默认图片上的倒计时时间
     */
    private long initTimeDifference(String date,String time) {
        try {
            //获取当前系统时间
            Calendar calendar = Calendar.getInstance();
            long from=calendar.getTimeInMillis();

            //获取给定时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");//24小时制
            String toDate=date+time;
            long to = simpleDateFormat.parse(toDate).getTime();

            //计算时间差
            long difference =Math.abs(from-to);
            long dayNum= (long) difference / (1000 * 60 * 60 * 24);

            return dayNum;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //fab动态设置颜色
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //新建日程
            case SET_SCHEDULE:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra("title");
                    String date = data.getStringExtra("date");
                    String time = data.getStringExtra("time");
                    String remark = data.getStringExtra("remark");
                    String cycle = data.getStringExtra("cycle");
                    String mark = data.getStringExtra("mark");
                    byte[] bitmapByte = data.getByteArrayExtra("bitmap");
                    schedules.add(new Schedule(title, date, time, remark, cycle, mark, bitmapByte));
                    adapter.notifyDataSetChanged();
                }
                break;
            case COUNTDOWN:
                if(resultCode==RESULT_DELETE){
                    int position=data.getIntExtra("position",-1);
                    schedules.remove(position);
                    adapter.notifyDataSetChanged();
                }
                if(resultCode==RESULT_OK){
                    String title = data.getStringExtra("title");
                    String date = data.getStringExtra("date");
                    String time = data.getStringExtra("time");
                    String remark = data.getStringExtra("remark");
                    String cycle = data.getStringExtra("cycle");
                    String mark = data.getStringExtra("mark");
                    byte[] bitmapByte = data.getByteArrayExtra("bitmap");
                    int position=data.getIntExtra("position",-1);
                    schedules.set(position, new Schedule(title, date, time, remark, cycle, mark, bitmapByte));
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}