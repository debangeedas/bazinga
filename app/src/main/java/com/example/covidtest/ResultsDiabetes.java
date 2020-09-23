package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.covidtest.adapters.CovidResultsCursorAdapter;
import com.example.covidtest.adapters.DiabetesResultsCursorAdapter;
import com.example.covidtest.data.TestContract;
import com.example.covidtest.data.TestDbHelper;

import java.util.ArrayList;

public class ResultsDiabetes extends AppCompatActivity {

    private TestDbHelper mDbHelper;
    ListView listView;
    ArrayList<String> results = new ArrayList<>(), datetimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_diabetes);

        mDbHelper = new TestDbHelper(this);
        listView = findViewById(R.id.listView);
        View view = findViewById(R.id.emptyView);
        listView.setEmptyView(view);

        displayDbItems();
    }

    private void displayDbItems() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TestContract.DiabetesEntry._ID,
                TestContract.DiabetesEntry.COLUMN_GLUCOSE_LEVEL,
                TestContract.DiabetesEntry.COLUMN_DATE_TIME};


        Cursor cursor = db.query(
                TestContract.DiabetesEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        // Iterate through all the returned rows in the cursor
        while (cursor.moveToNext()) {
            results.add(cursor.getString(cursor.getColumnIndex(TestContract.DiabetesEntry.COLUMN_GLUCOSE_LEVEL)));
            datetimes.add(cursor.getString(cursor.getColumnIndex(TestContract.DiabetesEntry.COLUMN_DATE_TIME)));
        }

        try {
            DiabetesResultsCursorAdapter rca = new DiabetesResultsCursorAdapter(this, cursor);
            listView.setAdapter(rca);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String resultSelected = results.get(position);
                    String datetimeSelected = datetimes.get(position);

                    //Parse date and time from datetime string of format YYYY-MM-DD HH:MM:SS.SSS
                    String date = "Test taken on " + datetimeSelected.substring(8, 10) + "/" + datetimeSelected.substring(5, 7)
                            + "/" + datetimeSelected.substring(0,4);
                    String time = "Time: " + datetimeSelected.substring(11, 17);

                    String msg = "Diabetes Test Results as taken on " + date + " at " + time + "\n\n";
                    msg += "Your blood glucose level is " + resultSelected + "mg/dl.";

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hello + " + msg);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }
            });

        } finally {
//            cursor.close();
        }
    }
}