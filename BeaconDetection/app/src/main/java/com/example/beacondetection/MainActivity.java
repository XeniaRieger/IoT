package com.example.beacondetection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;

import pl.droidsonroids.gif.GifImageView;
import com.anthonyfdev.dropdownview.DropDownView;

public class MainActivity extends AppCompatActivity {
    private TextView text_message;
    private EditText username;
    private ImageView BackgroundImage;
    private TextView LogoTitle;
    private ImageView SuccessIcon;
    private GifImageView LoadingIcon;
    private Button menubutton1;
    private Button menubutton2;
    private Button menubutton3;
    private Button menubutton4;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private BeaconManager beaconManager = null;
    protected static final String TAG = "MonitoringActivity";

    /*private static Connection getConnection() throws URISyntaxException, java.sql.SQLException {
        String dbUrl = System.getenv("mysql://b7e6ab00851195:a2312c6b@eu-cdbr-west-02.cleardb.net/heroku_478575134a3d5df?reconnect=true");
        return DriverManager.getConnection(dbUrl);
    }*/


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_message = (TextView) findViewById(R.id.BeaconMessage);
        username = (EditText) findViewById(R.id.UsernameBar);
        BackgroundImage = (ImageView) findViewById(R.id.BackgroundImage);
        LogoTitle = (TextView) findViewById(R.id.LogoTitle);
        SuccessIcon = (ImageView) findViewById(R.id.SuccessIcon);
        LoadingIcon = (GifImageView) findViewById(R.id.LoadingIcon);

        View inflatedView = getLayoutInflater().inflate(R.layout.footer, null);
        menubutton1 = (Button) inflatedView.findViewById(R.id.menubutton1);
        menubutton2 = (Button) inflatedView.findViewById(R.id.menubutton2);
        menubutton3 = (Button) inflatedView.findViewById(R.id.menubutton3);
        menubutton4 = (Button) inflatedView.findViewById(R.id.menubutton4);

        SuccessIcon.setVisibility(View.INVISIBLE);

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
                        text_message.setText("Attending lecture in room " + beacons.iterator().next().getId2() + "!");
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
    public void button1(View view) {
        Log.i(TAG, "HIIII");
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
}