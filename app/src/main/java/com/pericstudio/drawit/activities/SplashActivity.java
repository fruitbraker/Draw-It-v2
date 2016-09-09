package com.pericstudio.drawit.activities;

import android.app.ProgressDialog;
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

    //Maximum of 20 seconds
    private static final int MAX_RECURSION = 200;

    private int counter;
    private boolean isFBInitializing = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DeviceIdentifier.initialize(getApplicationContext());
        CMApiCredentials.initialize(APIKeys.CM_APP_ID, APIKeys.CM_API_KEY, getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        isFBInitializing = true;
        MyApplication.changeMusic(APIKeys.DASHBOARD_MUSIC_TAG);
        counter = 0;

        progressDialog = ProgressDialog.show(SplashActivity.this, "Initializing", "Please wait...");

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
            if(FacebookSdk.isInitialized() && isFBInitializing) {
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

//                SharedPreferences mSharedPreferences = getSharedPreferences(MyApplication.SHAREDPREF_TAG, Context.MODE_PRIVATE);
//                final String userID = mSharedPreferences.getString(MyApplication.SHAREDPREF_USERID, null);
//                if(!userID.equalsIgnoreCase("") || !(userID == null)) {
//                    MyApplication.setUserID(userID);
//
//                    LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
//                                    .equal(userID).searchQuery(),
//                            new Response.Listener<CMObjectResponse>() {
//                                @Override
//                                public void onResponse(CMObjectResponse response) {
//                                    List<CMObject> filler = response.getObjects();
//
//                                    if (filler.size() > 0) {
//                                        Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_LONG).show();
//                                        MyApplication.setUserID(userID);
//                                        MyApplication.setUserDataObject((UserObjectIDs) filler.get(0));
//                                        startActivity(new Intent(MyApplication.getContext(), DashboardMainActivity.class));
//                                    } else {
//                                        startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
//                                    }
//                                }
//                            });
//
//                } else {
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                }
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
