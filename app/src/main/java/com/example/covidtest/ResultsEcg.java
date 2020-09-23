package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.covidtest.adapters.DiabetesResultsCursorAdapter;
import com.example.covidtest.adapters.EcgResultsCursorAdapter;
import com.example.covidtest.data.TestContract;
import com.example.covidtest.data.TestDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResultsEcg extends AppCompatActivity {

    private TestDbHelper mDbHelper;
    ListView listView;
    ArrayList<String> datetimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_ecg);

        mDbHelper = new TestDbHelper(this);
        listView = findViewById(R.id.listView);
        View view = findViewById(R.id.emptyView);
        listView.setEmptyView(view);

        displayDbItems();
    }

    private void displayDbItems() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TestContract.EcgEntry._ID,
                TestContract.EcgEntry.COLUMN_DATE_TIME};

        Cursor cursor = db.query(
                TestContract.EcgEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        // Iterate through all the returned rows in the cursor
        while (cursor.moveToNext()) {
            datetimes.add(cursor.getString(cursor.getColumnIndex(TestContract.DiabetesEntry.COLUMN_DATE_TIME)));
        }

        try {
            EcgResultsCursorAdapter rca = new EcgResultsCursorAdapter(this, cursor);
            listView.setAdapter(rca);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String datetimeSelected = datetimes.get(position);

                    //Parse date and time from datetime string of format YYYY-MM-DD HH:MM:SS.SSS
                    String date = "Test taken on " + datetimeSelected.substring(8, 10) + "/" + datetimeSelected.substring(5, 7)
                            + "/" + datetimeSelected.substring(0,4);
                    String time = datetimeSelected.substring(11, 17);

                    String uriPath = "android.resource://" + getPackageName() + "/drawable/ecg_1";
                    Uri uri = Uri.parse(uriPath);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "ECG recorded on " + date + " at " + time);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent, "Share using"));
                }
            });

        } finally {
//            cursor.close();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}