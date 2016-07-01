package com.lishate.message;

public class GetServerConfigReqMessage extends baseReqMessage {
	public GetServerConfigReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ;
	}
}
