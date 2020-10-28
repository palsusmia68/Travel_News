package com.Surajtechstudio.smartguidebengali;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
       // SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
       // editor.putString(TAG_TOKEN, token);
        //editor.apply();
        ////////////////     my method       ///////////////////
        Toast.makeText(mCtx,""+token,Toast.LENGTH_LONG).show();
       SharedPreferences  pref = mCtx.getSharedPreferences(SHARED_PREF_NAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mm=sharedPreferences.getString(TAG_TOKEN,null);
       // Toast.makeText(mCtx,""+mm,Toast.LENGTH_LONG).show();
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }
}
