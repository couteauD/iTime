package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView listViewRecord;
    private ImageButton imageButtonNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewRecord=(ListView)this.findViewById(R.id.list_view_record);
        imageButtonNew=(ImageButton)this.findViewById(R.id.image_button_new);

        imageButtonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,newScheduleActivity.class);
                startActivity(i);
            }
        });
    }
}
