package com.armjld.enviohubs.Login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.armjld.enviohubs.UserDatabase;
import com.armjld.enviohubs.models.UserInFormation;
import com.armjld.enviohubs.models.userData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoginManager {

    public static boolean dataset = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference().child("Pickly").child("users");
    UserDatabase userDatabase = new UserDatabase();

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void setMyInfo(Context mContext) {
        mAuth = FirebaseAuth.getInstance();
        uDatabase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).keepSynced(true);

        uDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userData User = snapshot.getValue(userData.class);
                    assert User != null;

                    if (User.getActive().equals("false")) {
                        Toast.makeText(mContext, "تم اغلاق حسابك، الرجاء الاتصال بالدعم الفني", Toast.LENGTH_SHORT).show();
                        clearInfo(mContext);
                        return;
                    }

                    if (!User.getProvider().equals("Raya")) {
                        Toast.makeText(mContext, "لا يمكنك تسجيل الدخول الي هذا البرنامج", Toast.LENGTH_SHORT).show();
                        clearInfo(mContext);
                        return;
                    }

                    if (!User.getAccountType().equals("Hub") && !User.getAccountType().equals("Opera")) {
                        Toast.makeText(mContext, "لا يمكنك تسجيل الدخول" + User.getAccountType(), Toast.LENGTH_SHORT).show();
                        clearInfo(mContext);
                        return;
                    }

                    setSignInValues(mContext);
                    setUserInformation(User);

                    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();

                    dataset = true;
                    mContext.startActivity(new Intent(mContext, LoadingScreen.class));
                    ((Activity) mContext).finish();
                } else {
                    mContext.startActivity(new Intent(mContext, Login_Options.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setUserInformation(userData user) {
        UserInFormation.setAccountType(user.getAccountType());
        UserInFormation.setUserName(user.getName());
        UserInFormation.setUserDate(user.getDate());
        UserInFormation.setUserURL(user.getPpURL());
        UserInFormation.setId(user.getId());
        UserInFormation.setEmail(user.getEmail());
        UserInFormation.setPass(user.getMpass());
        UserInFormation.setuPhone(user.getPhone());
        UserInFormation.setUserState(user.getUserState());
        UserInFormation.setUserCity(user.getUserCity());
        UserInFormation.setCurrentdate(user.getCurrentDate());
        UserInFormation.setWalletmoney(String.valueOf(user.getWalletmoney()));
        UserInFormation.setIsConfirm(user.getIsConfirmed());
        UserInFormation.setProvider(user.getProvider());

        // ------- Set My Supervisor Codes -------- \\
        UserInFormation.setSupId(user.getMySuperId());
        UserInFormation.setMySup(user.getMySuper());
        UserInFormation.setSup_code(user.getSupervisor_code());
    }

    private void setSignInValues(Context mContext) {
        // --- Set Device Name ---
        userDatabase.setValue("device", getDeviceName());

        // ---- Set App Version ---
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = pInfo.versionName;
            userDatabase.setValue("app_version", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // ------ Set IP Address ---
        new GetPublicIP().execute();
        @SuppressLint("HardwareIds") String android_id = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
        userDatabase.setValue("unique_id", android_id);


        // ----- Set Device Token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) mContext, instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            userDatabase.setValue("device_token", deviceToken);
        });
    }

    public void clearInfo(Context mContext) {
        userDatabase.setValue("device_token", "");
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        mAuth.signOut();
        UserInFormation.clearUser();
        dataset = false;

        ((Activity) mContext).finish();
        mContext.startActivity(new Intent(mContext, com.armjld.enviohubs.Login.Login_Options.class));
        Toast.makeText(mContext, "تم تسجيل الخروج بنجاح", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("StaticFieldLeak")
    public class GetPublicIP extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String publicIP = "";
            try {
                java.util.Scanner s = new java.util.Scanner(
                        new java.net.URL(
                                "https://api.ipify.org")
                                .openStream(), "UTF-8")
                        .useDelimiter("\\A");
                publicIP = s.next();
                System.out.println("My current IP address is " + publicIP);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return publicIP;
        }

        @Override
        protected void onPostExecute(String publicIp) {
            super.onPostExecute(publicIp);
            userDatabase.setValue("ip", publicIp);
        }
    }


}
