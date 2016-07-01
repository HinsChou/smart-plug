package com.lishate.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {

	public static boolean CheckNetworkIsOn(Context context){
		NetworkInfo ni = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
		if(ni != null && ni.isConnected()){
			return true;
		}
		return false;
	}
	
	
}
