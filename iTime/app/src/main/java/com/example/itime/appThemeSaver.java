package com.example.itime;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class appThemeSaver {
    private Context context;
    private int theme=0;

    public appThemeSaver(Context context) {
        this.context = context;
    }

    public int  getTheme(){
        return theme;
    }


    public void save(int theme){
        try{
            //序列化
            this.theme=theme;
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("appThemeSaver.txt",Context.MODE_PRIVATE));
            outputStream.writeObject(this.theme);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int load(){
        try{
            //反序列化
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("appThemeSaver.txt"));
            theme = (int) inputStream.readObject();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return theme;
    }
}
