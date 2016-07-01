package com.lishate.message.rfcode;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SendRFCodeRspMessage extends baseRspMessage {

	public SendRFCodeRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_RFCODE_SEND_RSP;
	}
}
