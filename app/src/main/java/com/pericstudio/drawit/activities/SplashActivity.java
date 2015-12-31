package com.pericstudio.drawit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cloudmine.api.CMApiCredentials;
import com.facebook.FacebookSdk;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.music.MusicManager;
import com.pericstudio.drawit.utils.T;


public class SplashActivity extends AppCompatActivity {

    private static final String APP_ID = "2ee0288021974701a1f855ee13fb97f3";
    private static final String API_KEY = "fcb38f9211d74b67a87a72605abd7455";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        MusicManager.getMusicManager();
        MusicManager.getMusicManager().playMusic("DashboardActivity", getApplicationContext());

        try {
            Thread.sleep(1000);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } catch (InterruptedException ie) {
            T.showShortDebug(getApplicationContext(), "One second sleep. SplashActivity");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
