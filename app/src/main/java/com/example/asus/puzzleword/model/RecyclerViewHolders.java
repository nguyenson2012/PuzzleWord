package com.example.asus.puzzleword.model;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.puzzleword.R;

/**
 * Created by SON on 3/21/2016.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CardView cardView;
    public ImageView imageLevel;
    public TextView textViewTimeComplete;
    public TextView textViewDescription;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        imageLevel = (ImageView) itemView.findViewById(R.id.image_level);
        textViewTimeComplete = (TextView) itemView.findViewById(R.id.textview_time_complete);
        textViewDescription = (TextView) itemView.findViewById(R.id.textviewStage);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
    }

    @Override
    public void onClick(View view) {

    }
}
