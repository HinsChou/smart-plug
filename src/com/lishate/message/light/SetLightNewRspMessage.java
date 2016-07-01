package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SetLightNewRspMessage extends baseRspMessage {
	public SetLightNewRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_CONFIG_LIGHT_NEW_RSP;
	}
}
