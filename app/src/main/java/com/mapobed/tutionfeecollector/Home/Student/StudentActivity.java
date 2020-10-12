package com.mapobed.tutionfeecollector.Home.Student;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mapobed.tutionfeecollector.About.AboutActivity;
import com.mapobed.tutionfeecollector.Class.ClassActivity;
import com.mapobed.tutionfeecollector.Help.HelpActivity;
import com.mapobed.tutionfeecollector.Home.HorizontalRecyclerAdapter;
import com.mapobed.tutionfeecollector.Home.HorizontalRecyclerModelClass;
import com.mapobed.tutionfeecollector.Home.Student.Mates.MatesStudentActivity;
import com.mapobed.tutionfeecollector.Home.Student.Payment.StudentMonthlyPaymentActivity;
import com.mapobed.tutionfeecollector.Home.Student.Profile.StudentProfileActivity;
import com.mapobed.tutionfeecollector.Login.PreLoginActivity;
import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity implements HorizontalRecyclerAdapter.HomeInterface {
    private RecyclerView recyclerView;
    private List<HorizontalRecyclerModelClass> list;
    private SharedPreferences sharedPreferences;
    private TextView student_name;
    private AdView mAdView;
    private SharedPreferences.Editor editor;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3653476691713377/1380559967");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(StudentActivity.this, PreLoginActivity.class));
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        student_name = findViewById(R.id.student_name);
        student_name.setText("Welcome " + sharedPreferences.getString("name", null) + " ,");
        recyclerView = (RecyclerView) findViewById(R.id.rec_stu_home);
        list = new ArrayList<>();
        list.add(new HorizontalRecyclerModelClass("Mates", R.drawable.student));
        list.add(new HorizontalRecyclerModelClass("Payment", R.drawable.payment));
        list.add(new HorizontalRecyclerModelClass("Classes", R.drawable.classroom));
        list.add(new HorizontalRecyclerModelClass("Profile", R.drawable.profile_stu));
        list.add(new HorizontalRecyclerModelClass("Help", R.drawable.help));
        list.add(new HorizontalRecyclerModelClass("About", R.drawable.developer));
        list.add(new HorizontalRecyclerModelClass("Log out", R.drawable.exit));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new HorizontalRecyclerAdapter(list, this));
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onOptionClick(HorizontalRecyclerModelClass modelClass) {
        if (modelClass.getId() == "Mates") {
            startActivity(new Intent(StudentActivity.this, MatesStudentActivity.class));
        }
        if (modelClass.getId() == "Payment") {
            startActivity(new Intent(StudentActivity.this, StudentMonthlyPaymentActivity.class));
            finish();
        }
        if (modelClass.getId() == "Classes") {
            startActivity(new Intent(StudentActivity.this, ClassActivity.class));
        }
        if (modelClass.getId() == "Profile") {
            startActivity(new Intent(StudentActivity.this, StudentProfileActivity.class));
        }
        if (modelClass.getId() == "Help") {
            startActivity(new Intent(StudentActivity.this, HelpActivity.class));
        }
        if (modelClass.getId() == "About") {
            startActivity(new Intent(StudentActivity.this, AboutActivity.class));
        }
        if (modelClass.getId() == "Log out") {
            editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                startActivity(new Intent(this, PreLoginActivity.class));
                finish();
            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}