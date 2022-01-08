package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Statistics extends AppCompatActivity {

    private ImageButton back;
    private ScrollView scroll;
    private TextView titletext;
    private LinearLayout layout;
    private PieChartView bar;
    private TextView text;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        back = (ImageButton) findViewById(R.id.back);
        scroll = (ScrollView) findViewById(R.id.scroll);
        titletext = (TextView) findViewById(R.id.titletext);
        layout = (LinearLayout) findViewById(R.id.layout);

        username = MainActivity.getUsername();
        text = new TextView(this);
        bar = new PieChartView(this);

        // connect to database
        String path = "Attendance/" + username;
        DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference(path);

        // template for attendance percentage, should be repeated for every attended course
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text.setText("Course"); // Course name
        layout.addView(text);

        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(75, Color.GREEN)); // setting sample data, should be attended courses
        pieData.add(new SliceValue(25, Color.RED)); // not attended courses
        PieChartData pieChartData = new PieChartData(pieData);

        bar.setPieChartData(pieChartData);
        bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bar.setMinimumWidth(500);
        bar.setMinimumHeight(500);
        layout.addView(bar);

        // go back to MainActivity on back-button click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(Statistics.this, MainActivity.class);
                startActivity(start);
            }
        });
    }
}