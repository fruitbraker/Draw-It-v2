package com.pericstudio.drawit;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.pericstudio.drawit.utils.T;

public class MyApplication extends Application {

    public final static int DASHBOARD_MUSIC_TAG = R.raw.draw_it_dashboard;
    public final static int DRAWING_MUSIC_TAG = R.raw.draw_it_drawing;

    public static final String CM_APP_ID = "90aaa080f39d481b9ee84dedf1d53f87";
    public static final String CM_API_KEY = "7910de1abb7846e0a796641c8372422b";

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
