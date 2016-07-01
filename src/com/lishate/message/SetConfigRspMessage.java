package com.lishate.message;

import java.util.ArrayList;

public class SetConfigRspMessage extends baseRspMessage {
	
	private byte[] content = new byte[35];
	public void setContent(byte[] buf){
		content = buf;
	}
	/*
	public byte[] getContent(){
		return content;
	}
	*/
	
	public ArrayList<ConfigInfo> configinfos = new ArrayList<ConfigInfo>();
	
	public void Array2Content(){
		for(int i = 0; i<configinfos.size(); i++){
			ConfigInfo ci = configinfos.get(i);
			ci.writeByte(content, i * 5);
		}
	}
	
	public void Content2Array(){
		configinfos.clear();
		for(int i=0; i<7; i++){
			ConfigInfo ci = new ConfigInfo();
			ci.readByte(content, i * 5);
			configinfos.add(ci);
		}
	}

	public SetConfigRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_CONFIG_RSP;
	}
}
