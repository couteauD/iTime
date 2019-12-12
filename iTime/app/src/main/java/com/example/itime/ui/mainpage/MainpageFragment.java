package com.example.itime.ui.mainpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.itime.CountDownActivity;
import com.example.itime.MainActivity;
import com.example.itime.R;
import com.example.itime.ScheduleSaver;
import com.example.itime.model.ImageFilter;
import com.example.itime.model.Schedule;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainpageFragment extends Fragment {

    public static final int COUNTDOWN_CODE = 200;
    private ListView listViewSchedule;
    private List<Schedule> schedules = new ArrayList<>();
    private ScheduleSaver scheduleSaver;
    private ScheduleAdapter adapter;
    private Bitmap bitmap;

    private AutoScrollViewPager mViewPager;


    /**
     * 用来与外部activity交互的
     */
    private FragmentInteraction listterner;

    public MainpageFragment(){}

    /**
     * 当FRagmen被加载到activity的时候会被回调
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentInteraction)
        {
            listterner = (FragmentInteraction)context;

        }
        else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;

    }

    /**
     * 定义了所有activity必须实现的接口
     */
    public interface FragmentInteraction
    {
        /**
         * Fragment 向Activity传递指令，这个方法可以根据需求来定义
         * @param title,date,time,remark,cycle,mark,bitmap,position
         */
        void process(String title, String date, String time, String remark, String cycle, String mark, byte[] bitmap,int position);


    }


    @Override
    public void  onStop(){
        super.onStop();
        scheduleSaver.save();
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        scheduleSaver=new ScheduleSaver(getContext());
        schedules=scheduleSaver.load();

        final View root = inflater.inflate(R.layout.fragment_mainpage, container, false);
        listViewSchedule=root.findViewById(R.id.list_view_schedule);
        listViewSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listterner.process(schedules.get(i).getTitle(),schedules.get(i).getDate(),schedules.get(i).getTime(),schedules.get(i).getRemark(),schedules.get(i).getCycle(),schedules.get(i).getMark(),schedules.get(i).getbitmapByte(),i);
            }
        });

        if(schedules!=null) {
            adapter = new ScheduleAdapter(MainpageFragment.this.getContext(), R.layout.list_view_item_schedule, schedules);
            listViewSchedule.setAdapter(adapter);
        }


        //新建日程表
        if(getArguments()!=null) {
            Bundle bundle = getArguments();
            String title = bundle.getString("title");
            String date = bundle.getString("date");
            String time = bundle.getString("time");
            String remark = bundle.getString("remark");
            String cycle = bundle.getString("cycle");
            String mark = bundle.getString("mark");
            byte[] bitmap = bundle.getByteArray("bitmap");
            int position=bundle.getInt("position",-1);

            if(title!=null) {
                if(position!=-1){
                    schedules.set(position,new Schedule(title,date,time, remark, cycle, mark, bitmap));
                }
                else {
                    schedules.add(new Schedule(title, date, time, remark, cycle, mark, bitmap));
                }
            }
            if(title==null && position!=-1){
                schedules.remove(position);
                adapter.notifyDataSetChanged();
            }
        }

        //初始化AutoScrollViewPager对象
            mViewPager = (AutoScrollViewPager) root.findViewById(R.id.viewPager);
            //设置Adapter，这里需要重写loadImage方法，在里面加载图片
            mViewPager.setAdapter(new BaseViewPagerAdapter<byte[]>(getContext(), initData(), listener) {
                @Override
                public void loadImage(ImageView view, int position,byte[] url) {
                        //将byte[]转化为bitmap
                        Bitmap bitmap = BitmapFactory.decodeByteArray(url,0, url.length);
                        //将bitmap转化为Uri
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, null,null));
                        //使用Picasso框架加载图片
                        Picasso.with(getContext()).load(uri).into(view);
                    }
            });

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

            if(schedule.getbitmapByte()!=null) {

                bitmap = BitmapFactory.decodeByteArray(schedule.getbitmapByte(), 0, schedule.getbitmapByte().length);
                bitmap = ImageFilter.doBlur(bitmap, 30, false);
                ((ImageView)view.findViewById(R.id.image_view_img)).setImageBitmap(bitmap);
                ((TextView) view.findViewById(R.id.text_view_title)).setText(schedule.getTitle());
                ((TextView)view.findViewById(R.id.text_view_date)).setText(schedule.getDate());
                ((TextView) view.findViewById(R.id.text_view_remark)).setText(schedule.getRemark());
            }

            return view;
        }
    }


    private List<byte[]> initData() {
        List<byte[]> data = new ArrayList<>();
        for(int i=0;i<schedules.size();i++) {
            data.add(schedules.get(i).getbitmapByte());
        }
        return data;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //记得在销毁的时候调用onDestroy()方法。用来销毁定时器。
        mViewPager.onDestroy();
    }

    //定义点击事件
    private BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new BaseViewPagerAdapter.OnAutoViewPagerItemClickListener<byte[]>() {

        @Override
        public void onItemClick(int position, byte[] url) {
            listterner.process(schedules.get(position).getTitle(),schedules.get(position).getDate(),schedules.get(position).getTime(),schedules.get(position).getRemark(),schedules.get(position).getCycle(),schedules.get(position).getMark(),schedules.get(position).getbitmapByte(),position);
        }
    };

}