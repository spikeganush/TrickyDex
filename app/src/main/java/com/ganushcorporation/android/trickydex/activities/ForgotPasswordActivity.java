package com.ganushcorporation.android.trickydex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ganushcorporation.android.trickydex.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button resetPasswordButton;
    private ImageView backButton;
    private EditText emailEditText;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Clickable
        resetPasswordButton = findViewById(R.id.buttonReset);
        resetPasswordButton.setOnClickListener(this);

        backButton = findViewById(R.id.backButtonForgotPassword);
        backButton.setOnClickListener(this);

        // Progress bar
        progressBar = findViewById(R.id.progressBarForgotPassword);

        // Edit text
        emailEditText = findViewById(R.id.editTextEmailForgotPassword);

        // Firebase
        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonReset:
                resetPassword();
                break;
            case R.id.backButtonForgotPassword:
                finish();
                break;

        }

    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()){
            emailEditText.setError("Email has to be fill!");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email!");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password (could be in your junk/spam box)", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForgotPasswordActivity.this, "Email not sent! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}