package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightOnRspMessage extends baseRspMessage {
	public LightOnRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_ON_RSP;
	}
}
