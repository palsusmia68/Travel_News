package com.Surajtechstudio.smartguidebengali;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class isInternetAvailable {

    private static final String TAG = isInternetAvailable.class.getSimpleName();



    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Toast.makeText(context,"Please Check your Internet Connection",Toast.LENGTH_LONG);
            return false;
        }
        else
        {
            if(info.isConnected())
            {

                return true;
            }
            else
            {

                return true;
            }

        }
    }
}
