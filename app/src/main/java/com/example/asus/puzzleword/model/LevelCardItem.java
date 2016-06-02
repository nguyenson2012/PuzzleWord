package com.example.asus.puzzleword.model;

/**
 * Created by Asus on 5/16/2016.
 */
public class LevelCardItem {
    private String linkImage;
    private String levelTitle;

    public LevelCardItem(String linkImage, String levelTitle) {
        this.linkImage = linkImage;
        this.levelTitle = levelTitle;
    }

    public LevelCardItem() {

    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public void setLevelTitle(String levelTitle) {
        this.levelTitle = levelTitle;
    }
}
