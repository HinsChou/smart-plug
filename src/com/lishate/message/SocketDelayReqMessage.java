package com.lishate.message;

public class SocketDelayReqMessage extends baseReqMessage {

	public SocketDelayReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_DELAY_ONOFF_REQ;
	}
	
	public int OnOff = 0;
	public int TimeSpan = 0;
	
	
	
}
