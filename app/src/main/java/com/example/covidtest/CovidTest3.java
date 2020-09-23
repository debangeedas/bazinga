package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtest.data.TestContract;
import com.example.covidtest.data.TestDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CovidTest3 extends AppCompatActivity {
    Button backToHome;
    TextView covid, sub;
    private TestDbHelper mDbHelper;
    boolean isValidResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_test3);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String s = sharedPreferences.getString("response", "-");
        Log.e("Response : ", s);

        mDbHelper = new TestDbHelper(this);

        covid = findViewById(R.id.covidTextView);
        sub = findViewById(R.id.subTextView);
        backToHome = findViewById(R.id.back_to_home_covid);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backToHome.getText() == "Try Again") {
                    startActivity(new Intent(getApplicationContext(), CovidTest1.class));
                } else {
                    insertCovidTestResult();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        });
        analyseResult(s);
    }

    private void analyseResult(String s) {
        if (s.equals("3")) {
            backToHome.setText("An error has occured.");
            sub.setText("Invalid results obtained");
            isValidResult = false;
        } else if (s.equals("2") ) {
            backToHome.setText("Try Again");
            sub.setText("Invalid results obtained");
            isValidResult = false;
        } else if (s.charAt(0) == '0') {
            backToHome.setText("Try Again");
            sub.setText("Wrong strip configuration. Please insert the strip properly.");
            isValidResult = false;
        } else if (s.charAt(0) == '1'){
            if (s.charAt(1) == '0' && s.charAt(2) == '0') {
                covid.setText("COVID Negative");
                sub.setText("Only control line is visible.");
            } else if (s.charAt(1) == '1' && s.charAt(2) == '0') {
                covid.setText("COVID Positive");
                sub.setText("IgG Positive");
            } else if (s.charAt(1) == '0' && s.charAt(2) == '1') {
                covid.setText("COVID Positive");
                sub.setText("IgM Positive");
            } else if (s.charAt(1) == '1' && s.charAt(2) == '1') {
                covid.setText("COVID Positive");
                sub.setText("IgG and IgM Positive");
            }
            isValidResult = true;
        }
    }

    @Override
    public void onBackPressed() {}

    private void insertCovidTestResult() {
        if (isValidResult) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

            ContentValues values = new ContentValues();
            values.put(TestContract.CovidEntry.COLUMN_RESULT, covid.getText().toString());
            values.put(TestContract.CovidEntry.COLUMN_DESCRIPTION, sub.getText().toString());
            values.put(TestContract.CovidEntry.COLUMN_DATE_TIME, sdf.format(new Date()));

            long newRowId = db.insert(TestContract.CovidEntry.TABLE_NAME, null, values);
            Log.e("insertCovidTestResult: ", String.valueOf(newRowId));
        }

    }
}