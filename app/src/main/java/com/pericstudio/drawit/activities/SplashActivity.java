package com.pericstudio.drawit.activities;

/*
 * Copyright 2016 Eric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.DeviceIdentifier;
import com.facebook.FacebookSdk;
import com.pericstudio.drawit.APIKeys;
import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.utils.T;


public class SplashActivity extends AppCompatActivity {

    private static int MAX_RECURSION = 50;

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DeviceIdentifier.initialize(getApplicationContext());
        CMApiCredentials.initialize(APIKeys.CM_APP_ID, APIKeys.CM_API_KEY, getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        MyApplication.changeMusic(APIKeys.DASHBOARD_MUSIC_TAG);
//        MusicManager.getMusicManager();
//        MusicManager.getMusicManager().playMusic("DashboardActivityOld", getApplicationContext());
        counter = 0;
        sleepYo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    /**
     * Recursively sleeps until the FacebookSDK has been successfuly initialized. This to prevent
     * errors whenever the FacebookSDK fails to initialize in time.
     */
    private void sleepYo() {
        try {
            Thread.sleep(100);
            if(FacebookSdk.isInitialized()) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else if(counter < MAX_RECURSION) {
                counter++;
                sleepYo();
            } else {
                T.showLong(MyApplication.getContext(), "FacebookSDK failed to initialize. Submit a bug report");
            }
        } catch (InterruptedException ie) {
            T.showShortDebug(getApplicationContext(), "One second sleep. SplashActivity");
        }
    }
}
