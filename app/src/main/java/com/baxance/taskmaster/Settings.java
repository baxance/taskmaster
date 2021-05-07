package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.datastore.generated.model.Team;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        String username = preferences.getString("username", "Guest");
//        String userTeam = preferences.getString("userTeam", "none");

        ((TextView)findViewById(R.id.submitText)).setText(username);

//        RadioGroup teamSelect = findViewById(R.id.teamSelection);
//        String teamName = "";
//        if (teamSelect.getCheckedRadioButtonId() == R.id.redTeam) { // selected team = red team
//            teamName = "Red Team";
//        }
//        if (teamSelect.getCheckedRadioButtonId() == R.id.greenTeam) {
//            teamName = "Green Team";
//        }
//        if (teamSelect.getCheckedRadioButtonId() == R.id.blueTeam) {
//            teamName = "Blue Team";
//        }

//        String finalTeamName = teamName;
        findViewById(R.id.submitUsernameButton).setOnClickListener(view -> {
            String setUsername = ((TextView)findViewById(R.id.editTextTaskTitle)).getText().toString();
            String setTeamName = "";

            RadioGroup teamSelect = findViewById(R.id.teamSelection);
            if (teamSelect.getCheckedRadioButtonId() == R.id.redTeam) { // selected team = red team
                setTeamName = "Red Team";
            }
            if (teamSelect.getCheckedRadioButtonId() == R.id.greenTeam) {
                setTeamName = "Green Team";
            }
            if (teamSelect.getCheckedRadioButtonId() == R.id.blueTeam) {
                setTeamName = "Blue Team";
            }

            preferencesEditor.putString("username", setUsername);
            preferencesEditor.putString("userTeam", setTeamName);
            preferencesEditor.apply();

            ((TextView)findViewById(R.id.submitText)).setText(setUsername + " logged in with team " + setTeamName);

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