package com.lishate.message.deviceswitch;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SwitchGetOnOffReqMessage extends baseReqMessage {
	private byte index;
	public byte getIndex(){
		return index;
	}
	public void setIndex(Byte v){
		index = v;
	}
	public SwitchGetOnOffReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SWITCH_GETONOFF_REQ;
	}

}
