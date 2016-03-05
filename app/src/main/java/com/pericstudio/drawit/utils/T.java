package com.pericstudio.drawit.utils;

/*
 * Copyright 2016 Eric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
