package com.example.fedeortiz.doctor;

/**
 * Created by fede on 3/6/18.
 */

public class Symptoms {

    private String mSymptoms;
    private String mEmail;

    public Symptoms(String symptoms, String email) {
        mSymptoms = symptoms;
        mEmail = email;
    }

    public String getmSymptoms() {
        return mSymptoms;
    }

    public void setmSymptoms(String mSymptoms) {
        this.mSymptoms = mSymptoms;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }
}
