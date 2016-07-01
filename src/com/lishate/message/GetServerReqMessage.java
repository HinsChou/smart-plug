package com.lishate.message;

public class GetServerReqMessage extends baseReqMessage {
	public GetServerReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_GETSERVER_REQ;
		//setMsgType(MessageDef.MESSAGE_TYPE_GETSERVER_REQ);
	}
	
	
}
