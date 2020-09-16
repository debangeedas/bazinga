package com.example.covidtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

        TextInputLayout dob, address;
        EditText nameEt, dobEt, emailEt, addressEt;
        Button button;
        DatePickerDialog datePicker;
        Calendar calendar;
        RadioGroup genderRadioGroup;
        RadioButton genderRadioButton;
        String gender;
        ProgressBar progressBar;

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dob = findViewById(R.id.dobTextField);
        address = findViewById(R.id.addressTextField);
        nameEt = findViewById(R.id.nameEt);
        dobEt = findViewById(R.id.dobEt);
        emailEt = findViewById(R.id.emailEt);
        addressEt = findViewById(R.id.addressEt);
        button = findViewById(R.id.button);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        progressBar = findViewById(R.id.progressBar);

        dob.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);

                datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dobEt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, y, m, d);
                datePicker.show();
            }
        });

//        address.setEndIconOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
//                } else {
//                    getCurrentLocation();
//                }
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    Toast.makeText(MainActivity.this, "Please select a gender option", Toast.LENGTH_SHORT).show();
                } else if (nameEt.getText().toString().isEmpty()) {
                    nameEt.setError("Name is required");
                } else if (dobEt.getText().toString().isEmpty()) {
                    dobEt.setError("Date of birth is required");
                } else if (emailEt.getText().toString().isEmpty()) {
                    emailEt.setError("Email is required");
                } else {
                    genderRadioButton = findViewById(selectedId);
                    gender = genderRadioButton.getText().toString();

                    updateSharedPreferences();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        editor.putString("name", nameEt.getText().toString());
        editor.putString("email", emailEt.getText().toString());
        editor.putString("address", addressEt.getText().toString());
        editor.putString("dob", dobEt.getText().toString());
        editor.putString("gender", gender);

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean("detailsFilledIn", false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("detailsFilledIn", Boolean.TRUE);
            editor.apply();
        } else {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
//            } else {
//                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    @SuppressLint("MissingPermission")
//    private void getCurrentLocation() {
//        progressBar.setVisibility(View.VISIBLE);
//
//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationServices.getFusedLocationProviderClient(MainActivity.this)
//                .requestLocationUpdates(locationRequest, new LocationCallback() {
//
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        super.onLocationResult(locationResult);
//                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
//                                .removeLocationUpdates(this);
//                        if (locationResult != null && locationResult.getLocations().size() > 0) {
//                            int latestLocationIndex = locationResult.getLocations().size() - 1;
//
//                        }
//
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }, Looper.getMainLooper());
//
//    }

    @Override
    public void onBackPressed() {}
}