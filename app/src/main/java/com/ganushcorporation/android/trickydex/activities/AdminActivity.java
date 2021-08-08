package com.ganushcorporation.android.trickydex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backButton;
    private TextView welcome;
    private Button addTrick;
    // Firebase
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        // Clickable
        backButton = findViewById(R.id.backButtonAdmin);
        backButton.setOnClickListener(this);

        addTrick = findViewById(R.id.buttonAddTrick);
        addTrick.setOnClickListener(this);

        // TextView
        welcome = findViewById(R.id.textViewAdminWelcome);

        // Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    String firstName = userProfile.firstname;
                    String lastName = userProfile.lastname;

                    welcome.setText("Welcome "+firstName+" "+lastName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButtonAdmin:
                finish();
                break;
            case R.id.buttonAddTrick:
                startActivity(new Intent(AdminActivity.this, ManageTrickActivity.class));
                break;
        }

    }
}