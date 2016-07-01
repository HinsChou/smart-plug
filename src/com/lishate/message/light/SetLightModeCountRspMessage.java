package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SetLightModeCountRspMessage extends baseRspMessage {
	public SetLightModeCountRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_GET_LIGHT_MODE_COUNT_RSP;
	}
	
	private byte Count = 0;

	public byte getCount() {
		return Count;
	}

	public void setCount(byte count) {
		Count = count;
	}
	
}
