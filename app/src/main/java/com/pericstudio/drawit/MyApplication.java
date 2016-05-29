package com.pericstudio.drawit;

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

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.cloudmine.api.persistance.ClassNameRegistry;
import com.pericstudio.drawit.pojo.Drawing;
import com.pericstudio.drawit.pojo.UserObjectIDs;
import com.pericstudio.drawit.utils.T;

public class MyApplication extends Application {

    public static final int TICK_THRESHOLD = 5;
    public static final String SHAREDPREF_TAG = "DrawIt";
    public static final String SHAREDPREF_USERID = "UserID";

    public static String userID = "";
    public static boolean wasIntent = false;

    private static Music mMusic;
    private static MediaPlayer mMediaPlayer;

    private static UserObjectIDs userData;

    private static Context context;

    static {
        ClassNameRegistry.register(Drawing.CLASS_NAME, Drawing.class);
        ClassNameRegistry.register(UserObjectIDs.CLASS_NAME, UserObjectIDs.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    /**
     * Change the background music.
     * @param musicId The music id from the res folder.
     */
    public static void changeMusic(int musicId) {
        if(mMusic == null) {
            mMusic = new Music(musicId);
            mMusic.execute();
        } else {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMusic = new Music(musicId);
            mMusic.execute();
        }
    }

    /**
     * Sets the UserObjectID object for easy access and use.
     * @param obj the UserObjectId obtained from logging in
     */
    public static void setUserDataObject(UserObjectIDs obj) {
        userData = obj;
    }

    /**
     * Gets the UserObjectID
     * @return The UserObjectID object
     */
    public static UserObjectIDs getUserData() {
        return userData;
    }

    /**
     * Pauses the music
     */
    public static void onPauseMusic() {
        mMediaPlayer.pause();
    }

    /**
     * Resumes the music
     */
    public static void onResumeMusic() {
        mMediaPlayer.start();
    }

    /**
     * Stops the music
     */
    public static void onStopMusic() {
        T.showLongDebug(MyApplication.context, "" + mMusic.isCancelled());
    }

    /**
     * Gets the context of the application
     * @return The context
     */
    public static Context getContext() {
        return MyApplication.context;
    }

    /**
     * Sets the userID
     * @param id the user's id
     */
    public static void setUserID(String id) {
        userID = id;
    }

    /**
     * Handles the change of music asynchronously.
     * Probably not necessary.
     */
    private static class Music extends AsyncTask<Void, Void, Void> {

        int musicId;

        public Music(int musicId) {
            this.musicId = musicId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMediaPlayer = MediaPlayer.create(getContext(), musicId);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            mMusic.cancel(true);
            return null;
        }

    }

}


