package com.yobro.JavaClasses;

import java.io.Serializable;

public class Coordinates implements Serializable {

    //variable declaration
    private String latitude;
    private String longitude;

    //construction
    public Coordinates() { }

    public Coordinates(String latitude, String longitude)
    {
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude =latitude;
    }



    public String getLatitude()
    {
        return latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude=longitude;
    }
}
