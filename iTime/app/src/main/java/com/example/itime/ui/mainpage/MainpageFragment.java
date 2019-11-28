package com.example.itime.ui.mainpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itime.MainActivity;
import com.example.itime.R;
import com.example.itime.ScheduleSaver;
import com.example.itime.model.ImageFilter;
import com.example.itime.model.Schedule;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class MainpageFragment extends Fragment {

    private ListView listViewSchedule;
    private List<Schedule> schedules = new ArrayList<>();
    private ScheduleSaver scheduleSaver;
    private ScheduleAdapter adapter;
    private Bitmap bitmap;

    private AutoScrollViewPager mViewPager;

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

        if(schedules!=null) {
            adapter = new ScheduleAdapter(MainpageFragment.this.getContext(), R.layout.list_view_item_schedule, schedules);
            listViewSchedule.setAdapter(adapter);
        }

        if(((MainActivity)getActivity()).geTitle()!=null) {
            getListSchedule().add(new Schedule(((MainActivity) getActivity()).geTitle(), ((MainActivity) getActivity()).getDate(), ((MainActivity) getActivity()).getRemark(), ((MainActivity) getActivity()).getBitmapByte()));
            adapter.notifyDataSetChanged();
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
            final View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            if(schedule.getbitmapByte()!=null) {

                bitmap = BitmapFactory.decodeByteArray(schedule.getbitmapByte(), 0, schedule.getbitmapByte().length);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //高斯模糊处理图片
                        bitmap = ImageFilter.doBlur(bitmap, 30, false);
                        //处理完成后返回主线程
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ImageView) view.findViewById(R.id.image_view_img)).setImageBitmap(bitmap);
                            }
                        });
                    }
                }).start();
                ((TextView) view.findViewById(R.id.text_view_title)).setText(schedule.getTitle());
                ((TextView) view.findViewById(R.id.text_view_date)).setText(schedule.getDate());
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
            Toast.makeText(getContext(), String.valueOf(url), Toast.LENGTH_SHORT).show();
        }
    };
}