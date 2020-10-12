package com.mapobed.tutionfeecollector.Home.Student.Payment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.mapobed.tutionfeecollector.Home.Student.Payment.PaymentPortal.StudentPaymentPortalActivity;
import com.mapobed.tutionfeecollector.Home.Student.StudentActivity;
import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class StudentMonthlyPaymentActivity extends AppCompatActivity {
    private CheckBox jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec;
    private TextView total, mtbp;
    private Button payy;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseReference reference;
    private int ms = 0;
    private int[] mon = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_monthly_payment);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final List<String> name_mon = new ArrayList<>();
        name_mon.add(0, "January");
        name_mon.add(1, "February");
        name_mon.add(2, "March");
        name_mon.add(3, "April");
        name_mon.add(4, "May");
        name_mon.add(5, "June");
        name_mon.add(6, "July");
        name_mon.add(7, "August");
        name_mon.add(8, "September");
        name_mon.add(9, "October");
        name_mon.add(10, "November");
        name_mon.add(11, "December");
        jan = findViewById(R.id.jan);
        feb = findViewById(R.id.feb);
        mtbp = findViewById(R.id.month_to_be_paid);
        mar = findViewById(R.id.mar);
        apr = findViewById(R.id.apr);
        may = findViewById(R.id.may);
        jun = findViewById(R.id.jun);
        jul = findViewById(R.id.jul);
        aug = findViewById(R.id.aug);
        sep = findViewById(R.id.sep);
        oct = findViewById(R.id.oct);
        nov = findViewById(R.id.nov);
        dec = findViewById(R.id.dec);
        total = findViewById(R.id.total);
        payy = findViewById(R.id.payy);
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        int sem = Integer.parseInt(sharedPreferences.getString("semester", null));
        if (sem == 1)
            ms = 00;
        if (sem == 2)
            ms = 000;
        if (sem == 3)
            ms = 700;
        if (sem == 4)
            ms = 00;
        if (sem == 5)
            ms = 900;
        if (sem == 6)
            ms = 000;
        payy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jan.isChecked())
                    mon[0] = 1;
                else
                    mon[0] = 0;
                if (feb.isChecked())
                    mon[1] = 1;
                else
                    mon[1] = 0;
                if (mar.isChecked())
                    mon[2] = 1;
                else
                    mon[2] = 0;
                if (apr.isChecked())
                    mon[3] = 1;
                else
                    mon[3] = 0;
                if (may.isChecked())
                    mon[4] = 1;
                else
                    mon[4] = 0;
                if (jun.isChecked())
                    mon[5] = 1;
                else
                    mon[5] = 0;
                if (jul.isChecked())
                    mon[6] = 1;
                else
                    mon[6] = 0;
                if (aug.isChecked())
                    mon[7] = 1;
                else
                    mon[7] = 0;
                if (sep.isChecked())
                    mon[8] = 1;
                else
                    mon[8] = 0;
                if (oct.isChecked())
                    mon[9] = 1;
                else
                    mon[9] = 0;
                if (nov.isChecked())
                    mon[10] = 1;
                else
                    mon[10] = 0;
                if (dec.isChecked())
                    mon[11] = 1;
                else
                    mon[11] = 0;
                String month_to_be_paid = "";
                int count = 0;
                for (int i = 0; i < 12; i++) {
                    if (mon[i] == 1) {
                        if (month_to_be_paid.equals(null)) {
                            month_to_be_paid = name_mon.get(i);
                        } else {
                            month_to_be_paid = month_to_be_paid + name_mon.get(i) + " ";

                        }
                        count += ms;
                    }
                }
                total.setText("Total: " + count + " Rs.");
                mtbp.setText("Months: " + month_to_be_paid.trim());
                if (count != 0 && !month_to_be_paid.equals(null)) {
                    editor = sharedPreferences.edit();
                    editor.putInt("monthly_sal", ms);
                    editor.putInt("salary", count);
                    editor.putString("month", month_to_be_paid.trim());
                    editor.commit();
                    startActivity(new Intent(StudentMonthlyPaymentActivity.this, StudentPaymentPortalActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.co), "Select the month you want to pay", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(StudentMonthlyPaymentActivity.this, R.color.colorPrimaryDark));
                    snackbar.show();
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StudentMonthlyPaymentActivity.this, StudentActivity.class));
        finish();
    }
}