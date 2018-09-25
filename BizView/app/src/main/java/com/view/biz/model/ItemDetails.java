package com.view.biz.model;

import java.io.Serializable;

/**
 * Created by sharvari on 25-Aug-18.
 */

public class ItemDetails implements Serializable {


    String name;
    int image;

    public ItemDetails(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
