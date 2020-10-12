package com.mapobed.tutionfeecollector.Home.Teacher.Studentss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapobed.tutionfeecollector.Home.Teacher.Studentss.NewStudent.AddNewStudentActivity;
import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class MatesTeacherActivity extends AppCompatActivity implements MatesTeacherAdapter.StudentTeacherInterface {
    private RecyclerView recyclerView;
    private List<MatesTeacherModelClass> list;
    private FloatingActionButton add;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mates_teacher);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView = findViewById(R.id.mate_teacher_rec);
        add=findViewById(R.id.floatingActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MatesTeacherActivity.this,AddNewStudentActivity.class));
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        list = new ArrayList<>();
        list.add(new MatesTeacherModelClass("  Semester - I ", R.drawable.uoc));
        list.add(new MatesTeacherModelClass(" Semester - II ", R.drawable.uoc));
        list.add(new MatesTeacherModelClass("Semester - III", R.drawable.uoc));
        list.add(new MatesTeacherModelClass("Semester - IV ", R.drawable.uoc));
        list.add(new MatesTeacherModelClass(" Semester - V ", R.drawable.uoc));
        list.add(new MatesTeacherModelClass("Semester - VI ", R.drawable.uoc));
        recyclerView.setAdapter(new MatesTeacherAdapter(list, this));
    }

    @Override
    public void onClick(MatesTeacherModelClass modelClass) {
        startActivity(new Intent(MatesTeacherActivity.this, MatesTeacherFinalActivity.class)
                .putExtra("selected", modelClass.getNamee()));
    }
}