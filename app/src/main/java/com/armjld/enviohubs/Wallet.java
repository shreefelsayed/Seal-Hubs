package com.armjld.enviohubs;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

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
                Timber.i("Added Money to Wallet");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
