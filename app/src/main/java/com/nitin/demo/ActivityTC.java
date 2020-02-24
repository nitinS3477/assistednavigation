package com.nitin.demo;

import androidx.appcompat.app.AppCompatActivity;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.FullscreenPromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.nitin.assistant.CustomAssistant;

public class ActivityTC extends AppCompatActivity {

    TextView tvProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        tvProceed = findViewById(R.id.tvProceed);


        tvProceed.setOnClickListener(v ->
                startActivity(new Intent(
                        ActivityTC.this, ActivityCard.class
                )));




    }
}
