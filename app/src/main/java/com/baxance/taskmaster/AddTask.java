package com.baxance.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringJoiner;

public class AddTask extends AppCompatActivity {

    public ArrayList<Team> teams = new ArrayList<>();
    Handler mainThreadHandler;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(AddTask.this, MainActivity.class);
            startActivity(goToAddTaskIntent);
        });

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    Log.i("responsefromdb", "SOMETHIGUIDXFBG KLXJDHVBF " + response);
                    if (response.getData() != null) {
                        for (Team team : response.getData()) {
                            Log.e("castteam", "GETTING TEAM FROM DB: " + team.getName());
                            teams.add(team);
                        }
                    }
//                    mainThreadHandler.sendEmptyMessage(1);
                },
                response -> Log.i("retrieving Teams", "retrieved team: " + response.toString())
        );

//        RadioButton redTeamButton = findViewById(R.id.redTeam);
//        RadioButton greenTeamButton = findViewById(R.id.greenTeam);
//        RadioButton blueTeamButton = findViewById(R.id.blueTeam);

//        RadioGroup teamSelect = findViewById(R.id.teamSelection);
//        if (teamSelect.getCheckedRadioButtonId() == R.id.redTeam) { // selected team = red team
//            String redTeamId = teams.get(1).getId();
//        }
//        if (teamSelect.getCheckedRadioButtonId() == R.id.greenTeam) {
//            String greenTeamId = teams.get(0).getId();
//        }
//        if (teamSelect.getCheckedRadioButtonId() == R.id.blueTeam) {
//            String blueTeamId = teams.get(2).getId();
//        }

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
        TextView submitText = findViewById(R.id.submit);

            String textTitle = ((EditText) findViewById(R.id.editTextTaskTitle)).getText().toString();
            String textBody = ((EditText) findViewById(R.id.editTextTaskBody)).getText().toString();
            String textState = ((EditText) findViewById(R.id.editTextTaskState)).getText().toString();

            RadioGroup teamSelect = findViewById(R.id.teamSelection);
            Team teamId = null;
            if (teamSelect.getCheckedRadioButtonId() == R.id.redTeam) { // selected team = red team
                teamId = teams.get(1);
            }
            if (teamSelect.getCheckedRadioButtonId() == R.id.greenTeam) {
                teamId = teams.get(0);
            }
            if (teamSelect.getCheckedRadioButtonId() == R.id.blueTeam) {
                teamId = teams.get(2);
            }
            Log.e("team", "team selected = " + teamId);

            TaskTwo taskTwo = TaskTwo.builder()
                    .title(textTitle)
                    .team(teamId)
                    .body(textBody)
                    .state(textState)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(taskTwo),
                    response -> Log.i("mutate", "success"),
                    error -> Log.e("mutate", "error " + error)
            );

            submitText.setText("submitted!");
        });
    }
}