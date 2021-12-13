package com.example.beacondetection;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private TextView text_message;
    private EditText username;
    private Button menu;
    private ImageView background;
    private TextView apptitle;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private BeaconManager beaconManager = null;
    protected static final String TAG = "MonitoringActivity";

    private String name = username.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_message = (TextView) findViewById(R.id.text_message);
        username = (EditText) findViewById(R.id.username);
        menu = (Button) findViewById(R.id.menu);
        background = (ImageView) findViewById(R.id.background);
        apptitle = (TextView) findViewById(R.id.apptitle);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        // menu button
        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                background.setVisibility(View.INVISIBLE);
                apptitle.setVisibility(View.INVISIBLE);
                menu.setVisibility(View.INVISIBLE);
                username.setVisibility(View.INVISIBLE);
                text_message.setVisibility(View.INVISIBLE);
            }
        });

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
                text_message.setText("Beacon detected!");
                // developer message
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                text_message.setText("No beacons!");
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
}