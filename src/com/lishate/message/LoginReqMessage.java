package com.lishate.message;

public class LoginReqMessage extends baseReqMessage {

	public LoginReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LOGIN_REQ;
		//setMsgType(MessageDef.MESSAGE_TYPE_LOGIN_REQ);
	}
}
