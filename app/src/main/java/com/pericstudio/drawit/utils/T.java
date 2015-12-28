package com.pericstudio.drawit.utils;

import android.content.Context;
import android.widget.Toast;

public class T {
    private static final boolean isDebug = true;

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongDebug(Context context, String message) {
        if(isDebug)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showShortDebug(Context context, String message) {
        if(isDebug)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
