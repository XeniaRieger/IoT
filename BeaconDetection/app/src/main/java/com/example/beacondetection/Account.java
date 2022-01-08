package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Account extends AppCompatActivity {

    private ImageButton back;
    private RecyclerView recycler;
    private AccountAdapter ad;
    private ArrayList<Student> list;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        back = (ImageButton) findViewById(R.id.back);
        recycler = (RecyclerView) findViewById(R.id.list);

        // go back to MainActivity on back-button click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(Account.this, MainActivity.class);
                startActivity(start);
            }
        });

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        // displays the student info in recycler view
        ad = new AccountAdapter(this, list);
        recycler.setAdapter(ad);

        username = MainActivity.getUsername();

        String path = "Student/" + username;

        // get connection to Firebase and get specified path
        DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference(path);

        // get student data from database everytime data changes
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                Student student = new Student();
                student.setUsername(snapshot.getKey());
                student.setName(snapshot.child("name").getValue().toString());
                student.setSurname(snapshot.child("surname").getValue().toString());
                student.setAge(snapshot.child("age").getValue().toString());
                list.add(student);
                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}