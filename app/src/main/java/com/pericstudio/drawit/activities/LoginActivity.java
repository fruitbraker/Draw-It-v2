package com.pericstudio.drawit.activities;

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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.LoginResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pericstudio.drawit.APIKeys;
import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.pojo.User;
import com.pericstudio.drawit.pojo.UserObjectIDs;
import com.pericstudio.drawit.utils.T;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbAutolog;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private SharedPreferences mSharedPreferences;
    private boolean wasCreateActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.wasIntent = false;
        setContentView(R.layout.activity_login);
        T.showLongDebug(getApplicationContext(), APIKeys.CM_API_KEY);
        mSharedPreferences = getSharedPreferences(MyApplication.SHAREDPREF_TAG, Context.MODE_PRIVATE);
        wasCreateActivity = false;
        init();
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
                final Dialog dialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait...");
                dialog.show();

                final String userID = loginResult.getAccessToken().getUserId();
                final SharedPreferences.Editor editor = mSharedPreferences.edit();

                //Checks if the user has logged in through Facebook already. If not, userID is created
                LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
                                .equal(userID).searchQuery(),
                        new Response.Listener<CMObjectResponse>() {
                            @Override
                            public void onResponse(CMObjectResponse response) {
                                final List<CMObject> filler = response.getObjects();
                                if (filler.size() > 0) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_LONG).show();
                                    MyApplication.setUserID(userID);
                                    editor.putString(MyApplication.SHAREDPREF_USERID, userID);
                                    editor.apply();
                                    MyApplication.setUserDataObject((UserObjectIDs) filler.get(0));
                                    startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
                                } else {
                                    dialog.dismiss();
                                    final UserObjectIDs userObjectIDs = new UserObjectIDs(userID);
                                    userObjectIDs.save(getApplicationContext(), new Response.Listener<ObjectModificationResponse>() {
                                        @Override
                                        public void onResponse(ObjectModificationResponse objectModificationResponse) {
                                            MyApplication.setUserID(userID);
                                            editor.putString(MyApplication.SHAREDPREF_USERID, userID);
                                            editor.apply();
                                            MyApplication.setUserDataObject(userObjectIDs);
                                            startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
                                        }
                                    });
                                }
                            }
                        });
                dialog.dismiss();
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
        MyApplication.wasIntent = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login method
     * @param view The given parent view from the button. Usually not used.
     */
    public void login(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
//        if(cbAutolog.isChecked()) {
//            wasIntent = true;
//            startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
//        }else
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            T.showLong(this, "Invalid email");
        } else if (email.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            T.showLong(this, "One or more of the fields are empty");
        } else {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            new LoginAsync().execute(email, password);
        }
    }

    /**
     * Create account method.
     * @param view The given parent view from the button.
     */
    public void createAccount(View view) {
        MyApplication.wasIntent= true;
        wasCreateActivity = true;
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    /**
     * This AsyncTask logs the user in asynchronously with CloudMine.
     * If successful, the app will take the user to the Dashboard.
     */
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

                    LocallySavableCMObject.searchObjects(getApplicationContext(), SearchQuery.filter("ownerID")
                                    .equal(userID).searchQuery(),
                            new Response.Listener<CMObjectResponse>() {
                                @Override
                                public void onResponse(CMObjectResponse response) {
                                    List<CMObject> filler = response.getObjects();

                                    if (filler.size() > 0) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_LONG).show();
                                        MyApplication.setUserID(userID);
                                        editor.putString(MyApplication.SHAREDPREF_USERID, userID);
                                        editor.apply();
                                        MyApplication.setUserDataObject((UserObjectIDs) filler.get(0));
                                        startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
                                    } else {
                                        dialog.dismiss();
                                        final UserObjectIDs userObjectIDs = new UserObjectIDs(userID);
                                        userObjectIDs.save(getApplicationContext(), new Response.Listener<ObjectModificationResponse>() {
                                            @Override
                                            public void onResponse(ObjectModificationResponse objectModificationResponse) {
                                                MyApplication.setUserID(userID);
                                                editor.putString(MyApplication.SHAREDPREF_USERID, userID);
                                                editor.apply();
                                                MyApplication.setUserDataObject(userObjectIDs);
                                                startActivity(new Intent(getApplicationContext(), DashboardMainActivity.class));
                                            }
                                        });
                                    }
                                }
                            });

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

    @Override
    protected void onPause() {
        super.onPause();
        if(!MyApplication.wasIntent) {
            MyApplication.onPauseMusic();
        } else if(!wasCreateActivity)
            finish();
//        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.onResumeMusic();
        MyApplication.wasIntent = false;
        wasCreateActivity = true;
//        AppEventsLogger.deactivateApp(this);
    }

}
