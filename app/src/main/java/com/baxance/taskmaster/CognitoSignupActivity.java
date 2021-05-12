package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class CognitoSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognito_signup);

        ((Button) findViewById(R.id.signupConfirmationButton)).setOnClickListener(v -> {
            String username = ((EditText) findViewById(R.id.editTextTextEmailAddressConfirmation)).getText().toString();
            String password = ((EditText) findViewById(R.id.editTextTextConfirmationCode)).getText().toString();

            Amplify.Auth.signUp(
                    username,
                    password,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), username)
                            .build(),
                    r -> {
                        Intent intent = new Intent(CognitoSignupActivity.this, CognitoSignupConfirmationActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    },
                    r -> Log.i("signup activity", "signup failure: " + r.toString())
            );
        });
    }
}