package com.example.covidtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MobileLoginActivity extends AppCompatActivity {

    ImageView exit;
    Button button;
    CountryCodePicker ccp;
    EditText phoneNumberEt, otpEt;
    FirebaseAuth fAuth;
    ProgressBar pb;
    String verificationId, otp;
    PhoneAuthProvider.ForceResendingToken token;
    boolean verificationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_login);

        fAuth = FirebaseAuth.getInstance();
        exit = findViewById(R.id.exitToLogin);
        button = findViewById(R.id.verify_button);
        ccp = findViewById(R.id.ccp);
        phoneNumberEt = findViewById(R.id.phone_number_et);
        otpEt = findViewById(R.id.otp_et);
        pb = findViewById(R.id.progressBar);

        ccp.registerPhoneNumberTextView(phoneNumberEt);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(getApplicationContext(), "Updated to " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MobileLoginActivity.this, LoginActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verificationInProgress) {
                    if (!phoneNumberEt.getText().toString().isEmpty() && phoneNumberEt.getText().toString().length() == 10) {
                        String phoneNumber = ccp.getSelectedCountryCodeWithPlus() + phoneNumberEt.getText().toString();
                        pb.setVisibility(View.VISIBLE);
                        requestOTP(phoneNumber);
                    } else {
                        phoneNumberEt.setError("Valid phone number is required");
                    }
                } else {
                    otp = otpEt.getText().toString();
                    if (otp.length() == 6) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                        verifyAuth(credential);
                    } else {
                        otpEt.setError("Please enter a valid OTP");
                    }
                }
            }
        });
    }

    private void verifyAuth(PhoneAuthCredential c) {
        fAuth.signInWithCredential(c).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Authentication successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed. Please try again. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestOTP(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Verification failed. Please try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                pb.setVisibility(View.GONE);
                otpEt.setVisibility(View.VISIBLE);
                verificationId = s;
                token = forceResendingToken;
                button.setText("Verify");
                verificationInProgress = true;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        });
    }

    @Override
    public void onBackPressed() {}

}