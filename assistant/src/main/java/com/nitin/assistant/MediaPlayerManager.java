package com.nitin.assistant;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.HandlerThread;
import android.os.Process;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;

import androidx.annotation.NonNull;

class MediaPlayerManager {

    private static MediaPlayerManager singletonObject = null;

    private MediaPlayer mediaPlayer;

    private MediaPlayerManager() {
        mediaPlayer = new MediaPlayer();
    }



    @NonNull
    static MediaPlayerManager getInstance() {
        if (singletonObject == null) {
            singletonObject = new MediaPlayerManager();
        }
        return singletonObject;
    }

    public void play(String url) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    public void play(@NotNull Activity activity, String resource) {

        mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), activity.getResources().getIdentifier(resource, "raw", activity.getPackageName()));
        if (mediaPlayer != null)
            mediaPlayer.start();
    }

    public void stop() {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        singletonObject = null;
    }

}
