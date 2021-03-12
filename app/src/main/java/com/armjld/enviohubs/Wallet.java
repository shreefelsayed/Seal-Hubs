package com.armjld.enviohubs;

import androidx.annotation.NonNull;

import com.armjld.enviohubs.models.CaptinMoney;
import com.armjld.enviohubs.models.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Wallet {

    DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference().child("Pickly").child("users");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH);
    String datee = sdf.format(new Date());

    int denied = 5;

    public void addDeniedMoney(String id) {
        uDatabase.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("walletmoney").exists()) {
                    int currentValue = Integer.parseInt(Objects.requireNonNull(snapshot.child("walletmoney").getValue()).toString());
                    uDatabase.child(id).child("walletmoney").setValue(currentValue + denied);
                } else {
                    uDatabase.child(id).child("walletmoney").setValue(denied);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // ------ Add Package Money into Supplier Account
    public void addToSupplier(String gMoney, String uId, Data orderData) {
        int money = Integer.parseInt(gMoney);

        uDatabase.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("packMoney").exists()) {
                    int myMoney = Integer.parseInt(Objects.requireNonNull(snapshot.child("packMoney").getValue()).toString());
                    uDatabase.child(uId).child("packMoney").setValue((myMoney + money) + "");
                } else {
                    uDatabase.child(uId).child("packMoney").setValue((money) + "");
                }

                if (snapshot.child("totalMoney").exists()) {
                    int myMoney = Integer.parseInt(Objects.requireNonNull(snapshot.child("totalMoney").getValue()).toString());
                    uDatabase.child(uId).child("totalMoney").setValue((myMoney + money) + "");
                } else {
                    uDatabase.child(uId).child("totalMoney").setValue((money) + "");
                }

                addToUser(uId, Integer.parseInt(gMoney), orderData, "ourmoney");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void addToUser(String id, int money, Data orderData, String Action) {
        CaptinMoney captinMoney = new CaptinMoney(orderData.getId(), Action, datee, "false", orderData.getTrackid(), String.valueOf(money));
        uDatabase.child(id).child("payments").push().setValue(captinMoney);
    }
}
