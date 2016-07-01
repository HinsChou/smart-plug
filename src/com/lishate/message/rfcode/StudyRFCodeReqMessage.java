package com.lishate.message.rfcode;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class StudyRFCodeReqMessage extends baseReqMessage {
	
	public StudyRFCodeReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_RFCODE_STUDY_REQ;
	}
}
