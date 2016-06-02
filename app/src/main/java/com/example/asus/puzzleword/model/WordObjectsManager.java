package com.example.asus.puzzleword.model;

import java.util.ArrayList;

/**
 * Created by ThangDuong on 12-May-16.
 */
public class WordObjectsManager {
    private static WordObjectsManager ourInstance = new WordObjectsManager();
    private static boolean isCorrectAll;
    private ArrayList<WordObject> objectArrayList;

    private WordObjectsManager() {
        isCorrectAll = false;
        objectArrayList = new ArrayList<>();
    }

    public static WordObjectsManager getInstance() {
        return ourInstance;
    }

    public static boolean isCorrectAll() {
        return isCorrectAll;
    }

    public void add(WordObject wordObject) {
        objectArrayList.add(wordObject);
    }

    public WordObject get(int position) {
        return objectArrayList.get(position);
    }

    public String[][] checkResult(String[][] data) {
        String[][] myResult = data;
        isCorrectAll = true;
        for (int i = 0; i < objectArrayList.size(); i++) {
            if (!objectArrayList.get(i).isCorrect(data)) {
                myResult = objectArrayList.get(i).clearWrong(data);
                isCorrectAll = false;
            }
        }
        return myResult;
    }

    public WordObject getObjectAt(int x, int y) {
        WordObject obj = null;
        for (int i = 0; i < objectArrayList.size(); i++) {
            if (objectArrayList.get(i).isInsideWord(x, y)) {
                obj = objectArrayList.get(i);
                if (objectArrayList.get(i).startX == x && objectArrayList.get(i).startY == y)//return if click the first letter of word
                    return obj;
            }
        }
        return obj;
    }

    public ArrayList<WordObject> getObjectArrayList() {
        return objectArrayList;
    }

    public void setObjectArrayList(ArrayList<WordObject> objectArrayList) {
        this.objectArrayList = objectArrayList;
    }
}
