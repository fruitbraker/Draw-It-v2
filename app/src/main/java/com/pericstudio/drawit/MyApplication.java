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

import com.pericstudio.drawit.utils.T;

public class MyApplication extends Application {

    public static Music mMusic;
    private static MediaPlayer mMediaPlayer;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

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

    public static void onPauseMusic() {
        mMediaPlayer.pause();
    }

    public static void onResumeMusic() {
        mMediaPlayer.start();
    }

    public static void onStopMusic() {
        T.showLongDebug(MyApplication.context, "" + mMusic.isCancelled());
    }

    public static Context getContext() {
        return MyApplication.context;
    }

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
