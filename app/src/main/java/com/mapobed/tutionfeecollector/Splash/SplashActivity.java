package com.mapobed.tutionfeecollector.Splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapobed.tutionfeecollector.Login.PreLoginActivity;
import com.mapobed.tutionfeecollector.R;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("Login", false) == true && sharedPreferences.getString("Designation", null).equals("student")) {
            db = FirebaseDatabase.getInstance().getReference("Credential").child("Student");
            db.child(sharedPreferences.getString("username", null)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("semester", snapshot.child("semester").getValue(String.class));
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        startActivity(new Intent(SplashActivity.this, PreLoginActivity.class));
        finish();

    }
}