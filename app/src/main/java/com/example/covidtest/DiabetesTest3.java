package com.example.covidtest;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.covidtest.data.TestContract;
import com.example.covidtest.data.TestDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiabetesTest3 extends AppCompatActivity {

    Button backToHome;
    TextView level, heading;
    private TestDbHelper mDbHelper;
    boolean isValidResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes_test3);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String s = sharedPreferences.getString("diabetesResponse", "130");
        Log.e("Response : ", s);

        mDbHelper = new TestDbHelper(this);

        level = findViewById(R.id.levelTv);
        heading = findViewById(R.id.heading);
        backToHome = findViewById(R.id.back_to_home_diabetes);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backToHome.getText() == "Try Again") {
                    startActivity(new Intent(getApplicationContext(), DiabetesTest1.class));
                } else {
                    insertDiabetesTestResult();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                finish();
            }
        });
        analyseResult(s);
    }

    private void analyseResult(String s) {
        if (s.equals("3")) {
            heading.setText("An error has occured.");
            backToHome.setText("Try Again");
            isValidResult = false;
        } else {
            heading.setText("Glucose Level");
            level.setText(s + " mg/dl");
            backToHome.setText("Back to Home");
            isValidResult = true;
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void insertDiabetesTestResult() {
        if (isValidResult) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

            ContentValues values = new ContentValues();
            values.put(TestContract.DiabetesEntry.COLUMN_GLUCOSE_LEVEL, level.getText().toString());
            values.put(TestContract.DiabetesEntry.COLUMN_DATE_TIME, sdf.format(new Date()));

            long newRowId = db.insert(TestContract.DiabetesEntry.TABLE_NAME, null, values);
            Log.e("insertDiabetesResult: ", String.valueOf(newRowId));
        }

    }
}