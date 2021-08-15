package com.ganushcorporation.android.trickydex.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.models.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView profileButton;
    private ProgressBar slideProgressBar, airProgressBar, grabProgressBar, listProgressBar;
    private TextView adminTextView, textNumberAchieveSlides, textNumberAchieveAirs, textNumberAchieveGrabs,
            percentageSlideTextView, percentageAirTextView, percentageGrabTextView;
    // Firebase
    private FirebaseUser user;
    private DatabaseReference reference, reference2;
    private String userID;
    //Chip
    private Chip selectView, chipSlide, chipGrab, chipAir, chipTrickList;
    private ChipGroup chipSelectionGroup;
    //Card view
    private CardView cardViewSlide, cardViewGrab, cardViewAir, cardViewTrickList;
    //SharePreferences
    public String rememberSlide, rememberGrab, rememberAir, rememberTrickList,
                    userTotalSlide, totalSlide, userTotalAir, totalAir, userTotalGrab, totalGrab;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Clickable
        profileButton = findViewById(R.id.imageViewProfile);
        profileButton.setOnClickListener(this);

            //Card view
            cardViewSlide = findViewById(R.id.slideMaterialCardView);
            cardViewSlide.setOnClickListener(this);
            cardViewGrab = findViewById(R.id.grabMaterialCardView);
            cardViewGrab.setOnClickListener(this);
            cardViewAir = findViewById(R.id.airMaterialCardView);
            cardViewAir.setOnClickListener(this);
            cardViewTrickList = findViewById(R.id.listMaterialCardView);

        selectView = findViewById(R.id.selectViewChip);
        selectView.setOnClickListener(this);
        chipSlide = findViewById(R.id.chipSlide);
        chipSlide.setChecked(true);
        chipGrab = findViewById(R.id.chipGrab);
        chipGrab.setChecked(true);
        chipAir = findViewById(R.id.chipAir);
        chipAir.setChecked(true);
        chipTrickList = findViewById(R.id.chipTrickList);
        chipTrickList.setChecked(true);
        chipSelectionGroup = findViewById(R.id.chipSelectionGroup);

        // Progress bars
        slideProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        airProgressBar = (ProgressBar) findViewById(R.id.progressBarAirs);
        grabProgressBar = (ProgressBar) findViewById(R.id.progressBarGrabs);
        listProgressBar = (ProgressBar) findViewById(R.id.progressBarList);


        // TextView
        adminTextView = findViewById(R.id.textViewAdmin);
        adminTextView.setOnClickListener(this);
        textNumberAchieveSlides = findViewById(R.id.textNumberAchieveSlides);
        textNumberAchieveGrabs = findViewById(R.id.textNumberAchieveGrabs);
        textNumberAchieveAirs = findViewById(R.id.textNumberAchieveAirs);
        percentageSlideTextView = findViewById(R.id.textPercentageSlides);
        percentageAirTextView = findViewById(R.id.textPercentageAirs);
        percentageGrabTextView = findViewById(R.id.textPercentageGrabs);


        //Remember credentials
        SharedPreferences sharedPreferences = getSharedPreferences("isChecked",MODE_PRIVATE);
        rememberSlide = sharedPreferences.getString("slide", null);
        rememberGrab = sharedPreferences.getString("grab", null);
        rememberAir = sharedPreferences.getString("air", null);
        rememberTrickList = sharedPreferences.getString("trickList", null);

        editor = getSharedPreferences("isChecked", MODE_PRIVATE).edit();

        //Shared preference for the card Slide
        if (rememberSlide != null) {
            if(rememberSlide.equals("true")) {
                cardViewSlide.setVisibility(View.VISIBLE);
                chipSlide.setChecked(true);
            }
            if(rememberSlide.equals("false")) {
                cardViewSlide.setVisibility(View.GONE);
                chipSlide.setChecked(false);
            }
        } else {
            cardViewSlide.setVisibility(View.VISIBLE);
            chipSlide.setChecked(true);
        }

        //Shared preference for the card Grab
        if(rememberGrab != null) {
            if(rememberGrab.equals("true")) {
                cardViewGrab.setVisibility(View.VISIBLE);
                chipGrab.setChecked(true);
            }
            if(rememberGrab.equals("false")) {
                cardViewGrab.setVisibility(View.GONE);
                chipGrab.setChecked(false);
            }
        } else {
            cardViewGrab.setVisibility(View.VISIBLE);
            chipGrab.setChecked(true);
        }

        //Shared preference for the card Air
        if(rememberAir != null) {
            if(rememberAir.equals("true")) {
                cardViewAir.setVisibility(View.VISIBLE);
                chipAir.setChecked(true);
            }
            if(rememberAir.equals("false")) {
                cardViewAir.setVisibility(View.GONE);
                chipAir.setChecked(false);
            }
        } else {
            cardViewAir.setVisibility(View.VISIBLE);
            chipAir.setChecked(true);
        }

        //Shared preference for the card Trick list
        if(rememberTrickList != null) {
            if(rememberTrickList.equals("true")) {
                cardViewTrickList.setVisibility(View.VISIBLE);
                chipTrickList.setChecked(true);
            }
            if(rememberTrickList.equals("false")) {
                cardViewTrickList.setVisibility(View.GONE);
                chipTrickList.setChecked(false);
            }

        } else {
            cardViewTrickList.setVisibility(View.VISIBLE);
            chipTrickList.setChecked(true);
        }

        // Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference2 = FirebaseDatabase.getInstance().getReference("Tricks");


        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalSlide = snapshot.child("Slide").getValue().toString();
                totalAir = snapshot.child("Air").getValue().toString();
                totalGrab = snapshot.child("Grab").getValue().toString();


                reference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);

                        if(userProfile != null) {
                            Boolean admin = userProfile.admin;


                            userTotalSlide = String.valueOf(userProfile.Slide);
                            userTotalAir = String.valueOf(userProfile.Air);
                            userTotalGrab = String.valueOf(userProfile.Grab);
                            textNumberAchieveSlides.setText(userTotalSlide + "/" + totalSlide);
                            textNumberAchieveAirs.setText(userTotalAir + "/" + totalAir);
                            textNumberAchieveGrabs.setText(userTotalGrab + "/" + totalGrab);

                            double doubleTotalSlide = convertToDouble(totalSlide);
                            double doubleTotalGrab = convertToDouble(totalGrab);
                            double doubleTotalAir =  convertToDouble(totalAir);
                            double totalUserSlide = convertToDouble(userTotalSlide);
                            double totalUserAir = convertToDouble(userTotalAir);
                            double totalUserGrab = convertToDouble(userTotalGrab);

                            String percentageSlide = String.valueOf((int) ((totalUserSlide/doubleTotalSlide) * 100));
                            String percentageAir = String.valueOf((int) ((totalUserAir/doubleTotalAir) * 100));
                            String percentageGrab= String.valueOf((int) ((totalUserGrab/doubleTotalGrab) * 100));


                            percentageSlideTextView.setText(percentageSlide + "%");
                            percentageAirTextView.setText(percentageAir + "%");
                            percentageGrabTextView.setText(percentageGrab + "%");

                            slideProgressBar.setMax((int)doubleTotalSlide);
                            slideProgressBar.setProgress((int)totalUserSlide);

                            airProgressBar.setMax((int)doubleTotalAir);
                            airProgressBar.setProgress((int)totalUserAir);

                            listProgressBar.setMax(8);
                            listProgressBar.setProgress(1);

                            grabProgressBar.setMax((int)doubleTotalGrab);
                            grabProgressBar.setProgress((int)totalUserGrab);








                            if(admin) {
                                adminTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MenuActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();

                    }
                });










            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenuActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();

            }
        });







    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewProfile:
                startActivity((new Intent(MenuActivity.this, ProfileActivity.class)));
                break;
            case R.id.textViewAdmin:
                startActivity(new Intent(MenuActivity.this, AdminActivity.class));
                break;
            case R.id.selectViewChip:
                openChip();
                break;
            case R.id.slideMaterialCardView:
                openTrickList("Slide");
                break;
            case R.id.grabMaterialCardView:
                openTrickList("Grab");
                break;
            case R.id.airMaterialCardView:
                openTrickList("Air");
                break;
        }
    }

    private void openTrickList(String params) {
        Intent intent = new Intent(MenuActivity.this, TrickListActivity.class);
        intent.putExtra("type", params);
        startActivity(intent);
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static Double convertToDouble(String str) {
        double n=0;
        if(str != null && str.length()>0) {
            n = Double.parseDouble(str);
        }
        return n;
    }

    private void openChip() {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)selectView.getLayoutParams();
        params.setMargins(0, 0 , 0 , 0);
        selectView.setLayoutParams(params);
        selectView.setCloseIconVisible(true);
        chipSelectionGroup.setVisibility(View.VISIBLE);

        chipSlide.setOnClickListener(view -> {
            cardViewSelect("slide");
        });

        chipGrab.setOnClickListener(view -> {
            cardViewSelect("grab");
        });

        chipAir.setOnClickListener(view -> {
            cardViewSelect("air");
        });

        chipTrickList.setOnClickListener(view -> {
            cardViewSelect("trickList");
        });

        selectView.setOnCloseIconClickListener(view -> {
            params.setMargins(0, 125 , 0 , 0);
            selectView.setLayoutParams(params);
            selectView.setCloseIconVisible(false);
            chipSelectionGroup.setVisibility(View.GONE);
        });

    }

    private void cardViewSelect(String params) {
        if(params.equals("slide")) {
            if(chipSlide.isChecked()){
                cardViewSlide.setVisibility(View.VISIBLE);
                editor.putString("slide", "true");
            } else {
                cardViewSlide.setVisibility(View.GONE);
                editor.putString("slide", "false");
            }
            editor.apply();

        }

        if (params.equals("grab")) {
            if(chipGrab.isChecked()){
                cardViewGrab.setVisibility(View.VISIBLE);
                editor.putString("grab", "true");
            } else {
                cardViewGrab.setVisibility(View.GONE);
                editor.putString("grab", "false");
            }
            editor.apply();

        }

        if(params.equals("air")) {
            if(chipAir.isChecked()){
                cardViewAir.setVisibility(View.VISIBLE);
                editor.putString("air", "true");
            } else {
                cardViewAir.setVisibility(View.GONE);
                editor.putString("air", "false");
            }
            editor.apply();

        }

        if (params.equals("trickList")){
            if(chipTrickList.isChecked()){
                cardViewTrickList.setVisibility(View.VISIBLE);
                editor.putString("trickList", "true");
            } else {
                cardViewTrickList.setVisibility(View.GONE);
                editor.putString("trickList", "false");
            }
            editor.apply();

        }
    }
}

