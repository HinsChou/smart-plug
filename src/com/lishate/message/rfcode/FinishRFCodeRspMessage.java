package com.lishate.message.rfcode;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class FinishRFCodeRspMessage extends baseRspMessage {
	public FinishRFCodeRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_RFCODE_FINISH_RSP;
	}
}
