package com.example.fedeortiz.doctor;

/**
 * Created by fede on 3/5/18.
 */

public class Patient {
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mZipcode;
    private String mPhoneNumber;
    private String mChiefComplaint;
    private String mOnset;

    public Patient (String firstname, String lastname, String email, String address, String city, String state, String zipcode, String phoneNumber, String chiefComplaint, String onset) {
        mFirstName = firstname;
        mLastName = lastname;
        mEmail = email;
        mAddress = address;
        mCity = city;
        mState = state;
        mZipcode = zipcode;
        mPhoneNumber = phoneNumber;
        mChiefComplaint = chiefComplaint;
        mOnset = onset;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmZipcode() {
        return mZipcode;
    }

    public void setmZipcode(String mZipcode) {
        this.mZipcode = mZipcode;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmChiefComplaint() {
        return mChiefComplaint;
    }

    public void setmChiefComplaint(String mChiefComplaint) {
        this.mChiefComplaint = mChiefComplaint;
    }

    public String getmOnset() {
        return mOnset;
    }

    public void setmOnset(String mOnset) {
        this.mOnset = mOnset;
    }
}
