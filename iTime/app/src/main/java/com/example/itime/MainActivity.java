package com.example.itime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.itime.ui.mainpage.MainpageFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainpageFragment.FragmentInteraction{

    private static final int SET_SCHEDULE=201;
    private static final int COUNTDOWN=202;
    private AppBarConfiguration mAppBarConfiguration;
    private byte[] bitmapByte;
    private String title,date,time,remark,cycle,mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,newScheduleActivity.class);
                startActivityForResult(intent,SET_SCHEDULE);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_mainpage, R.id.nav_mark, R.id.nav_component,
                R.id.nav_color, R.id.nav_senior, R.id.nav_setting,R.id.nav_setting,R.id.nav_about,R.id.nav_help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SET_SCHEDULE:
                if(resultCode==RESULT_OK) {
                    title = data.getStringExtra("title");
                    date = data.getStringExtra("date");
                    time = data.getStringExtra("time");
                    remark = data.getStringExtra("remark");
                    cycle=data.getStringExtra("cycle");
                    mark=data.getStringExtra("mark");
                    bitmapByte= data.getByteArrayExtra("bitmap");

                    FragmentManager manager=getSupportFragmentManager();
                    FragmentTransaction transation = manager.beginTransaction();
                    MainpageFragment mainpageFragment=new MainpageFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("title",title);
                    bundle.putString("date",date);
                    bundle.putString("time",time);
                    bundle.putString("remark",remark);
                    bundle.putString("cycle",cycle);
                    bundle.putString("mark",mark);
                    bundle.putByteArray("bitmap",bitmapByte);

                    mainpageFragment.setArguments(bundle);
                    transation.replace(R.id.nav_host_fragment,mainpageFragment);
                    transation.addToBackStack(null);
                    transation.commit();
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * 给Fragment调用
     * @param title,date,time,remark,cycle,mark,bitmap,position
     */
    @Override
    public void process(String title, String date, String time, String remark, String cycle, String mark, byte[] bitmap,int position) {
        if (title!= null) {
            Intent intent=new Intent(MainActivity.this,CountDownActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("date",date);
            intent.putExtra("time",time);
            intent.putExtra("remark",remark);
            intent.putExtra("cycle",cycle);
            intent.putExtra("mark",mark);
            intent.putExtra("bitmap",bitmap);
            intent.putExtra("position",position);
            startActivityForResult(intent,COUNTDOWN);
            MainActivity.this.finish();

        }
    }
}
