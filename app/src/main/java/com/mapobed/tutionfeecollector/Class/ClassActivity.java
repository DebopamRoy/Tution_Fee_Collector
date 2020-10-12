package com.mapobed.tutionfeecollector.Class;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mapobed.tutionfeecollector.R;

public class ClassActivity extends AppCompatActivity {
private SharedPreferences sharedPreferences;
private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        int sem= Integer.parseInt(sharedPreferences.getString("semester",null));
        if (sem==0){
            findViewById(R.id.constraintLayout_one).setVisibility(View.VISIBLE);
            findViewById(R.id.constraintLayout_two).setVisibility(View.VISIBLE);
            findViewById(R.id.constraintLayout_three).setVisibility(View.VISIBLE);
            findViewById(R.id.constraintLayout_four).setVisibility(View.VISIBLE);
            findViewById(R.id.constraintLayout_five).setVisibility(View.VISIBLE);
            findViewById(R.id.constraintLayout_six).setVisibility(View.VISIBLE);

        }
        if (sem==1){
            findViewById(R.id.constraintLayout_one).setVisibility(View.VISIBLE);
        }
        if (sem==2){
            findViewById(R.id.constraintLayout_two).setVisibility(View.VISIBLE);
        }
        if (sem==3){
            findViewById(R.id.constraintLayout_three).setVisibility(View.VISIBLE);
        }
        if (sem==4){
            findViewById(R.id.constraintLayout_four).setVisibility(View.VISIBLE);
        }
        if (sem==5){
            findViewById(R.id.constraintLayout_five).setVisibility(View.VISIBLE);
        }
        if (sem==6){
            findViewById(R.id.constraintLayout_six).setVisibility(View.VISIBLE);
        }
    }
}