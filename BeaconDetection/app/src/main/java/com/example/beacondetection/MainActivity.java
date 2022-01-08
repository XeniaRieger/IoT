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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private static TextView text_message;
    private static ImageView BackgroundImage;
    private static TextView LogoTitle;
    private static EditText usernameEdit;
    private static String username;
    private static ImageView SuccessIcon;
    private static GifImageView LoadingIcon;
    private static Button menubutton1;
    private static Button menubutton2;
    private static Button menubutton3;
    private static Button menubutton4;
    private static Button menubutton5;
    private ArrayList<Lecture> next_lectures;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private BeaconManager beaconManager = null;
    private static final String TAG = "MonitoringActivity"; // source of the logged messages later, for debugging

    private String usernameWarning;

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

        // get buttons from the drop-down menu
        View inflatedView = getLayoutInflater().inflate(R.layout.footer, null);
        menubutton1 = (Button) inflatedView.findViewById(R.id.menubutton1);
        menubutton2 = (Button) inflatedView.findViewById(R.id.menubutton2);
        menubutton3 = (Button) inflatedView.findViewById(R.id.menubutton3);
        menubutton4 = (Button) inflatedView.findViewById(R.id.menubutton4);
        menubutton5 = (Button) inflatedView.findViewById(R.id.menubutton5);

        // only show this icon when beacons are detected
        SuccessIcon.setVisibility(View.INVISIBLE);

        // if the start page is accessed from another page keep the username from before
        usernameEdit = (EditText) findViewById(R.id.username);
        if(username == null) {
            username = usernameEdit.getText().toString();
        } else {
            usernameEdit.setText(username);
        }

        // message to show when user wants to proceed with unset username
        usernameWarning = "Please set username";

        // drop down menu
        DropDownView dropdown = (DropDownView) findViewById(R.id.dropdown);
        View collapsedView = LayoutInflater.from(this).inflate(R.layout.header, null, false);
        View expandedView = LayoutInflater.from(this).inflate(R.layout.footer, null, false);
        dropdown.setHeaderView(collapsedView);
        dropdown.setExpandedView(expandedView);

        // click on menu shows buttons
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

        // ask user for permission to use location on first entering the app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if permission is specified in android manifest
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // check if permission was already granted
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

        // no grouping IDs as we only define one MonitoringRegion
        Region MonitoringRegion = new Region("MonitoringBeacons", null, null, null);
        // depends on Beacon type, we use iBeacons layout (for other types: change the string accordingly)
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        // detects beacons in a range of 70 meters
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

        // provides a collection of all detected beacons -> only work with close beacons
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // do not do anything if username is not set
                if(username.equals("")) {
                    return;
                }
                // check distance for every beacon
                for (Beacon beacon: beacons) {
                    if(beacon.getDistance() < 2) {
                        // show the user that a beacon is detected
                        SuccessIcon.setVisibility(View.VISIBLE);
                        LoadingIcon.setVisibility(View.INVISIBLE);
                        String beaconRoom = beacons.iterator().next().getId2().toString();
                        text_message.setText("Attending lecture in room " + beaconRoom + "!");

                        // save the attendance to the database, in progress!!!
                        next_lectures = NextLectures.list;
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
                                if (date.equals(today) && lec.getRoom().equals(beaconRoom)) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance("https://iotprojectg4-79ffa-default-rtdb.firebaseio.com/").getReference("Lecture/"+lec.getCourse()+"/"+lec.getLecture());
                                    reference.push().setValue(username);
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
        beaconManager.startRangingBeacons(MonitoringRegion);

        // remove message 'set username' when username is set and save username
        usernameEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(text_message.getText().equals(usernameWarning)) {
                    text_message.setText("");
                }
                username = s.toString();

            }
        });
    }
    public void button1(View view) {
        if(isSetUsername()) {
            Intent courses = new Intent(MainActivity.this, MyCourses.class);
            startActivity(courses);
        }
    }
    public void button2(View view) {
        if(isSetUsername()) {
            Intent next_lec = new Intent(MainActivity.this, NextLectures.class);
            startActivity(next_lec);
        }
    }
    public void button3(View view) {
        if(isSetUsername()) {
            Intent past_lec = new Intent(MainActivity.this, PastLectures.class);
            startActivity(past_lec);
        }
    }
    public void button4(View view) {
        if(isSetUsername()) {
            Intent stat = new Intent(MainActivity.this, Statistics.class);
            startActivity(stat);
        }
    }
    public void button5(View view) {
        if(isSetUsername()) {
            Intent acc = new Intent(MainActivity.this, Account.class);
            startActivity(acc);
        }
    }

    public Boolean isSetUsername() {
        if(username.equals("")) {
            text_message.setText(usernameWarning);
            return false;
        }
        else {
            return true;
        }
    }

    public static String getUsername() {
        return username;
    }
}