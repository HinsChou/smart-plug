package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightOnReqMessage extends baseReqMessage {

	public LightOnReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_ON_REQ;//MessageDef.MESSAGE
	}
}
