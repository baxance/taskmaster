package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTask = findViewById(R.id.addTaskButton);
        addTask.setOnClickListener(view -> {
            Intent goToAddTaskInteny = new Intent(MainActivity.this, AddTask.class);
            startActivity(goToAddTaskInteny);
        });

        Button viewTasks = findViewById(R.id.viewTaskButton);
        addTask.setOnClickListener(v -> {
            Intent goToViewTasksIntent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToViewTasksIntent);
        });
    }

}