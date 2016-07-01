package com.lishate.message.temperature;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class GetTemperatureReqMessage extends baseReqMessage {
	public GetTemperatureReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_TEMPE_GET_REQ;
	}
}
