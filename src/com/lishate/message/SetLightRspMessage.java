package com.lishate.message;

public class SetLightRspMessage extends baseRspMessage {

	public SetLightRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_LIGHT_RSP;
	}
}
