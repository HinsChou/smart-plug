package com.lishate.message;

public class PublicTimezoneSetRspMessage extends baseRspMessage {
	
	public PublicTimezoneSetRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_PUBLIC_TIMEZONE_SET_RSP;
	}

}
