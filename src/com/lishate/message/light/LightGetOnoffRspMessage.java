package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightGetOnoffRspMessage extends baseRspMessage {
	
	private byte onOff = 0;
	public byte getOnOff(){
		return onOff;
	}
	public void setOnOff(byte v){
		onOff = v;
	}
	public LightGetOnoffRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_GETONOFF_RSP;
	}
}
