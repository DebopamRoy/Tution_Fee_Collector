package com.mapobed.tutionfeecollector.Home.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.mapobed.tutionfeecollector.Home.Teacher.Payments.MonthBasedActivity;
import com.mapobed.tutionfeecollector.Home.Teacher.Studentss.MatesTeacherActivity;
import com.mapobed.tutionfeecollector.Login.PreLoginActivity;
import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity implements HorizontalRecyclerAdapter.HomeInterface {
    private RecyclerView recyclerView;
    private List<HorizontalRecyclerModelClass> list;
    private SharedPreferences sharedPreferences;
    private TextView teacher_name;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

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
                startActivity(new Intent(TeacherActivity.this, PreLoginActivity.class));
                finish();
            }
        });

        mAdView.loadAd(adRequest);        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        teacher_name = findViewById(R.id.teacher_name);
        teacher_name.setText("Welcome " + sharedPreferences.getString("name", null) + " ,");
        recyclerView = findViewById(R.id.rec_tea_home);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        list = new ArrayList<>();
        list.add(new HorizontalRecyclerModelClass("Students", R.drawable.student));
        list.add(new HorizontalRecyclerModelClass("Payment", R.drawable.payment));
        list.add(new HorizontalRecyclerModelClass("Classes", R.drawable.classroom));
        list.add(new HorizontalRecyclerModelClass("Help", R.drawable.help));
        list.add(new HorizontalRecyclerModelClass("About", R.drawable.developer));
        list.add(new HorizontalRecyclerModelClass("Log out", R.drawable.exit));
        recyclerView.setAdapter(new HorizontalRecyclerAdapter(list, this));
    }

    @Override
    public void onOptionClick(HorizontalRecyclerModelClass modelClass) {
        if (modelClass.getId() == "Students") {
            startActivity(new Intent(TeacherActivity.this, MatesTeacherActivity.class));
        }
        if (modelClass.getId() == "Payment") {
            startActivity(new Intent(TeacherActivity.this, MonthBasedActivity.class));
        }
        if (modelClass.getId() == "Classes") {
            startActivity(new Intent(TeacherActivity.this, ClassActivity.class));
        }
        if (modelClass.getId() == "Help") {
            startActivity(new Intent(TeacherActivity.this, HelpActivity.class));
        }
        if (modelClass.getId() == "About") {
            startActivity(new Intent(TeacherActivity.this, AboutActivity.class));
        }
        if (modelClass.getId() == "Log out") {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            if (mInterstitialAd.isLoaded())
                mInterstitialAd.show();
            else {
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