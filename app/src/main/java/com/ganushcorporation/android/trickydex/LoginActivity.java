package com.ganushcorporation.android.trickydex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ganushcorporation.android.trickydex.activities.ForgotPasswordActivity;
import com.ganushcorporation.android.trickydex.activities.MenuActivity;
import com.ganushcorporation.android.trickydex.activities.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextEmail, editTextPassword;
    private Button registerButton, loginButton;
    private TextView forgotPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private CheckBox rememberMeCheckBox;
    private String rememberEmail, rememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Clickable
        registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(this);

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this);

        forgotPassword = findViewById(R.id.textViewForgotPasswordLogin);
        forgotPassword.setOnClickListener(this);

        // Progress bar
        progressBar = findViewById(R.id.progressBarLogin);

        // Edit text
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        //CheckBox
        rememberMeCheckBox = findViewById(R.id.checkBoxRememberMe);

        //Remember credentials
        SharedPreferences sharedPreferences = getSharedPreferences("remember",MODE_PRIVATE);
        rememberEmail = sharedPreferences.getString("email", null);
        rememberPassword = sharedPreferences.getString("password", null);

        if(rememberEmail != null) {
            progressBar.setVisibility(View.VISIBLE);
            editTextEmail.setText(rememberEmail);
            editTextPassword.setText(rememberPassword);
            rememberMeCheckBox.setChecked(true);

            rememberLogin();
        }



}

    private void rememberLogin() {
        mAuth.signInWithEmailAndPassword(rememberEmail, rememberPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        // Redirect to the MenuActivity
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Failed to login, please check your credentials!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                Intent registerActivity = new Intent(this, RegisterActivity.class);
                startActivity(registerActivity);
                break;
            case R.id.buttonLogin:
                userLogin();
                break;
            case R.id.textViewForgotPasswordLogin:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

        }

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid password!");
            editTextEmail.requestFocus();
            return;

        }

        if(password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("The password mush have at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        // Redirect to the MenuActivity
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));



                        if(rememberMeCheckBox.isChecked()){
                            SharedPreferences.Editor editor = getSharedPreferences("remember", MODE_PRIVATE).edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply();
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Failed to login, please check your credentials!", Toast.LENGTH_LONG).show();
                }

            }
        });








    }
}