package com.lishate.message.magnet;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class MagnetReportRspMessage extends baseRspMessage {
	public MagnetReportRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_MAGNET_REPORT_RSP;
	}

}
