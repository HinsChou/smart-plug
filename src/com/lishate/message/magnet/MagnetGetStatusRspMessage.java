package com.lishate.message.magnet;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class MagnetGetStatusRspMessage extends baseRspMessage {
	
	public MagnetGetStatusRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_MAGNET_GET_STATUE_RSP;
	}
	private byte onOff = 0;
	public byte getOnOff(){
		return onOff;
	}
	public void setOnOff(byte v){
		onOff = v;
	}
	
	private byte ku = 0;
	public byte getKu(){
		return ku;
	}
	public void setKu(byte v){
		ku = v;
	}
}
