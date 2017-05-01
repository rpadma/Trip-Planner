package com.etuloser.padma.rohit.homework09a;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Rohit on 4/21/2017.
 */

public class trip implements Serializable{

    private String tripname,tripLocationname,timgurl,towner,status,tkey;
     private String townername;
    private String createdate;

    private ArrayList<message> chatobjs;

    public ArrayList<message> getChatobjs() {
        return chatobjs;
    }

    public void setChatobjs(ArrayList<message> chatobjs) {
        this.chatobjs = chatobjs;
    }

    private ArrayList<String> members;

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getTownername() {
        return townername;
    }

    public void setTownername(String townername) {
        this.townername = townername;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    ArrayList<message> chat;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTkey() {
        return tkey;
    }

    public void setTkey(String tkey) {
        this.tkey = tkey;
    }

    public ArrayList<message> getChat() {
        return chat;
    }

    public void setChat(ArrayList<message> chat) {
        this.chat = chat;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    public String getTripLocationname() {
        return tripLocationname;
    }

    public void setTripLocationname(String tripLocationname) {
        this.tripLocationname = tripLocationname;
    }

    public String getTimgurl() {
        return timgurl;
    }

    public void setTimgurl(String timgurl) {
        this.timgurl = timgurl;
    }

    public String getTowner() {
        return towner;
    }

    public void setTowner(String towner) {
        this.towner = towner;
    }
}
