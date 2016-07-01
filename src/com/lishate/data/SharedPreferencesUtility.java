package com.lishate.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtility {
	
	private static final String KEY_MENU = "menu";
	private static final String SHARE_MENU = "menu_position";
	private static final String KEY_RENWU_MENU = "renwu_menu";
	private static final String SHARE_RENWU_MENU = "renwu_menu_position";
	private static final String KEY_VIBRAT = "vibrat";
	private static final String SHARE_VIBRAT = "vibrat_bool";
	private static final String SHARE_LANGUAGE = "language_pos";
	private static final String SHARE_DEVICEID = "deviceid";
	private static final String KEY_DEVICEID = "key_deviceid";

	private static SharedPreferences mMenuPositionPreferences;
	private static SharedPreferences mVibrate;
	private static SharedPreferences mLanguage;
	private static SharedPreferences mDeviceId;
	
	public static void Init(Context context){
		mMenuPositionPreferences = context.getSharedPreferences(SHARE_MENU, 0);
		mVibrate = context.getSharedPreferences(SHARE_VIBRAT, 0);
		mLanguage = context.getSharedPreferences(SHARE_LANGUAGE, 0);
		mDeviceId = context.getSharedPreferences(SHARE_DEVICEID, 0);
	}
	
	public static void putDeviceId(long deviceid){
		SharedPreferences.Editor localEditor = mDeviceId.edit();
		localEditor.putLong(KEY_DEVICEID, deviceid);
		localEditor.commit();
	}
	
	public static long getDeviceId(){
		return mDeviceId.getLong(KEY_DEVICEID, 0);
	}
	
	public static void putMenuPosition(int positionint)
	{
	    SharedPreferences.Editor localEditor = mMenuPositionPreferences.edit();
	    localEditor.putInt(KEY_MENU, positionint);
	    localEditor.commit();
	}
	
	public static int getMenuPosition()
	  {
	    return mMenuPositionPreferences.getInt(KEY_MENU, 0);
	  }
	
	public static void putRenwuMenuPosotion(int positionint){
		SharedPreferences.Editor localEditor = mMenuPositionPreferences.edit();
	    localEditor.putInt(KEY_RENWU_MENU, positionint);
	    localEditor.commit();
	}
	
	public static int getRenwuMenuPosition(){
		return mMenuPositionPreferences.getInt(KEY_RENWU_MENU, 0);
	}

	public static void putVibrate(boolean v){
		SharedPreferences.Editor localEditor = mVibrate.edit();
	    localEditor.putBoolean(KEY_VIBRAT, v);
	    localEditor.commit();
	}
	
	public static boolean getVibrate(){
		return mVibrate.getBoolean(KEY_VIBRAT, false);
	}
	
	public static void putLanguage(int i){
		SharedPreferences.Editor localEditor = mLanguage.edit();
	    localEditor.putInt(SHARE_LANGUAGE, i);
	    localEditor.commit();
	}
	
	public static int getLanguage(){
		return mLanguage.getInt(SHARE_LANGUAGE, 0);
	}
}
