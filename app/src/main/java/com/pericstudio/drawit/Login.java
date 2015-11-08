package com.pericstudio.drawit;

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
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.response.LoginResponse;
import com.pericstudio.drawit.music.MusicManager;

public class Login extends AppCompatActivity {

    private static final String APP_ID = "2ee0288021974701a1f855ee13fb97f3";
    private static final String API_KEY = "fcb38f9211d74b67a87a72605abd7455";

    private EditText etEmail, etPassword;
    private CheckBox cbAutolog;

    private SharedPreferences mSharedPreferences;
    private boolean wasIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isAutoLog;
        MusicManager.getMusicManager();
        MusicManager.getMusicManager().playMusic("Dashboard", getApplicationContext());
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
        setContentView(R.layout.activity_login);
        mSharedPreferences = getSharedPreferences("DrawIt", Context.MODE_PRIVATE);
        isAutoLog = mSharedPreferences.getBoolean("AutoLogin", false);

        if (isAutoLog)
            goToDashboard();
        else
            init();
    }

    private void goToDashboard() {
        wasIntent = true;
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

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
        wasIntent = false;
    }

    public void login(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
        } else if (email.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            Toast.makeText(this, "One or more of the fields are empty", Toast.LENGTH_LONG).show();
        } else {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            new LoginAsync().execute(email, password);
        }
    }

    public void createAccount(View view) {
        startActivity(new Intent(this, CreateAccount.class));
    }

    private class LoginAsync extends AsyncTask<String, Void, Void> {

        String email, password;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Login.this, "Logging in", "Please wait...");

//            Snackbar.make(getCurrentFocus(), "Logging in...", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
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
                    editor.putString("SessionToken", loginResponse.getSessionToken().transportableRepresentation());
                    editor.putString("UserID", loginResponse.getUserObject(User.class).getObjectId());

                    if (cbAutolog.isChecked()) {
                        editor.putBoolean("AutoLogin", true);
                    }
                    editor.commit();
                    dialog.dismiss();
                    wasIntent = true;

                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError.toString().equalsIgnoreCase("com.android.volley.ServerError"))
                        Toast.makeText(getApplicationContext(), "Email/password is incorrect", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error logging in. Check internet connection and try again", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            return null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!wasIntent)
            MusicManager.getMusicManager().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.getMusicManager().resume();
        wasIntent = false;
    }

}
