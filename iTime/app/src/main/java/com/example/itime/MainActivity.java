package com.example.itime;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private static final int SET_SCHEDULE=201;
    private AppBarConfiguration mAppBarConfiguration;
    private byte[] bitmapByte;
    private String title,date,remark;
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
                    remark = data.getStringExtra("remark");
                    bitmapByte= data.getByteArrayExtra("bitmap");
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

    public byte[] getBitmapByte(){
        return bitmapByte;
    }

    public String geTitle(){
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getRemark(){
        return remark;
    }

}
