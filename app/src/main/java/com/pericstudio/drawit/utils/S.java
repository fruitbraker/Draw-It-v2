package com.pericstudio.drawit.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class S {

    public static void showSnackLong(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackShort(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

}
