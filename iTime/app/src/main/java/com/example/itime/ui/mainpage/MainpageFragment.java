package com.example.itime.ui.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.itime.MainActivity;
import com.example.itime.R;
import com.example.itime.ScheduleSaver;
import com.example.itime.model.Schedule;
import com.example.itime.newScheduleActivity;

import java.util.ArrayList;
import java.util.List;

public class MainpageFragment extends Fragment {

    private ListView listViewSchedule;
    private List<Schedule> schedules = new ArrayList<>();
    private ScheduleSaver scheduleSaver;

    @Override
    public void  onStop(){
        super.onStop();
        scheduleSaver.save();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        scheduleSaver=new ScheduleSaver(getContext());
        schedules=scheduleSaver.load();

        View root = inflater.inflate(R.layout.fragment_mainpage, container, false);
        listViewSchedule=root.findViewById(R.id.list_view_schedule);

        ScheduleAdapter adapter = new ScheduleAdapter(MainpageFragment.this.getContext(), R.layout.list_view_item_schedule, schedules);
        listViewSchedule.setAdapter(adapter);

        getListSchedule().add(new Schedule(((MainActivity)getActivity()).geTitle(),((MainActivity)getActivity()).getDate(),((MainActivity)getActivity()).getRemark(),((MainActivity)getActivity()).getBitmapByte()));
        adapter.notifyDataSetChanged();
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
            if(schedule!=null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(schedule.getbitmapByte(), 0, schedule.getbitmapByte().length);
                ((ImageView) view.findViewById(R.id.image_view_img)).setImageBitmap(bitmap);
                ((TextView) view.findViewById(R.id.text_view_title)).setText(schedule.getTitle());
                ((TextView) view.findViewById(R.id.text_view_date)).setText(schedule.getDate());
                ((TextView) view.findViewById(R.id.text_view_remark)).setText(schedule.getRemark());
            }
            return view;
        }
    }
}