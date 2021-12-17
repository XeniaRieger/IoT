package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Statistics extends AppCompatActivity {

    private ImageButton back;
    private ScrollView scroll;
    private TextView titletext;
    private LinearLayout layout;
    private PieChartView bar;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        back = (ImageButton) findViewById(R.id.back);
        scroll = (ScrollView) findViewById(R.id.scroll);
        titletext = (TextView) findViewById(R.id.titletext);
        layout = (LinearLayout) findViewById(R.id.layout);

        // do for each course
        text = new TextView(this);
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text.setText("Course Name");
        layout.addView(text);

        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(85, Color.GREEN));
        pieData.add(new SliceValue(15, Color.RED));
        PieChartData pieChartData = new PieChartData(pieData);

        bar = new PieChartView(this);
        bar.setPieChartData(pieChartData);
        bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bar.setMinimumWidth(500);
        bar.setMinimumHeight(500);
        layout.addView(bar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(Statistics.this, MainActivity.class);
                startActivity(start);
            }
        });
    }
}