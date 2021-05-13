package com.baxance.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringJoiner;

public class AddTask extends AppCompatActivity {

    static int FILE_UPLOAD_REQUEST_CODE = 123;
    static int GET_IMAGE_CODE = 100;
    File fileToUpload;

    public ArrayList<Team> teams = new ArrayList<>();
    Handler mainThreadHandler;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(AddTask.this, MainActivity.class);
            startActivity(goToAddTaskIntent);
        });

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    Log.i("responsefromdb", "SOMETHIGUIDXFBG KLXJDHVBF " + response);
                    if (response.getData() != null) {
                        for (Team team : response.getData()) {
                            Log.e("castteam", "GETTING TEAM FROM DB: " + team.getName());
                            teams.add(team);
                        }
                    }
//                    mainThreadHandler.sendEmptyMessage(1);
                },
                response -> Log.i("retrieving Teams", "retrieved team: " + response.toString())
        );

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
        TextView submitText = findViewById(R.id.submit);

            String textTitle = ((EditText) findViewById(R.id.editTextTaskTitle)).getText().toString();
            String textBody = ((EditText) findViewById(R.id.editTextTaskBody)).getText().toString();
            String textState = ((EditText) findViewById(R.id.editTextTaskState)).getText().toString();

            RadioGroup teamSelect = findViewById(R.id.teamSelection);
            Team teamId = null;
            if (teamSelect.getCheckedRadioButtonId() == R.id.redTeam) { // selected team = red team
                teamId = teams.get(1);
            }
            if (teamSelect.getCheckedRadioButtonId() == R.id.greenTeam) {
                teamId = teams.get(0);
            }
            if (teamSelect.getCheckedRadioButtonId() == R.id.blueTeam) {
                teamId = teams.get(2);
            }
            Log.e("team", "team selected = " + teamId);

            TaskTwo taskTwo = TaskTwo.builder()
                    .title(textTitle)
                    .team(teamId)
                    .body(textBody)
                    .state(textState)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(taskTwo),
                    response -> Log.i("mutate", "success"),
                    error -> Log.e("mutate", "error " + error)
            );

            submitText.setText("submitted!");
        });

        ((Button) findViewById(R.id.selectImageButton)).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{".jpg", ".png", ".gif"});
                startActivityForResult(intent, GET_IMAGE_CODE);
        });

        ((Button) findViewById(R.id.))

    }

    void uploadFile(){
        File file = new File(getApplicationContext().getFilesDir(), "Key?");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append("content example");
            writer.close();

            Amplify.Storage.uploadFile(
                    "Key?",
                    file,
                    result -> Log.i("amplifyapp", "upload success: " + result.getKey()),
                    storageFailure -> Log.e("amplifyapp", "upload failed", storageFailure)
            );
        } catch (IOException e) {
            Log.e("amplifyapp", "upload failed", e);
            e.printStackTrace();
        }

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{".jpg", ".png", ".gif"});
//        startActivityForResult(intent, FILE_UPLOAD_REQUEST_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_UPLOAD_REQUEST_CODE){
            File file = new File(getApplicationContext().getFilesDir(), "uploading file");
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(inputStream, new FileOutputStream(file));
                saveFileToS3(file, "filename");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void saveFileToS3(File file, String filename){
        Amplify.Storage.uploadFile(
                filename,
                file,
                r -> {},
                r -> {}
        );
    }

    void downloadFileFromS3(String key){
        Amplify.Storage.downloadFile(
                key,
                new File(getApplicationContext().getFilesDir(), key),
                r -> {
                    ImageView imageView = findViewById(R.id.imageViewS3);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(r.getFile().getPath()));
                },
                r -> {});
                }

}