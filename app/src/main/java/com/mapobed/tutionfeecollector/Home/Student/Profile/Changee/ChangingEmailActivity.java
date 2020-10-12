package com.mapobed.tutionfeecollector.Home.Student.Profile.Changee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapobed.tutionfeecollector.Home.Student.Profile.StudentProfileActivity;
import com.mapobed.tutionfeecollector.R;

import java.util.HashMap;
import java.util.Map;

public class ChangingEmailActivity extends AppCompatActivity {
    private TextView last;
    private TextInputLayout latest;
    private Button save;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_email);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        last = findViewById(R.id.last_email);
        latest = findViewById(R.id.new_email);
        save = findViewById(R.id.save_email);
        databaseReference = FirebaseDatabase.getInstance().getReference("Credential").child("Student");
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        last.setText("Last Email : " + getIntent().getStringExtra("email"));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latest.getEditText().getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(latest.getEditText().getText().toString().trim()).matches()) {
                    latest.setError("Wrong Email Address");
                    latest.requestFocus();
                    return;
                }
                latest.setErrorEnabled(false);
                if (latest.getEditText().getText().toString().trim().equals(getIntent().getStringExtra("email"))) {
                    Snackbar snackbar =
                            Snackbar.make(findViewById(R.id.ccc), "Previous Email Address & new Email Address are same", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingEmailActivity.this, R.color.colorPrimaryDark));
                    snackbar.show();
                    latest.requestFocus();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("password", getIntent().getStringExtra("password"));
                map.put("name", getIntent().getStringExtra("name"));
                map.put("email", latest.getEditText().getText().toString().trim());
                map.put("phone", getIntent().getStringExtra("phone"));
                map.put("college", getIntent().getStringExtra("college"));
                map.put("semester", getIntent().getStringExtra("semester"));
                map.put("username", getIntent().getStringExtra("username"));

                databaseReference.child(sharedPreferences.getString("username", null)).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangingEmailActivity.this, "Password successfully changed.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangingEmailActivity.this, StudentProfileActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar =
                                Snackbar.make(findViewById(R.id.ccc), "Process was interrupted. Please try again", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingEmailActivity.this, R.color.colorPrimaryDark));
                        snackbar.show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StudentProfileActivity.class));
        finish();
    }
}