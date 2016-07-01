package com.lishate.message;

public class baseReqMessage extends baseMessage {

	public baseReqMessage(){
		Direct = MessageDef.DIRECT_REQ;
		//setDirect(MessageDef.DIRECT_REQ);
	}
}
