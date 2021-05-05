package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.datastore.generated.model.TaskTwo;

import java.util.ArrayList;

public class TaskDetail extends AppCompatActivity {
    private static final String TAG = "taskdetail";

    public ArrayList<TaskTwo> taskList = new ArrayList<>();
    RecyclerView taskRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
//        TaskDatabase taskDatabase;
//
//        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDatabase").allowMainThreadQueries().build();


        Intent intent = getIntent();
        if (intent.getStringExtra("taskTitle") == null){
            Log.i(TAG, "no associated task");
        } else {
            String title = intent.getStringExtra("taskTitle");
            String body = intent.getStringExtra("taskBody");
            String state = intent.getStringExtra("taskState");
            Log.i(TAG, "onCreate: " + title);
            ((TextView)findViewById(R.id.taskTitle)).setText(title);
            ((TextView)findViewById(R.id.taskBody)).setText(body);
            ((TextView)findViewById(R.id.taskState)).setText(state);
        }

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(TaskDetail.this, MainActivity.class);
            startActivity(goToAddTaskIntent);
        });
    }
}