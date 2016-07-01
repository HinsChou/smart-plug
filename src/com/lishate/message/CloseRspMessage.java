package com.lishate.message;

public class CloseRspMessage extends baseRspMessage {

	public CloseRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_CLOSE_RSP;
	}
}
