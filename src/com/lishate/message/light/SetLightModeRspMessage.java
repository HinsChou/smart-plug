package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SetLightModeRspMessage extends baseRspMessage{
	public SetLightModeRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_LIGHT_MODE_RSP;
	}
}
