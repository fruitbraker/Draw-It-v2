package com.pericstudio.drawit;

import android.app.Application;

public class MyApplication extends Application {

    public static final String CM_APP_ID = "90aaa080f39d481b9ee84dedf1d53f87";
    public static final String CM_API_KEY = "7910de1abb7846e0a796641c8372422b";

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
