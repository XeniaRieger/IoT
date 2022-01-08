package com.example.beacondetection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;
import com.anthonyfdev.dropdownview.DropDownView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private TextView text_message;
    private ImageView BackgroundImage;
    private TextView LogoTitle;
    private EditText username;
    private ImageView SuccessIcon;
    private GifImageView LoadingIcon;
    private ImageButton submit;
    private static String user;
    private Button menubutton1;
    private Button menubutton2;
    private Button menubutton3;
    private Button menubutton4;
    private Button menubutton5;
    private ArrayList<Lecture> next_lectures; //TO DELETE

    public static String getUser() {
        return user;
    }

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private BeaconManager beaconManager = null;
    protected static final String TAG = "MonitoringActivity";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_message = (TextView) findViewById(R.id.BeaconMessage);
        BackgroundImage = (ImageView) findViewById(R.id.BackgroundImage);
        LogoTitle = (TextView) findViewById(R.id.LogoTitle);
        SuccessIcon = (ImageView) findViewById(R.id.SuccessIcon);
        LoadingIcon = (GifImageView) findViewById(R.id.LoadingIcon);

        View inflatedView = getLayoutInflater().inflate(R.layout.footer, null);
        menubutton1 = (Button) inflatedView.findViewById(R.id.menubutton1);
        menubutton2 = (Button) inflatedView.findViewById(R.id.menubutton2);
        menubutton3 = (Button) inflatedView.findViewById(R.id.menubutton3);
        menubutton4 = (Button) inflatedView.findViewById(R.id.menubutton4);
        menubutton5 = (Button) inflatedView.findViewById(R.id.menubutton5);

        SuccessIcon.setVisibility(View.INVISIBLE);

        submit = (ImageButton) findViewById(R.id.submit);
        username = (EditText) findViewById(R.id.username);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                System.out.println(user);
                username.onEditorAction(EditorInfo.IME_ACTION_DONE);
                username.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.INVISIBLE);
            }
        });

        // drop down menu
        DropDownView dropdown = (DropDownView) findViewById(R.id.dropdown);
        View collapsedView = LayoutInflater.from(this).inflate(R.layout.header, null, false);
        View expandedView = LayoutInflater.from(this).inflate(R.layout.footer, null, false);
        dropdown.setHeaderView(collapsedView);
        dropdown.setExpandedView(expandedView);

        collapsedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click the drop down
                // will open or close
                if (dropdown.isExpanded()) {
                    dropdown.collapseDropDown();
                } else {
                    dropdown.expandDropDown();
                }
            }
        });

        beaconManager = BeaconManager.getInstanceForApplication(this);

        // ask for permission to use location
        // check if SDK is new enough
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("This app needs background location access");
                    builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @TargetApi(23)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_BACKGROUND_LOCATION);
                        }
                    });
                    builder.show();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

        Region MonitoringRegion = new Region("MonitoringBeacons", null, null, null);
        // depends on Beacon type
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                // developer message
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                // developer message
                Log.i(TAG, "I no longer see a beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                // developer message
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon: beacons) {
                    if(beacon.getDistance() < 2) {
                        SuccessIcon.setVisibility(View.VISIBLE);
                        LoadingIcon.setVisibility(View.INVISIBLE);
                        String beaconRoom = beacons.iterator().next().getId2().toString();
                        text_message.setText("Attending lecture in room " + beaconRoom + "!");

                        //next_lectures = NextLectures.getNextLectures();

                        if(next_lectures != null) {
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = null;
                            Date today = null;
                            for (Lecture lec : next_lectures) {
                                try {
                                    today = df.parse(df.format(new Date()));
                                    date = df.parse(lec.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (date.equals(today) && lec.getRoom().equalsIgnoreCase(beaconRoom)) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference("Attendance/"+lec.getCourse()+"/"+lec.getLecture());
                                    reference.push().setValue(user);
                                }
                            }
                        }
                    }
                    // developer message
                    Log.i(TAG, "The beacon I see is about "+beacon.getDistance()+" meters away.");
                }
            }
        });

        beaconManager.startMonitoring(MonitoringRegion);
        // find close beacons
        beaconManager.startRangingBeacons(MonitoringRegion);
    }

    //TO REGISTER THE ATTENDANCE, NOT COMPLETED YET, TO PUT AS A WORK IN PROGRESS IN THE REPORT
    public void registerAttendance(ArrayList<Lecture> next_lec){

        if(next_lec != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            Date today = null;
            for (Lecture lec : next_lec) {
                try {
                    today = df.parse(df.format(new Date()));
                    date = df.parse(lec.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date.equals(today) && lec.getRoom().equalsIgnoreCase("dl40")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference("Attendance/"+lec.getCourse()+"/"+lec.getLecture());
                    reference.push().setValue(user);
                }
            }
        }
    }

    public void button1(View view) {
        Intent courses = new Intent(MainActivity.this, MyCourses.class);
        startActivity(courses);
    }
    public void button2(View view) {
        Intent next_lec = new Intent(MainActivity.this, NextLectures.class);
        startActivity(next_lec);
    }
    public void button3(View view) {
        Intent past_lec = new Intent(MainActivity.this, PastLectures.class);
        startActivity(past_lec);
    }
    public void button4(View view) {
        Intent stat = new Intent(MainActivity.this, Statistics.class);
        startActivity(stat);
    }
    public void button5(View view) {
        Intent acc = new Intent(MainActivity.this, Account.class);
        startActivity(acc);
    }
}