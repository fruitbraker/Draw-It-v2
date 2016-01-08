package com.pericstudio.drawit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.DeviceIdentifier;
import com.facebook.FacebookSdk;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.music.MusicManager;
import com.pericstudio.drawit.utils.T;


public class SplashActivity extends AppCompatActivity {

    private static final String APP_ID = "90aaa080f39d481b9ee84dedf1d53f87";
    private static final String API_KEY = "f787bfbc752a45219abcac28ca9e9df5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DeviceIdentifier.initialize(getApplicationContext());
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        MusicManager.getMusicManager();
        MusicManager.getMusicManager().playMusic("DashboardActivity", getApplicationContext());
        sleepYo();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void sleepYo() {
        try {
            Thread.sleep(100);
            if(FacebookSdk.isInitialized())
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            else
                sleepYo();
        } catch (InterruptedException ie) {
            T.showShortDebug(getApplicationContext(), "One second sleep. SplashActivity");
        }
    }
}
