package com.example.itime;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import com.example.itime.ui.mainpage.MainpageFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


public class MainActivity extends AppCompatActivity implements MainpageFragment.FragmentInteraction{

    private static final int SET_SCHEDULE=201;
    private static final int COUNTDOWN=202;
    private AppBarConfiguration mAppBarConfiguration;
    private appThemeSaver appthemeSaver;
    private byte[] bitmapByte;
    private String title,date,time,remark,cycle,mark;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appthemeSaver=new appThemeSaver(this);
        int themeColor=appthemeSaver.load();
        setContentView(R.layout.activity_main);

        //状态栏设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(themeColor);
        }
        //标题栏设置
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(themeColor);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(createColorStateList(themeColor, themeColor, themeColor, themeColor));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,newScheduleActivity.class);
                startActivityForResult(intent,SET_SCHEDULE);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemTextColor(createColorStateList(Color.BLACK, themeColor, themeColor, themeColor));
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

            case COUNTDOWN:
                if(resultCode==RESULT_FIRST_USER){
                    int position=data.getIntExtra("position",-1);

                    FragmentManager manager=getSupportFragmentManager();
                    FragmentTransaction transation = manager.beginTransaction();
                    MainpageFragment mainpageFragment=new MainpageFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);

                    mainpageFragment.setArguments(bundle);
                    transation.replace(R.id.nav_host_fragment,mainpageFragment);
                    transation.addToBackStack(null);
                    transation.commit();
                }
                if(resultCode==RESULT_OK){
                    title = data.getStringExtra("title");
                    date = data.getStringExtra("date");
                    time = data.getStringExtra("time");
                    remark = data.getStringExtra("remark");
                    cycle=data.getStringExtra("cycle");
                    mark=data.getStringExtra("mark");
                    bitmapByte= data.getByteArrayExtra("bitmap");
                    position=data.getIntExtra("position",-1);

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
                    bundle.putInt("position",position);

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
        }
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
}
