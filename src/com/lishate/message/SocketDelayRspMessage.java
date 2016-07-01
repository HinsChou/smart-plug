package com.lishate.message;

public class SocketDelayRspMessage extends baseRspMessage {

	public SocketDelayRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_DELAY_ONOFF_RSP;
	}
}
