package com.etuloser.padma.rohit.homework09a;


import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rohit on 4/21/2017.
 */

public class message {

    private String msg;
    private String mimgurl;
    private String userid;
    private String msgkey;

    public String getMsgkey() {
        return msgkey;
    }

    public void setMsgkey(String msgkey) {
        this.msgkey = msgkey;
    }

    private String name;
    private String when;
    ArrayList<String> deletedusers;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMimgurl() {
        return mimgurl;
    }

    public void setMimgurl(String mimgurl) {
        this.mimgurl = mimgurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public ArrayList<String> getDeletedusers() {
        return deletedusers;
    }

    public void setDeletedusers(ArrayList<String> deletedusers) {
        this.deletedusers = deletedusers;
    }
}
