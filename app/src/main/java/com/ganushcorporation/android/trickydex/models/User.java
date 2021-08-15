package com.ganushcorporation.android.trickydex.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class User {

    public String firstname, lastname, email;
    public Boolean admin;
    public Map<String, Object> listDone;
    public int Slide, Grab, Air, trickList;


    public User(){

    }

    public User(String firstname, String lastname, String email){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = false;

    }

    public User(String firstname, String lastname, String email, Map<String, Object> listDone, int Slide, int Grab, int Air){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = false;
        this.listDone = listDone;
        this.Slide = Slide;
        this.Air = Air;
        this.Grab = Grab;

    }

    public User(Map<String, Object> listDone){
        this.listDone = listDone;
    }




}
