package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class PastLectures extends AppCompatActivity {

    private ImageButton back;
    private ScrollView scroll;
    private TextView titletext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_lectures);

        back = (ImageButton) findViewById(R.id.back);
        scroll = (ScrollView) findViewById(R.id.scroll);
        titletext = (TextView) findViewById(R.id.titletext);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(PastLectures.this, MainActivity.class);
                startActivity(start);
            }
        });
    }
}