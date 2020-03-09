package com.nitin.demo;

import androidx.appcompat.app.AppCompatActivity;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.FullscreenPromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.nitin.assistant.CustomAssistant;

public class ActivityCard extends AppCompatActivity {

    View banking, business;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Internet Saathi");

        banking = findViewById(R.id.tvBanking);
        business = findViewById(R.id.tvBusiness);

        banking.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCard.this,ActivityDetails.class);
            intent.putExtra("name",getResources().getString(R.string.textBanking));
            startActivity(intent);
        });

        business.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCard.this,ActivityDetails.class);
            intent.putExtra("name",getResources().getString(R.string.textBusiness));
            startActivity(intent);
        });



    }
}
