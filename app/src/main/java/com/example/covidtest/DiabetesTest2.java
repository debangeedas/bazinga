package com.example.covidtest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DiabetesTest2 extends AppCompatActivity {

    ImageView captureView;
    Button capture, recapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes_test2);

        captureView = findViewById(R.id.cameraView);
        capture = findViewById(R.id.capture_button);
        recapture = findViewById(R.id.recapture_button);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capture.getText() == "Save and Proceed") {
                    startActivity(new Intent(getApplicationContext(), CovidTest3.class));
                } else {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
                }
            }
        });

        recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap b = (Bitmap)data.getExtras().get("data");
        captureView.setImageBitmap(b);

        capture.setText("Save and Proceed");
        recapture.setEnabled(true);

        captureView.setPadding(0,0,0,0);
        captureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void onBackPressed() {}
}