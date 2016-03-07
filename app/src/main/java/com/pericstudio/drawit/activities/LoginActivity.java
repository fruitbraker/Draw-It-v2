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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.LoginResponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pericstudio.drawit.APIKeys;
import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.pojo.User;
import com.pericstudio.drawit.pojo.UserObjectIDs;
import com.pericstudio.drawit.utils.T;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbAutolog;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private SharedPreferences mSharedPreferences;
    private boolean wasIntent, wasCreateActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        T.showLongDebug(getApplicationContext(), APIKeys.CM_API_KEY);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mSharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
        wasCreateActivity = false;

        if (mSharedPreferences.getBoolean("AutoLogin", false))
            goToDashboard();
        else
            init();
    }

    /**
     * The method used when the user is previously logged in.
     */
    private void goToDashboard() {
        wasIntent = true;
        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "Logging in", "Please wait...");

        final String userID = mSharedPreferences.getString("UserID", null);

        if(userID != null) {
            LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
                    .equal(userID).searchQuery(), new Response.Listener<CMObjectResponse>() {
                @Override
                public void onResponse(CMObjectResponse cmObjectResponse) {
                    List<CMObject> cmObjectList = cmObjectResponse.getObjects();
                    UserObjectIDs userObjectIDs = (UserObjectIDs) cmObjectList.get(0);
                    ArrayList<String> drawings = userObjectIDs.getInProgressDrawingIDs();
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
                }
            });
        } else
            T.showLong(this, "Login failed. Please login again.");
        dialog.dismiss();
    }

    /**
     * Initializes everything. This is so that it doesn't clutter up the onCreate method.
     */
    private void init() {
        etEmail = (EditText) findViewById(R.id.etLoginEmail);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("Keyboard Log", "Done Pressed");
                    login(getCurrentFocus());
                }
                return false;
            }
        });
        cbAutolog = (CheckBox) findViewById(R.id.cbAutoLog);
        loginButton = (LoginButton) findViewById(R.id.login_button_fb);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Dialog dialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait...");
                startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));

//                final String userID = loginResult.getAccessToken().getUserId();
//                final SharedPreferences.Editor editor = mSharedPreferences.edit();

//                LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
//                                .equal(userID).searchQuery(),
//                        new Response.Listener<CMObjectResponse>() {
//                            @Override
//                            public void onResponse(CMObjectResponse response) {
//                                List<CMObject> filler = response.getObjects();
//                                if (filler.size() > 0) {
//                                    Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_LONG).show();
//                                    editor.putString("UserID", userID);
//                                    editor.apply();
//                                    startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
//                                } else {
//                                    UserObjectIDs userObjectIDs = new UserObjectIDs(userID);
//                                    userObjectIDs.save(getApplicationContext(), new Response.Listener<ObjectModificationResponse>() {
//                                        @Override
//                                        public void onResponse(ObjectModificationResponse objectModificationResponse) {
//                                            editor.putString("UserID", userID);
//                                            editor.apply();
//                                            startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
//                                        }
//                                    });
//                                }
//                            }
//                        });


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });

        wasIntent = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(cbAutolog.isChecked()) {
            wasIntent = true;
            startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            T.showLong(this, "Invalid email");
        } else if (email.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            T.showLong(this, "One or more of the fields are empty");
        } else {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            new LoginAsync().execute(email, password);
        }
    }

    public void createAccount(View view) {
        wasIntent = true;
        wasCreateActivity = true;
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    private class LoginAsync extends AsyncTask<String, Void, Void> {

        String email, password;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LoginActivity.this, "Logging in", "Please wait...");
        }

        @Override
        protected Void doInBackground(String... params) {
            email = params[0];
            password = params[1];

            CMUser user = new CMUser(email, password);

            final SharedPreferences.Editor editor = mSharedPreferences.edit();
            user.login(getApplicationContext(), new Response.Listener<LoginResponse>() {
                @Override
                public void onResponse(LoginResponse loginResponse) {
                    final String userID = loginResponse.getUserObject(User.class).getObjectId();

                    editor.putString("SessionToken", loginResponse.getSessionToken().transportableRepresentation());
                    editor.putString("UserID", userID);

                    if (cbAutolog.isChecked()) {
                        editor.putBoolean("AutoLogin", true);
                    }
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError.toString().equalsIgnoreCase("com.android.volley.ServerError"))
                        T.showLong(getApplicationContext(), "Email/password is incorrect");
                    else
                        T.showLong(getApplicationContext(), "Connection error. Please check network settings and try again");
                    dialog.dismiss();
                }
            });
            return null;
        }

    }

    public void cancelTest(View view) {
        MyApplication.onStopMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!wasIntent) {
            MyApplication.onPauseMusic();
        }

        else if(!wasCreateActivity)
            finish();
//        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.onResumeMusic();
        wasIntent = false;
        wasCreateActivity = true;
//        AppEventsLogger.deactivateApp(this);
    }

}
