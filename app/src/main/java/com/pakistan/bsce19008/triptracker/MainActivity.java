package com.pakistan.bsce19008.triptracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.strictmode.Violation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    Button tripsBtn,startBtn,stopBtn;
    TextView textViewTitle, textViewCurrentDetail;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;
    private LocationManager locationManager;

    private static final float MAX_ACCELERATION = 10.0f; // m/s^2
    private static final float MAX_TURNING_RATE = 5.0f; // rad/s
    private static final float MAX_SPEED = 20.0f; // km/h
    private float lastAccelValue = 0.0f;
    private float accelThreshold = 10.0f; // set the threshold for harsh braking m/s^2
    private float accelChangeThreshold = 20.0f; // set the threshold for strong acceleration m/s^2
    private float speedThreshold = 20.0f; // set the threshold for strong acceleration m/s^2
    float gyroThreshold = 5.0f; // set this to an appropriate value for detecting harsh turns
    float gyroChangeThreshold = 1.0f; // set this to an appropriate value for detecting significant changes in gyroscope readings

    float lastGyroValue = 0.0f;
    float speedL;

//    private Location currentLocation;
     Date startTime;
     Date endTime;
//    private long startTime;
    private boolean isTripStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Button access
        tripsBtn = findViewById(R.id.tripScreenId);
        startBtn = findViewById(R.id.StartButton);
        stopBtn = findViewById(R.id.StopButton);
//        TextView Access
        textViewTitle = findViewById(R.id.tripTitleView);
        textViewCurrentDetail = findViewById(R.id.CurrentTripDetail);
        SimpleDateFormat formatter = new SimpleDateFormat(" MMMM,h:mm a");
        //String to store all data
        StringBuilder violationsBuilder = new StringBuilder();



        int permissionCode = 1;
        int fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(this, permissions, permissionCode);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                 speedL = location.getSpeed();
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                // Do something with the speed
            }
        });


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //start trip
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTripStarted) {

                    startTime = new Date();
                    String formattedDateStart = formatter.format(startTime);
                    textViewCurrentDetail.setText(formattedDateStart +" - start \nHave a nice journey......");
//                    violationsBuilder.append(formattedDateStart +" - start \n");
//                    accelerometerSensor
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            float acc_x = event.values[0];
                            float acc_y = event.values[1];
                            float acc_z = event.values[2];
                            // Calculate the change in acceleration
                            float accelChange = Math.abs(acc_x - lastAccelValue);
                            lastAccelValue = acc_x;
                            // Check if the change in acceleration exceeds the threshold
                            if (accelChange > accelThreshold) {
                                Date accTime = new Date();
                                String formattedAccTime = formatter.format(accTime);
//                            Violation violation = new Violation(Violation.Type.HARSH_BRAKING, accTime);
//                            violations.add(violation);
                                // Harsh braking detected
                                violationsBuilder.append(formattedAccTime +" - Harsh break \n");
                            }
                            if (accelChange > accelChangeThreshold) {
                                Date accTime = new Date();
                                String formattedAccTime = formatter.format(accTime);
                                // Strong acceleration detected
                                violationsBuilder.append(formattedAccTime + " - Strong acceleration \n");
                            }
                            // Check if the vehicle is overspeeding
//                        float speed = event.values[6];
                            if (speedL > speedThreshold) {
                                Date speedTime = new Date();
                                String formattedSpeedTime = formatter.format(speedTime);
                                // Overspeeding detected
                                violationsBuilder.append(formattedSpeedTime + " - Overspeeding \n");
                            }
                        }
                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {
                        }
                    },accelerometerSensor,sensorManager.SENSOR_DELAY_NORMAL);

//                gyroscopeSensor
                    sensorManager.registerListener(new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            float gyr_x = event.values[0];
                            float gyr_y = event.values[1];
                            float gyr_z = event.values[2];

                            // Calculate the change in gyroscope readings
                            float gyroChange = Math.abs(gyr_x - lastGyroValue);
                            lastGyroValue = gyr_x;
                            // Check if the change in gyroscope readings exceeds the threshold
                            if (gyroChange > gyroChangeThreshold) {
                                Date gyroTime = new Date();
                                String formattedGyroTime = formatter.format(gyroTime);
                                // Harsh turning detected
                                violationsBuilder.append(formattedGyroTime +" - Harsh turning \n");
                            }
                        }
                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {
                        }
                    },gyroscopeSensor,sensorManager.SENSOR_DELAY_NORMAL);



                    isTripStarted = true;
                }
//                textViewCurrentDetail.append("\nStart Time: "+startTime +"\n End Time: "+endTime);

//
                Log.d("here i click to start app", "onClick: start is running ");
            }
        });
        //stop trip
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isTripStarted) {
                    textViewCurrentDetail.setText("");
                    endTime = new Date();
                    textViewCurrentDetail.append(startTime+" -Start\n"+endTime+" -End"+"\n\n\n Results:\n"+violationsBuilder);
                    isTripStarted = false;
                }
                Log.d("here i click to stop app", "onClick: stop is running ");
            }
        });

        tripsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TripsScreen.class);
                startActivity(i);
            }
        });
    }

}