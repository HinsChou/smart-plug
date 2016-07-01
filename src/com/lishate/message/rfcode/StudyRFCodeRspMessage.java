package com.lishate.message.rfcode;

import android.util.Log;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class StudyRFCodeRspMessage extends baseRspMessage {
	private static final String TAG = null;
	
	private byte[] codeBuf = new byte[128];
	
	public byte[] getCodeBuf() {
		return codeBuf;
	}
	public void setCodeBuf(byte[] codeBuf) {
		Log.d(TAG, "set Code Buf");
		this.codeBuf = codeBuf;
	}
	
	public StudyRFCodeRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_RFCODE_STUDY_RSP;
	}
	
}
