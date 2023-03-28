package com.pakistan.bsce19008.triptracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private List<Violation> violations = new ArrayList<>();
    private Location currentLocation;
    private long startTime;
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


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //start trip
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTripStarted) {
                    startTime = System.currentTimeMillis();
                    isTripStarted = true;
                }
//              locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
//                        @Override
//                        public void onLocationChanged(@NonNull Location location) {
//
//                        }
//                    });
//                accelerometerSensor
                sensorManager.registerListener(new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        float acc_x = event.values[0];
                        float acc_y = event.values[1];
                        float acc_z = event.values[2];
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
                    }
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    }
                },gyroscopeSensor,sensorManager.SENSOR_DELAY_NORMAL);
            }
        });
        //stop trip
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sensorManager.unregisterListener();
//                locationManager.removeUpdates(this);
                if (isTripStarted) {
                    isTripStarted = false;
                }
                // create a StringBuilder to store the violations text
                StringBuilder violationsText = new StringBuilder();
                // iterate over the list of violations and append each violation's details to the StringBuilder
                for (Violation violation : violations) {
                    violationsText.append(violation).append("\n\n");
                }
                // set the text of the TextView to the violations text
                textViewCurrentDetail.setText(violationsText.toString());
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