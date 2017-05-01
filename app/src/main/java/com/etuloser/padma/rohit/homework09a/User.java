package com.etuloser.padma.rohit.homework09a;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Rohit on 4/20/2017.
 */

public class User implements Serializable {

    private String firstname;
    private String lastname;
    private String gender;
    private String imgurl;
    private String email;
    private String uid;
    private String key;

    ArrayList<friend> flist;
    ArrayList<String> subltrip;

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender='" + gender + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public ArrayList<friend> getFlist() {
        return flist;
    }

    public void setFlist(ArrayList<friend> flist) {
        this.flist = flist;
    }

    public ArrayList<String> getSubltrip() {
        return subltrip;
    }

    public void setSubltrip(ArrayList<String> subltrip) {
        this.subltrip = subltrip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

/*
    public User(HashMap<String,Objects> map) {

        this.firstname=map.get("firstname").toString();
         this.lastname=map.get("lastname").toString();
         this.gender=map.get("gender").toString();
         this.imgurl=map.get("imgurl").toString();
         this.email=map.get("email").toString();
         this.uid=map.get("uid").toString();
         this.key=map.get("key").toString();



    }*/
}


