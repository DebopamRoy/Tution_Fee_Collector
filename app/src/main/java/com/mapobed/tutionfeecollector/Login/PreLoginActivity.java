package com.mapobed.tutionfeecollector.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mapobed.tutionfeecollector.Home.Student.StudentActivity;
import com.mapobed.tutionfeecollector.Home.Teacher.TeacherActivity;
import com.mapobed.tutionfeecollector.R;

public class PreLoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("Login", false) == true) {
            if (sharedPreferences.getString("Designation", null).equals("student")) {
                startActivity(new Intent(PreLoginActivity.this, StudentActivity.class));
                finish();
            }
            if (sharedPreferences.getString("Designation", null).equals("teacher")) {
                startActivity(new Intent(PreLoginActivity.this, TeacherActivity.class));
                finish();
            }
        }
        findViewById(R.id.student).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreLoginActivity.this, StudentLoginActivity.class));
                finish();
            }
        });
        findViewById(R.id.teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreLoginActivity.this, TeacherLoginActivity.class));
                finish();
            }
        });

    }


}