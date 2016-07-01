package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SetLightDelReqMessage extends baseReqMessage{
	public SetLightDelReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_CONFIG_LIGHT_DEL_REQ;
	}
	
	private byte LightID = 0;

	public byte getLightID() {
		return LightID;
	}

	public void setLightID(byte lightID) {
		LightID = lightID;
	}
	
}
