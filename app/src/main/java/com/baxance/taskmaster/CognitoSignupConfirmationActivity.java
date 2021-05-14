package com.baxance.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;

import org.w3c.dom.Text;

public class CognitoSignupConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognito_signup_confirmation);

        String username = getIntent().getStringExtra("username");

        ((EditText) findViewById(R.id.editTextTextEmailAddressConfirmation)).setText(username);

        ((Button) findViewById(R.id.signupConfirmationButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String confirmationCode =
                                ((EditText) findViewById(R.id.editTextTextConfirmationCode))
                                    .getText().toString();
                        Amplify.Auth.confirmSignUp(
                                username,
                                confirmationCode,
                                r -> {
                                    startActivity(new Intent(CognitoSignupConfirmationActivity.this,
                                            MainActivity.class));
                                },
                                r -> {
                                    Toast.makeText(CognitoSignupConfirmationActivity.this, "confirmation failed", Toast.LENGTH_LONG);
                                }

                        );
                    }
                }
        );
    }
}