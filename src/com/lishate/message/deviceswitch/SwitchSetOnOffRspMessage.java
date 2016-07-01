package com.lishate.message.deviceswitch;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SwitchSetOnOffRspMessage extends baseRspMessage {

	public SwitchSetOnOffRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SWITCH_SETONOFF_RSP;
	}
}
