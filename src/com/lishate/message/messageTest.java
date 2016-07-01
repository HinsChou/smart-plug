package com.lishate.message;

import android.util.Log;

public class messageTest {

	private final static String TAG = "messagetest";
	private int temp = 0;
	
	public int getTemp(){
		//Log.d(TAG, "get temp " + temp);
		return temp;
	}
	public void setTemp(int t){
		//Log.d(TAG, "set temp " + t);
		temp = t;
	}
	
	public int WWS =0x80000000;//0x7FFFFFFF;// 9204514244744040552L;// 1234567890;
	
}
