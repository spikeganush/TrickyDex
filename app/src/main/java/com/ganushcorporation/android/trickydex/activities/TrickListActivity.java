package com.ganushcorporation.android.trickydex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.adapters.TrickAdapter;
import com.ganushcorporation.android.trickydex.models.Trick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrickListActivity extends AppCompatActivity implements View.OnClickListener {

    // Firebase
    private DatabaseReference reference;

    private ImageView buttonBack;

    public RecyclerView recyclerView;
    public DatabaseReference databaseReference;
    public TrickAdapter trickAdapter;
    public ArrayList<Trick> listTrick;
    public ArrayList<String> listTrickId;
    private String type;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_list);

        type = getIntent().getStringExtra("type");
        welcome = findViewById(R.id.textViewTrickListTitleUser);
        welcome.setText(type + " list");

        buttonBack = findViewById(R.id.backButtonTrickList);
        buttonBack.setOnClickListener(this);

        recyclerView = findViewById(R.id.trickListUser);
        databaseReference = FirebaseDatabase.getInstance().getReference("Tricks");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listTrickId = new ArrayList<>();
        listTrick = new ArrayList<>();
        trickAdapter = new TrickAdapter(this, listTrick, type, listTrickId);
        recyclerView.setAdapter(trickAdapter);

        databaseReference.orderByChild("category").equalTo(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Trick trick = dataSnapshot.getValue(Trick.class);
                    listTrickId.add(dataSnapshot.getKey());
                    listTrick.add(trick);
                }
                trickAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButtonTrickList:
                finish();
                break;
        }

    }
}