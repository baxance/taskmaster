package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.StringJoiner;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(AddTask.this, MainActivity.class);
            startActivity(goToAddTaskIntent);
        });

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
            TextView submitText = findViewById(R.id.submit);
//            TextView textTitle = AddTask.this.findViewById(R.id.editTextTaskTitle);
//            TextView textBody = AddTask.this.findViewById(R.id.editTextTaskBody);
//            TextView textState = AddTask.this.findViewById(R.id.editTextTaskState);

            String textTitle = ((EditText) findViewById(R.id.editTextTaskTitle)).getText().toString();
            String textBody = ((EditText) findViewById(R.id.editTextTaskBody)).getText().toString();
            String textState = ((EditText) findViewById(R.id.editTextTaskState)).getText().toString();
            int textTeam = ((RadioGroup) findViewById(R.id.teamSelection)).getCheckedRadioButtonId();

            TaskTwo taskTwo = TaskTwo.builder()
                    .title(textTitle)
                    .team(textTeam)
                    .body(textBody)
                    .state(textState)
                    .build();

//            Log.e("tasktwo", "tt body = " + taskTwo.getBody());

            Amplify.API.mutate(
                    ModelMutation.create(taskTwo),
                    response -> Log.i("mutate", "success"),
                    error -> Log.e("mutate", "error " + error)
            );

//            Log.i("title", "title= " + textTitle);
//            Log.i("body", "body= " + textBody);
//            Log.i("state", "state= " + textState);
            submitText.setText("submitted!");
        });
    }
}