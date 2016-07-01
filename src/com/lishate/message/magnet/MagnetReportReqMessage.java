package com.lishate.message.magnet;

import android.util.Log;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class MagnetReportReqMessage extends baseReqMessage {
	
	private byte onOff = 0;
	public byte getOnOff(){
		return onOff;
	}
	public void setOnOff(int v){
		
		onOff = (byte) v;
		//Log.d("", "set on off v:" + v + " onoff:" + onOff);
	}
	
	private byte ku = 0;
	public byte getKu(){
		return ku;
	}
	public void setKu(byte v){
		ku = v;
	}
	
	public MagnetReportReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_MAGNET_REPORT_REQ;
	}

}
