package com.mapobed.tutionfeecollector.Home.Student.Profile.Changee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class ChangingPhoneActivity extends AppCompatActivity {
    private TextView old_phn;
    private TextInputLayout new_phn;
    private Button save;
    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_phone);

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
        old_phn = findViewById(R.id.chng_phn);
        new_phn = (TextInputLayout) findViewById(R.id.new_phn);
        save = findViewById(R.id.save_phn);
        old_phn.setText("Last Phone: +91" + getIntent().getStringExtra("phone"));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new_phn.getEditText().getText().toString().isEmpty() || new_phn.getEditText().getText().toString().length() != 10) {
                    new_phn.setError("Mobile number is not valid");
                    new_phn.requestFocus();
                    return;
                }
                new_phn.setErrorEnabled(false);
                if (getIntent().getStringExtra("phone").equals(new_phn.getEditText().getText().toString().trim())) {
                    Snackbar snackbar =
                            Snackbar.make(findViewById(R.id.acp_p), "Provided phone number is same as old number", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingPhoneActivity.this, R.color.colorPrimaryDark));
                    snackbar.show();
                    new_phn.requestFocus();
                    return;
                }
                String ph = new_phn.getEditText().getText().toString().trim();
                Map<String, String> map = new HashMap<>();
                map.put("password", getIntent().getStringExtra("password"));
                map.put("name", getIntent().getStringExtra("name"));
                map.put("email", getIntent().getStringExtra("email"));
                map.put("phone", ph);
                map.put("college", getIntent().getStringExtra("college"));
                map.put("semester", getIntent().getStringExtra("semester"));
                map.put("username", getIntent().getStringExtra("username"));

                databaseReference.child(sharedPreferences.getString("username", null)).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangingPhoneActivity.this, "Password successfully changed.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangingPhoneActivity.this, StudentProfileActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar =
                                Snackbar.make(findViewById(R.id.psa_c), "Process was interrupted. Please try again", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(ChangingPhoneActivity.this, R.color.colorPrimaryDark));
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