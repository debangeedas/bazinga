package com.example.covidtest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.covidtest.data.TestContract.*;

public class TestDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    public TestDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_COVID_TABLE =  "CREATE TABLE " + CovidEntry.TABLE_NAME + " ("
                + CovidEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CovidEntry.COLUMN_RESULT + " TEXT NOT NULL, "
                + CovidEntry.COLUMN_DESCRIPTION + " TEXT, "
                + CovidEntry.COLUMN_DATE_TIME + " TEXT NOT NULL);";

        db.execSQL(CREATE_COVID_TABLE);


        String CREATE_ECG_TABLE =  "CREATE TABLE " + EcgEntry.TABLE_NAME + " ("
                + EcgEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + EcgEntry.COLUMN_IMAGE + " BLOB NOT NULL, "
                + EcgEntry.COLUMN_DATE_TIME + " TEXT NOT NULL);";

        db.execSQL(CREATE_ECG_TABLE);


        String CREATE_DIABETES_TABLE =  "CREATE TABLE " + DiabetesEntry.TABLE_NAME + " ("
                + DiabetesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DiabetesEntry.COLUMN_GLUCOSE_LEVEL + " REAL NOT NULL, "
                + DiabetesEntry.COLUMN_DATE_TIME + " TEXT NOT NULL);";

        db.execSQL(CREATE_DIABETES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
