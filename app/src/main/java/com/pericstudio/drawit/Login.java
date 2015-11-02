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

public class Login extends AppCompatActivity {

    private static final String APP_ID = "2ee0288021974701a1f855ee13fb97f3";
    private static final String API_KEY = "fcb38f9211d74b67a87a72605abd7455";

    private EditText etEmail, etPassword;
    private CheckBox cbAutolog;

    private SharedPreferences sharedPreferences;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isAutoLog;
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("LoginTest", Context.MODE_PRIVATE);
        isAutoLog = sharedPreferences.getBoolean("AutoLogin", false);
        
        if (isAutoLog) goToDashboard(); else init();
    }

    private void goToDashboard() {
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
    }

    public void login(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
        } else if(email.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
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

    @Override
    protected void onPause() {
        super.onPause();
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
            user.login(getApplicationContext(), new Response.Listener<LoginResponse>() {
                @Override
                public void onResponse(LoginResponse loginResponse) {
                    if(cbAutolog.isChecked()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("AutoLogin", true);
                        editor.putString("SessionToken", loginResponse.getSessionToken().transportableRepresentation());
                        editor.commit();
                    }
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.getCause().getMessage();
                    dialog.dismiss();
                }
            });
            return null;
        }


    }

}
