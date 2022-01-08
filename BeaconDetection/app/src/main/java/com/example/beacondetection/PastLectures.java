package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PastLectures extends AppCompatActivity {

    private ImageButton back;
    private RecyclerView recycler;
    private LectureAdapter ad;
    public static ArrayList<Lecture> list;
    public SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_lectures);

        back = (ImageButton) findViewById(R.id.back);
        recycler = (RecyclerView) findViewById(R.id.list);
        df = new SimpleDateFormat("dd/MM/yyyy");

      //View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(PastLectures.this, MainActivity.class);
                startActivity(start);
            }
        });

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        ad = new LectureAdapter(this, list);
        recycler.setAdapter(ad);

        String path = "Lectures/"  + MainActivity.getUser();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference(path);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snap: snapshot.getChildren()) {
                    String course = snap.getKey();
                    for(DataSnapshot snap1: snap.getChildren()){
                        Date date = null;
                        Date today = null;
                        String snapDate = snap1.child("date").getValue().toString();
                        try {
                            date = df.parse(snapDate);
                            today = df.parse(df.format(new Date()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //TIME STILL TO MANAGE
                        if(date.before(today)) {
                            Lecture lecture = new Lecture();
                            lecture.setCourse(course);
                            lecture.setLecture(snap1.getKey());
                            lecture.setDate(snapDate);
                            lecture.setTime(snap1.child("time").getValue().toString());
                            lecture.setRoom(snap1.child("room").getValue().toString());
                            list.add(lecture);
                        }
                    }
                }
                Collections.sort(list);
                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}