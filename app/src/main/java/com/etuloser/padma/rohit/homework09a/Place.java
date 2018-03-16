package com.etuloser.padma.rohit.homework09a;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Rohit on 5/1/2017.
 */

public class Place implements Serializable {

    private  String placename;
    private String addbyname;
private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddbyname() {
        return addbyname;
    }

    public void setAddbyname(String addbyname) {
        this.addbyname = addbyname;
    }


private String AddedbyUid;

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }


    public String getAddedbyUid() {
        return AddedbyUid;
    }

    public void setAddedbyUid(String addedbyUid) {
        AddedbyUid = addedbyUid;
    }
}
