package com.lishate.message.magnet;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class MagnetGetStatusReqMessage extends baseReqMessage {

	public MagnetGetStatusReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_MAGNET_GET_STATUE_REQ;
	}
}
