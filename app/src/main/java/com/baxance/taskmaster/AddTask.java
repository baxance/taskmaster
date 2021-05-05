package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;

public class AddTask extends AppCompatActivity {

//    TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDatabase").allowMainThreadQueries().build();

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(AddTask.this, MainActivity.class);
            startActivity(goToAddTaskIntent);
        });

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
            TextView submitText = findViewById(R.id.submit);
            TextView textTitle = AddTask.this.findViewById(R.id.editTextTaskTitle);
            TextView textBody = AddTask.this.findViewById(R.id.editTextTaskBody);
            TextView textState = AddTask.this.findViewById(R.id.editTextTaskState);

//            TaskTwo task = new TaskTwo(textTitle.getText().toString(),
//                    textBody.getText().toString(),
//                    textState.getText().toString());
//            TaskTwo task = new TaskTwo()
//            taskDatabase.taskDao().insert(task);
////////////////////////////////////////////////////////////////////////////////////////////////////
            TaskTwo taskTwo = TaskTwo.builder()
                    .title(textTitle.toString())
                    .body(textBody.toString())
                    .state(textState.toString())
                    .build();

            Log.e("tasktwo", "tt body = " + taskTwo.getBody());

            Amplify.API.mutate(
                    ModelMutation.create(taskTwo),
                    response -> Log.i("mutate", "success"),
                    error -> Log.e("mutate", "error " + error)
            );
////////////////////////////////////////////////////////////////////////////////////////////////////
//            TaskTwo taskTwo = TaskTwo.builder()
//                    .title("test")
//                    .body("test description")
//                    .state("test state")
//                    .build();
//
//            Log.e("tasktwo", "tt body = " + taskTwo.getBody());
//
//            Amplify.API.mutate(
//                    ModelMutation.create(taskTwo),
//                    response -> Log.i("mutate", "success"),
//                    error -> Log.e("mutate", "error " + error)
//            );
////////////////////////////////////////////////////////////////////////////////////////////////////
            Log.i("title", "title= " + textTitle.getText().toString());
            Log.i("body", "body= " + textBody.getText().toString());
            Log.i("state", "state= " + textState.getText().toString());
            submitText.setText("submitted!");
        });
    }
}