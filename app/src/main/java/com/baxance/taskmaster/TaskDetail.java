package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {
    private static final String TAG = "taskdetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        if (intent.getStringExtra("task") == null){
            Log.i(TAG, "no associated task");
        } else {
            String name = intent.getStringExtra("task");
            Log.i(TAG, "onCreate: " + name);
            ((TextView)findViewById(R.id.taskName)).setText(name);
        }

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(TaskDetail.this, MainActivity.class);
            startActivity(goToAddTaskIntent);
        });
    }
}