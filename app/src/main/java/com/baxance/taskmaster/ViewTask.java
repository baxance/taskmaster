package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;

public class ViewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(ViewTask.this, MainActivity.class);
            AnalyticsEvent event = AnalyticsEvent.builder()
                    .name("View Task Event")
                    .addProperty("Intents", 1)
                    .build();
            Amplify.Analytics.recordEvent(event);
            startActivity(goToAddTaskIntent);
        });
    }
}