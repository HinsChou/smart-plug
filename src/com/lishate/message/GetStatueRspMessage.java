package com.lishate.message;

public class GetStatueRspMessage extends baseRspMessage {

	public int openclose = 0;
	
	public int getOpenClose(){
		return openclose;
	}
	
	public void setOpenClose(int oc){
		openclose = oc;
	}
	
	public GetStatueRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_GET_STATUS_RSP;
	}
	
}