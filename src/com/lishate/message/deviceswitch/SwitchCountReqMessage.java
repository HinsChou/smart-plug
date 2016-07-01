package com.lishate.message.deviceswitch;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SwitchCountReqMessage extends baseReqMessage {

	public SwitchCountReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SWITCH_COUNT_REQ;
	}
}
