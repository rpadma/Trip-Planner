package com.etuloser.padma.rohit.homework09a;

import java.io.Serializable;

/**
 * Created by Rohit on 4/21/2017.
 */

public class friend implements Serializable  {

    private String frdname;
    private String fuid;
    private String pimagurl;
    private String status;   // 0- if it friend, 1 if it pending , 2 if requested.

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrdname() {
        return frdname;
    }

    public void setFrdname(String frdname) {
        this.frdname = frdname;
    }

    public String getFuid() {
        return fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    public String getPimagurl() {
        return pimagurl;
    }

    public void setPimagurl(String pimagurl) {
        this.pimagurl = pimagurl;
    }


}
