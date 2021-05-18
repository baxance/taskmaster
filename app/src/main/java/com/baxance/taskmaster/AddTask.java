package com.baxance.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Database;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
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

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTwo;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringJoiner;

public class AddTask extends AppCompatActivity {

    static int GET_IMAGE_CODE = 100;
    String key;
    File fileToUpload;

    FusedLocationProviderClient fusedLocationClient;
    Geocoder geocoder;

    public ArrayList<Team> teams = new ArrayList<>();
    Handler mainThreadHandler;

    List<Address> addresses;

    AnalyticsEvent event = AnalyticsEvent.builder()
            .name("Add Task Event")
            .addProperty("Intents", 1)
            .build();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        intentFilterData();

        requestLocationPermissions();
        loadLocationProviderClientAndGeocoder();
        getCurrentLocation();
        locationUpdates();

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//
//                        }
//                    }
//                });

        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(AddTask.this, MainActivity.class);
            Amplify.Analytics.recordEvent(event);
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

            saveFileToS3(fileToUpload);

            TaskTwo taskTwo = TaskTwo.builder()
                    .title(textTitle)
                    .team(teamId)
                    .body(textBody)
                    .state(textState)
                    .key(key)
                    .address(addresses.get(0).getAddressLine(0))
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
            Amplify.Analytics.recordEvent(event);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{".jpg", ".png", ".gif"});
                startActivityForResult(intent, GET_IMAGE_CODE);

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE_CODE){
            fileToUpload = new File(getApplicationContext().getFilesDir(), "uploading");
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(inputStream, new FileOutputStream(fileToUpload));

                ImageView imageView = findViewById(R.id.imageViewS3);
                imageView.setImageBitmap(BitmapFactory.decodeFile(fileToUpload.getPath()));

                System.out.println("within the activity S3 result");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void saveFileToS3(File file){
            Random random = new Random();

            Amplify.Storage.uploadFile(
                    key = random.toString(),
                    file,
                    result -> Log.i("amplifyapp", "upload success: " + result.getKey()),
                    storageFailure -> Log.e("amplifyapp", "upload failed", storageFailure)
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

    void intentFilterData(){
        Intent intent = getIntent();
        if (intent.getType() != null) {
            if (intent.getType().startsWith("image/")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    loadImageFromIntent(uri);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void loadImageFromIntent(Uri uri){
        fileToUpload = new File(getApplicationContext().getFilesDir(), "tempFile");
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileUtils.copy(inputStream, new FileOutputStream(fileToUpload));

            ImageView imageView = findViewById(R.id.imageViewS3);
            imageView.setImageBitmap(BitmapFactory.decodeFile(fileToUpload.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void requestLocationPermissions(){
        requestPermissions(
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                1
        );
    }

    void loadLocationProviderClientAndGeocoder(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    }

    void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("get current location", "permission not granted");
            return;
        }
        fusedLocationClient.flushLocations();
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @NotNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull @NotNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        })
                .addOnCompleteListener(
                        data -> {
                            Log.i("location", "on complete: " + data.toString());
                        }
                )
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        Log.i("location", "on success: " + location.toString());
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                            Log.i("Address", "current address: " + addresses.toString());
                            String streetAddress = addresses.get(0).getAddressLine(0);
                            Log.i("Address", "current location: " + streetAddress);
                        } catch (IOException e) {
                            Log.e("address", "get current location: failed");
                            e.printStackTrace();
                        }
                    }
                })
                .addOnCanceledListener(() -> Log.i("fldkirughb", "onCanceled: canceled"))
                .addOnFailureListener(error -> Log.i("fldkirughb", "onFailure: " + error.toString()));
    }

    void locationUpdates(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                try {
                    String address = geocoder.getFromLocation(
                            locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude(),
                            1
                    ).get(0).getAddressLine(0);
                    Log.i("updates", "location resules subscribed: " + address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }
}