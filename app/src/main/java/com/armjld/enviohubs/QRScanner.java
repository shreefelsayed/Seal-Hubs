package com.armjld.enviohubs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.armjld.enviohubs.HubHome.MainActivity;
import com.armjld.enviohubs.models.Data;
import com.armjld.enviohubs.models.UserInFormation;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QRScanner extends BaseScannerActivity implements ZXingScannerView.ResultHandler {

    TextView txtCounter;
    CountDownTimer resend;
    ImageView btnBack;
    SpinKitView loading;
    private ZXingScannerView mScannerView;

    @Override
    public void onBackPressed() {
        MainActivity.getDelv();
        MainActivity.getRecived();
        finish();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_q_r_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        txtCounter = findViewById(R.id.txtCounter);
        btnBack = findViewById(R.id.btnBack);
        loading = findViewById(R.id.loading);

        // -------- Get Fresh data on Finish the Activity
        btnBack.setOnClickListener(v -> {
            MainActivity.getDelv();
            MainActivity.getRecived();
            finish();
        });

        // ---- Initialize Scanner
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        mScannerView.setAutoFocus(true);
        mScannerView.setLaserEnabled(true);
        mScannerView.setBorderColor(Color.WHITE);
        mScannerView.stopCameraPreview();
        mScannerView.setIsBorderCornerRounded(true);
        mScannerView.setBorderCornerRadius(300);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        mScannerView.stopCameraPreview();
        checkForOrder(rawResult.getText());
    }


    // ---------- Check Order Data from Database .. ------------ \\
    private void checkForOrder(String trackID) {
        loading.setVisibility(View.VISIBLE);
        String strF = String.valueOf(trackID.charAt(0));
        DatabaseReference mDatabase;

        // ------------ Check for order Refrence
        getRefrence ref = new getRefrence();
        if (!strF.equals("R")) {
            mDatabase = ref.getRef("Esh7nly");
        } else {
            mDatabase = ref.getRef("Raya");
        }

        // ---- Check if QR is Valid
        mDatabase.orderByChild("trackid").equalTo(trackID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // ----- Check which action to Make ..

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data orderData = ds.getValue(Data.class);
                        assert orderData != null;

                        String statue = orderData.getStatue();
                        OrdersClass ordersClass = new OrdersClass(QRScanner.this);

                        // ---- Send the action to Class
                        switch (statue) {
                            case "placed":
                            case "accepted":
                            case "recived":
                            case "recived2":  // Recive order from PickUp Captin
                                if (!orderData.getpHub().equals(UserInFormation.getSup_code())) {
                                    Toast.makeText(QRScanner.this, "تم استلام الشحنه لكن هذه الشحنه لا ينبغي ان يتم تسليمها لك", Toast.LENGTH_LONG).show();
                                }

                                if (orderData.getpHub().equals(orderData.getdHub()) && !orderData.getpHub().equals("")) { // if it's the same HUB
                                    ordersClass.recSameAction(orderData);
                                } else { // if it's a diffrent hub to Deliver
                                    ordersClass.recAction(orderData);
                                }

                                break;
                            case "hubP":
                                if(orderData.getpHub().equals(UserInFormation.getSup_code()) && orderData.getdHub().equals(UserInFormation.getSup_code())) {
                                    ordersClass.recSameAction(orderData);
                                } else {
                                    ordersClass.recFromHub(orderData);
                                }
                                break;
                            case "hub1Denied":  // if i am getting a returned order from another HUB
                                ordersClass.incomeDeniedAction(orderData);
                                break;
                            case "denied":  // if i am getting a returned order from a CAPTIN
                                ordersClass.denied(orderData);
                                break;
                            default:  // ---- Order Statue isn't valid to make this action ..
                                Toast.makeText(QRScanner.this, "لا يمكنك تسجيل اي اكشن علي هذه الشحنه", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        break;
                    }
                } else {
                    Toast.makeText(QRScanner.this, "رقم التتبع غير صحيح، حاول مره اخري ..", Toast.LENGTH_SHORT).show();
                }

                // -------------- Start Scanning Again in 5 Secs ----------
                loading.setVisibility(View.GONE);
                startCounter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // ---------- Start a Delay Counter ------------ \\
    private void startCounter() {
        mScannerView.stopCameraPreview();
        resend = new CountDownTimer(3000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                txtCounter.setVisibility(View.VISIBLE);
                txtCounter.setText((l / 1000) + "");
            }

            @Override
            public void onFinish() {
                txtCounter.setVisibility(View.GONE);
                mScannerView.resumeCameraPreview(QRScanner.this);
            }
        }.start();
    }
}