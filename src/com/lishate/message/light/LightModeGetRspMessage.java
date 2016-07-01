package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightModeGetRspMessage extends  baseRspMessage {
	public LightModeGetRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_MODE_GET_REQ;
	}
	
	private byte[] data;
	public void setModeItemData(byte[] mdata){
		data = mdata;
	}
	
	public byte[] getModeItemData(){
		return data;
	}
}
