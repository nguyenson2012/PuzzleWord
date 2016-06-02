package com.example.asus.puzzleword.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.asus.puzzleword.R;
import com.example.asus.puzzleword.model.RecyclerViewHolders;
import com.example.asus.puzzleword.model.Stage;

import java.util.List;

/**
 * Created by SON on 3/21/2016.
 */
public class GridViewLevelAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<Stage> stageItems;
    private Activity context;
    private int currentLevel;

    public GridViewLevelAdapter(Activity context, List<Stage> levelCardItems, int currentLevel) {
        this.stageItems = levelCardItems;
        this.context = context;
        this.currentLevel = currentLevel;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_level, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.imageLevel.setImageResource(R.drawable.action_word);
        if (position >= currentLevel) {
            holder.imageLevel.setImageResource(R.drawable.lock_icon);
            holder.textViewTimeComplete.setText("");
        }
        if (position == currentLevel - 1)
            holder.textViewTimeComplete.setText("");
        holder.textViewTimeComplete.setText(stageItems.get(position).getSecondComplete() + "s");
        holder.textViewDescription.setText(stageItems.get(position).getDescriptionStage());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_level_anim);
        holder.cardView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return this.stageItems.size();
    }

    public void changeCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
}