package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightModeSetRspMessage extends baseRspMessage {
	public LightModeSetRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_LIGHT_MODE_RSP;
	}
}
