package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SetLightDataRspMessage extends baseRspMessage {
	public SetLightDataRspMessage(){
		MsgType = MessageDef.MESSAGE_TYPE_SET_LIGHT_DATA_RSP;
	}

}
