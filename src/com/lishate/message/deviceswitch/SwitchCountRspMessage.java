package com.lishate.message.deviceswitch;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SwitchCountRspMessage extends baseRspMessage {
	
	private byte count;
	public byte getCount(){
		return count;
	}
	public void setCount(byte v){
		count = v;
	}

	public SwitchCountRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SWITCH_COUNT_RSP;
	}
}
