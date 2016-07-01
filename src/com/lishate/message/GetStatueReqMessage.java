package com.lishate.message;

public class GetStatueReqMessage extends baseReqMessage {

	public GetStatueReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_GET_STATUS_REQ;
	}
}
