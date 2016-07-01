package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SetLightDelRspMessage extends baseRspMessage {
	public SetLightDelRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_CONFIG_LIGHT_DEL_RSP;
	}
}
