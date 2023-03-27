package com.pakistan.bsce19008.triptracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button tripsBtn,startBtn,stopBtn;
    TextView textViewTitle, textViewCurrentDetail;

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

    }
}