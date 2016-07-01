package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class LightTargetCountRspMessage extends baseRspMessage {

	private int count;
	public int getCount(){
		return count;
	}
	public void setCount(int v){
		count = v;
	}
	
	public LightTargetCountRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_TARGETCOUNT_RSP;
	}
}
