package com.armjld.enviohubs.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.armjld.enviohubs.HubHome.MainActivity;
import com.armjld.enviohubs.R;
import com.armjld.enviohubs.getRefrence;
import com.armjld.enviohubs.models.Data;
import com.armjld.enviohubs.models.UserInFormation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class LoadingScreen extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        if (UserInFormation.getId() == null) {
            finish();
            startActivity(new Intent(this, StartUp.class));
            return;
        }

        TextView txtLoading = findViewById(R.id.txtLoading);

        txtLoading.setText("جاري تجهيز الشحنات ..");

        if (UserInFormation.getAccountType().equals("Hub")) {
            getRecived();
        }
    }


    //  ------- Get Recived Orders -------------- \\
    private void getRecived() {
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef("Esh7nly");

        MainActivity.listRecived.clear();
        MainActivity.listRecived.trimToSize();

        mDatabase.orderByChild("pHub").equalTo(UserInFormation.getSup_code()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data orderData = ds.getValue(Data.class);

                        // ---- Check if order state if it should be here in this tab
                        assert orderData != null;
                        if (orderData.getStatue().equals("hubP") || orderData.getStatue().equals("hub1Denied")) {
                            // ------ Add Order to Recived List
                            MainActivity.listRecived.add(orderData);
                        }
                    }
                }

                getProviderRecived();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getProviderRecived() {
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef("Raya");

        mDatabase.orderByChild("pHub").equalTo(UserInFormation.getSup_code()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data orderData = ds.getValue(Data.class);

                        // ---- Check if order state if it should be here in this tab
                        assert orderData != null;
                        if (orderData.getStatue().equals("hubP") || orderData.getStatue().equals("hub1Denied")) {
                            // ------ Add Order to Recived List
                            MainActivity.listRecived.add(orderData);
                        }
                    }
                }

                getDelv();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void getDelv() {
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef("Esh7nly");

        MainActivity.listDelv.clear();
        MainActivity.listDelv.trimToSize();

        MainActivity.listDenied.clear();
        MainActivity.listDenied.trimToSize();

        mDatabase.orderByChild("dHub").equalTo(UserInFormation.getSup_code()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data orderData = ds.getValue(Data.class);

                        // ---- Check if order state if it should be here in this tab
                        assert orderData != null;
                        if (orderData.getStatue().equals("hubD") || orderData.getStatue().equals("hub2Denied")) {
                            // ------ Add Order to Recived List
                            MainActivity.listDelv.add(orderData);
                        } else if (orderData.getStatue().equals("deniedD")) {
                            MainActivity.listDenied.add(orderData);
                        }
                    }
                }

                getProviderDelv();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getProviderDelv() {
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef("Raya");

        mDatabase.orderByChild("dHub").equalTo(UserInFormation.getSup_code()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Data orderData = ds.getValue(Data.class);

                        // ---- Check if order state if it should be here in this tab
                        assert orderData != null;
                        if (orderData.getStatue().equals("hubD") || orderData.getStatue().equals("hub2Denied")) {
                            // ------ Add Order to Recived List
                            MainActivity.listDelv.add(orderData);
                        } else if (orderData.getStatue().equals("deniedD")) {
                            MainActivity.listDenied.add(orderData);
                        }
                    }
                }

                whatToDo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void whatToDo() {
        startActivity(new Intent(this, MainActivity.class));
    }
}