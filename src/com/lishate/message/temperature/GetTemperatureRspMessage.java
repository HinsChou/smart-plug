package com.lishate.message.temperature;

import com.lishate.message.MessageDef;
import com.lishate.message.baseRspMessage;

public class GetTemperatureRspMessage extends baseRspMessage {
	public GetTemperatureRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_TEMPE_GET_RSP;
	}
	
	private byte[] tdata = new byte[2];
	public byte[] GetTData(){
		return tdata;
	}
	public void SetTData(byte[] v){
		tdata = v;
	}
}
