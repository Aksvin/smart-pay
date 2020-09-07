package com.smartcoins.utils;


import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkCheck
{
    public static boolean isNetworkAvailable(Context paramContext)
    {
        ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
        return (localConnectivityManager.getActiveNetworkInfo() != null) && (localConnectivityManager.getActiveNetworkInfo().isConnected());
    }
}