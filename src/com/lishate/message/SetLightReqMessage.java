package com.lishate.message;

public class SetLightReqMessage extends baseReqMessage{
	
	private byte[] data = new byte[4];
	
	public byte[] getRGBData(){
		return data;
	}

	public SetLightReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_LIGHT_REQ;
		//setMsgType(MessageDef.MESSAGE_TYPE_GETSERVER_REQ);
	}
	
	
}
