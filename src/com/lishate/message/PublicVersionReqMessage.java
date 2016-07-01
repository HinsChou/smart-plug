package com.lishate.message;

public class PublicVersionReqMessage extends baseReqMessage {
	
	private int mcmd = 0;
	public int getMCMD(){
		return mcmd;
	}
	public void setMCMD(int mcmd){
		this.mcmd = mcmd;
	}

	public PublicVersionReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_PUBLIC_VERSION_REQ;
	}
}
