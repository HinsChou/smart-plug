package com.lishate.message;

public class CloseReqMessage extends baseReqMessage {

	public CloseReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_CLOSE_REQ;
	}
}
