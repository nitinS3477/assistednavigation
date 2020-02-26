package com.nitin.demo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nitin.assistant.CustomAssistant;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Internet Saathi");
        }
        final TextView tvE = findViewById(R.id.tvEnglish);
        final TextView tvH = findViewById(R.id.tvHindi);

        String temp = "tvNext";
        TextView tvN = findViewById(getResources().getIdentifier(temp, "id", getPackageName()));


        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.ENGLISH);

                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
        });

//        tvE.setOnClickListener(view ->
//        {
//            start();
//        });

        tvN.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, ActivityTC.class)));


    }

    private void start() {
        textToSpeech.setLanguage(Locale.forLanguageTag("hi"));
        textToSpeech.speak("हिंदी भाषा का चयन के लिए यहाँ क्लिक करें", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:

                CustomAssistant.init(this.getApplication(), "sample2", "hi");
                CustomAssistant.guide(this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
