package com.lishate.message;

import java.util.ArrayList;

import android.util.Log;

public class SetConfigReqMessage extends baseReqMessage {

	private static final String TAG = null;
	private byte[] content = new byte[36];
	private byte[] indexbuf = new byte[6];
	public byte[] getContent(){
		Log.d(TAG, "setConfigReqMessage get content length is: " + content.length);
		return content;
	}
	
	public byte[] getIndexBuf(){
		return indexbuf;
	}
	public void setIndexBuf(byte[] buf){
		indexbuf = buf;
	}
	
	public ArrayList<ConfigInfo> configinfos = new ArrayList<ConfigInfo>();
	
	public void Array2Content(){
		for(int i = 0; i<configinfos.size(); i++){
			ConfigInfo ci = configinfos.get(i);
			ci.writeByte(content, i * 5);
		}
	}
	
	private int index = 0;
	public int getIndex(){
		return index;
	}
	public void setIndex(int tindex){
		index = tindex;
	}
	
	
	
	
	public SetConfigReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_CONFIG_REQ;
	}
}
