package com.mapobed.tutionfeecollector.Home.Teacher.Payments.StudentByMonth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class ShowPaidStudentActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<PaidStudentModelClass> list;
    private RecyclerView recyclerView;
    private String sem = null;
    private String monthee;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_paid_student);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        findViewById(R.id.animationView_last).setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Payment");
        recyclerView = findViewById(R.id.paid_stu_rec);
        monthee = getIntent().getStringExtra("month") + " 2020";
        Toast.makeText(this, "" + monthee, Toast.LENGTH_SHORT).show();
        String semester = getIntent().getStringExtra("selected").trim();
        if (semester.equals("Semester - I"))
            sem = "1";
        if (semester.equals("Semester - II"))
            sem = "2";
        if (semester.equals("Semester - III"))
            sem = "3";
        if (semester.equals("Semester - IV"))
            sem = "4";
        if (semester.equals("Semester - V"))
            sem = "5";
        if (semester.equals("Semester - VI"))
            sem = "6";
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        databaseReference.child(monthee).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list.size() != 0)
                    list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("semester").getValue().toString().equals(sem)) {
                        PaidStudentModelClass psmc = new PaidStudentModelClass();
                        psmc.setPaid_name(snapshot1.child("name").getValue().toString());
                        psmc.setPaid_user_name(snapshot1.child("username").getValue().toString());
                        psmc.setPaid_mode(snapshot1.child("mode").getValue().toString());
                        psmc.setPaid_date(snapshot1.child("payment_date").getValue().toString());

                        list.add(psmc);
                    }
                }
                recyclerView.setAdapter(new PaidStudentAdapter(list));
                findViewById(R.id.animationView_last).setVisibility(View.GONE);
                if (list.size() == 0) {
                    findViewById(R.id.animationView_nts).setVisibility(View.VISIBLE);
                    findViewById(R.id.nts).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.animationView_nts).setVisibility(View.GONE);
                    findViewById(R.id.nts).setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar =
                        Snackbar.make(findViewById(R.id.paid_con), "The process was terminated. Please try again", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(ShowPaidStudentActivity.this, R.color.colorPrimaryDark));
                snackbar.show();
            }
        });
    }
}