package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightOffReqMessage extends baseReqMessage {
	public LightOffReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_OFF_REQ;
	}
}
