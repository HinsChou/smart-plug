package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SetLightDataReqMessage extends baseReqMessage {
	public SetLightDataReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_SET_LIGHT_DATA_REQ;
	}
	
	private byte id=0;
	private byte type = 0;
	private byte r = 0;
	private byte g = 0;
	private byte b = 0;
	private byte Bright = 0;
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getR() {
		return r;
	}
	public void setR(byte r) {
		this.r = r;
	}
	public byte getG() {
		return g;
	}
	public void setG(byte g) {
		this.g = g;
	}
	public byte getB() {
		return b;
	}
	public void setB(byte b) {
		this.b = b;
	}
	public byte getBright() {
		return Bright;
	}
	public void setBright(byte bright) {
		Bright = bright;
	}
	
	private byte[] contentbuf = new byte[6];
	public byte[] GetContentBuf(){
		return contentbuf;
	}
}
