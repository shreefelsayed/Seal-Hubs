package com.armjld.enviohubs.HubHome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.armjld.enviohubs.Login.LoginManager;
import com.armjld.enviohubs.Login.StartUp;
import com.armjld.enviohubs.R;
import com.armjld.enviohubs.getRefrence;
import com.armjld.enviohubs.models.Data;
import com.armjld.enviohubs.models.UserInFormation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 90;
    public static String whichFrag = "Received";
    public static BottomNavigationView bottomNavigationView;

    public static ArrayList<Data> listRecived = new ArrayList<>();
    public static ArrayList<Data> listDelv = new ArrayList<>();
    public static ArrayList<Data> listDenied = new ArrayList<>();
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {
        Fragment fragment = null;
        String fragTag = "";
        switch (item.getItemId()) {
            case R.id.recived: {
                fragment = new HubRecived();
                fragTag = "Received";
                break;
            }

            case R.id.delivered: {
                fragment = new HubDelivered();
                fragTag = "Delivered";
                break;
            }

            case R.id.denied: {
                fragment = new HubDenied();
                fragTag = "Denied";
                break;
            }
        }
        assert fragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragTag).addToBackStack("Received").commit();
        return true;
    };
    boolean doubleBackToExitPressedOnce = false;

    public static void getRecived() {
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef("Esh7nly");

        listRecived.clear();
        listRecived.trimToSize();

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
                            listRecived.add(orderData);
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

    private static void getProviderRecived() {
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
                            listRecived.add(orderData);
                        }
                    }
                }

                HubRecived.getOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void getDelv() {
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef("Esh7nly");

        listDelv.clear();
        listDelv.trimToSize();

        listDenied.clear();
        listDenied.trimToSize();

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
                            listDelv.add(orderData);
                        } else if (orderData.getStatue().equals("deniedD")) {
                            listDenied.add(orderData);
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

    private static void getProviderDelv() {
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
                            listDelv.add(orderData);
                        } else if (orderData.getStatue().equals("deniedD")) {
                            listDenied.add(orderData);
                        }
                    }
                }

                HubDelivered.getOrders();
                HubDenied.getOrders();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Home");
        if (fragment != null && fragment.isVisible()) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                System.exit(0);
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "اضغط مرة اخري للخروج من التطبيق", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            whichFrag = "Received";
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HubRecived(), whichFrag).addToBackStack("Received").commit();
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!LoginManager.dataset) {
            finish();
            startActivity(new Intent(this, StartUp.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (UserInFormation.getId() == null) {
            finish();
            startActivity(new Intent(this, StartUp.class));
        }

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, whichFrag(), whichFrag).addToBackStack("Received").commit();

        checkForCamera();
    }

    private void checkForCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
    }

    private Fragment whichFrag() {
        Fragment frag = null;
        switch (whichFrag) {
            case "Received": {
                frag = new HubRecived();
                bottomNavigationView.setSelectedItemId(R.id.recived);
                break;
            }
            case "Delivered": {
                frag = new HubDelivered();
                bottomNavigationView.setSelectedItemId(R.id.delivered);
                break;
            }

            case "Denied": {
                frag = new HubDenied();
                bottomNavigationView.setSelectedItemId(R.id.denied);
                break;
            }

        }
        return frag;
    }

}