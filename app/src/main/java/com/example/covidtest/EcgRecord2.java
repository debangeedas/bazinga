package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EcgRecord2 extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private long timeLeft = 10000; //in milliseconds
    Button start, restart;
    TextView countdown;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_record2);

        start = findViewById(R.id.start_ecg);
        restart = findViewById(R.id.restart_ecg);
        countdown = findViewById(R.id.time_left_tv);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start.getText() == "Save and Proceed")
                    startActivity(new Intent(getApplicationContext(), EcgRecord3.class));
                else {
                    startTimer();
                }
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                start.setText("Save and Proceed");
                start.setEnabled(true);
                restart.setEnabled(true);
                timeLeft = 10000;
            }
        }.start();

        isRunning = true;
        start.setEnabled(false);
        restart.setEnabled(false);
    }

    private void updateTimerText() {
        long seconds = timeLeft/1000;
        countdown.setText(seconds + " seconds left");
    }

}