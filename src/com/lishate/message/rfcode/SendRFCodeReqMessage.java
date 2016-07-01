package com.lishate.message.rfcode;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SendRFCodeReqMessage extends baseReqMessage {

	public SendRFCodeReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_RFCODE_SEND_REQ;
	}
	
	private byte[] codeBuf = new byte[128];
	
	public byte[] getCodeBuf() {
		return codeBuf;
	}
	public void setCodeBuf(byte[] codeBuf) {
		this.codeBuf = codeBuf;
	}
}
