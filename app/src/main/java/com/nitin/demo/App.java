package com.nitin.demo;

import android.app.Application;

import com.nitin.assistant.CustomAssistant;

import java.util.Locale;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        String languageCode = Locale.getDefault().getLanguage();
        CustomAssistant.init(this, "sample", languageCode);


    }


}
