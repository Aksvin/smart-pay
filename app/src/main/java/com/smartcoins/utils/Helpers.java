package com.smartcoins.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class Helpers
{


    public static boolean getSharedPreferencesBoolean(Context paramContext, String paramString, boolean paramBoolean)
    {
        return PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean(paramString, paramBoolean);
    }

    public static float getSharedPreferencesFloat(Context paramContext, String paramString, float paramFloat)
    {
        return PreferenceManager.getDefaultSharedPreferences(paramContext).getFloat(paramString, paramFloat);
    }

    public static int getSharedPreferencesInt(Context paramContext, String paramString, int paramInt)
    {
        return PreferenceManager.getDefaultSharedPreferences(paramContext).getInt(paramString, paramInt);
    }

    public static long getSharedPreferencesLong(Context paramContext, String paramString, long paramLong)
    {
        return PreferenceManager.getDefaultSharedPreferences(paramContext).getLong(paramString, paramLong);
    }

    public static String getSharedPreferencesString(Context paramContext, String paramString1, String paramString2)
    {
        return PreferenceManager.getDefaultSharedPreferences(paramContext).getString(paramString1, paramString2);
    }


    public static void putSharedPreferencesBoolean(Context paramContext, String paramString, boolean paramBoolean)
    {
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        localEditor.putBoolean(paramString, paramBoolean);
        localEditor.commit();
    }

    public static void putSharedPreferencesFloat(Context paramContext, String paramString, float paramFloat)
    {
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        localEditor.putFloat(paramString, paramFloat);
        localEditor.commit();
    }

    public static void putSharedPreferencesInt(Context paramContext, String paramString, int paramInt)
    {
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        localEditor.putInt(paramString, paramInt);
        localEditor.commit();
    }

    public static void putSharedPreferencesLong(Context paramContext, String paramString, long paramLong)
    {
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        localEditor.putLong(paramString, paramLong);
        localEditor.commit();
    }

    public static void putSharedPreferencesString(Context paramContext, String paramString1, String paramString2)
    {
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        localEditor.putString(paramString1, paramString2);
        localEditor.commit();
    }

    public static class Keys
    {
        public static final String loggedin = "login";
        public static final String phone = "phone";
        public static final String name = "operator";
        public static final String id = "id";
        public static final String api  = "api";


    }

    public static class Constants
    {
        public static final String loggedin = "login";
        public static final String role_admin = "admin";

        public static final String level_teacher = "teacher";
        public static final String level_owner = "owner";
        public static final String level_admin = "admin";

    }

    public static void show(Context c,String str)
    {
        Toast.makeText(c, str, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidphoneNumber(String phone){

        Boolean value=false;

        if (phone.isEmpty()|| phone.length()<10 || phone.length()>13 ) {
            value = false;
        }
        else if(phone.startsWith("065")||phone.startsWith("+25565") ||
                phone.startsWith("067")||phone.startsWith("+25567")||
                phone.startsWith("071")||phone.startsWith("+25571")||
                phone.startsWith("075")||phone.startsWith("+25575")||
                phone.startsWith("074")|| phone.startsWith("+25574")||
                phone.startsWith("076")||phone.startsWith("+25576")||
                phone.startsWith("078")||phone.startsWith("+25578")||
                phone.startsWith("068")||phone.startsWith("+25568")||
                phone.startsWith("069")||phone.startsWith("+25569")||
                phone.startsWith("062")|| phone.startsWith("+25562")||
                phone.startsWith("077")||phone.startsWith("+25577")
                && phone.length()==13){
            value = true;
        }

        return  value;
    }

    public static String formatedNumber(String mobileNumber){
        String number ="";
        int index = mobileNumber.length();

        if (mobileNumber.startsWith("0")){
            number = "255" + number.substring(1, index);

        }if (mobileNumber.startsWith("+255")){
            number = "" + number.substring(1, index);

        } if (mobileNumber.startsWith("255")){
            number = mobileNumber;
        }

        return number;

    }

}
