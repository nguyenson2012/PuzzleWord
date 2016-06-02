package com.example.asus.puzzleword.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.asus.puzzleword.R;
import com.example.asus.puzzleword.StaticVariable;
import com.example.asus.puzzleword.adapter.FragmentDrawer;
import com.example.asus.puzzleword.adapter.GridSpacingItemDecoration;
import com.example.asus.puzzleword.adapter.GridViewLevelAdapter;
import com.example.asus.puzzleword.adapter.RecyclerItemClickListener;
import com.example.asus.puzzleword.model.Stage;

import java.util.ArrayList;

/**
 * Created by Asus on 5/16/2016.
 */
public class HomeActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    int column = 2;
    int spacing = 0;
    boolean includeEdge = false;
    GridViewLevelAdapter rcAdapter;
    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
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
                            intent.putExtra("levelposition", position);
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        recyclerViewLevel = (RecyclerView) findViewById(R.id.recyclerview_level);
        mFragmentManager = getFragmentManager();
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);

    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0://Home
                break;
            case 1://Share
                shareApp();
                break;
            case 2://About
                showInfomationApp();
                break;
            case 3://feedback
                sendEmail();

            default:
                break;
        }
    }

    private void sendEmail() {
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.developer_email));
        email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject_email));
        try {
            startActivity(Intent.createChooser(email, "Choose an email client from..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(HomeActivity.this, "No email client installed.",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void showInfomationApp() {

    }

    private void shareApp() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.link_google_play_for_share));

        startActivity(Intent.createChooser(share, getResources().getString(R.string.share_app_title)));
    }
}
