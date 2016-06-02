package com.example.asus.puzzleword;

import android.graphics.Bitmap;

import com.example.asus.puzzleword.model.Stage;
import com.example.asus.puzzleword.model.WordObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Asus on 5/30/2016.
 */
public class StaticVariable {
    private static StaticVariable instance;
    private static ArrayList<Stage> allStage;
    private static HashMap<Integer, ArrayList<Bitmap>> bitMapImageStageMap;

    public StaticVariable() {

        allStage = new ArrayList<Stage>();
        bitMapImageStageMap = new HashMap<Integer, ArrayList<Bitmap>>();
        loadAllStage();
        loadbitMapImageStage();
    }

    public static StaticVariable getInstance() {
        if (instance == null) {
            instance = new StaticVariable();
        }
        return instance;
    }

    public static HashMap<Integer, ArrayList<Bitmap>> getBitMapImageStageMap() {
        return bitMapImageStageMap;
    }

    private void loadbitMapImageStage() {

        for (int i = 1; i <= allStage.size(); i++) {
            ArrayList<Bitmap> listBitmapImageStage = new ArrayList<Bitmap>();
            bitMapImageStageMap.put(i, listBitmapImageStage);
        }
    }

    public void loadAllStage() {
        addStageAction();
        addStageAnimal();
        addStageFruit();
    }

    private void addStageFruit() {
        ArrayList<WordObject> listQuestion = new ArrayList<WordObject>();
        listQuestion.add(new WordObject(2, 0, "What fruit is it?", "GRAPES", WordObject.VERTICAL, "http://i.imgur.com/Br53yts.png",1));
        listQuestion.add(new WordObject(2, 2, "What fruit is it?", "APPLE", WordObject.HORIZONTAL, "http://i.imgur.com/PARXizB.png",1));
        listQuestion.add(new WordObject(5, 2, "What fruit is it?", "LEMON", WordObject.VERTICAL, "http://i.imgur.com/byGWcLj.png",2));
        listQuestion.add(new WordObject(3, 6, "What fruit is it?", "BANANA", WordObject.HORIZONTAL, "http://i.imgur.com/JPjPc9l.png",2));
        listQuestion.add(new WordObject(8, 4, "What fruit is it?", "PEACH", WordObject.VERTICAL, "http://i.imgur.com/cWynn4H.png",3));
        Stage stageFruit = new Stage("http://i.imgur.com/B9YVtuG.png", 3, listQuestion, "Fruits",0);
        allStage.add(stageFruit);
    }

    private void addStageAnimal() {
        ArrayList<WordObject> listQuestion = new ArrayList<WordObject>();
        listQuestion.add(new WordObject(0, 1, "It's a ......", "DUCK", WordObject.HORIZONTAL, "http://i.imgur.com/yDS5nPF.png",1));
        listQuestion.add(new WordObject(2, 1, "It's a ......", "CHICKEN", WordObject.VERTICAL, "http://i.imgur.com/RQYVZfB.png",1));
        listQuestion.add(new WordObject(2, 4, "It's a ......", "COW", WordObject.HORIZONTAL, "http://i.imgur.com/HXNjbls.png",2));
        listQuestion.add(new WordObject(0, 6, "It's a ......", "SHEEP", WordObject.HORIZONTAL, "http://i.imgur.com/qDXiwyk.png",3));
        listQuestion.add(new WordObject(0, 3, "It's a ......", "MOUSE", WordObject.VERTICAL, "http://i.imgur.com/MKFWSjp.png",2));
        listQuestion.add(new WordObject(4, 6, "It's a ......", "PIG", WordObject.VERTICAL, "http://i.imgur.com/DC3cOkX.jpg",3));
        Stage stageAnimal = new Stage("http://i.imgur.com/Wl3wiPK.png", 2, listQuestion, "Animals on the farm",0);
        allStage.add(stageAnimal);
    }

    private void addStageAction() {
        ArrayList<WordObject> listQuestion = new ArrayList<WordObject>();
        listQuestion.add(new WordObject(1, 0, "I .... a hamburger", "EAT", WordObject.VERTICAL, "http://i.imgur.com/8qu7nQk.png",1));
        listQuestion.add(new WordObject(1, 1, "I have an ", "ARMY", WordObject.HORIZONTAL, "http://i.imgur.com/eluOFW6.png",2));
        listQuestion.add(new WordObject(0, 0, "I ..... a book", "READ", WordObject.HORIZONTAL, "http://i.imgur.com/AlGqiAD.jpg",1));
        listQuestion.add(new WordObject(0, 2, "...... up!", "STAND", WordObject.HORIZONTAL, "http://i.imgur.com/vOnIjew.jpg",3));
        Stage stageAction = new Stage("http://i.imgur.com/oe3PGwC.png", 1, listQuestion, "Action verb",0);
        allStage.add(stageAction);
    }

    public ArrayList<Stage> getAllStage() {
        return allStage;
    }
}
