package com.pericstudio.drawit.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.CreationResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.objects.User;
import com.pericstudio.drawit.objects.UserObjectIDs;
import com.pericstudio.drawit.utils.S;
import com.pericstudio.drawit.utils.T;

import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

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
            T.showLong(this, "Invalid Email");
        } else if(username.equalsIgnoreCase("") || password.equalsIgnoreCase("") || confirmPassword.equalsIgnoreCase(""))
            T.showLong(this, "One or more fields are missing");
        else if(!password.equalsIgnoreCase(confirmPassword)) {
            T.showLong(this, "Passwords do not match");
        } else if (password.length() < 5) {
            T.showLong(this, "Password needs to be at least 5 characters long");
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
            S.showSnackLong(getCurrentFocus(), "Creating account...");
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
                                T.showLong(getApplicationContext(), "Email is in use");
                            } else {
                                CMUser.searchUserProfiles(getApplicationContext(),
                                        SearchQuery.filter("userUsername").equal(username).searchQuery(),
                                        new Response.Listener<CMObjectResponse>() {

                                            @Override
                                            public void onResponse(CMObjectResponse cmObjectResponse) {
                                                List<CMObject> users = cmObjectResponse.getObjects();

                                                if(users.size() > 0) {
                                                    T.showLong(getApplicationContext(), "Username is already taken");
                                                } else {
                                                    User newUser = new User(email, username, password);
                                                    newUser.create(getApplicationContext(), new Response.Listener<CreationResponse>() {
                                                        @Override
                                                        public void onResponse(CreationResponse creationResponse) {
                                                            T.showLong(getApplicationContext(), "Account created successfully");
                                                            final String userID = creationResponse.getObjectId();

                                                            //TODO: facebook id, and yadadada
                                                            UserObjectIDs userObjectIDs = new UserObjectIDs(userID, "", "");
                                                            userObjectIDs.save(getApplicationContext(), new Response.Listener<ObjectModificationResponse>() {
                                                                @Override
                                                                public void onResponse(ObjectModificationResponse objectModificationResponse) {
                                                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                                }
                                                            });
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
