package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightModeSetReqMessage extends baseReqMessage {
	public LightModeSetReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_MODE_SET_REQ;
	}
	
	private byte[] data = new byte[15];
	public void setModeData(byte[] mdata){
		data = mdata;
	}
	
	public byte[] getModeData(){
		return data;
	}
}
