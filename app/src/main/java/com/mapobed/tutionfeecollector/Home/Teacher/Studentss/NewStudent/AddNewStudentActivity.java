package com.mapobed.tutionfeecollector.Home.Teacher.Studentss.NewStudent;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapobed.tutionfeecollector.R;

import java.util.HashMap;
import java.util.Map;

public class AddNewStudentActivity extends AppCompatActivity {
    private ImageButton imageButton_info;
    private TextInputLayout name, semester, email, phone, college;
    private Button submit_info_button;
    private TextView info;
    private String usrnm;
    private Map<String, String> map;
    private AdView mAdView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        databaseReference = FirebaseDatabase.getInstance().getReference("Credential");
        imageButton_info = findViewById(R.id.imageButton_info);
        info = findViewById(R.id.textView_info);
        name = findViewById(R.id.name_info);
        semester = findViewById(R.id.sem_info);
        email = findViewById(R.id.email_info);
        phone = findViewById(R.id.phone_info);
        college = findViewById(R.id.college_info);
        submit_info_button = findViewById(R.id.submit_button_info);
        imageButton_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.getVisibility() == View.VISIBLE)
                    info.setVisibility(View.INVISIBLE);
                else
                    info.setVisibility(View.VISIBLE);
            }
        });
        submit_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nm = name.getEditText().getText().toString().trim();
                final String sem = semester.getEditText().getText().toString().trim();
                final String eml = email.getEditText().getText().toString().trim();
                final String clg = college.getEditText().getText().toString().trim();
                final String phn = phone.getEditText().getText().toString().trim();
                if (nm.isEmpty() || nm.length() == 0) {
                    name.setError("Name is required");
                    name.requestFocus();
                    return;
                }
                name.setErrorEnabled(false);

                if (sem.isEmpty() || sem.length() < 1) {
                    semester.setError("Semester is required");
                    semester.requestFocus();
                    return;
                }
                semester.setErrorEnabled(false);

                if (eml.isEmpty() || eml.length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(eml).matches()) {
                    email.setError("Email is wrong");
                    email.requestFocus();
                    return;
                }
                email.setErrorEnabled(false);

                if (clg.isEmpty() || clg.length() == 0) {
                    college.setError("College is required");
                    college.requestFocus();
                    return;
                }
                college.setErrorEnabled(false);

                if (phn.isEmpty() || phn.length() < 10) {
                    phone.setError("Phone Number is wrong");
                    phone.requestFocus();
                    return;
                }
                phone.setErrorEnabled(false);

                usrnm = nm.toLowerCase().replace(' ', '_');

                map = new HashMap<>();
                map.put("college", clg);
                map.put("email", eml);
                map.put("name", nm);
                map.put("password", usrnm);
                map.put("phone", phn);
                map.put("semester", sem);
                databaseReference.child("Student").child(usrnm).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.child("name").getValue(String.class).toLowerCase().equals(nm.toLowerCase()) &&
                                    snapshot.child("semester").getValue(String.class).toLowerCase().equals(sem.toLowerCase()) &&
                                    snapshot.child("college").getValue(String.class).toLowerCase().equals(clg.toLowerCase()) &&
                                    snapshot.child("email").getValue(String.class).toLowerCase().equals(eml.toLowerCase()) &&
                                    snapshot.child("phone").getValue(String.class).toLowerCase().equals(phn.toLowerCase())) {
                                Snackbar snackbar =
                                        Snackbar.make(findViewById(R.id.con), "Details already recorded. No need to do it again.", Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(AddNewStudentActivity.this, R.color.colorPrimaryDark));
                                snackbar.show();
                            }
                            else {
                                map.put("username", usrnm+phn.charAt(0));
                                addNewStudent(usrnm+phn.charAt(0));
                            }
                        } else {
                            map.put("username", usrnm);
                            addNewStudent(usrnm);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar snackbar =
                                Snackbar.make(findViewById(R.id.con), "Please try again", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(AddNewStudentActivity.this, R.color.colorPrimaryDark));
                        snackbar.show();
                    }
                });


            }
        });
    }

    private void addNewStudent(String usrn) {
        databaseReference.child("Student").child(usrn).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddNewStudentActivity.this, "Student details were added.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Snackbar snackbar =
                            Snackbar.make(findViewById(R.id.con), "Please try again", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(AddNewStudentActivity.this, R.color.colorPrimaryDark));
                    snackbar.show();
                }
            }
        });
    }
}