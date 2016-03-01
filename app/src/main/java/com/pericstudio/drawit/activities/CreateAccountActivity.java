package com.pericstudio.drawit.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.CreationResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.pojo.User;
import com.pericstudio.drawit.pojo.UserObjectIDs;
import com.pericstudio.drawit.utils.S;
import com.pericstudio.drawit.utils.T;

import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {


    private EditText etEmail, etUsername, etPassword, etConfirmPassword, etFirstName, etLastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        init();
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.etCreateEmail);
        etUsername = (EditText) findViewById(R.id.etCreateDisplayName);
        etPassword = (EditText) findViewById(R.id.etCreatePassword);
        etConfirmPassword = (EditText) findViewById(R.id.etCreateConfirmPassword);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
    }

    public void createAccountAsync(View view) {
        String emailInput = etEmail.getText().toString().trim();
        String usernameInput = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String firstNameInput = etFirstName.getText().toString().trim();
        String lastNameInput = etLastName.getText().toString().trim();

        String emailOutput = emailInput.substring(0,1).toUpperCase() + emailInput.substring(1);
        String usernameOutput = usernameInput.substring(0,1).toUpperCase() + usernameInput.substring(1);
        String firstNameOutput = firstNameInput.substring(0,1).toUpperCase() + firstNameInput.substring(1);
        String lastNameOutput = lastNameInput.substring(0,1).toUpperCase() + lastNameInput.substring(1);

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            T.showLong(this, "Invalid Email");
        } else if(usernameOutput.equalsIgnoreCase("") || password.equalsIgnoreCase("") || confirmPassword.equalsIgnoreCase("") ||
                firstNameOutput.equalsIgnoreCase("") || lastNameOutput.equalsIgnoreCase(""))
            T.showLong(this, "One or more fields are missing");
        else if(!password.equalsIgnoreCase(confirmPassword)) {
            T.showLong(this, "Passwords do not match");
        } else if(password.length() < 5) {
            T.showLong(this, "Password needs to be at least 5 characters long");
        } else if(firstNameInput.length() <=1) {
            T.showShort(this, "First name needs to be more than 1 ");
        } else {
            new CreateAccountTask().execute(emailOutput, usernameOutput, password, firstNameOutput, lastNameOutput);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private class CreateAccountTask extends AsyncTask<String, Void, Void> {

        String email, username, password, firstName, lastName;

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
            firstName = params[3];
            lastName = params[4];

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

                                                if (users.size() > 0) {
                                                    T.showLong(getApplicationContext(), "Username is already taken");
                                                } else {
                                                    User newUser = new User(email, username, password, firstName, lastName);
                                                    newUser.create(getApplicationContext(), new Response.Listener<CreationResponse>() {
                                                        @Override
                                                        public void onResponse(CreationResponse creationResponse) {
                                                            T.showLong(getApplicationContext(), "Account created successfully");
                                                            final String userID = creationResponse.getObjectId();

                                                            UserObjectIDs userObjectIDs = new UserObjectIDs(userID);
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
