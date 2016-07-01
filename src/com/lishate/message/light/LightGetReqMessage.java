package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightGetReqMessage extends baseReqMessage {
	public LightGetReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_GET_REQ;
	}
}
