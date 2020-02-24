package com.nitin.demo;

import android.app.Application;

import com.nitin.assistant.CustomAssistant;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CustomAssistant.init(this, "sample");

    }


}
