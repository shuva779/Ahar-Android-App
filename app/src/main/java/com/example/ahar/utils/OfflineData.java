package com.example.ahar.utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Written by Istiak Saif on 17/04/21.
 */
public class OfflineData extends Application {
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
