package com.example.asus.puzzleword.model;

/**
 * Created by ThangDuong on 12-May-16.
 */
public class WordObject {
    public static final int HORIZONTAL = 0; //ngang
    public static final int VERTICAL = 1;
    public static final int NOT_CLICKED = -1;
    private static int clickedPositionX;
    private static int clickedPositionY;
    public int startX;
    public int startY;
    private String question;
    private String result;
    private int orientation;
    private String imageLink;

    public WordObject() {
        question = "NOT SETUP YET";
        result = "NOT YET";
        startX = 0;
        startY = 0;
        orientation = HORIZONTAL;
        clickedPositionX = NOT_CLICKED;
        clickedPositionY = NOT_CLICKED;
    }

    public WordObject(int x, int y, String question, String result, int ori, String imageLink) {
        this.question = question;
        this.result = result;
        this.imageLink = imageLink;
        startX = x;
        startY = y;
        orientation = ori;
        clickedPositionX = NOT_CLICKED;
        clickedPositionY = NOT_CLICKED;
    }

    public static int getClickedPositionX() {
        return clickedPositionX;
    }

    public static int getClickedPositionY() {
        return clickedPositionY;
    }

    public boolean isInsideWord(int x, int y) {
        boolean myResult = false;
        if (orientation == HORIZONTAL && startY == y && x >= startX && x < startX + result.length()) {
            myResult = true;
        }
        if (orientation == VERTICAL && startX == x && y >= startY && y < startY + result.length()) {
            myResult = true;
        }
        return myResult;
    }

    public boolean setClickedPosition(int x, int y) {
        boolean myResult = false;
        clickedPositionX = NOT_CLICKED;
        clickedPositionY = NOT_CLICKED;
        if (isInsideWord(x, y)) {
            myResult = true;
            clickedPositionX = x;
            clickedPositionY = y;
        }
        return myResult;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean isCorrect(String[][] data) {
        boolean correct = true;
        char[] temp = result.toCharArray();
        if (orientation == HORIZONTAL) {
            for (int i = 0; i < temp.length; i++) {
                if (data[startX + i][startY] != Character.toString(temp[i])) {
                    correct = false;
                    break;
                }
            }
        } else {
            for (int i = 0; i < temp.length; i++) {
                if (data[startX][startY + i] != Character.toString(temp[i])) {
                    correct = false;
                    break;
                }
            }
        }
        return correct;
    }

    public String[][] clearWrong(String[][] data) // Return data and only clear this question
    {
        String[][] myResult = data;
        char[] temp = result.toCharArray();

        if (orientation == HORIZONTAL) {
            for (int i = 0; i < temp.length; i++) {
                myResult[startX + i][startY] = "";
            }
        } else {
            for (int i = 0; i < temp.length; i++) {
                myResult[startX][startY + i] = "";
            }
        }
        return myResult;
    }
}
