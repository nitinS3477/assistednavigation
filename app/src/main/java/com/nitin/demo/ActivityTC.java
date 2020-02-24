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
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nitin.assistant.CustomAssistant;

public class ActivityTC extends AppCompatActivity {

    TextView tvProceed;
    CheckBox cBTerms;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        linearLayout = findViewById(R.id.tvTermsConditions);
        tvProceed = findViewById(R.id.tvProceed);
        cBTerms = findViewById(R.id.cbTerms);

        linearLayout.setOnClickListener(v -> {
            cBTerms.setChecked(true);

        });





        tvProceed.setOnClickListener(v ->
                startActivity(new Intent(
                        ActivityTC.this, ActivityCard.class
                )));




    }
}
