package com.yobro.JavaClasses;

import java.io.Serializable;

public class UserProfile implements Serializable{

    private String personName;
    private String personGivenName ;
    private String personFamilyName;
    private String personEmail;
    private String personId;
    private String personPhoto;
    private String personLatitude;
    private String personLongitude;


    public UserProfile(){

    }


    public UserProfile(String personName, String personGivenName, String personFamilyName, String personEmail, String personId, String personPhoto, String personLatitude, String personLongitude) {
        this.personName = personName;
        this.personGivenName = personGivenName;
        this.personFamilyName = personFamilyName;
        this.personEmail = personEmail;
        this.personId = personId;
        this.personPhoto = personPhoto;
        this.personLatitude = personLatitude;
        this.personLongitude = personLongitude;

    }


    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonGivenName() {
        return personGivenName;
    }

    public void setPersonGivenName(String personGivenName) {
        this.personGivenName = personGivenName;
    }

    public String getPersonFamilyName() {
        return personFamilyName;
    }

    public void setPersonFamilyName(String personFamilyName) {
        this.personFamilyName = personFamilyName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonPhoto() {
        return personPhoto;
    }

    public void setPersonPhoto(String personPhoto) {
        this.personPhoto = personPhoto;
    }

    public String getPersonLatitude() {
        return personLatitude;
    }

    public void setPersonLocation(String personLatitude) {
        this.personLatitude = personLatitude;
    }

    public String getPersonLongitude() {
        return personLongitude;
    }

    public void setPersonLongitude(String personLongitude) {
        this.personLongitude = personLongitude;
    }
}
