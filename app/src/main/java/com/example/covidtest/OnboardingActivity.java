package com.example.covidtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout onboardingIndicators;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        onboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        button = findViewById(R.id.onboardingButton);

        setUpOnboardingItems();

        final ViewPager2 onboardingViewpager = findViewById(R.id.onboardingViewPager);
        onboardingViewpager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);
        onboardingViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onboardingViewpager.getCurrentItem()+1 < onboardingAdapter.getItemCount()) {
                    onboardingViewpager.setCurrentItem(onboardingViewpager.getCurrentItem()+1);
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });
    }

    private void setUpOnboardingItems () {
        List<OnboardingItem> onboardingItemList = new ArrayList<>();

        OnboardingItem covidTest = new OnboardingItem();
        covidTest.setImage(R.drawable.onboard_4);
        covidTest.setTitle("Test for COVID-19");
        covidTest.setDescription("");

        OnboardingItem ecgMonitoring = new OnboardingItem();
        ecgMonitoring.setImage(R.drawable.onboard_1);
        ecgMonitoring.setTitle("Record your own ECG");
        ecgMonitoring.setDescription("");

        OnboardingItem diabetesTest = new OnboardingItem();
        diabetesTest.setImage(R.drawable.onboard_2);
        diabetesTest.setTitle("Test for Diabetes");
        diabetesTest.setDescription("");

        OnboardingItem shareResult = new OnboardingItem();
        shareResult.setImage(R.drawable.onboard_3);
        shareResult.setTitle("Share results with a doctor");
        shareResult.setDescription("");

        onboardingItemList.add(covidTest);
        onboardingItemList.add(ecgMonitoring);
        onboardingItemList.add(diabetesTest);
        onboardingItemList.add(shareResult);

        onboardingAdapter = new OnboardingAdapter(onboardingItemList);
    }

    private void setupOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);

        for (int i=0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            onboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index) {
        int childCount = onboardingAdapter.getItemCount();
        for (int i=0; i<childCount; i++) {
            ImageView imageView = (ImageView) onboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }

        if (index == onboardingAdapter.getItemCount()-1) {
            button.setText("Start");
        } else {
            button.setText("Next");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean("notFirstTime", false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("notFirstTime", Boolean.TRUE);
            editor.apply();
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
}