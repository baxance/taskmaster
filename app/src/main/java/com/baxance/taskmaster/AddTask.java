package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTask extends AppCompatActivity {

    TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDatabase").allowMainThreadQueries().build();

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

            Task task = new Task(textTitle.getText().toString(), textBody.getText().toString(), textState.getText().toString());
            taskDatabase.taskDao().insert(task);

            Log.i("title", "title= " + textTitle.getText().toString());
            Log.i("body", "body= " + textBody.getText().toString());
            Log.i("state", "state= " + textState.getText().toString());
            submitText.setText("submitted!");
        });
    }
}