package com.lishate.message.deviceswitch;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class SwitchGetOnOffRspMessage extends baseRspMessage {
	private byte index;
	public byte getIndex(){
		return index;
	}
	public void setIndex(byte v){
		index = v;
	}
	
	public byte count;
	public byte getCount(){
		return count;
	}
	public void setCount(byte v){
		count = v;
	}
	
	private byte[] onoff;
	public byte[] getOnOff(){
		return onoff;
	}
	public void setOnOff(byte[] buf){
		onoff = buf;
	}
	
	public SwitchGetOnOffRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SWITCH_GETONOFF_RSP;
	}
}
