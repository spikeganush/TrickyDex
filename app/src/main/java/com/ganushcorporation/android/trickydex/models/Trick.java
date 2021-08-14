package com.ganushcorporation.android.trickydex.models;

import java.util.ArrayList;

public class Trick {

    public String name, info, category, id;
    public int difficulty;
    public boolean isSelected;


    public Trick(){

    }
    public Trick(String name, String category, String info, int difficulty){
        this.name = name;
        this.category = category;
        this.info = info;
        this.difficulty = difficulty;

    }
    public Trick(String id, String name, String category, String info, int difficulty){
        this.id = id;
        this.name = name;
        this.category = category;
        this.info = info;
        this.difficulty = difficulty;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setId(String id) {
        this.id = id;
    }
}
