package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewAdapter.TaskListener{

    public ArrayList<Task> taskList = new ArrayList<>();
    RecyclerView taskRV;

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

        Task task1 = new Task("title 1", "body 1", "STATE");
        Task task2 = new Task("title 2", "body 2", "STATE");
        Task task3 = new Task("title 3", "body 3", "STATE");

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        Button midTile = findViewById(R.id.dishesButton);
        midTile.setOnClickListener(new View.OnClickListener() {
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

        taskRV = findViewById(R.id.recyclerView);
        taskRV.setLayoutManager(new LinearLayoutManager(this));
        taskRV.setAdapter(new ViewAdapter(taskList, this));

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

    @Override
    public void tListener(Task task) {
        String taskTitle = task.getTitle();
        Intent viewTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
        viewTaskDetail.putExtra("task", taskTitle);
        startActivity(viewTaskDetail);



        Log.i("task listener", "here " + task.getBody());
    }

}