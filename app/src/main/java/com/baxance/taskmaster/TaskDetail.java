package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;

import java.io.File;
import java.util.ArrayList;

public class TaskDetail extends AppCompatActivity {
    private static final String TAG = "taskdetail";

    static int GET_IMAGE_CODE = 100;
    File fileToUpload;

    public ArrayList<TaskTwo> taskList = new ArrayList<>();
    RecyclerView taskRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

//        downloadFileFromS3(Amplify.API.query(
//                ModelQuery.list(TaskTwo.class, TaskTwo.KEY.contains())
//        ));
//        TaskDatabase taskDatabase;
//        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDatabase").allowMainThreadQueries().build();

        Intent intent = getIntent();
        if (intent.getStringExtra("taskTitle") == null){
            Log.i(TAG, "no associated task");
        } else {
            String title = intent.getStringExtra("taskTitle");
            String body = intent.getStringExtra("taskBody");
            String state = intent.getStringExtra("taskState");
            String imageKey = intent.getStringExtra("taskImageKey");
            String address = intent.getStringExtra("taskAddress");
            Log.i(TAG, "onCreate: " + title);
            ((TextView)findViewById(R.id.taskTitle)).setText(title);
            ((TextView)findViewById(R.id.taskBody)).setText(body);
            ((TextView)findViewById(R.id.taskState)).setText(state);
            ((TextView)findViewById(R.id.taskAddress)).setText(address);
//            ((ImageView)findViewById(R.id.taskImageViewS3)).setImageBitmap();
            Amplify.Storage.downloadFile(
                    imageKey,
                    new File(getApplicationContext().getFilesDir(), imageKey),
                    r -> {
                        ImageView imageView = findViewById(R.id.taskImageViewS3);
                        imageView.setImageBitmap(BitmapFactory.decodeFile(r.getFile().getPath()));
                    },
                    r -> {});
        }

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(TaskDetail.this, MainActivity.class);
            AnalyticsEvent event = AnalyticsEvent.builder()
                    .name("add task intent Event")
                    .addProperty("Intents", 1)
                    .build();
            Amplify.Analytics.recordEvent(event);
            startActivity(goToAddTaskIntent);
        });
    }

//    void downloadFileFromS3(String key){
//        Amplify.Storage.downloadFile(
//                key,
//                new File(getApplicationContext().getFilesDir(), key),
//                r -> {
//                    ImageView imageView = findViewById(R.id.taskImageViewS3);
//                    imageView.setImageBitmap(BitmapFactory.decodeFile(r.getFile().getPath()));
//                },
//                r -> {});
//    }

//    @Override
//    public void tListener(TaskTwo task) {
//        String taskTitle = task.getTitle();
//        String taskBody = task.getBody();
//        String taskState = task.getState();
////        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDatabase").allowMainThreadQueries().build();
////        ArrayList<Task> tasks = (ArrayList<Task>)taskDatabase.taskDao().getTask(taskTitle);
////        Log.i("task from DB", "task from DB on click = " + tasks);
//        Intent viewTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
//        viewTaskDetail.putExtra("taskTitle", taskTitle);
//        viewTaskDetail.putExtra("taskBody", taskBody);
//        viewTaskDetail.putExtra("taskState", taskState);
//        Log.i("title", "task TITLE = " + taskTitle);
//        Log.i("body", "task BODY = " + taskBody);
//        Log.i("state", "task STATE = " + taskState);
//        startActivity(viewTaskDetail);
//        Log.i("task listener", "here " + task.getBody());
//    }

//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GET_IMAGE_CODE){
//            fileToUpload = new File(getApplicationContext().getFilesDir(), "uploading");
//            try {
//                InputStream inputStream = getContentResolver().openInputStream(data.getData());
//                FileUtils.copy(inputStream, new FileOutputStream(fileToUpload));
//
//                ImageView imageView = findViewById(R.id.taskImageViewS3);
//                imageView.setImageBitmap(BitmapFactory.decodeFile(fileToUpload.getPath()));
//
//                System.out.println("within the activity S3 result");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}