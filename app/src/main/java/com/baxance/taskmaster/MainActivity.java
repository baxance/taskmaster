package com.baxance.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.services.securitytoken.model.Tag;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;
import com.amplifyframework.datastore.generated.model.Team;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.StringJoiner;

public class MainActivity extends AppCompatActivity implements ViewAdapter.TaskListener{

    public ArrayList<TaskTwo> tasks = new ArrayList<>();
    Handler mainThreadHandler;
    RecyclerView taskRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("amplify.app","success");
        } catch (AmplifyException e) {
            Log.e("amplify.app", "error " + e);
        }

        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("username", "Guest");
        String userTeam = preferences.getString("userTeam", "");

        TextView usernameSet = findViewById(R.id.usernameText);
        usernameSet.setText(username + " tasks with " + userTeam);

        //////////////////////RECYCLER VIEW/////////////////////////////
        taskRV = findViewById(R.id.recyclerView);
        taskRV.setAdapter(new ViewAdapter(tasks, this));
        taskRV.setLayoutManager(new LinearLayoutManager(this));
        ////////////////////////////////////////////////////////////////

        mainThreadHandler = new Handler(this.getMainLooper()){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.i("HANDLE messsage", "hit second handler");
                if (msg.what == 1){
                    StringJoiner stringJoiner = new StringJoiner(", ");
                    for (TaskTwo task : tasks){
                        stringJoiner.add(task.getTitle());
                    }
//                    ((TextView) findViewById(R.id.recyclerView)).setText(stringJoiner.toString());
                    taskRV.getAdapter().notifyDataSetChanged();
                }
            }
        };

        Amplify.API.query(
                ModelQuery.list(TaskTwo.class),
                response -> {
                    if (response.getData() != null) {
                        for (TaskTwo task : response.getData()) {
                            Log.e("cast list", "SOMETHING SLKHJJFBSLIUFHB " + task.getTitle());
                            tasks.add(task);
                        }
                    }
                    mainThreadHandler.sendEmptyMessage(1);
                },
                response -> Log.i("retrieving tasks", "retrieved task = " + response.toString())
        );

        ///////////BUTTONS///////////////////

        Button addTask = findViewById(R.id.addTaskButton);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent goToAddTaskIntent = new Intent(MainActivity.this, AddTask.class);
                startActivity(goToAddTaskIntent);
            }
        });

        Button viewTasks = findViewById(R.id.viewTaskButton);
        viewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v){
                Intent goToViewTasksIntent = new Intent(MainActivity.this, ViewTask.class);
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
    public void tListener(TaskTwo task) {
        String taskTitle = task.getTitle();
        String taskBody = task.getBody();
        String taskState = task.getState();
//        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDatabase").allowMainThreadQueries().build();
//        ArrayList<Task> tasks = (ArrayList<Task>)taskDatabase.taskDao().getTask(taskTitle);
//        Log.i("task from DB", "task from DB on click = " + tasks);
        Intent viewTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
        viewTaskDetail.putExtra("taskTitle", taskTitle);
        viewTaskDetail.putExtra("taskBody", taskBody);
        viewTaskDetail.putExtra("taskState", taskState);
        Log.i("title", "task TITLE = " + taskTitle);
        Log.i("body", "task BODY = " + taskBody);
        Log.i("state", "task STATE = " + taskState);
        startActivity(viewTaskDetail);
        Log.i("task listener", "here " + task.getBody());
    }

}