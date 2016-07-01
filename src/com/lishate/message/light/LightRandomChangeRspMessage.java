package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightRandomChangeRspMessage extends baseRspMessage {

	public LightRandomChangeRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_RANDOMCHANGE_RSP;
	}
}
