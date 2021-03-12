package com.armjld.enviohubs;

import android.content.Context;
import android.widget.Toast;

import com.armjld.enviohubs.models.Data;
import com.armjld.enviohubs.models.UserInFormation;
import com.armjld.enviohubs.models.notiData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrdersClass {

    Context mContext;
    DatabaseReference nDatabase = FirebaseDatabase.getInstance().getReference().child("Pickly").child("notificationRequests");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH);
    String datee = sdf.format(new Date());
    int maxTries = 2;

    public OrdersClass(Context mContext) {
        this.mContext = mContext;
    }


    // ------- When HUB recive Order from CAPTIN
    public void recAction(Data orderData) {
        // --- Get Refrence
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        mDatabase.child(orderData.getId()).child("statue").setValue("hubP");
        mDatabase.child(orderData.getId()).child("pHubReciveTime").setValue(datee);

        mDatabase.child(orderData.getId()).child("pHub").setValue(UserInFormation.getSup_code());
        mDatabase.child(orderData.getId()).child("pHubName").setValue(UserInFormation.getUserName());

        mDatabase.child(orderData.getId()).child("uAccepted").setValue("");

        // -- Toast
        Toast.makeText(mContext, "تم استلام الشحنه", Toast.LENGTH_SHORT).show();

        // --- Logs
        logOrder(mDatabase, orderData.getId(), " تم استلام الشحنه من مخزن " + orderData.getpHub() + " في مخزن " + UserInFormation.getUserName());
    }

    public void recFromHub(Data orderData) {
        // --- Get Refrence
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        mDatabase.child(orderData.getId()).child("statue").setValue("hubD");
        mDatabase.child(orderData.getId()).child("dHubReciveTime").setValue(datee);

        mDatabase.child(orderData.getId()).child("dHub").setValue(UserInFormation.getSup_code());
        mDatabase.child(orderData.getId()).child("dHubName").setValue(UserInFormation.getUserName());

        mDatabase.child(orderData.getId()).child("uAccepted").setValue("");

        // -- Toast
        Toast.makeText(mContext, "تم استلام الشحنه", Toast.LENGTH_SHORT).show();

        // --- Logs
        logOrder(mDatabase, orderData.getId(), " تم استلام الشحنه من مخزن " + orderData.getpHub() + " في مخزن " + UserInFormation.getUserName());

    }

    // -------- When a hub recived an order from another HUB
    public void recSameAction(Data orderData) {
        // --- Get Refrence
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        mDatabase.child(orderData.getId()).child("statue").setValue("hubD");
        mDatabase.child(orderData.getId()).child("uAccepted").setValue("");

        mDatabase.child(orderData.getId()).child("pHub").setValue(UserInFormation.getSup_code());
        mDatabase.child(orderData.getId()).child("pHubReciveTime").setValue(datee);
        mDatabase.child(orderData.getId()).child("pHubName").setValue(UserInFormation.getUserName());

        mDatabase.child(orderData.getId()).child("dHub").setValue(UserInFormation.getSup_code());
        mDatabase.child(orderData.getId()).child("dHubReciveTime").setValue(datee);
        mDatabase.child(orderData.getId()).child("dHubName").setValue(UserInFormation.getUserName());

        // ---- Toast
        Toast.makeText(mContext, "تم استلام الشحنه في قائمه التسليمات", Toast.LENGTH_SHORT).show();

        // Logs
        logOrder(mDatabase, orderData.getId(), " تم استلام الشحنه في مخزن " + UserInFormation.getUserName());
    }

    // ----- When Hub recived a denied order from another hub
    public void incomeDeniedAction(Data orderData) {
        // --- Get Refrence
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        mDatabase.child(orderData.getId()).child("statue").setValue("hub2Denied");
        mDatabase.child(orderData.getId()).child("hub2DeniedTime").setValue(datee);

        // -- Notify Sender about return in the way
        String message = "جاري تسليم المرتجع الخاص بـ " + orderData.getDName() + " و سيتم تسليمه لك في اقرب وقت";
        notiData Noti = new notiData(UserInFormation.getId(), orderData.getuId(), orderData.getId(), message, datee, "false", "orderinfo", "Envio", UserInFormation.getUserURL(), "Raya");
        nDatabase.child(orderData.getuId()).push().setValue(Noti);

        // --- Logs
        logOrder(mDatabase, orderData.getId(), "تم استلام المرتجع في مخزن " + UserInFormation.getUserName());

        // -- Toast
        Toast.makeText(mContext, "تم استلام المرتجع", Toast.LENGTH_SHORT).show();
    }

    // ----- When Order has the first Try
    public void denied(Data orderData) {
        // ------- if the order already had 2 tries
        if (Integer.parseInt(orderData.getTries()) == maxTries) {
            sendOrderToSupplier(orderData);
            return;
        }

        // --- Get Refrence
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        mDatabase.child(orderData.getId()).child("statue").setValue("deniedD");
        mDatabase.child(orderData.getId()).child("uAccepted").setValue("");
        mDatabase.child(orderData.getId()).child("deniedDTime").setValue(datee);

        // --- Send notification to Supplier that order has the first try


        // --- Logs
        logOrder(mDatabase, orderData.getId(), "تم استلام المرتجع في مخزن من " + orderData.getuAccepted());

        // -- Toast
        Toast.makeText(mContext, "تم استلام المرتجع", Toast.LENGTH_SHORT).show();
    }

    // ----- set Order As Delivered
    public void delivered(Data orderData) {
        getRefrence ref = new getRefrence();
        Wallet wallet = new Wallet();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        mDatabase.child(orderData.getId()).child("statue").setValue("delivered");
        mDatabase.child(orderData.getId()).child("dilverTime").setValue(datee);

        // ---- Send Notifications to Supplier
        String message = "تم تسليم شحنه " + orderData.getDName() + " بنجاح";
        notiData Noti = new notiData(UserInFormation.getId(), orderData.getuId(), orderData.getId(), message, datee, "false", "orderinfo", "Seal Express", UserInFormation.getUserURL(), "Raya");
        nDatabase.child(orderData.getuId()).push().setValue(Noti);

        // --- Add Money to Supplier Wallet
        if (!orderData.getProvider().equals("Esh7nly")) wallet.addToSupplier(orderData.getGMoney(), orderData.getuId(), orderData);

        // -- Add Log
        String Log = "قام المخزن " + UserInFormation.getUserName() + " بتسليم الشحنه للعميل " + orderData.getDName();
        logOrder(mDatabase, orderData.getId(), Log);

        // -- Toast
        Toast.makeText(mContext, "تم توصيل الشحنة", Toast.LENGTH_SHORT).show();
    }

    // When order has 2 tries and it will ship back to Supplier
    public String sendOrderToSupplier(Data orderData) {
        // --- Get Refrence
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        // -- Toast
        Toast.makeText(mContext, "تم استلام المرتجع", Toast.LENGTH_SHORT).show();

        // ---- Update Database Childs
        if (!orderData.getpHub().equals(UserInFormation.getSup_code())) {
            mDatabase.child(orderData.getId()).child("statue").setValue("hub1Denied");
            mDatabase.child(orderData.getId()).child("hub1DeniedTime").setValue(datee);

            // --- Logs
            logOrder(mDatabase, orderData.getId(), "تم استلام المرتجع في مخزن " + UserInFormation.getUserName());

            return "hub1Denied";
        } else {
            incomeDeniedAction(orderData);
            logOrder(mDatabase, orderData.getId(), "قام المخزن " + UserInFormation.getUserName() + " بتحويل الشحنه لارجاعها الي مخزن " + orderData.getpHubName());
            return "hub2Denied";
        }
    }

    // When order has 2 tries and it will ship back to Supplier
    public String forceDenied (Data orderData) {
        // --- Get Refrence
        getRefrence ref = new getRefrence();
        DatabaseReference mDatabase = ref.getRef(orderData.getProvider());

        // -- Toast
        Toast.makeText(mContext, "تم تسجيل كمرتجع", Toast.LENGTH_SHORT).show();

        // ---- Update Database Childs
        if (!orderData.getpHub().equals(UserInFormation.getSup_code())) {
            mDatabase.child(orderData.getId()).child("statue").setValue("hub1Denied");
            mDatabase.child(orderData.getId()).child("hub1DeniedTime").setValue(datee);

            // --- Add a Try Cause it's coming back from another hub, so it should be paid
            int tries = Integer.parseInt(orderData.getTries()) + 1;
            orderData.setTries(tries + "");
            mDatabase.child(orderData.getId()).child("tries").setValue(tries + "");

            // --- Logs
            logOrder(mDatabase, orderData.getId(), "تم استلام المرتجع في مخزن " + UserInFormation.getUserName());

            return "hub1Denied";
        } else {
            incomeDeniedAction(orderData);
            logOrder(mDatabase, orderData.getId(), "قام المخزن " + UserInFormation.getUserName() + " بتحويل الشحنه لارجاعها الي مخزن " + orderData.getpHubName());
            return "hub2Denied";
        }
    }

    // -------- Add Logs to Order
    public void logOrder(DatabaseReference mDatabase, String id, String message) {
        long tsLong = System.currentTimeMillis() / 1000;
        String logC = Long.toString(tsLong);
        mDatabase.child(id).child("logs").child(logC).setValue(message);
    }
}
