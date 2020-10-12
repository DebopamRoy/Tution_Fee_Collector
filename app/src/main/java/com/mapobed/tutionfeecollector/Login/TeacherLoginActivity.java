package com.mapobed.tutionfeecollector.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapobed.tutionfeecollector.Home.Teacher.TeacherActivity;
import com.mapobed.tutionfeecollector.R;

public class TeacherLoginActivity extends AppCompatActivity {
    private TextInputLayout teacher_username, teacher_password;
    private DatabaseReference db;
    private SharedPreferences sharedPreferences;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3653476691713377/6933443000");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(TeacherLoginActivity.this, TeacherActivity.class));
                finish();
            }
        });


        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        db = FirebaseDatabase.getInstance().getReference("Credential").child("Teacher");
        teacher_username = (TextInputLayout) findViewById(R.id.teacher_user_name);
        teacher_password = (TextInputLayout) findViewById(R.id.teacher_password);
        findViewById(R.id.forgot_teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(TeacherLoginActivity.this)
                        .setTitle("Forgot Password ?")
                        .setMessage("Please contact developer for further assistance.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setIcon(R.drawable.yor_mapobed).show();
            }
        });
        findViewById(R.id.teacher_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = teacher_username.getEditText().getText().toString().trim();
                final String password = teacher_password.getEditText().getText().toString().trim();
                if (username.isEmpty() || checkusername(username)) {
                    teacher_username.setError("Invalid Username");
                    teacher_username.requestFocus();
                    return;
                }
                teacher_username.setErrorEnabled(false);
                if (password.length() < 6 || password.length() > 22 || password.isEmpty()) {
                    teacher_password.setError("Invalid Password");
                    teacher_password.requestFocus();
                    return;
                }
                teacher_password.setErrorEnabled(false);
                findViewById(R.id.teacher_login).setEnabled(false);
                findViewById(R.id.teacher_user_name).setEnabled(false);
                findViewById(R.id.teacher_password).setEnabled(false);
                findViewById(R.id.forgot_teacher).setVisibility(View.INVISIBLE);
                findViewById(R.id.animationView001).setVisibility(View.VISIBLE);
                db.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (password.equals(snapshot.child("password").getValue(String.class))) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("Login", true);
                                editor.putString("Designation", "teacher");
                                editor.putString("name", snapshot.child("name").getValue(String.class));
                                editor.putString("username", snapshot.child("username").getValue(String.class));
                                editor.putString("semester", "0");
                                editor.apply();

                                if (mInterstitialAd.isLoaded())
                                    mInterstitialAd.show();
                                else {
                                    startActivity(new Intent(TeacherLoginActivity.this, TeacherActivity.class));
                                    finish();
                                }

                            } else {
                                showIt();
                                Snackbar snackbar =
                                        Snackbar.make(findViewById(R.id.constrain), "Username password mismatch", Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(TeacherLoginActivity.this, R.color.colorPrimaryDark));
                                snackbar.show();
                            }
                        } else {
                            showIt();
                            Snackbar snackbar =
                                    Snackbar.make(findViewById(R.id.constrain), "Username seems to be not registered", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(TeacherLoginActivity.this, R.color.colorPrimary));
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showIt();
                        Snackbar snackbar
                                = Snackbar.make(findViewById(R.id.constrain), "Login process was interrupted", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(TeacherLoginActivity.this, R.color.colorPrimary));
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TeacherLoginActivity.this, PreLoginActivity.class));
        finish();
    }


    public Boolean checkusername(String user_name) {
        for (int i = 0; i < user_name.length(); i++) {
            if (user_name.charAt(i) == '.' || user_name.charAt(i) == '#' || user_name.charAt(i) == '$' || user_name.charAt(i) == '[' || user_name.charAt(i) == ']') {
                return true;
            }
        }
        return false;
    }

    public void showIt() {
        findViewById(R.id.teacher_login).setEnabled(true);
        findViewById(R.id.teacher_user_name).setEnabled(true);
        findViewById(R.id.teacher_password).setEnabled(true);
        findViewById(R.id.forgot_teacher).setVisibility(View.VISIBLE);
        findViewById(R.id.animationView001).setVisibility(View.INVISIBLE);
    }
}
