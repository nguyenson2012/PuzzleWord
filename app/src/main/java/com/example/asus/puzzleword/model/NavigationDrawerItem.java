package com.example.asus.puzzleword.model;

/**
 * Created by SON on 3/17/2016.
 */
public class NavigationDrawerItem {
    String title;
    int image;

    public NavigationDrawerItem() {

    }

    public NavigationDrawerItem(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
