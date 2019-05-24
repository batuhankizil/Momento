package com.example.batu.momento.Model;

import android.content.Context;

import com.example.batu.momento.Utils.PreferenceUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Users implements Serializable {
    public String userId;
    public String eMail;
    public String profilePhoto;
    public String fullName;
    public String birtday;
    public String gender;
    public String about;
    private transient String password;

    /*public Users getUser(Context context){
        return PreferenceUtils.instance.getSavedObjectFromPreference(context,"user", Users.class);
    }*/

    public Users(){

    }

    public Users(String userId, String eMail, String profilePhoto, String fullName, String birtday, String gender, String about, String password) {
        this.userId = userId;
        this.eMail = eMail;
        this.profilePhoto = profilePhoto;
        this.fullName = fullName;
        this.birtday = birtday;
        this.gender = gender;
        this.about = about;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirtday() {
        return birtday;
    }

    public void setBirtday(String birtday) {
        this.birtday = birtday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
