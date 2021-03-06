package com.example.covidtest.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.covidtest.R;
import com.example.covidtest.data.TestContract;

public class DiabetesResultsCursorAdapter extends CursorAdapter {

    public DiabetesResultsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_diabetes, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateTv = view.findViewById(R.id.itemDate);
        TextView resultTv = view.findViewById(R.id.itemGlucoseLevel);
        TextView timeTv = view.findViewById(R.id.itemTime);

        String datetime = cursor.getString(cursor.getColumnIndexOrThrow(TestContract.DiabetesEntry.COLUMN_DATE_TIME));
        String result = "Glucose Level: " + cursor.getString(cursor.getColumnIndexOrThrow(TestContract.DiabetesEntry.COLUMN_GLUCOSE_LEVEL));

        //Parse date and time from datetime string of format YYYY-MM-DD HH:MM:SS.SSS
        String date = "Test taken on " + datetime.substring(8, 10) + "/" + datetime.substring(5, 7) + "/" + datetime.substring(0,4);
        String time = "Time: " + datetime.substring(11, 17);

        dateTv.setText(date);
        resultTv.setText(result);
        timeTv.setText(time);
    }
}
