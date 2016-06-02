package com.example.asus.puzzleword.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.puzzleword.R;
import com.example.asus.puzzleword.adapter.FragmentDrawer;

/**
 * Created by Asus on 6/2/2016.
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener,FragmentDrawer.FragmentDrawerListener{
    private Button buttonResume,buttonLevel;
    private Button buttonRate,buttonShare,buttonLike;
    private int currentLevel;
    private String prefName="data";
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        getCurrentLevel();
        setupView();
        registerForEvent();

    }

    private void registerForEvent() {
        buttonResume.setOnClickListener(this);
        buttonLevel.setOnClickListener(this);
        buttonRate.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonLike.setOnClickListener(this);
    }

    private void getCurrentLevel() {
        SharedPreferences pre = getSharedPreferences
                (prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        //editor.clear();
        editor.commit();
        currentLevel = pre.getInt("currentLevel", 1);
    }

    private void setupView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        buttonResume=(Button)findViewById(R.id.button_resume);
        buttonLevel=(Button)findViewById(R.id.button_choose_level);
        buttonRate=(Button)findViewById(R.id.button_rate_app);
        buttonShare=(Button)findViewById(R.id.button_share_app);
        buttonLike=(Button)findViewById(R.id.button_like_app);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        switch (viewId){
            case R.id.button_resume:
                resumeLevel(currentLevel);
                break;
            case R.id.button_choose_level:
                chooseLevel();
                break;
            case R.id.button_rate_app:
                rateApp();
                break;
            case R.id.button_share_app:
                shareApp();
                break;
            case R.id.button_like_app:
                likeApp();
                break;
        }
    }

    private void likeApp() {
    }

    private void rateApp() {

    }

    private void chooseLevel() {
        Intent intent=new Intent(StartActivity.this,HomeActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.right_in, 0);
    }

    public void resumeLevel(int currentLevel){
        Intent intent=new Intent(StartActivity.this,MainActivity.class);
        intent.putExtra("levelposition", currentLevel);
        startActivity(intent);
        //overridePendingTransition(R.anim.right_in, 0);
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
            Toast.makeText(StartActivity.this, "No email client installed.",
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
