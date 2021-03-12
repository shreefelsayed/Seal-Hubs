package com.armjld.enviohubs.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.armjld.enviohubs.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Timer;
import java.util.TimerTask;

public class StartUp extends AppCompatActivity {

    public static double minVersion;
    public static double lastVersion;
    static DatabaseReference uDatabase;
    SharedPreferences sharedPreferences = null;
    int codee = 10001;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "اضغط مرة اخري للخروج من التطبيق", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_startup);

        uDatabase = FirebaseDatabase.getInstance().getReference().child("Pickly").child("users");

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        assert pInfo != null;
        String version = pInfo.versionName;
        double dVersion = Double.parseDouble(version);

        // -------------- Check for Updates --------------- //
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1200).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                minVersion = mFirebaseRemoteConfig.getDouble("min_version_enviohubs");
                lastVersion = mFirebaseRemoteConfig.getDouble("last_version_enviohubs");
                sharedPreferences = getSharedPreferences("com.armjld.enviohubs", MODE_PRIVATE);

                if (dVersion >= minVersion) {
                    if (dVersion >= lastVersion) {
                        whatToDo();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(StartUp.this).create();
                        alertDialog.setTitle("تحديث");
                        alertDialog.setMessage("يوجد تحديث جديد للتطبيق");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "تحديث", (dialog, which) ->  {
                                    openWebURL("https://play.google.com/store/apps/details?id=com.armjld.enviohubs");
                                    finish();
                                    dialog.dismiss();
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "لاحقا", (dialog, which) ->  {
                            dialog.dismiss();
                        });
                        alertDialog.show();
                    }
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(StartUp.this).create();
                    alertDialog.setTitle("تحديث");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("يوجد تحديث جديد للتطبيق");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "تحديث", (dialog, which) ->  {
                        openWebURL("https://play.google.com/store/apps/details?id=com.armjld.enviohubs");
                        finish();
                        dialog.dismiss();
                    });
                    alertDialog.show();
                }
            }
        });

    }

    public void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
        startActivity(browse);
    }

    private void whatToDo() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    LoginManager _lgnMn = new LoginManager();
                    _lgnMn.setMyInfo(StartUp.this);
                    FirebaseCrashlytics.getInstance().setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                } else {
                    finish();
                    startActivity(new Intent(StartUp.this, Login_Options.class));
                }
            }
        }, 2500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == codee) {
            if (resultCode != RESULT_OK) {
                whatToDo();
                Toast.makeText(this, "لم يتم تحديث التطبيق", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
