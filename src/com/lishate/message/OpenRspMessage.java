package com.lishate.message;

public class OpenRspMessage extends baseRspMessage {

	public OpenRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_OPEN_RSP;
	}
}
