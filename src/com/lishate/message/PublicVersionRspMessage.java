package com.lishate.message;

public class PublicVersionRspMessage extends baseRspMessage {

	public PublicVersionRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_PUBLIC_VERSION_RSP;
	}
	
	private byte[] buf = new byte[5];
	public byte[] getVersionBuf(){
		return buf;
	}
}
