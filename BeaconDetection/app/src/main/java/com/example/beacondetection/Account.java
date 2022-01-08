package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class Account extends AppCompatActivity {

    private ImageButton back;
    private RecyclerView recycler;
    private AccountAdapter ad;
    private ArrayList<Student> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        back = (ImageButton) findViewById(R.id.back);
        recycler = (RecyclerView) findViewById(R.id.list);

        // get username
        //View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);

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
        ad = new AccountAdapter(this, list);
        recycler.setAdapter(ad);

        String path = "Student/" + MainActivity.getUser();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference(path);
        //Toast.makeText(this, "Firebase connection successful", Toast.LENGTH_LONG).show();

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