package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightRandomChangeReqMessage extends baseReqMessage {

	private int randomType = 0;
	public int getRandomType(){
		return randomType;
	}
	public void setRandomType(int v){
		randomType = v;
	}
	
	private int splitTime = 0;
	public int getSplitTime(){
		return splitTime;
	}
	public void setSplitTime(int v){
		splitTime = v;
	}
	
	public LightRandomChangeReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_RANDOMCHANGE_REQ;
	}
}
