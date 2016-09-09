package com.pericstudio.drawit.utils;

import android.content.Context;
import android.widget.Toast;

public class T {
    private static final boolean isDebug = true;

    /**
     * Quick way to show toast messages.
     * @param context The application's context
     * @param message The message for the toast
     */
    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Quick way to show toast messages.
     * @param context The application's context
     * @param message The message for the toast
     */
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Quick way to show toast messages. Debug only.
     * @param context The application's context
     * @param message The message for the toast
     */
    public static void showLongDebug(Context context, String message) {
        if(isDebug)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Quick way to show toast messages. Debug only.
     * @param context The application's context
     * @param message The message for the toast
     */
    public static void showShortDebug(Context context, String message) {
        if(isDebug)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
