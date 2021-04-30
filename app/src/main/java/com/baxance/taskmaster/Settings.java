package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        String username = preferences.getString("username", "Guest");

        ((TextView)findViewById(R.id.submitText)).setText(username);

        findViewById(R.id.submitUsernameButton).setOnClickListener(view -> {
            String setUsername = ((TextView)findViewById(R.id.editTextTextPersonName)).getText().toString();

            preferencesEditor.putString("username", setUsername);
            preferencesEditor.apply();

            ((TextView)findViewById(R.id.submitText)).setText(setUsername + " logged in");

        });

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(Settings.this, MainActivity.class);
            startActivity(goToAddTaskIntent);
        });

//        Button addTaskButton = findViewById(R.id.submitUsernameButton);
//        addTaskButton.setOnClickListener(view -> {
//            TextView submitText = findViewById(R.id.submitText);
//            submitText.setText("Username Submitted!");
//        });
    }
}