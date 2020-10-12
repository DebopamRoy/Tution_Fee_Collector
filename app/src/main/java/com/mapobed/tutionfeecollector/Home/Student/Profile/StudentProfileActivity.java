package com.mapobed.tutionfeecollector.Home.Student.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.mapobed.tutionfeecollector.Home.Student.Profile.Changee.ChangingEmailActivity;
import com.mapobed.tutionfeecollector.Home.Student.Profile.Changee.ChangingPasswordActivity;
import com.mapobed.tutionfeecollector.Home.Student.Profile.Changee.ChangingPhoneActivity;
import com.mapobed.tutionfeecollector.R;

public class StudentProfileActivity extends AppCompatActivity {
    private TextView name, username, password, phone, college, email, paid_pro;
    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference, reference;
    private String pass;
    private String eml, clg, nam, usrnm;
    private String phn, sem;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        databaseReference = FirebaseDatabase.getInstance().getReference("Credential").child("Student");
        reference = FirebaseDatabase.getInstance().getReference("Payment");
        name = findViewById(R.id.name_details);
        username = findViewById(R.id.user_pro);
        password = findViewById(R.id.password_pro);
        paid_pro = findViewById(R.id.months_paid_pro);
        phone = findViewById(R.id.phone_pro);
        college = findViewById(R.id.college_pro);
        email = findViewById(R.id.email_pro);
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        databaseReference.child(sharedPreferences.getString("username", null)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nam = snapshot.child("name").getValue(String.class);
                pass = snapshot.child("password").getValue(String.class);
                eml = snapshot.child("email").getValue(String.class);
                phn = snapshot.child("phone").getValue(String.class);
                clg = snapshot.child("college").getValue(String.class);
                sem = sharedPreferences.getString("semester", null);
                usrnm = snapshot.child("username").getValue(String.class);
                name.setText("Name : " + nam);
                username.setText("Username : " + usrnm);
                password.setText("Password : " + " ******* ");
                phone.setText("Phone : +91 " + snapshot.child("phone").getValue(String.class));
                college.setText("College : " + clg);
                email.setText("Email Address : " + eml);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar =
                        Snackbar.make(findViewById(R.id.c1), "Please try again later.", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(StudentProfileActivity.this, R.color.colorPrimary));
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
            reference.child(monthee).child(sharedPreferences.getString("username", null)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                        paid_pro.setText(paid_pro.getText() + "   " + finalMonthee1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Snackbar snackbar =
                            Snackbar.make(findViewById(R.id.c1), "Please try again later.", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(StudentProfileActivity.this, R.color.colorPrimary));
                    snackbar.show();
                }
            });
        }
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentProfileActivity.this, ChangingPasswordActivity.class)
                        .putExtra("password", pass)
                        .putExtra("name", nam)
                        .putExtra("email", eml)
                        .putExtra("phone", phn)
                        .putExtra("college", clg)
                        .putExtra("semester", sem)
                        .putExtra("username", usrnm));
                finish();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentProfileActivity.this, ChangingPhoneActivity.class)
                        .putExtra("password", pass)
                        .putExtra("name", nam)
                        .putExtra("email", eml)
                        .putExtra("phone", phn)
                        .putExtra("college", clg)
                        .putExtra("semester", sem)
                        .putExtra("username", usrnm));
                finish();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentProfileActivity.this, ChangingEmailActivity.class)
                        .putExtra("password", pass)
                        .putExtra("name", nam)
                        .putExtra("email", eml)
                        .putExtra("phone", phn)
                        .putExtra("college", clg)
                        .putExtra("semester", sem)
                        .putExtra("username", usrnm));
                finish();
            }
        });
    }
}