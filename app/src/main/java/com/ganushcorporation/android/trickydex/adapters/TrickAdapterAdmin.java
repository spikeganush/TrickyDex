package com.ganushcorporation.android.trickydex.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

public class TrickAdapterAdmin extends RecyclerView.Adapter<TrickAdapterAdmin.TrickViewHolder>  {

    public Context context;
    public ArrayList<Trick> list;
    private ArrayList<String> trickId;
    public EditText  editTextNameUpdate, editTextInfoUpdate;
    public SeekBar seekBarDifficultyUpdate;
    public ProgressBar progressBarUpdate;
    public ImageView buttonCloseUpdate;
    public Button buttonValidateTrickUpdate;
    public DatabaseReference referenceUpdate;
    public Spinner categorySpinnerUpdate;
    public String categoryToPushUpdate, idTrickTest;
    public Trick trick;

    private DatabaseReference reference;


    public TrickAdapterAdmin(Context context, ArrayList<Trick> list, ArrayList<String> trickId) {
        this.context = context;
        this.list = list;
        this.trickId = trickId;
    }

    @NonNull
    @Override
    public TrickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trick, parent,false);
        reference = FirebaseDatabase.getInstance().getReference("Tricks");
        return new TrickViewHolder(v);



    }

    @Override
    public void onBindViewHolder(@NonNull TrickViewHolder holder, int position) {
        trick = list.get(position);
        holder.trickName.setText(trick.name);

        holder.trickInfo = trick.info;
        holder.trickDifficulty = trick.difficulty;
        holder.trickCategory = trick.category;
        holder.idTrick = trickId.get(position);


        holder.cardTrick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpUpdate();
                idTrickTest = holder.idTrick;
                Log.e("onBindViewHolder: ", idTrickTest);
                editTextNameUpdate.setText(holder.trickName.getText());
                editTextInfoUpdate.setText(holder.trickInfo);
                int position;

                if(holder.trickCategory.equals("Slide")) {
                    position = 0;
                } else if (holder.trickCategory.equals("Grab")) {
                    position = 1;
                } else {
                    position = 2;
                }
                categorySpinnerUpdate.setSelection(position);
                seekBarDifficultyUpdate.setProgress(holder.trickDifficulty);
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
        buttonValidateTrickUpdate.setOnClickListener(view -> {
                    /////////////////////////////////////////
                    //--------------UPDATE TRICK-----------//
                    /////////////////////////////////////////

                    Map<String, Object> childUpdates = new HashMap<>();
                    String newName = editTextNameUpdate.getText().toString();
                    String newInfo = editTextInfoUpdate.getText().toString();
                    String category = categoryToPushUpdate;
                    int difficulty = seekBarDifficultyUpdate.getProgress();

                    childUpdates.put(idTrickTest +"/name", newName);
                    childUpdates.put(idTrickTest +"/info", newInfo);
                    childUpdates.put(idTrickTest +"/category", category);
                    childUpdates.put(idTrickTest +"/difficulty", difficulty);
                    Log.e( "onBindViewHolder: ", idTrickTest);
                    referenceUpdate.updateChildren(childUpdates);
                    dialogUpdate.dismiss();
                }
        );



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


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TrickViewHolder extends RecyclerView.ViewHolder {
                TextView trickName;
                CardView cardTrick;
                int trickDifficulty;
                String trickCategory, trickInfo, idTrick;


        public TrickViewHolder(@NonNull View itemView) {
            super(itemView);
            trickName = itemView.findViewById(R.id.textViewTrickName);
            trickInfo = "";
            cardTrick = itemView.findViewById(R.id.cardViewTrickAdmin);
            trickDifficulty = 0;
            trickCategory = "";
            idTrick= "";

        }
    }
}
