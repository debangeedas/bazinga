package com.example.covidtest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiabetesTest2 extends AppCompatActivity {

    ImageView captureView;
    Button capture, recapture;
    Bitmap b;
    private static final int REQUEST_CODE_PERMISSION = 1;
    Intent intent;
    String str;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes_test2);

        captureView = findViewById(R.id.cameraView);
        capture = findViewById(R.id.capture_button);
        recapture = findViewById(R.id.recapture_button);

        ActivityCompat.requestPermissions(DiabetesTest2.this, new String[]{Manifest.permission.INTERNET}, 2);
        ActivityCompat.requestPermissions(DiabetesTest2.this, new String[]{Manifest.permission.CAMERA}, 1);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DiabetesTest2.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                } else {
                    if (capture.getText() == "Save and Proceed") {
                        connectServer();
                        intent = new Intent(getApplicationContext(), DiabetesTest3.class);
                        startActivity(intent);
                    } else {
                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
                        //TODO: handle flashlight
                    }
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

        b = (Bitmap)data.getExtras().get("data");
        captureView.setImageBitmap(b);

        capture.setText("Save and Proceed");
        recapture.setEnabled(true);

        captureView.setPadding(0,0,0,0);
        captureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void connectServer() {
        String postUrl = "https://diab-det.herokuapp.com/";

        MultipartBody.Builder multipartBodyBuilder;
        multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Make Sure the Selected File is an Image.", Toast.LENGTH_SHORT).show();
            return;
        }
        byte[] byteArray = stream.toByteArray();

        multipartBodyBuilder.addFormDataPart("image", "Android_Flask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));
        RequestBody postBodyImage = multipartBodyBuilder.build();
        postRequest(postUrl, postBodyImage);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed to Connect to Server. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            str = response.body().string();
                            Log.e("Server's Response: ", str);

                            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("diabetesResponse", str);
                            editor.apply();

                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {}
}