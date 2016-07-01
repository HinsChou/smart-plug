package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightModeGetReqMessage extends baseReqMessage {
	public LightModeGetReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_MODE_GET_REQ;
	}
	
	public int ModeIndex = 0;
	public int getModeIndex(){
		return ModeIndex;
	}
}
