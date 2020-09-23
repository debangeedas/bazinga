package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.covidtest.data.TestContract;
import com.example.covidtest.data.TestDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EcgRecord2 extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private long timeLeft = 10000; //in milliseconds
    ImageView ecgImage;
    Button start, restart;
    TextView countdown;
    boolean isRunning = false;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_record2);

        start = findViewById(R.id.start_ecg);
        restart = findViewById(R.id.restart_ecg);
        countdown = findViewById(R.id.time_left_tv);
        pb = findViewById(R.id.progressBar);
        ecgImage = findViewById(R.id.ecg_graph);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start.getText() == "Save and Proceed") {
                    Intent intent = new Intent(getApplicationContext(), EcgRecord3.class);
//                    BitmapDrawable bd = (BitmapDrawable) ecgImage.getDrawable();
//                    Bitmap b = bd.getBitmap();
//                    intent.putExtra("bitmap", b);
                    startActivity(intent);
                }
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
        pb.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                pb.setVisibility(View.INVISIBLE);
                String uri = "@drawable/ecg_1";
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                ecgImage.setImageDrawable(res);

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