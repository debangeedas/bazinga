package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CovidTest3 extends AppCompatActivity {
    Button backToHome;
    TextView covid, sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_test3);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String s = sharedPreferences.getString("response", "-");
//        Log.e("Response : ", s);

        covid = findViewById(R.id.covidTextView);
        sub = findViewById(R.id.subTextView);
        backToHome = findViewById(R.id.back_to_home_covid);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backToHome.getText() == "Try Again") {
                    startActivity(new Intent(getApplicationContext(), CovidTest1.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        });
        analyseResult("101");
    }

    private void analyseResult(String s) {
        if (s.equals("2") || s.charAt(0) == '0') {
            backToHome.setText("Try Again");
            sub.setText("Invalid results obtained");
        } else if (s.charAt(0) == '1'){
            if (s.charAt(1) == '0' && s.charAt(2) == '0') {
                covid.setText("COVID Negative");
                sub.setText("Only the control line is visible.");
            } else if (s.charAt(1) == '0' && s.charAt(2) == '1') {
                covid.setText("COVID Positive");
                sub.setText("IgG Positive");
            } else if (s.charAt(1) == '1' && s.charAt(2) == '0') {
                covid.setText("COVID Positive");
                sub.setText("IgM Positive");
            } else if (s.charAt(1) == '1' && s.charAt(2) == '1') {
                covid.setText("COVID Positive");
                sub.setText("IgG and IgM Positive");
            }
        }
    }
}