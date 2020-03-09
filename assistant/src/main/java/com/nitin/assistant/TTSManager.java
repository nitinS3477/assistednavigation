package com.nitin.assistant;

import android.app.Activity;

import com.mapzen.speakerbox.Speakerbox;

import androidx.annotation.NonNull;

public class TTSManager {

    private static TTSManager singletonObject = null;

    private Speakerbox speakerbox;

    private TTSManager()
    {

    }


    @NonNull
    static TTSManager getInstance() {

        if (singletonObject == null) {
            singletonObject = new TTSManager();
        }
        return singletonObject;
    }

    public void init(Activity activity) {
        speakerbox = new Speakerbox(activity.getApplication());
    }

    public void play(String url) {
        speakerbox.play(url);

    }

    public void stop() {
        speakerbox.stop();
        speakerbox.shutdown();
    }


}

