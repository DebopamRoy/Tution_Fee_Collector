package com.mapobed.tutionfeecollector.Home.Teacher.Studentss.DetailsActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class TeacherStudentDetailsActivity extends AppCompatActivity {
    private String user_name_details;
    private DatabaseReference databaseReference, reference;
    private TextView name, username, password, phone, college, email, paid;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_student_details);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        name = findViewById(R.id.name_details);
        username = findViewById(R.id.user_details);
        password = findViewById(R.id.password_details);
        phone = findViewById(R.id.phone_details);
        college = findViewById(R.id.college_details);
        email = findViewById(R.id.email_details);
        paid = findViewById(R.id.months_paid_details);
        user_name_details = getIntent().getStringExtra("name").toLowerCase().trim().replace(" ", "_");
        databaseReference = FirebaseDatabase.getInstance().getReference("Credential").child("Student");
        reference = FirebaseDatabase.getInstance().getReference("Payment");
        databaseReference.child(user_name_details).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText("Name: " + snapshot.child("name").getValue(String.class));
                username.setText("Username: " + snapshot.child("username").getValue(String.class));
                password.setText("Password: " + snapshot.child("password").getValue(String.class));
                phone.setText("Phone Number: +91 " + snapshot.child("phone").getValue(String.class));
                college.setText("College: " + snapshot.child("college").getValue(String.class));
                email.setText("Email Address: " + snapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar =
                        Snackbar.make(findViewById(R.id.c1), "Please try again later.", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(TeacherStudentDetailsActivity.this, R.color.colorPrimary));
                snackbar.show();
            }
        });
        for (int i = 1; i < 13; i++) {
            String monthee = "";
            if (i == 1)
                monthee = "January 2020";
            if (i == 2)
                monthee = "February 2020";
            if (i == 3)
                monthee = "March 2020";
            if (i == 4)
                monthee = "April 2020";
            if (i == 5)
                monthee = "May 2020";
            if (i == 6)
                monthee = "June 2020";
            if (i == 7)
                monthee = "July 2020";
            if (i == 8)
                monthee = "August 2020";
            if (i == 9)
                monthee = "September 2020";
            if (i == 10)
                monthee = "October 2020";
            if (i == 11)
                monthee = "November 2020";
            if (i == 12)
                monthee = "December 2020";
            final String finalMonthee1 = monthee;
            reference.child(monthee).child(user_name_details).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                        paid.setText(paid.getText() + "   " + finalMonthee1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Snackbar snackbar =
                            Snackbar.make(findViewById(R.id.c1), "Please try again later.", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(TeacherStudentDetailsActivity.this, R.color.colorPrimary));
                    snackbar.show();
                }
            });
        }
    }
}