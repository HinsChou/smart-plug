package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightGetRspMessage extends baseRspMessage {
	public LightGetRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_GET_RSP;
	}
	
	private byte[] data = new byte[4];
	public void setRGBData(byte[] rgbdata){
		data = rgbdata;
	}
	public byte[] getRGBData(){
		return data;
	}
	
}
