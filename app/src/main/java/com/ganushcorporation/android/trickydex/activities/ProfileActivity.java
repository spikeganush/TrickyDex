package com.ganushcorporation.android.trickydex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ganushcorporation.android.trickydex.LoginActivity;
import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView firstNameTextView, lastNameTextView, emailTextView;
    private Button logout;
    private ImageView backButton;
    // Firebase
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        // Clickable
        logout = findViewById(R.id.buttonLogout);
        logout.setOnClickListener(this);

        backButton =findViewById(R.id.backButtonAccount);
        backButton.setOnClickListener(this);

        // Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // Textview
        firstNameTextView = findViewById(R.id.textViewAccountFirstName);
        lastNameTextView = findViewById(R.id.textViewAccountLastName);
        emailTextView = findViewById(R.id.textViewAccountEmail);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    String firstName = userProfile.firstname;
                    String lastName = userProfile.lastname;
                    String email = userProfile.email;

                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    emailTextView.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogout:
                Logout();
                break;
            case R.id.backButtonAccount:
                finish();
                break;

    }
}

    private void Logout() {
        SharedPreferences.Editor editor = getSharedPreferences("remember", MODE_PRIVATE).edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));

    }
}