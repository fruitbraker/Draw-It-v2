package com.pericstudio.drawit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.CreationResponse;

import java.util.List;

public class CreateAccount extends AppCompatActivity {

    // Find this in your developer console
    private static final String APP_ID = "2ee0288021974701a1f855ee13fb97f3";
    // Find this in your developer console
    private static final String API_KEY = "fcb38f9211d74b67a87a72605abd7455";

    private EditText etEmail, etUsername, etPassword, etConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
        setContentView(R.layout.activity_create_account);
        init();
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.etCreateEmail);
        etUsername = (EditText) findViewById(R.id.etCreateDisplayName);
        etPassword = (EditText) findViewById(R.id.etCreatePassword);
        etConfirmPassword = (EditText) findViewById(R.id.etCreateConfirmPassword);
    }

    public void createAccountAsync(View view) {
        boolean clearToCreate = false;
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if(username.equalsIgnoreCase("") || password.equalsIgnoreCase("") || confirmPassword.equalsIgnoreCase(""))
            Toast.makeText(this, "One or more fields are missing", Toast.LENGTH_SHORT).show();
        else if(!password.equalsIgnoreCase(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(this, "Password needs to be at least 5 characters", Toast.LENGTH_LONG).show();
        } else {
            clearToCreate = true;
        }

        if(clearToCreate)
            new CreateAccountTask().execute(email, username, password);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private class CreateAccountTask extends AsyncTask<String, Void, Void> {

        String email, username, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar.make(getCurrentFocus(), "Creating account...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        @Override
        protected Void doInBackground(String... params) {

            email = params[0];
            username = params[1];
            password = params[2];

            CMUser.searchUserProfiles(getApplicationContext(), SearchQuery.filter("userEmail").equal(email).searchQuery(),
                    new Response.Listener<CMObjectResponse>() {

                        @Override
                        public void onResponse(CMObjectResponse cmObjectResponse) {
                            List<CMObject> users = cmObjectResponse.getObjects();

                            if (users.size() > 0) {
                                Toast.makeText(getApplicationContext(), "Email is in use", Toast.LENGTH_LONG).show();
                            } else {
                                CMUser.searchUserProfiles(getApplicationContext(),
                                        SearchQuery.filter("userUsername").equal(username).searchQuery(),
                                        new Response.Listener<CMObjectResponse>() {

                                            @Override
                                            public void onResponse(CMObjectResponse cmObjectResponse) {
                                                List<CMObject> users = cmObjectResponse.getObjects();

                                                if(users.size() > 0) {
                                                    Toast.makeText(getApplicationContext(), "Username is already taken", Toast.LENGTH_LONG).show();
                                                } else {
                                                    User newUser = new User(email, username, password);
                                                    newUser.create(getApplicationContext(), new Response.Listener<CreationResponse>() {
                                                        @Override
                                                        public void onResponse(CreationResponse creationResponse) {
                                                            Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(getApplicationContext(), Login.class));
                                                        }
                                                    });
                                                }

                                            }
                                        });
                            }
                        }
                    });



            return null;
        }
    }
}
