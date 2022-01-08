package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        back = (ImageButton) findViewById(R.id.back);
        scroll = (ScrollView) findViewById(R.id.scroll);
        titletext = (TextView) findViewById(R.id.titletext);
        layout = (LinearLayout) findViewById(R.id.layout);
        // get username
        View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
        text = new TextView(this);
        bar = new PieChartView(this);

        String path = "Student/" + MainActivity.getUser();
        DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference(path);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                /*list.clear();
                Student student = new Student();
                student.setUsername(snapshot.getKey());
                student.setName(snapshot.child("name").getValue().toString());
                student.setSurname(snapshot.child("surname").getValue().toString());
                student.setAge(snapshot.child("age").getValue().toString());
                list.add(student);

                past_lectures = NextLectures.list;*/

                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //text.setText(); // Course name
                layout.addView(text);

                List<SliceValue> pieData = new ArrayList<>();
                pieData.add(new SliceValue(85, Color.GREEN));
                pieData.add(new SliceValue(15, Color.RED));
                PieChartData pieChartData = new PieChartData(pieData);

                bar.setPieChartData(pieChartData);
                bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                bar.setMinimumWidth(500);
                bar.setMinimumHeight(500);
                layout.addView(bar);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(Statistics.this, MainActivity.class);
                startActivity(start);
            }
        });
    }
}