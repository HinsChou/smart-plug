package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightOffRspMessage extends baseRspMessage {
	public LightOffRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_OFF_RSP;
	}
}
