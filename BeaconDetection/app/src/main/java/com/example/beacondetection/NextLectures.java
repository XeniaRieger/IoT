package com.example.beacondetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class NextLectures extends AppCompatActivity {

    private ImageButton back;
    private RecyclerView recycler;
    private MyAdapter ad;
    private ArrayList<Lecture> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_lectures);

        back = (ImageButton) findViewById(R.id.back);
        recycler = (RecyclerView) findViewById(R.id.list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(NextLectures.this, MainActivity.class);
                startActivity(start);
            }
        });

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        ad = new MyAdapter(this, list);
        recycler.setAdapter(ad);

        DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference("Lectures/dile9663");
        Toast.makeText(this, "Firebase connection successful", Toast.LENGTH_LONG).show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snap: snapshot.getChildren()) {
                    String course = snap.getKey();
                    for(DataSnapshot snap1: snap.getChildren()){
                        System.out.println(snap1);
                        Lecture lecture = new Lecture();
                        lecture.setCourse(course);
                        lecture.setLecture(snap1.getKey());
                        lecture.setDate(snap1.child("date").getValue().toString());
                        lecture.setTime(snap1.child("time").getValue().toString());
                        lecture.setRoom(snap1.child("room").getValue().toString());
                        list.add(lecture);
                    }
                }
                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}