package com.ganushcorporation.android.trickydex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.adapters.TrickAdapterAdmin;
import com.ganushcorporation.android.trickydex.models.Trick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class  ManageTrickActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner categorySpinner;
    private SeekBar seekBarDifficulty;
    private EditText editTextName, editTextInfo;
    private Button buttonValidateTrick, buttonAdd;
    private ProgressBar progressBar;
    private String categoryToPush;
    private ImageView buttonClose;
    public ArrayList<String> listTrickId;

    private EditText  editTextNameUpdate, editTextInfoUpdate;
    private SeekBar seekBarDifficultyUpdate;
    private ProgressBar progressBarUpdate;
    private ImageView buttonCloseUpdate;
    private Button buttonValidateTrickUpdate;
    private DatabaseReference referenceUpdate;
    private Spinner categorySpinnerUpdate;
    private String categoryToPushUpdate;
    // Firebase
    private DatabaseReference reference;

    private ImageView buttonBack;

    public RecyclerView recyclerView;
    public DatabaseReference databaseReference;
    public TrickAdapterAdmin trickAdapterAdmin;
    public ArrayList<Trick> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trick);


        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);

        buttonBack = findViewById(R.id.backButtonAdmin2);
        buttonBack.setOnClickListener(this);

        recyclerView = findViewById(R.id.trickList);
        databaseReference = FirebaseDatabase.getInstance().getReference("Tricks");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listTrickId = new ArrayList<>();
        list = new ArrayList<>();
        trickAdapterAdmin = new TrickAdapterAdmin(this, list, listTrickId);
        recyclerView.setAdapter(trickAdapterAdmin);

        databaseReference.orderByChild("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Trick trick = dataSnapshot.getValue(Trick.class);
                    listTrickId.add(dataSnapshot.getKey());
                    list.add(trick);
                }
                trickAdapterAdmin.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                showPopUp();
                break;
            case R.id.backButtonAdmin2:
                finish();
                break;
        }
        
    }

    private void showPopUp() {
        Dialog dialog = new Dialog(this, R.style.dialogPopUp);
        dialog.setContentView(R.layout.add_trick);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.show();
        // EditText
        editTextName = dialog.findViewById(R.id.editTextTrickName);
        editTextInfo = dialog.findViewById(R.id.editTextInfo);

        //Seekbar
        seekBarDifficulty = dialog.findViewById(R.id.seekBarDifficulty);

        // Progress bar
        progressBar = dialog.findViewById(R.id.progressBarRegisterManageTrick);

        // ImageView/Button
        buttonClose = dialog.findViewById(R.id.imageViewCloseBtnInfo);
        buttonClose.setOnClickListener(view -> dialog.dismiss());

        // Clickable
        buttonValidateTrick = dialog.findViewById(R.id.buttonValidateTrick);
        buttonValidateTrick.setOnClickListener(view -> addTrick());


        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Tricks");

        // Spinner

        //get the spinner from the xml.
        categorySpinner = dialog.findViewById(R.id.spinnerCategory);
        //create a list of items for the spinner.
        String[] category = new String[]{"Slide", "Grab", "Air"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, category);
        //set the spinners adapter to the previously created one.
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                categoryToPush = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addTrick() {
        String name = editTextName.getText().toString();
        String category = categoryToPush;
        String info = editTextInfo.getText().toString();
        int difficulty = seekBarDifficulty.getProgress();
        Trick trick = new Trick(name, category, info, difficulty);

        reference.push().setValue(trick).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ManageTrickActivity.this,name+" has been registered successfully!", Toast.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ManageTrickActivity.this,name+" failed to be registered! Try again!", Toast.LENGTH_LONG).show();
            }
        });

    }



    public void updateTrick() {

    }


}