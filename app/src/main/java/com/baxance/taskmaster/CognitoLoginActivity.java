package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class CognitoLoginActivity extends AppCompatActivity {

    Handler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognito_login);

        loginHandler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message message){
                if (message.what == 1){
                    Log.i("login activity", "login success");
                    Toast.makeText(CognitoLoginActivity.this, "logged in", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CognitoLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (message.what == 2){
                    Log.i("login activity", "login fail");
                    Toast.makeText(CognitoLoginActivity.this, "login fail", Toast.LENGTH_LONG).show();
                }
            }
        };

        ((Button) findViewById(R.id.signupConfirmationButton)).setOnClickListener(v -> {
            String username = ((EditText) findViewById(R.id.editTextTextEmailAddressConfirmation)).getText().toString();
            String password = ((EditText) findViewById(R.id.editTextTextConfirmationCode)).getText().toString();
            Log.i("Sign in button action", "starting signin");
            Amplify.Auth.signIn(
                    username,
                    password,
                    r -> loginHandler.sendEmptyMessage(1),
                    r -> loginHandler.sendEmptyMessage(2)
            );

        });

    }
}