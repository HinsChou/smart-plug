package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightGetOnoffReqMessage extends baseReqMessage {
	public LightGetOnoffReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_GETONOFF_REQ;
	}
}
