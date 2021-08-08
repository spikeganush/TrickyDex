package com.ganushcorporation.android.trickydex.models;

import java.lang.reflect.Array;

public class User {

    public String firstname, lastname, email;
    public Boolean admin;

    public User(){

    }

    public User(String firstname, String lastname, String email){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = false;

    }


}
