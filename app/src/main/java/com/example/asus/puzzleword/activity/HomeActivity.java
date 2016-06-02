package com.example.asus.puzzleword.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.asus.puzzleword.R;
import com.example.asus.puzzleword.StaticVariable;
import com.example.asus.puzzleword.adapter.GridSpacingItemDecoration;
import com.example.asus.puzzleword.adapter.GridViewLevelAdapter;
import com.example.asus.puzzleword.adapter.RecyclerItemClickListener;
import com.example.asus.puzzleword.model.Stage;

import java.util.ArrayList;

/**
 * Created by Asus on 5/16/2016.
 */
public class HomeActivity extends AppCompatActivity {
    int column = 2;
    int spacing = 0;
    boolean includeEdge = false;
    GridViewLevelAdapter rcAdapter;
    private RecyclerView recyclerViewLevel;
    private ArrayList<Stage> stageItems;
    private GridLayoutManager gridLayoutManager;
    private int screenWidth;
    private int screenHeight;
    private int currentLevel = 1;
    private String prefName = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        getTimeCompleteStage();
        getCurrentLevel();
        setDefaultDataLevel();
        setUpView();
        setAdapterForRecyclerView();
        registerEvent();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getTimeCompleteStage();
        getCurrentLevel();
        rcAdapter.changeCurrentLevel(currentLevel);
        rcAdapter.notifyDataSetChanged();
    }

    private void getCurrentLevel() {
        SharedPreferences pre = getSharedPreferences
                (prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        //editor.clear();
        editor.commit();
        currentLevel = pre.getInt("currentLevel", 1);
    }

    private void getTimeCompleteStage() {
        SharedPreferences pre = getSharedPreferences
                (prefName, MODE_PRIVATE);
        for (Stage stage : StaticVariable.getInstance().getAllStage()) {
            int timeComplete = pre.getInt(stage.getDescriptionStage() + "", 0);
            stage.setSecondComplete(timeComplete);
        }
    }

    private void registerEvent() {
        recyclerViewLevel.addOnItemTouchListener(new RecyclerItemClickListener(HomeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position < currentLevel) {
                            // TODO Handle item click
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            intent.putExtra("levelposition", position+1);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, 0);
                        }
                    }
                })
        );
    }

    private void setAdapterForRecyclerView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        spacing = screenWidth / column / 3;
        gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerViewLevel.setHasFixedSize(true);
        recyclerViewLevel.setLayoutManager(gridLayoutManager);
        rcAdapter = new GridViewLevelAdapter(HomeActivity.this, stageItems, currentLevel);
        recyclerViewLevel.setAdapter(rcAdapter);
        recyclerViewLevel.addItemDecoration(new GridSpacingItemDecoration(column, spacing, includeEdge));
    }

    private void setDefaultDataLevel() {
        stageItems = StaticVariable.getInstance().getAllStage();

    }

    private void setUpView() {
        recyclerViewLevel = (RecyclerView) findViewById(R.id.recyclerview_level);
    }
}
