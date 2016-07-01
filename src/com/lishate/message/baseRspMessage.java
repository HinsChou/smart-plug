package com.lishate.message;

import android.util.Log;

public class baseRspMessage extends baseMessage {

	private static final String TAG = "baseRspMessage";
	private int _rspStatue = 0;
	public int getRspStatue(){
		Log.d(TAG, "get rsp statue " + _rspStatue);
		return _rspStatue;
	}
	public void setRspStatue(int _rsp){
		Log.d(TAG, "set rsp statue " + _rsp);
		_rspStatue = _rsp;
	}
	
	public baseRspMessage(){
		Direct = MessageDef.DIRECT_RSP;
		//setDirect(MessageDef.DIRECT_RSP);
	}
	
	
}
