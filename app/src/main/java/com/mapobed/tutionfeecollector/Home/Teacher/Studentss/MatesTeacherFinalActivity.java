package com.mapobed.tutionfeecollector.Home.Teacher.Studentss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapobed.tutionfeecollector.Home.Student.Mates.MateStudentAdapter;
import com.mapobed.tutionfeecollector.Home.Student.Mates.StudentMatesModelClass;
import com.mapobed.tutionfeecollector.Home.Teacher.Studentss.DetailsActivity.TeacherStudentDetailsActivity;
import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class MatesTeacherFinalActivity extends AppCompatActivity implements MateStudentAdapter.clicker {
    private DatabaseReference databaseReference;
    private List<StudentMatesModelClass>list;
    private String sem = "";
    private MateStudentAdapter adapter;
    private SearchView searchView;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mates_teacher_final);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        searchView=findViewById(R.id.searchView);
        findViewById(R.id.animationView_scnd_last).setVisibility(View.VISIBLE);
        final RecyclerView recyclerView;
        databaseReference = FirebaseDatabase.getInstance().getReference("Credential").child("Student");
        String selected = getIntent().getStringExtra("selected").trim();
        if (selected.equals("Semester - I"))
            sem = "1";
        if (selected.equals("Semester - II"))
            sem = "2";
        if (selected.equals("Semester - III"))
            sem = "3";
        if (selected.equals("Semester - IV"))
            sem = "4";
        if (selected.equals("Semester - V"))
            sem = "5";
        if (selected.equals("Semester - VI"))
            sem = "6";
        recyclerView = findViewById(R.id.mates_teacher_final);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list.size()!=0)
                    list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("semester").getValue().toString().equals(sem)){
                        StudentMatesModelClass vmc = new StudentMatesModelClass();
                        vmc.setName(snapshot1.child("name").getValue().toString());
                        vmc.setCollege(snapshot1.child("college").getValue().toString());
                        vmc.setEmail(snapshot1.child("email").getValue().toString());
                        vmc.setPhone("+91 "+snapshot1.child("phone").getValue().toString());

                        list.add(vmc);
                    }
                }
                adapter=new MateStudentAdapter(list,MatesTeacherFinalActivity.this);
                recyclerView.setAdapter(adapter);
                findViewById(R.id.animationView_scnd_last).setVisibility(View.GONE);
                if (list.size()==0){
                    findViewById(R.id.animationView_nts01).setVisibility(View.VISIBLE);
                    findViewById(R.id.nts01).setVisibility(View.VISIBLE);
                }
                else {
                    findViewById(R.id.animationView_nts01).setVisibility(View.GONE);
                    findViewById(R.id.nts01).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar =
                        Snackbar.make(findViewById(R.id.mate_con), "The process was terminated. Please try again", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(MatesTeacherFinalActivity.this, R.color.colorPrimaryDark));
                snackbar.show();
            }
        });

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void clickked(StudentMatesModelClass matesTeacherModelClass) {
        startActivity(new Intent(this, TeacherStudentDetailsActivity.class).putExtra("name",matesTeacherModelClass.getName()));
    }
}