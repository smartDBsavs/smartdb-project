package smartdbsavs.com.smartdb_demo.utils;

/**
 * Created by hi on 24-02-2018.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import smartdbsavs.com.smartdb_demo.constant.AppConstants;

public class PrefHelper {


    private SharedPreferences sharedPreferences;
    String TAG = PrefHelper.class.getSimpleName();
    private static PrefHelper instance;
    private Context mContext;


    public static PrefHelper getInstance(Context pContext)
    {
//        if (null == instance) {
            instance = new PrefHelper(pContext);
            return instance;
//        }
//

    }

    private PrefHelper(Context pContext)
    {
    mContext = pContext;
    sharedPreferences = mContext.getSharedPreferences(AppConstants.SMART_PREFERENCE, Context.MODE_PRIVATE);

    }

    public String getRegistrationId() {
    String registrationId = sharedPreferences.getString(AppConstants.PREFS_REGID, null);
    return registrationId;
    }

    public void setRegistationId(String registrationId)
    {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(AppConstants.PREFS_REGID, registrationId);
    editor.apply();
    }

    public void setStatus(Boolean bool)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AppConstants.CHECK_STATUS,bool);
        editor.apply();
    }

    public Boolean getStatusId() {
        Boolean status = sharedPreferences.getBoolean(AppConstants.CHECK_STATUS, false);
        return status;
    }

    public void setNotifStatusKey(String str)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.CHECK_NOTISTATUS_KEY,str);
        editor.apply();
    }

    public String getNotifStatusKey() {
        String key = sharedPreferences.getString(AppConstants.CHECK_NOTISTATUS_KEY, null);
        return key;
    }

    public void setNotifStatusMsg(String str)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.CHECK_NOTISTATUS_MSG,str);
        editor.apply();
    }

    public String getNotifStatusMsg() {
        String msg = sharedPreferences.getString(AppConstants.CHECK_NOTISTATUS_MSG, null);
        return msg;
    }

    public void setNotifStatusUrl(String str)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.CHECK_NOTISTATUS_URL,str);
        editor.apply();
    }

    public String getNotifStatusUrl() {
        String url = sharedPreferences.getString(AppConstants.CHECK_NOTISTATUS_URL, null);
        return url;
    }

    public int getMemId() {
        int memId = sharedPreferences.getInt(AppConstants.PREFS_REGID, 0);
        return memId;
    }

    public void setMemId(int memId)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.PREFS_REGID, memId);
        editor.apply();
    }

    }
