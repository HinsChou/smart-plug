package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SetLightNewReqMessage extends baseReqMessage {
	public SetLightNewReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_CONFIG_LIGHT_NEW_REQ;
	}
	
	private byte LightID = 0;

	public byte getLightID() {
		return LightID;
	}

	public void setLightID(byte lightID) {
		LightID = lightID;
	}
	
}
