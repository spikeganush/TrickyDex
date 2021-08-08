package com.ganushcorporation.android.trickydex.models;

public class Trick {

    public String name, info, category;
    public int difficulty;

    public Trick(){

    }

    public Trick(String name, String category, String info, int difficulty){
        this.name = name;
        this.category = category;
        this.info = info;
        this.difficulty = difficulty;

    }

}
