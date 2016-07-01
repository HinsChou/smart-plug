package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SetLightModeCountReqMessage extends baseReqMessage {
	public SetLightModeCountReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_GET_LIGHT_MODE_COUNT_REQ;
	}
	
	
}
