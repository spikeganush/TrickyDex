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
    public int slide, grab, air, trickList;


    public User(){

    }

    public User(String firstname, String lastname, String email){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = false;

    }

    public User(String firstname, String lastname, String email, Map<String, Object> listDone, int slide){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = false;
        this.listDone = listDone;
        this.slide = slide;

    }

//    public User(Map<String, Object> listDone){
//        this.listDone = listDone;
//    }

//    public User(int slide){
//        this.slide = slide;
//    }


}
