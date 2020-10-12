package com.mapobed.tutionfeecollector.Home.Student.Payment.PaymentPortal;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapobed.tutionfeecollector.Home.Student.Payment.StudentMonthlyPaymentActivity;
import com.mapobed.tutionfeecollector.Home.Student.StudentActivity;
import com.mapobed.tutionfeecollector.Login.PreLoginActivity;
import com.mapobed.tutionfeecollector.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudentPaymentPortalActivity extends AppCompatActivity implements StudentPaymentBottomSheetDialogue.BottomSheetButtonListener {
    private static final int PDF_MAKER = 222;
    private TextView pay_req, upi_pay;
    private TextView amo, mon;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String name, username, month;
    private int semester, amount;
    private ConstraintLayout co_pay;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    public static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    public static final String PHONE_PAY_PACKAGE_NAME = "com.phonepe.app";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    int PAYTM_PAY_REQUEST_CODE = 231;
    int PHONEPAY_PAY_REQUEST_CODE = 312;
    String upiId = "sanjoyghoshcs@okicici";
    String transactionNote, dest_name = "Sanjoy Ghosh";
    Uri uri;
    int ms = 0;
    private InterstitialAd mInterstitialAd;
    String each_month[];
    Bitmap bmp, scalable_bmp;
    static String mode;
    private AdView mAdView;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_payment_portal);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(StudentPaymentPortalActivity.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3653476691713377/3620367030");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(StudentPaymentPortalActivity.this, PreLoginActivity.class));
                finish();
            }
        });

        rewardedAd=new RewardedAd(this,"ca-app-pub-3653476691713377/4417786840");



        co_pay = findViewById(R.id.co_pay);
        amo = findViewById(R.id.amount);
        mon = findViewById(R.id.monthes);
        sharedPreferences = getSharedPreferences("com.mapobed.tutionfeecollector.user", MODE_PRIVATE);
        name = sharedPreferences.getString("name", null);
        username = sharedPreferences.getString("username", null);
        semester = Integer.parseInt(sharedPreferences.getString("semester", null));
        amount = sharedPreferences.getInt("salary", 0);
        month = sharedPreferences.getString("month", null);
        ms = sharedPreferences.getInt("monthly_sal", 0);
        amo.setText("Total Amount: " + amount + " Rs.");
        mon.setText("Months: " + month.trim().replace(" ", ","));
        databaseReference = FirebaseDatabase.getInstance().getReference("Payment");
        pay_req = findViewById(R.id.pay_req);
        upi_pay = findViewById(R.id.upi_pay);
        pay_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        StudentPaymentPortalActivity.this);
                alertDialog.setTitle("Hello!");
                alertDialog.setMessage("It seems that you have already paid your teacher. Select your mode of payment.");
                alertDialog.setIcon(R.drawable.yor_mapobed);
                alertDialog.setPositiveButton("Cash",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mode = "Cash Payment";
                                uploadRequest();
                            }
                        });
                alertDialog.setNegativeButton("Net Banking",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mode = "Net Banking";
                                uploadRequest();
                            }
                        });

                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
        upi_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentPaymentBottomSheetDialogue studentPaymentBottomSheetDialogue = new StudentPaymentBottomSheetDialogue();
                studentPaymentBottomSheetDialogue.show(getSupportFragmentManager(), "payment_method_bottom_sheet");
            }
        });
        transactionNote = "Fees for month:" + month;
    }

    public static String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public void uploadRequest() {
        pay_req.setClickable(false);
        pay_req.setEnabled(false);
        upi_pay.setClickable(false);
        upi_pay.setEnabled(false);
        findViewById(R.id.animationView02).setVisibility(View.VISIBLE);
        String[] split = month.split(" ");
        each_month = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            each_month[i] = split[i];
            databaseReference.child(split[i] + " 2020").child(username).setValue(new StudentPaymentModelClass(name, username, mode, amount, semester, getDateTime())).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (ContextCompat.checkSelfPermission(StudentPaymentPortalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                            ActivityCompat.requestPermissions(StudentPaymentPortalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PDF_MAKER);
                        else {
                            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hello);
                            scalable_bmp = Bitmap.createScaledBitmap(bmp, 1200, 510, true);
                            createPdf(name, username, mode, each_month, ms, amount);
                        }
                        findViewById(R.id.animationView02).setVisibility(View.GONE);
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(StudentPaymentPortalActivity.this);
                        builder.setTitle("Request granted successfully.");
                        builder.setMessage("Do you want to explore more ?");
                        builder.setIcon(R.drawable.yor_mapobed);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (mInterstitialAd.isLoaded())
                                    mInterstitialAd.show();
                                else {

                                    startActivity(new Intent(StudentPaymentPortalActivity.this, StudentActivity.class));
                                    finish();
                                }

                            }
                        }).setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (rewardedAd.isLoaded()) {

                                    rewardedAd = new RewardedAd(StudentPaymentPortalActivity.this, "ca-app-pub-3653476691713377/4417786840");

                                    RewardedAdCallback rewardedAdCallback=new RewardedAdCallback() {
                                        @Override
                                        public void onRewardedAdOpened() {
                                        }

                                        @Override
                                        public void onRewardedAdClosed() {
                                            super.onRewardedAdClosed();

                                            System.exit(0);

                                        }

                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                                        }
                                    };

                                }
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        builder.setCancelable(true);
                        alertDialog.show();
                    } else {
                        Snackbar snackbar =
                                Snackbar.make(findViewById(R.id.constrain01), "The process was interrupted.Please try again later", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(StudentPaymentPortalActivity.this, R.color.colorPrimary));
                        snackbar.show();

                    }
                }
            });
        }
    }




    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String t_note, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", t_note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Snackbar snackbar = Snackbar.make(co_pay, "GPay is not installed in your device. Try something else", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }
    }

    private void payWithPaytm() {
        if (isAppInstalled(this, PAYTM_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(PAYTM_PACKAGE_NAME);
            startActivityForResult(intent, PAYTM_PAY_REQUEST_CODE);
        } else {
            Snackbar snackbar = Snackbar.make(co_pay, "Paytm is not installed in your device. Try something else", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }
    }

    private void payWithPhonePay() {
        if (isAppInstalled(this, PHONE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(PHONE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, PHONEPAY_PAY_REQUEST_CODE);
        } else {
            Snackbar snackbar = Snackbar.make(co_pay, "Phone Pay is not installed in your device. Try something else", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PDF_MAKER && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            createPdf(name, username, mode, each_month, ms, amount);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StudentPaymentPortalActivity.this, StudentMonthlyPaymentActivity.class));
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String status = null;
        if (requestCode == GOOGLE_PAY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            if (status.equals("success")) {
                mode = "GPay UPI payment";
                uploadRequest();
            } else {
                Snackbar snackbar = Snackbar.make(co_pay, "Transaction Failed", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        } else if (requestCode == PAYTM_PAY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            if (status.equals("success")) {
                mode = "Paytm UPI payment";
                uploadRequest();
            } else {
                Snackbar snackbar = Snackbar.make(co_pay, "Transaction Failed", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        } else if (requestCode == PHONEPAY_PAY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            if (status.equals("success")) {
                mode = "PhonePay UPI payment";
                uploadRequest();
            } else {
                Snackbar snackbar = Snackbar.make(co_pay, "Transaction Failed", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar.make(co_pay, "Something went wrong.Please try again.", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }
    }

    void createPdf(String s, String un, String mode, String month[], int amount, int total) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scalable_bmp, 0, 0, paint);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC));
        canvas.drawText("TUITION ORGANIZER", pageInfo.getPageWidth() / 4, 265, paint);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Email : debo.roy79@gmail.com", 1170, 60, paint);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Call : +916290060688", 1170, 120, paint);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setUnderlineText(false);
        canvas.drawText("Payment Invoice", pageInfo.getPageWidth() / 2, 600, paint);

        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setUnderlineText(false);
        canvas.drawText("Name : " + s, 30, 750, paint);

        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setUnderlineText(false);
        canvas.drawText("Username : " + un, 30, 830, paint);


        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setUnderlineText(false);
        canvas.drawText("Date : " + java.text.DateFormat.getDateInstance().format(new Date()), 1170, 750, paint);


        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setUnderlineText(false);
        canvas.drawText("Time : " + java.text.DateFormat.getTimeInstance().format(new Date()), 1170, 830, paint);

        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setUnderlineText(false);
        canvas.drawText("Paid to : Sanjoy Ghosh", 30, 910, paint);

        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        paint.setUnderlineText(false);
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setUnderlineText(false);
        canvas.drawText("Mode of payment : " + mode, 1170, 910, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(20, 990, pageInfo.getPageWidth() - 20, 1080, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Sl No.", 200, 1050, paint);
        canvas.drawText("Months", 580, 1050, paint);
        canvas.drawText("Amount", 980, 1050, paint);
        canvas.drawLine(350, 1000, 350, 1070, paint);
        canvas.drawLine(800, 1000, 800, 1070, paint);
        int yy = 1100;
        for (int i = 0; i < month.length; i++) {
            yy += 70;
            canvas.drawText(String.valueOf(i + 1), 200, yy, paint);
            canvas.drawText(String.valueOf(month[i]), 550, yy, paint);
            canvas.drawText(String.valueOf(amount), 980, yy, paint);

        }
        yy += 70;
        canvas.drawLine(400, yy, pageInfo.getPageWidth() - 40, yy, paint);

        yy += 70;
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        canvas.drawText("Total", 550, yy, paint);

        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        canvas.drawText(" " + total + " Rs.", 980, yy, paint);

        yy += 100;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);
        canvas.drawRect(40, yy, pageInfo.getPageWidth() - 40, yy + 340, paint);

        int texty = yy;

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("Hello " + s + ",", 60, texty + 40, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("If you have encountered any problem with amount or months ", 60, texty + 80, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("to be paid,then you can contact your teacher along with", 60, texty + 120, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("required verification. If you have encountered any technical", 60, texty + 160, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("problem then you can contact me through email or phone ", 60, texty + 200, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("number provided above ASAP.", 60, texty + 240, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("Kind Regards,", 60, texty + 280, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("Debopam Roy", 60, texty + 320, paint);
        pdfDocument.finishPage(page);

       /* File file1 = new File(Environment.getExternalStorageDirectory(), "Organizer");
        if (!file1.exists())
            file1.mkdirs();*/

        File file = new File(Environment.getExternalStorageDirectory(), "/Payment " + java.text.DateFormat.getDateInstance().format(new Date()) + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            file.setWritable(false);
            Toast.makeText(this, "Payment details has been saved in internal storage inside Organizer folder.", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar.make(co_pay, "Payment details has been saved in internal storage inside Organizer folder.", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }


    @Override
    public void buttonClicked(String text) {
        if (text.equals("Paytm")) {
            uri = getUpiPaymentUri(dest_name, upiId, transactionNote, String.valueOf(amount));
            payWithPaytm();
        }
        if (text.equals("PhonePay")) {
            uri = getUpiPaymentUri(dest_name, upiId, transactionNote, String.valueOf(amount));
            payWithPhonePay();
        }
        if (text.equals("GPay")) {
            uri = getUpiPaymentUri(dest_name, upiId, transactionNote, String.valueOf(amount));
            payWithGPay();
        }
    }

    private class LoadAdError {
    }
}