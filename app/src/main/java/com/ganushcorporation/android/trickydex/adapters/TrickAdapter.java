package com.ganushcorporation.android.trickydex.adapters;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.ganushcorporation.android.trickydex.R;
import com.ganushcorporation.android.trickydex.models.Trick;

import java.util.ArrayList;

public class TrickAdapter extends RecyclerView.Adapter<TrickAdapter.TrickListViewHolder> {

    private Context context;
    private ArrayList<Trick> list;
    private Trick trick;
    private String type;
    private TextView info;
    public ImageView buttonCloseInfo;
    public TrickAdapter(Context context, ArrayList<Trick> list, String typePass) {
        this.context = context;
        this.list = list;
        this.type = typePass;

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

        holder.trickName.setText(trick.name);
        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo();

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


        public TrickListViewHolder(@NonNull View itemView) {
            super(itemView);
            trickName = itemView.findViewById(R.id.textViewTrickNameUser);
            infoButton = itemView.findViewById(R.id.imageViewInfo);
            cardTrick = itemView.findViewById(R.id.cardViewTrickUser);
            cardTrickRight = itemView.findViewById(R.id.ConstraintLayoutTrickListRight);


        }
    }
}
