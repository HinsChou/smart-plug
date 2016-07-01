package com.lishate.message.deviceswitch;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SwitchSetOnoffReqMessage extends baseReqMessage{
	private byte index;
	public byte getIndex(){
		return index;
	}
	public void setIndex(byte v){
		index = v;
	}
	
	private byte onOff;
	public byte getOnOff(){
		return onOff;
	}
	public void setOnOff(byte v){
		onOff = v;
	}
	
	public SwitchSetOnoffReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SWITCH_SETONOFF_REQ;
	}

}
