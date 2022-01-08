package com.example.beacondetection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Statistics extends AppCompatActivity {

    private ImageButton back;
    private ScrollView scroll;
    private TextView titletext;
    private LinearLayout layout;
    private PieChartView bar;
    private TextView text;

    private ArrayList<Lecture> past_lectures;
    private HashMap<String, Integer> map_attended;
    private HashMap<String, Integer> map_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        back = (ImageButton) findViewById(R.id.back);
        scroll = (ScrollView) findViewById(R.id.scroll);
        titletext = (TextView) findViewById(R.id.titletext);
        layout = (LinearLayout) findViewById(R.id.layout);

        //View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
       
        text = new TextView(this);
        bar = new PieChartView(this);

        String path = "Student/" + MainActivity.getUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference(path);

//        reference.addValueEventListener(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                // count attended lectures for each course
//                for(DataSnapshot snap : snapshot.getChildren()) {
//                    String course = snap.child("course").getValue().toString();
//                    if (map_attended.containsKey(course)) {
//                        int value = map_attended.get(course) + 1;
//                        map_attended.replace(course, value);
//                    } else {
//                        map_attended.put(course, 1);
//                    }
//                }
//                // count total lectures
//                past_lectures = PastLectures.list;
//                for(Lecture lec : past_lectures) {
//                    String course = snap.child("course").getValue().toString();
//                    if (map_attended.containsKey(course)) {
//                        int value = map_attended.get(course) + 1;
//                        map_attended.replace(course, value);
//                    } else {
//                        map_attended.put(course, 1);
//                    }
//                }
//
//                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                text.setText(snapshot.child("course").getValue().toString()); // Course name
//                layout.addView(text);
//
//                int total_courses = past_lectures.size();
//                int attended_courses = 0;
//                int not_attended_courses = total_courses - attended_courses;
//
//                List<SliceValue> pieData = new ArrayList<>();
//                pieData.add(new SliceValue(attended_courses, Color.GREEN));
//                pieData.add(new SliceValue(not_attended_courses, Color.RED));
//                PieChartData pieChartData = new PieChartData(pieData);
//
//                bar.setPieChartData(pieChartData);
//                bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                bar.setMinimumWidth(500);
//                bar.setMinimumHeight(500);
//                layout.addView(bar);
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(Statistics.this, MainActivity.class);
                startActivity(start);
            }
        });
    }
}