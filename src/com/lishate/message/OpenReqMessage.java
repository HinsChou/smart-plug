package com.lishate.message;

public class OpenReqMessage extends baseReqMessage {

	public OpenReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_OPEN_REQ;
	}
}
