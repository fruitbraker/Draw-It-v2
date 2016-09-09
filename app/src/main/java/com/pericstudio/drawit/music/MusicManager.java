package com.pericstudio.drawit.music;

import android.content.Context;
import android.media.MediaPlayer;

import com.pericstudio.drawit.R;


public class MusicManager {
    private static MusicManager musicManager;

    private final static String DASHBOARD_MUSIC_TAG = "DashboardActivityOld";
    private final static String DRAWING_MUSIC_TAG = "Drawing";

    private static MediaPlayer mediaPlayer = null;

    private boolean continuePlay = false;

    public void playMusic(String musicName, Context context) {
        if (mediaPlayer != null && !continuePlay) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (!continuePlay) {
            switch (musicName) {
                case DASHBOARD_MUSIC_TAG:
                    mediaPlayer = MediaPlayer.create(context, R.raw.draw_it_dashboard);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    break;
                case DRAWING_MUSIC_TAG:
                    mediaPlayer = MediaPlayer.create(context, R.raw.draw_it_drawing);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    break;
            }
        }

    }

    public void pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    public void setContinuePlay(boolean continuePlay) {
        this.continuePlay = continuePlay;
    }


    //    public static void release() {
//        if (mediaPlayer != null) {
//            if (mediaPlayer.isPlaying())
//                mediaPlayer.stop();
//        }
//        mediaPlayer.release();
//        mediaPlayer = null;
//    }

    public static MusicManager getMusicManager() {
        if (musicManager == null)
            musicManager = new MusicManager();
        return musicManager;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
