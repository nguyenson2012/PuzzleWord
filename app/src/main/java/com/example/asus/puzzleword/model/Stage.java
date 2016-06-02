package com.example.asus.puzzleword.model;

import java.util.ArrayList;

/**
 * Created by Asus on 5/30/2016.
 */
public class Stage {
    private int positionStage;
    private String imageStageLink;
    private ArrayList<WordObject> listQuestion;
    private String descriptionStage;
    private int secondComplete;

    public Stage() {

    }

    public Stage(String imageStageLink, int positionStage, ArrayList<WordObject> listQuestion, String descriptionStage,int secondComplete) {
        this.imageStageLink = imageStageLink;
        this.positionStage = positionStage;
        this.listQuestion = listQuestion;
        this.descriptionStage = descriptionStage;
        this.secondComplete=secondComplete;
    }

    public int getPositionStage() {
        return positionStage;
    }

    public void setPositionStage(int positionStage) {
        this.positionStage = positionStage;
    }

    public String getImageStageLink() {
        return imageStageLink;
    }

    public void setImageStageLink(String imageStageLink) {
        this.imageStageLink = imageStageLink;
    }

    public ArrayList<WordObject> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(ArrayList<WordObject> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public String getDescriptionStage() {
        return descriptionStage;
    }

    public void setDescriptionStage(String descriptionStage) {
        this.descriptionStage = descriptionStage;
    }

    public int getSecondComplete() {
        return secondComplete;
    }

    public void setSecondComplete(int secondComplete) {
        this.secondComplete = secondComplete;
    }
}
