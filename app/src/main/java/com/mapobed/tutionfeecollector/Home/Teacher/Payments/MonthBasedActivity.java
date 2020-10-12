package com.mapobed.tutionfeecollector.Home.Teacher.Payments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mapobed.tutionfeecollector.Home.Teacher.Payments.StudentByMonth.StudentByMonthActivity;
import com.mapobed.tutionfeecollector.R;

import java.util.ArrayList;
import java.util.List;

public class MonthBasedActivity extends AppCompatActivity implements MonthBasedAdapter.MonthlyBasedInterface {
    private RecyclerView recyclerView;
    List<MonthBasedModelClass>list;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_based);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView=findViewById(R.id.monthly_based_rec);
        list=new ArrayList<>();
        list.add(new MonthBasedModelClass("January",R.drawable.january));
        list.add(new MonthBasedModelClass("February",R.drawable.february));
        list.add(new MonthBasedModelClass("March",R.drawable.march));
        list.add(new MonthBasedModelClass("April",R.drawable.april));
        list.add(new MonthBasedModelClass("May",R.drawable.may));
        list.add(new MonthBasedModelClass("June",R.drawable.june));
        list.add(new MonthBasedModelClass("July",R.drawable.july));
        list.add(new MonthBasedModelClass("August",R.drawable.august));
        list.add(new MonthBasedModelClass("September",R.drawable.september));
        list.add(new MonthBasedModelClass("October",R.drawable.october));
        list.add(new MonthBasedModelClass("November",R.drawable.november));
        list.add(new MonthBasedModelClass("December",R.drawable.december));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MonthBasedAdapter(list,this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void monthClicked(MonthBasedModelClass pos_of_month) {
        startActivity(new Intent(MonthBasedActivity.this, StudentByMonthActivity.class).putExtra("month_name",pos_of_month.getName()));
    }
}