package com.baxance.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;
import androidx.room.Room;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.securitytoken.model.Tag;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.StringJoiner;

public class MainActivity extends AppCompatActivity implements ViewAdapter.TaskListener{

    AnalyticsEvent event = AnalyticsEvent.builder()
            .name("Home Intent Event")
            .addProperty("Intents", 1)
            .build();

    public ArrayList<TaskTwo> tasks = new ArrayList<>();
    Handler mainThreadHandler;
    RecyclerView taskRV;

    public static String TAG = "main activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerFirebase();

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
            Amplify.configure(getApplicationContext());
            Log.i("amplify.app","success");
        } catch (AmplifyException e) {
            e.printStackTrace();
        }

        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );



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
                Amplify.Analytics.recordEvent(event);
                startActivity(goToAddTaskIntent);
            }
        });

        Button viewTasks = findViewById(R.id.viewTaskButton);
        viewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v){
                Intent goToViewTasksIntent = new Intent(MainActivity.this, ViewTask.class);
                Amplify.Analytics.recordEvent(event);
                startActivity(goToViewTasksIntent);
            }
        });

        Button settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent goToViewTasksIntent = new Intent(MainActivity.this, Settings.class);
                Amplify.Analytics.recordEvent(event);
                startActivity(goToViewTasksIntent);
            }
        });

        ((Button) findViewById(R.id.signupPageButton)).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CognitoSignupActivity.class);
            Amplify.Analytics.recordEvent(event);
            startActivity(intent);
        });

        ((Button) findViewById(R.id.loginPageButton)).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CognitoLoginActivity.class);
            Amplify.Analytics.recordEvent(event);
            startActivity(intent);
        });

        ((Button) findViewById(R.id.logoutButton)).setOnClickListener(v -> {
            Amplify.Auth.signOut(
                    () -> Log.i("Auth", "sign out success"),
                    error -> Log.e("Auth", error.toString())
            );
            finish();
            Amplify.Analytics.recordEvent(event);
            startActivity(getIntent());
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("in the onresume");
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            String username = authUser.getUsername();
            ((TextView) findViewById(R.id.usernameText)).setText(username);
        }
    }

    @Override
    public void tListener(TaskTwo task) {
        String taskTitle = task.getTitle();
        String taskBody = task.getBody();
        String taskState = task.getState();
        String taskImageKey = task.getKey();
        String taskAddress = task.getAddress();
//        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDatabase").allowMainThreadQueries().build();
//        ArrayList<Task> tasks = (ArrayList<Task>)taskDatabase.taskDao().getTask(taskTitle);
//        Log.i("task from DB", "task from DB on click = " + tasks);
        Intent viewTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
        viewTaskDetail.putExtra("taskTitle", taskTitle);
        viewTaskDetail.putExtra("taskBody", taskBody);
        viewTaskDetail.putExtra("taskState", taskState);
        viewTaskDetail.putExtra("taskImageKey", taskImageKey);
        viewTaskDetail.putExtra("taskAddress", taskAddress);
        Log.i("title", "task TITLE = " + taskTitle);
        Log.i("body", "task BODY = " + taskBody);
        Log.i("state", "task STATE = " + taskState);
        Log.i("address", "task ADDRESS = " + taskAddress);
        Amplify.Analytics.recordEvent(event);
        startActivity(viewTaskDetail);
        Log.i("task listener", "here " + task.getBody());
    }

    void registerFirebase(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG,"Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.i("firebase", token);
//                        String message = getString(R.string., token);
//                        Log.d(TAG, message);
                        Toast.makeText(getApplicationContext(), "POPUP MESSAGE FOR LAB 38", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}