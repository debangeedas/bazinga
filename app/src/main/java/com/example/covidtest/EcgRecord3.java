package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.covidtest.data.TestContract;
import com.example.covidtest.data.TestDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EcgRecord3 extends AppCompatActivity {

    private TestDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_record3);

        Button backToHome = findViewById(R.id.back_to_home_ecg);
        Button share = findViewById(R.id.share_ecg);
        mDbHelper = new TestDbHelper(this);

        ImageView iv = findViewById(R.id.ecg_graph_result);
        String uri = "@drawable/ecg_1";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        iv.setImageDrawable(res);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertEcgTestResult();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {}

    private void insertEcgTestResult() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        ContentValues values = new ContentValues();
        values.put(TestContract.EcgEntry.COLUMN_DATE_TIME, sdf.format(new Date()));

        long newRowId = db.insert(TestContract.EcgEntry.TABLE_NAME, null, values);
        Log.e("insertEcgResult: ", String.valueOf(newRowId));
    }
}