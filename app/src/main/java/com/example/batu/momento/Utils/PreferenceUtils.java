package com.example.batu.momento.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.batu.momento.Model.Users;
import com.google.gson.Gson;

import java.io.Serializable;

public class PreferenceUtils {
    public PreferenceUtils(){

    }

    public static final PreferenceUtils instance = new PreferenceUtils();

    public void saveObject(Context context, String serializedObjectKey, Serializable obj){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        /*String serializedObject = gson.toJson(obj);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);*/
        sharedPreferencesEditor.apply();
    }

    public <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }
    public static boolean saveEmail(String email, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constans.KEY_EMAIL, email);
        prefsEditor.apply();
        return true;
    }

    public static String getEmail(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constans.KEY_EMAIL, null);
    }

    public static boolean savePassword(String password, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constans.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constans.KEY_PASSWORD, null);
    }
}
