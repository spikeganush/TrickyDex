package com.ganushcorporation.android.trickydex.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.models.Trick;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TrickAdapterAdmin extends RecyclerView.Adapter<TrickAdapterAdmin.TrickViewHolder>  {

    public Context context;
    public ArrayList<Trick> list;
    public EditText  editTextNameUpdate, editTextInfoUpdate;
    public SeekBar seekBarDifficultyUpdate;
    public ProgressBar progressBarUpdate;
    public ImageView buttonCloseUpdate;
    public Button buttonValidateTrickUpdate;
    public DatabaseReference referenceUpdate;
    public Spinner categorySpinnerUpdate;
    public String categoryToPushUpdate;
    public Trick trick;


    public TrickAdapterAdmin(Context context, ArrayList<Trick> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trick, parent,false);
        return new TrickViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull TrickViewHolder holder, int position) {
        trick = list.get(position);
        holder.trickName.setText(trick.name);
        holder.cardTrick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpUpdate();
                editTextNameUpdate.setText(trick.name);
                editTextInfoUpdate.setText(trick.info);
                int position;

                if(trick.category.equals("Slide")) {
                    position = 0;
                } else if (trick.category.equals("Grab")) {
                    position = 1;
                } else {
                    position = 2;
                }
                categorySpinnerUpdate.setSelection(position);
                seekBarDifficultyUpdate.setProgress(trick.difficulty);
            }
        });

        if(trick.category.equals("Slide")){
            holder.cardTrick.setCardBackgroundColor(Color.parseColor("#FA6767"));
            return;
        }
        if(trick.category.equals("Grab")) {
            holder.cardTrick.setCardBackgroundColor(Color.parseColor("#67A2FA"));
            return;
        }
        if(trick.category.equals("Air")) {
            holder.cardTrick.setCardBackgroundColor(Color.parseColor("#67FAA2"));
            return;
        }

    }

    private void showPopUpUpdate() {
        final Dialog dialogUpdate = new Dialog(context, R.style.dialogPopUp);
        dialogUpdate.setContentView(R.layout.update_trick);
        dialogUpdate.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogUpdate.show();
        // EditText
        editTextNameUpdate = dialogUpdate.findViewById(R.id.editTextTrickNameUpdate);
        editTextInfoUpdate = dialogUpdate.findViewById(R.id.editTextInfoUpdate);

        //Seekbar
        seekBarDifficultyUpdate = dialogUpdate.findViewById(R.id.seekBarDifficultyUpdate);

        // Progress bar
        progressBarUpdate = dialogUpdate.findViewById(R.id.progressBarRegisterManageTrickUpdate);

        // ImageView/Button
        buttonCloseUpdate = dialogUpdate.findViewById(R.id.imageViewCloseBtnUpdate);
        buttonCloseUpdate.setOnClickListener(view -> dialogUpdate.dismiss());

        // Clickable
        buttonValidateTrickUpdate = dialogUpdate.findViewById(R.id.buttonUpdateTrick);
        buttonValidateTrickUpdate.setOnClickListener(view -> updateTrick());

        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        referenceUpdate = database.getReference("Tricks");

        // Spinner

        //get the spinner from the xml.
        categorySpinnerUpdate = dialogUpdate.findViewById(R.id.spinnerCategoryUpdate);
        //create a list of items for the spinner.
        String[] categoryUpdate = new String[]{"Slide", "Grab", "Air"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterUpdate = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, categoryUpdate);
        //set the spinners adapter to the previously created one.
        categorySpinnerUpdate.setAdapter(adapterUpdate);

        categorySpinnerUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                categoryToPushUpdate = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void updateTrick() {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TrickViewHolder extends RecyclerView.ViewHolder {
                TextView trickName;
                CardView cardTrick;

                EditText trickNameUpdate, trickInfoUpdate;
                Spinner categoryUpdate;
                SeekBar difficultyUpdate;


        public TrickViewHolder(@NonNull View itemView) {
            super(itemView);
            trickName = itemView.findViewById(R.id.textViewTrickName);
            cardTrick = itemView.findViewById(R.id.cardViewTrickAdmin);

            trickNameUpdate = itemView.findViewById(R.id.editTextTrickNameUpdate);
            trickInfoUpdate = itemView.findViewById(R.id.editTextInfoUpdate);
            categoryUpdate = itemView.findViewById(R.id.spinnerCategoryUpdate);
            difficultyUpdate = itemView.findViewById(R.id.seekBarDifficultyUpdate);

        }
    }
}
