package com.example.itime;

import android.content.Context;

import com.example.itime.model.Schedule;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ScheduleSaver {
    private Context context;
    private ArrayList<Schedule> schedules=new ArrayList<>();

    public ScheduleSaver(Context context) {
        this.context = context;
    }

    public ArrayList<Schedule> getSchedules(){
        return schedules;
    }


    public void save(){
        try{
            //序列化
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(schedules);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Schedule> load(){
        try{
            //反序列化
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
            schedules = (ArrayList<Schedule>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return schedules;
    }
}
