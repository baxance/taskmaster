package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("username", "Guest");

        TextView usernameSet = findViewById(R.id.usernameText);
        usernameSet.setText(username + " tasks");

        Button topTile = findViewById(R.id.vacuumButton);
        topTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String taskName = ((TextView)findViewById(R.id.vacuumButton)).getText().toString();
                Intent viewTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
                viewTaskDetail.putExtra("task", taskName);
                startActivity(viewTaskDetail);
            }
        });

        Button midtile = findViewById(R.id.dishesButton);
        midtile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String taskName = ((TextView)findViewById(R.id.dishesButton)).getText().toString();
                Intent viewTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
                viewTaskDetail.putExtra("task", taskName);
                startActivity(viewTaskDetail);
            }
        });

        Button lowTile = findViewById(R.id.catsButton);
        lowTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String taskName = ((TextView)findViewById(R.id.catsButton)).getText().toString();
                Intent viewTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
                viewTaskDetail.putExtra("task", taskName);
                startActivity(viewTaskDetail);
            }
        });

        Button addTask = findViewById(R.id.addTaskButton);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
            Intent goToAddTaskIntent = new Intent(MainActivity.this, ViewTask.class);
            startActivity(goToAddTaskIntent);
        }
        });

        Button viewTasks = findViewById(R.id.viewTaskButton);
        viewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v){
                Intent goToViewTasksIntent = new Intent(MainActivity.this, AddTask.class);
                startActivity(goToViewTasksIntent);
            }
        });

        Button settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goToViewTasksIntent = new Intent(MainActivity.this, Settings.class);
                startActivity(goToViewTasksIntent);
            }
        });
    }

}