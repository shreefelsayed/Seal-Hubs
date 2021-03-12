package com.armjld.enviohubs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.armjld.enviohubs.models.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class OrderInfo extends AppCompatActivity {

    public static Data orderData;

    private static final int PHONE_CALL_CODE = 100;
    DatabaseReference uDatabase, nDatabase;
    TextView date3, date, orderto, OrderFrom, txtPack, txtWeight, ordercash2, txtState, txtPostDate2;
    TextView dsUsername , txtOnwerPhone;
    TextView dsPAddress, dsDAddress, txtCallCustomer, txtCustomerName;
    ImageView supPP;
    ImageView btnClose;
    TextView txtNotes;
    LinearLayout linSupplier;

    String orderState = "placed";
    String ownerName = "";
    String dPhone = "";
    String ownerPhone = "";

    ListView recyclerActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        if(orderData == null) {
            finish();
            return;
        }

        uDatabase = getInstance().getReference().child("Pickly").child("users");
        nDatabase = getInstance().getReference().child("Pickly").child("notificationRequests");

        btnClose = findViewById(R.id.btnClose);

        // -- Reciver
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtCallCustomer = findViewById(R.id.txtCallCustomer);
        dsDAddress = findViewById(R.id.dsDAddress);

        // -- Package
        date = findViewById(R.id.date);
        orderto = findViewById(R.id.orderto);
        OrderFrom = findViewById(R.id.OrderFrom);
        txtPack = findViewById(R.id.txtPack);
        txtWeight = findViewById(R.id.txtWeight);
        ordercash2 = findViewById(R.id.ordercash2);
        txtState = findViewById(R.id.fees2);
        txtPostDate2 = findViewById(R.id.txtPostDate2);
        date3 = findViewById(R.id.date3);

        // --- Supplier
        linSupplier = findViewById(R.id.linSupplier);
        txtOnwerPhone = findViewById(R.id.txtOnwerPhone);
        txtNotes = findViewById(R.id.txtNotes);
        supPP = findViewById(R.id.supPP);
        dsPAddress = findViewById(R.id.ddPhone);
        dsUsername = findViewById(R.id.ddUsername);
        // --- Actions
        recyclerActions = findViewById(R.id.recyclerActions);
        recyclerActions.setVisibility(View.GONE);

        // --- Toolbar
        TextView tbTitle = findViewById(R.id.toolbar_title);
        tbTitle.setText("بيانات الشحنة");

        btnClose.setOnClickListener(v -> finish());
        linSupplier.setVisibility(View.GONE);

        orderState = orderData.getStatue();
        dPhone = orderData.getDPhone();

        txtCallCustomer.setOnClickListener(v -> {
            if (dPhone.equals("")) return;

            AlertDialog alertDialog = new AlertDialog.Builder(OrderInfo.this).create();
            alertDialog.setTitle("إتصال");
            alertDialog.setMessage("هل تريد الاتصال بالعميل ؟");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "اتصال", (dialog, which) ->  {
                checkPermission(Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + dPhone));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                dialog.dismiss();
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لا", (dialog, which) ->  {
                dialog.dismiss();
            });
            alertDialog.show();
        });

        txtOnwerPhone.setOnClickListener(v -> {
            if (ownerPhone.equals("")) return;

            AlertDialog alertDialog = new AlertDialog.Builder(OrderInfo.this).create();
            alertDialog.setTitle("إتصال");
            alertDialog.setMessage("هل تريد الاتصال بالراسل ؟");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "اتصال", (dialog, which) ->  {
                checkPermission(Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + ownerPhone));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                dialog.dismiss();
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لا", (dialog, which) ->  {
                dialog.dismiss();
            });
            alertDialog.show();
        });

        setData();
    }

    private void setData() {
        OrderStatue orderStatue = new OrderStatue();
        // ------- Set the Data
        caculateTime _cacu = new caculateTime();
        String from = orderData.reStateP();
        String to = orderData.reStateD();
        String PAddress = "عنوان الاستلام : " + from + " - " + orderData.getmPAddress();
        String DAddress = "عنوان التسليم : " + to;
        String money = "سعر الشحنه : " + orderData.getGMoney();
        String pDate = orderData.getpDate();
        String dDate = orderData.getDDate();
        String pack = "المحتوي : " + orderData.getPackType();
        String weight = "الوزن : " + orderData.getPackWeight() + " كيلو";
        DAddress = DAddress + " - " + orderData.getDAddress();

        txtState.setText(orderStatue.longStatue(orderData.getStatue()));
        ordercash2.setText(money + " ج");
        date3.setText(pDate);
        date.setText(dDate);
        txtPack.setText(pack);
        txtWeight.setText(weight);
        orderto.setText(to);
        OrderFrom.setText(from);
        txtNotes.setText("الملاحظات : " + orderData.getNotes());
        txtCallCustomer.setText("رقم هاتف العميل : " + dPhone);
        txtCallCustomer.setPaintFlags(txtCallCustomer.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtOnwerPhone.setPaintFlags(txtOnwerPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtCustomerName.setText(orderData.getDName());
        dsPAddress.setText(PAddress);
        dsDAddress.setText(DAddress);
        txtPostDate2.setText(_cacu.setPostDate(orderData.getDate()));

        // ------- Set More Supplier Data ------- //
        getSupData(orderData.getuId());
        getActions(orderData.getId());
    }

    private void getActions(String id) {
        ArrayList<String> actions = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Pickly").child("raya").child(id).child("logs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    ArrayAdapter<String> aa = new ArrayAdapter<>(OrderInfo.this, R.layout.list_white_text, R.id.txtItem, actions);
                    recyclerActions.setAdapter(aa);

                    actions.clear();
                    actions.trimToSize();

                    for(DataSnapshot logs : snapshot.getChildren()) {
                        actions.add(Objects.requireNonNull(logs.getValue()).toString());
                        aa.notifyDataSetChanged();
                    }
                    recyclerActions.setVisibility(View.VISIBLE);
                } else {
                    recyclerActions.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getSupData(String owner) {
        uDatabase.child(owner).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Supplier Main Info ---
                ownerName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String dsPP = Objects.requireNonNull(snapshot.child("ppURL").getValue()).toString();
                Picasso.get().load(Uri.parse(dsPP)).into(supPP);
                dsUsername.setText(ownerName);
                ownerPhone = Objects.requireNonNull(snapshot.child("phone").getValue()).toString();
                linSupplier.setVisibility(View.VISIBLE); // Show the Info
                txtOnwerPhone.setText("رقم هاتف الراسل : " + ownerPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PHONE_CALL_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Phone Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Phone Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}