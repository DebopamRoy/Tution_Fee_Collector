package com.mapobed.tutionfeecollector.Home.Student.Profile.Changee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ChangingPasswordActivity extends AppCompatActivity {
    private TextInputLayout old_p, new_p, confirm_p;
    private Button save;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changing);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        databaseReference = FirebaseDatabase.getInstance().getReference("Credential").child("Student");
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        old_p = (TextInputLayout) findViewById(R.id.old_password);
        new_p = (TextInputLayout) findViewById(R.id.new_password);
        confirm_p = (TextInputLayout) findViewById(R.id.confirm_password);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!old_p.getEditText().getText().toString().trim().equals(getIntent().getStringExtra("password"))) {
                    Snackbar snackbar =
                            Snackbar.make(findViewById(R.id.psa_c), "Old password is wrong", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingPasswordActivity.this, R.color.colorPrimaryDark));
                    snackbar.show();
                    old_p.requestFocus();
                    return;
                } else {
                    if (new_p.getEditText().getText().toString().isEmpty() || new_p.getEditText().getText().toString().trim().length() < 6 || new_p.getEditText().getText().toString().trim().length() > 22) {
                        new_p.setError("Password must be between 6 & 22 characters");
                        new_p.requestFocus();
                        return;
                    }
                    new_p.setErrorEnabled(false);
                    if (old_p.getEditText().getText().toString().isEmpty() || old_p.getEditText().getText().toString().trim().length() < 6 || old_p.getEditText().getText().toString().trim().length() > 22) {
                        old_p.setError("Password must be between 6 & 22 characters");
                        old_p.requestFocus();
                        return;
                    }
                    old_p.setErrorEnabled(false);
                    if (!new_p.getEditText().getText().toString().equals(confirm_p.getEditText().getText().toString().trim())) {
                        Snackbar snackbar =
                                Snackbar.make(findViewById(R.id.psa_c), "Password doesnot match", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingPasswordActivity.this, R.color.colorPrimaryDark));
                        snackbar.show();
                        new_p.setError("Password mismatch");
                        confirm_p.setError("Password mismatch");
                        confirm_p.requestFocus();
                        return;
                    }
                    new_p.setErrorEnabled(false);
                    confirm_p.setErrorEnabled(false);
                    if (confirm_p.getEditText().getText().toString().trim().equals(old_p.getEditText().getText().toString().trim())) {
                        Snackbar snackbar =
                                Snackbar.make(findViewById(R.id.psa_c), "old password & new password are same", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingPasswordActivity.this, R.color.colorPrimaryDark));
                        snackbar.show();
                        confirm_p.requestFocus();
                        return;
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("password", confirm_p.getEditText().getText().toString().trim());
                    map.put("name", getIntent().getStringExtra("name"));
                    map.put("email", getIntent().getStringExtra("email"));
                    map.put("phone", getIntent().getStringExtra("phone"));
                    map.put("college", getIntent().getStringExtra("college"));
                    map.put("semester", getIntent().getStringExtra("semester"));
                    map.put("username", getIntent().getStringExtra("username"));


                    databaseReference.child(sharedPreferences.getString("username", null)).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChangingPasswordActivity.this, "Password successfully changed.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangingPasswordActivity.this, StudentProfileActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar snackbar =
                                    Snackbar.make(findViewById(R.id.psa_c), "Process was interrupted. Please try again", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingPasswordActivity.this, R.color.colorPrimaryDark));
                            snackbar.show();
                        }
                    });
                }
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