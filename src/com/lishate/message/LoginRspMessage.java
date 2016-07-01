package com.lishate.message;

public class LoginRspMessage extends baseRspMessage {

	public LoginRspMessage(){
		super();
		//setMsgType(MessageDef.MESSAGE_TYPE_LOGIN_RSP);
		MsgType = MessageDef.MESSAGE_TYPE_LOGIN_RSP;
	}
}
