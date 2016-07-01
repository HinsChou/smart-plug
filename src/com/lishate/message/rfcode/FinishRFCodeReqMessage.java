package com.lishate.message.rfcode;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class FinishRFCodeReqMessage extends baseReqMessage {
	public FinishRFCodeReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_RFCODE_FINISH_REQ;
	}
}
