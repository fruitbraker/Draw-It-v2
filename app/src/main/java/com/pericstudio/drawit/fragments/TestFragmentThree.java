package com.pericstudio.drawit.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.activities.LoginActivity;

public class TestFragmentThree extends Fragment {

    private TextView tvTest;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    public TestFragmentThree() {

    }

    public static TestFragmentThree newInstance(String params) {
        TestFragmentThree testFragmentThree = new TestFragmentThree();
        Bundle args = new Bundle();
        args.putString("number", params);
        testFragmentThree.setArguments(args);
        return testFragmentThree;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_test_three, container, false);
        tvTest = (TextView) layout.findViewById(R.id.tv_test_fragment_three);
        Bundle args = getArguments();
        tvTest.setText(args.getString("number"));
        loginButton = (LoginButton) layout.findViewById(R.id.login_button_fb);
        listenButton();
        return layout;
    }

    private void listenButton() {
        callbackManager = CallbackManager.Factory.create();
//        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                final Dialog dialog = ProgressDialog.show(MyApplication.getContext(), "Loading", "Please wait...");
//                dialog.show();
//
//                final String userID = loginResult.getAccessToken().getUserId();
//                SharedPreferences mSharedPreferences = MyApplication.getContext().getSharedPreferences(MyApplication.SHAREDPREF_TAG, Context.MODE_PRIVATE);
//                final SharedPreferences.Editor editor = mSharedPreferences.edit();
//
//                //Checks if the user has logged in through Facebook already. If not, userID is created
//                startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if(currentAccessToken == null){
                    //User logged out
                    SharedPreferences mSharedPreferences = MyApplication.getContext().getSharedPreferences(MyApplication.SHAREDPREF_TAG, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(MyApplication.SHAREDPREF_USERID, "");
                    editor.apply();
                    MyApplication.wasIntent = true;
                    startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
                }
            }
        };
    }

}
