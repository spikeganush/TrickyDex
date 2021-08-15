package com.ganushcorporation.android.trickydex.adapters;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.models.Trick;
import com.ganushcorporation.android.trickydex.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TrickAdapter extends RecyclerView.Adapter<TrickAdapter.TrickListViewHolder> {

    private Context context;
    private ArrayList<Trick> list;
    private ArrayList<String> trickId;
    private Trick trick;
    private String type;
    private TextView info, infoTrickName;
    public ImageView buttonCloseInfo;
    public User userProfile, userListTrickChecked;
    // Firebase
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Map<String, Object> childUpdates;


    public TrickAdapter(Context context, ArrayList<Trick> list, String typePass, ArrayList<String> trickId) {
        this.context = context;
        this.list = list;
        this.type = typePass;
        this.trickId = trickId;
    }


    @NonNull
    @Override
    public TrickListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trick_list, parent,false);


        return new TrickListViewHolder(v);



    }

    @Override
    public void onBindViewHolder(@NonNull TrickListViewHolder holder, int position) {
        trick = list.get(position);
        int pos = holder.getBindingAdapterPosition();
        holder.trickName.setText(trick.name);
        holder.idTrick = trickId.get(pos);
        holder.infoTrick = trick.info;

        // Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();



        reference = FirebaseDatabase.getInstance().getReference("Users");

        //in some cases, it will prevent unwanted situations
        holder.trickDone.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.trickDone.setChecked(trick.isSelected());

        holder.trickDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                holder.trickDone.setSelected(isChecked);
                childUpdates = new HashMap<>();
                childUpdates.put(userID+ "/"+type+"/", 0);

                reference = FirebaseDatabase.getInstance().getReference("Users");

                reference.updateChildren(childUpdates);
            }
        });

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userListTrickChecked = snapshot.getValue(User.class);



                if (userListTrickChecked != null) {
                    if(userListTrickChecked.listDone.containsValue(holder.idTrick)) {
                        holder.trickDone.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.trickDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                list.get(holder.getBindingAdapterPosition()).setSelected(isChecked);
                childUpdates = new HashMap<>();


                reference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userProfile = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(holder.trickDone.isChecked()) {
                    try {
                        childUpdates.put(userID+ "/listDone/" + holder.idTrick, holder.idTrick);
                        childUpdates.put(userID+ "/"+type+"/", ServerValue.increment(1));

                    } catch(Exception e) {

                    }




                } else {
                    childUpdates.put(userID+ "/"+type+"/", ServerValue.increment(-1));
                    reference.child(userID+ "/listDone/" + holder.idTrick).removeValue();

                }
                reference.updateChildren(childUpdates);
            }
        });


        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo();
                info.setText(holder.infoTrick);
                infoTrickName.setText(holder.trickName.getText() + " info:");
            }
        });


        if(type.equals("Slide")){
            holder.cardTrick.setCardBackgroundColor(Color.parseColor("#FA6767"));
            holder.cardTrickRight.setBackgroundResource(R.drawable.right_card_slide);
            return;
        }
        if(type.equals("Grab")) {
            holder.cardTrick.setCardBackgroundColor(Color.parseColor("#67A2FA"));
            holder.cardTrickRight.setBackgroundResource(R.drawable.right_card_grab);
            return;
        }
        if(type.equals("Air")) {
            holder.cardTrick.setCardBackgroundColor(Color.parseColor("#9F67FA"));
            holder.cardTrickRight.setBackgroundResource(R.drawable.right_card_air);
        }



    }


    private void showInfo() {
        final Dialog dialogUpdate = new Dialog(context, R.style.dialogPopUp);
        dialogUpdate.setContentView(R.layout.info_popup);
        dialogUpdate.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogUpdate.show();

        // ImageView/Button
        buttonCloseInfo = dialogUpdate.findViewById(R.id.imageViewCloseBtnInfo);
        buttonCloseInfo.setOnClickListener(view -> dialogUpdate.dismiss());

        // textView
        info = dialogUpdate.findViewById(R.id.textViewInfo);
        infoTrickName = dialogUpdate.findViewById(R.id.textViewInfoName);


    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public static class TrickListViewHolder extends RecyclerView.ViewHolder {
        TextView trickName;
        CardView cardTrick;
        ImageView infoButton;
        ConstraintLayout cardTrickRight;
        String infoTrick, idTrick;
        CheckBox trickDone;


        public TrickListViewHolder(@NonNull View itemView) {
            super(itemView);
            trickName = itemView.findViewById(R.id.textViewTrickNameUser);
            infoButton = itemView.findViewById(R.id.imageViewInfo);
            cardTrick = itemView.findViewById(R.id.cardViewTrickUser);
            cardTrickRight = itemView.findViewById(R.id.ConstraintLayoutTrickListRight);
            trickDone = itemView.findViewById(R.id.checkBoxDoneTrickList);
            infoTrick = "";




        }
    }
}
