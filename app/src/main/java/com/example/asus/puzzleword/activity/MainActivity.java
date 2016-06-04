package com.example.asus.puzzleword.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.puzzleword.R;
import com.example.asus.puzzleword.StaticVariable;
import com.example.asus.puzzleword.adapter.GridviewAdapter;
import com.example.asus.puzzleword.model.WordObject;
import com.example.asus.puzzleword.model.WordObjectsManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class MainActivity extends Activity implements GridviewAdapter.OnItemGridViewClick {
    public static final int AD_HEIGHT = 50;
    public static final int NUM_OF_COLLUMN = 10;
    public static final int NUM_OF_ROW = NUM_OF_COLLUMN;
    public static final int MAX_NUM_OF_KEYBOARD_BTN_PER_ROW = 10;
    private static GridView gridView;
//    public int PARENT_VERTICAL_MARGIN;
    private int LINE_HEIGHT;
    private int BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT;
    private int BTN_KEYBOARD_EDGE_SIZE;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private TextView txtView_question;
    private ImageView imgView_question;
//    private Context context;
    private KeyboardButton[] keyboard_btn;
    private ImageButton del_btn;
    private String[][] gridViewData = new String[NUM_OF_COLLUMN][NUM_OF_ROW];//gridViewData[x][y]
    private WordObjectsManager objManger = WordObjectsManager.getInstance();
    private GridviewAdapter adapter;
    private ImageButton btCheckAnswer;
//    private Button btSolve;
    private ImageButton btClear;
    private DisplayImageOptions opt;
    private ImageLoader imageLoader;
    private AdView mAdView;
    //    private ArrayList<WordObject> listQuestion;
    private ArrayList<Bitmap> listBitmapImageQuestion;
    private StaticVariable staticVariable;
    private String prefName = "data";
    private int doneLevel;
    private int currentLevel;
    private int timeStartLevel = 0;
    private int timeCompleteLevel = 0;
    private boolean allLevelDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPlayGame();
        staticVariable = StaticVariable.getInstance();
        getLevelPosition();
        getDoneLevel();
        timeStartLevel = (int) (System.currentTimeMillis() / 1000);
//        context = this;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setMinimumHeight(AD_HEIGHT);

        initImageLoader(getApplicationContext());
        setupImageDisplayOptions();
        setupGridView();
        //TODO
//        LINE_HEIGHT = screenWidth / 13;
        LINE_HEIGHT = (screenHeight - gridView.getMinimumHeight()
                - mAdView.getMinimumHeight())/5;
        BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT = LINE_HEIGHT/8; // If need to adjust, change here
        BTN_KEYBOARD_EDGE_SIZE = LINE_HEIGHT-2*BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT;
        //If the size above is not correct
        if(BTN_KEYBOARD_EDGE_SIZE*MAX_NUM_OF_KEYBOARD_BTN_PER_ROW+
                BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT*(MAX_NUM_OF_KEYBOARD_BTN_PER_ROW+1)>screenWidth)
        {
            int temp = screenWidth/((MAX_NUM_OF_KEYBOARD_BTN_PER_ROW));
            BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT = temp/8; // If need to adjust, change here
            BTN_KEYBOARD_EDGE_SIZE = temp-2*BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT;
        }

        txtView_question = (TextView) findViewById(R.id.textViewQuestion);
        imgView_question = (ImageView) findViewById(R.id.imageView);

        setupKeyboard();

        getListBitmapImageStage();
        //getListBitmapImageStage();
        onItemGridViewClick(objManger.get(0).startX + objManger.get(0).startY * NUM_OF_COLLUMN);
    }

    private void getDoneLevel() {
        SharedPreferences pre = getSharedPreferences
                (prefName, MODE_PRIVATE);
        doneLevel=pre.getInt("doneLevel",0);
    }

    private void setPlayGame() {
        SharedPreferences pre = getSharedPreferences
                (prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        editor.putBoolean("PlayGame",true);
        editor.commit();
    }


    private void getListBitmapImageStage() {
        listBitmapImageQuestion = new ArrayList<Bitmap>();
        listBitmapImageQuestion = StaticVariable.getBitMapImageStageMap().get(new Integer(doneLevel+1));
        if (listBitmapImageQuestion.size() == 0)
            new ImageDownloader().execute("");
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(6 * 1024 * 1024); // 6 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        // Initialize ImageLoader with configuration.
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config.build());
    }

    private void setupKeyboard() {
        keyboard_btn = new KeyboardButton[27];
        keyboard_btn[0] = new KeyboardButton((Button) findViewById(R.id.btnQ), "Q");
        keyboard_btn[1] = new KeyboardButton((Button) findViewById(R.id.btnW), "W");
        keyboard_btn[2] = new KeyboardButton((Button) findViewById(R.id.btnE), "E");
        keyboard_btn[3] = new KeyboardButton((Button) findViewById(R.id.btnR), "R");
        keyboard_btn[4] = new KeyboardButton((Button) findViewById(R.id.btnT), "T");
        keyboard_btn[5] = new KeyboardButton((Button) findViewById(R.id.btnY), "Y");
        keyboard_btn[6] = new KeyboardButton((Button) findViewById(R.id.btnU), "U");
        keyboard_btn[7] = new KeyboardButton((Button) findViewById(R.id.btnI), "I");
        keyboard_btn[8] = new KeyboardButton((Button) findViewById(R.id.btnO), "O");
        keyboard_btn[9] = new KeyboardButton((Button) findViewById(R.id.btnP), "P");
        keyboard_btn[10] = new KeyboardButton((Button) findViewById(R.id.btnA), "A");
        keyboard_btn[11] = new KeyboardButton((Button) findViewById(R.id.btnS), "S");
        keyboard_btn[12] = new KeyboardButton((Button) findViewById(R.id.btnD), "D");
        keyboard_btn[13] = new KeyboardButton((Button) findViewById(R.id.btnF), "F");
        keyboard_btn[14] = new KeyboardButton((Button) findViewById(R.id.btnG), "G");
        keyboard_btn[15] = new KeyboardButton((Button) findViewById(R.id.btnH), "H");
        keyboard_btn[16] = new KeyboardButton((Button) findViewById(R.id.btnJ), "J");
        keyboard_btn[17] = new KeyboardButton((Button) findViewById(R.id.btnK), "K");
        keyboard_btn[18] = new KeyboardButton((Button) findViewById(R.id.btnL), "L");
        keyboard_btn[19] = new KeyboardButton((Button) findViewById(R.id.btnZ), "Z");
        keyboard_btn[20] = new KeyboardButton((Button) findViewById(R.id.btnX), "X");
        keyboard_btn[21] = new KeyboardButton((Button) findViewById(R.id.btnC), "C");
        keyboard_btn[22] = new KeyboardButton((Button) findViewById(R.id.btnV), "V");
        keyboard_btn[23] = new KeyboardButton((Button) findViewById(R.id.btnB), "B");
        keyboard_btn[24] = new KeyboardButton((Button) findViewById(R.id.btnN), "N");
        keyboard_btn[25] = new KeyboardButton((Button) findViewById(R.id.btnM), "M");
//        keyboard_btn[26] = new KeyboardButton((Button) findViewById(R.id.btnDel), "Del");

        btCheckAnswer = (ImageButton) findViewById(R.id.btcheckAnswer);

//        btSolve = (Button) findViewById(R.id.btSolve);
        btClear = (ImageButton) findViewById(R.id.btClear);
//        txtView_question=(TextView)findViewById(R.id.textViewQuestion);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BTN_KEYBOARD_EDGE_SIZE+BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT
                , BTN_KEYBOARD_EDGE_SIZE+BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT);
        params.setMargins(0,0,BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT,BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT);

        btCheckAnswer.setLayoutParams(params);
//        btSolve.setLayoutParams(params);
        btClear.setLayoutParams(params);

        //TODO
        for (int i = 0; i < keyboard_btn.length-1; i++) {//exclude button del
            keyboard_btn[i].button = setLayoutButton(keyboard_btn[i].button
                    , BTN_KEYBOARD_EDGE_SIZE, BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT);
        }

        //Setup for Del button
        del_btn = (ImageButton)findViewById(R.id.btnDel);
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    gridViewData[WordObject.getClickedPositionX()][WordObject.getClickedPositionY()] = "";
                    adapter.backClickedPosition();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("MainActivity", "Not Clicked any cell yet.");
                }
            }
        });
        del_btn = setLayoutImageButton(del_btn, BTN_KEYBOARD_EDGE_SIZE, BTN_KEYBOARD_MARGIN_LEFT_AND_RIGHT);

        setOnTouchKeyboard();
    }

    private ImageButton setLayoutImageButton(ImageButton v, int edgeSize, int margin_left_and_right) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(edgeSize, edgeSize);
        params.setMargins(margin_left_and_right, 0, margin_left_and_right, 0);
        v.setLayoutParams(params);
        return v;
    }

    private Button setLayoutButton(Button v, int edgeSize, int margin_left_and_right) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(edgeSize, edgeSize);
        params.setMargins(margin_left_and_right, 0, margin_left_and_right, 0);
        v.setLayoutParams(params);
        return v;
    }

    private void setOnTouchKeyboard() {
        for (int i = 0; i < keyboard_btn.length-1; i++) {//exclude button del
            setOnClickEachButton(keyboard_btn[i]);
        }
        btCheckAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAnswer()) {
                    Toast.makeText(getApplicationContext(), "You Win", Toast.LENGTH_SHORT).show();
                    updateTimeCompleteLevel(doneLevel);
                    increaseLevel();
                    //initializeQuestion();
                    if (!allLevelDone)
                        setupGridView();
                    getListBitmapImageStage();
                    onItemGridViewClick(objManger.get(0).startX + objManger.get(0).startY * NUM_OF_COLLUMN);
                    timeStartLevel = (int) (System.currentTimeMillis() / 1000);
                } else
                    Toast.makeText(getApplicationContext(), "You Lose", Toast.LENGTH_SHORT).show();
            }
        });
        //TODO
        /*btSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveLevel();
            }
        });*/
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLevel();
            }
        });
//        adapter.notifyDataSetChanged();
    }

    private void updateTimeCompleteLevel(int doneLevel) {
//        Calendar calendar=Calendar.getInstance();
//        int timestopLevel=calendar.get(Calendar.SECOND);
        int timestopLevel = (int) (System.currentTimeMillis() / 1000);
        timeCompleteLevel = timestopLevel - timeStartLevel;
        staticVariable.getAllStage().get(doneLevel).setSecondComplete(timeCompleteLevel);
        SharedPreferences pre = getSharedPreferences
                (prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt(staticVariable.getAllStage().get(doneLevel).getDescriptionStage() + "", timeCompleteLevel);
        editor.commit();
        timeStartLevel = 0;
        timeCompleteLevel = 0;
    }

    private void setOnClickEachButton(final KeyboardButton kbtn) {
        if (kbtn.key != "Del") {
            kbtn.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        gridViewData[WordObject.getClickedPositionX()][WordObject.getClickedPositionY()] = kbtn.key;
                        adapter.nextClickedPosition();
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("MainActivity", "Not Clicked any cell yet.");
                    }
                }
            });
        } else {
            kbtn.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        gridViewData[WordObject.getClickedPositionX()][WordObject.getClickedPositionY()] = "";
                        adapter.backClickedPosition();
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("MainActivity", "Not Clicked any cell yet.");
                    }
                }
            });
        }
    }

    private void initializeQuestion() {
        objManger.setObjectArrayList(new ArrayList<WordObject>());
//        objManger.add(new WordObject(1, 0, "I .... a hamburger", "EAT", WordObject.VERTICAL, "http://i.imgur.com/8qu7nQk.png"));
//        objManger.add(new WordObject(1, 1, "I have an ", "ARMY", WordObject.HORIZONTAL, "http://i.imgur.com/eluOFW6.png"));
//        objManger.add(new WordObject(0, 0, "I ..... a book", "READ", WordObject.HORIZONTAL, "http://i.imgur.com/AlGqiAD.jpg"));
//        objManger.add(new WordObject(0, 2, "...... up!", "STAND", WordObject.HORIZONTAL, "http://i.imgur.com/vOnIjew.jpg"));
        objManger.setObjectArrayList(staticVariable.getAllStage().get(currentLevel - 1).getListQuestion());
//        listQuestion = objManger.getObjectArrayList();
    }

    private void setupGridView() {
        initializeQuestion();
        //Reset gridview
        for (int i = 0; i < gridViewData.length; i++) {
            for (int j = 0; j < gridViewData[0].length; j++)//the board is rectangular
            {
                WordObject temp = objManger.getObjectAt(i, j);
                if (temp != null) {
                    gridViewData[i][j] = GridviewAdapter.ENABLE;
//                    Log.e("OBJ","i,j = "+i+" , "+j);
                } else {
                    gridViewData[i][j] = GridviewAdapter.DISABLE;
//                    Log.e("NULL","i,j = "+i+" , "+j);
                }
            }
        }

        adapter = new GridviewAdapter(this, gridViewData);
        adapter.setUpListWord(objManger.getObjectArrayList());
        gridView = (GridView) findViewById(R.id.layout_gridview);
        gridView.setMinimumHeight(screenWidth);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(NUM_OF_COLLUMN);
//        gridView.setColumnWidth((int) ((gridView.getWidth() / NUM_OF_COLLUMN) * 0.9));
        gridView.setMinimumWidth(screenWidth);
        setGridViewBG();
    }

    private void setGridViewBG() {
//        Drawable bgDrawable = ResourcesCompat.getDrawable(getResources(),R.drawable.gridview_bg,null);//Scale the images
//        Bitmap bitmap = ((BitmapDrawable) bgDrawable).getBitmap();
//        Drawable drawable = new BitmapDrawable(
//                getResources(), Bitmap.createScaledBitmap(bitmap,
//                bitmap.getWidth() / 4,
//                bitmap.getHeight() / 4,
//                true)
//        );
//        gridView.setBackground(drawable);
        //gridView.setBackgroundResource(R.drawable.background);
        imageLoader.loadImage("http://i.imgur.com/Owtf9Dr.png", new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                gridView.setBackground(new BitmapDrawable(loadedImage));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
    public void onItemGridViewClick(int position) {
        int positionX = position % NUM_OF_COLLUMN;
        int positionY = position / NUM_OF_COLLUMN;
//        changeQuestion(positionX,positionY);
        txtView_question.setText(objManger.getObjectAt(positionX, positionY).getQuestion());
        imageLoader.displayImage(objManger.getObjectAt(positionX, positionY).getImageLink(), imgView_question, opt, new SimpleImageLoadingListener() {

        });

        /*for (int i = 0; i < listQuestion.size(); i++) {
            WordObject wordObject = listQuestion.get(i);
            if (wordObject.getResult().equals(objManger.getObjectAt(positionX, positionY).getResult())) {
                //imgView_question.setImageBitmap(listBitmapImageQuestion.get(i));
                imageLoader.displayImage(wordObject.getImageLink(), imgView_question, opt, new SimpleImageLoadingListener() {
                });
            }
        }*/
//        Log.e("LOL","onItemGridViewClick");
    }

    private boolean checkAnswer() {
        boolean checkAnswer = true;
        for (WordObject question : objManger.getObjectArrayList()) {
            String answer = "";
            int firstX = question.startX;
            int firstY = question.startY;
            if (question.getOrientation() == WordObject.HORIZONTAL) {
                for (int i = 0; i < question.getResult().length(); i++) {
                    answer = answer.concat(gridViewData[firstX + i][firstY]);
                }
                if (!answer.equals(question.getResult()))
                    checkAnswer = false;
            } else {
                for (int i = 0; i < question.getResult().length(); i++) {
                    answer = answer.concat(gridViewData[firstX][firstY + i]);
                }
                if (!answer.equals(question.getResult()))
                    checkAnswer = false;
            }

        }
        return checkAnswer;
    }

    /*private void solveLevel() {
        for (WordObject question : objManger.getObjectArrayList()) {
            String answer = question.getResult();
            int firstX = question.startX;
            int firstY = question.startY;
            if (question.getOrientation() == WordObject.HORIZONTAL) {
                for (int i = 0; i < question.getResult().length(); i++) {
                    gridViewData[firstX + i][firstY] = answer.substring(i, i + 1);
                }
            } else {
                for (int i = 0; i < question.getResult().length(); i++) {
                    gridViewData[firstX][firstY + i] = answer.substring(i, i + 1);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }*/

    private void clearLevel() {
        for (WordObject question : objManger.getObjectArrayList()) {
            int firstX = question.startX;
            int firstY = question.startY;
            if (question.getOrientation() == WordObject.HORIZONTAL) {
                for (int i = 0; i < question.getResult().length(); i++) {
                    gridViewData[firstX + i][firstY] = "";
                }
            } else {
                for (int i = 0; i < question.getResult().length(); i++) {
                    gridViewData[firstX][firstY + i] = "";
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void setupImageDisplayOptions() {
        opt = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_error_48pt)
                .showImageOnFail(R.drawable.ic_error_48pt)
                .showImageOnLoading(R.drawable.loading)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    private void getLevelPosition() {
        Intent intent = getIntent();
        currentLevel = intent.getIntExtra("levelposition", 1);
    }

    private void increaseLevel() {
        SharedPreferences pre = getSharedPreferences
                (prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        if (doneLevel < staticVariable.getAllStage().size()) {
            doneLevel++;
            currentLevel++;
        } else {
            allLevelDone = true;
        }
        if(doneLevel>pre.getInt("doneLevel",0)) {
            editor.putInt("doneLevel", doneLevel);
            editor.putInt("currentLevel",currentLevel);
            editor.commit();
        }
    }


    class KeyboardButton {
        Button button;
        String key;

        KeyboardButton(Button btn, String k) {
            button = btn;
            key = k;
        }
    }

    class ImageDownloader extends AsyncTask<String, Void, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... param) {
            listBitmapImageQuestion = new ArrayList<Bitmap>();
            // TODO Auto-generated method stub
            for (WordObject wordObject : objManger.getObjectArrayList()) {
                imageLoader.loadImage(wordObject.getImageLink(), opt, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        StaticVariable.getBitMapImageStageMap().get(new Integer(doneLevel+1)).add(loadedImage);
                        listBitmapImageQuestion.add(loadedImage);
                    }
                });
            }
            return listBitmapImageQuestion;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> result) {
            Log.i("Async-Example", "onPostExecute Called");

        }
    }

}