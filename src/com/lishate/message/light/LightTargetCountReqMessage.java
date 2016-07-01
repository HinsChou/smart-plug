package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class LightTargetCountReqMessage extends baseReqMessage {

	public LightTargetCountReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_LIGHT_TARGETCOUNT_REQ;
	}
}
