package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SetLightModeReqMessage extends baseReqMessage {

	public SetLightModeReqMessage(){
		MsgType = MessageDef.MESSAGE_TYPE_SET_LIGHT_MODE_REQ;
	}
	
	private byte index = 0;
	private byte lightid = 0;
	private byte startH = 0;
	private byte startM = 0;
	private byte endH = 0;
	private byte endM = 0;
	private byte startR = 0;
	private byte startG = 0;
	private byte startB = 0;
	private byte endR = 0;
	private byte endG = 0;
	private byte endB = 0;
	
	
	public byte getIndex() {
		return index;
	}
	public void setIndex(byte index) {
		this.index = index;
	}
	public byte getLightid() {
		return lightid;
	}
	public void setLightid(byte lightid) {
		this.lightid = lightid;
	}
	public byte getStartH() {
		return startH;
	}
	public void setStartH(byte startH) {
		this.startH = startH;
	}
	public byte getStartM() {
		return startM;
	}
	public void setStartM(byte startM) {
		this.startM = startM;
	}
	public byte getEndH() {
		return endH;
	}
	public void setEndH(byte endH) {
		this.endH = endH;
	}
	public byte getEndM() {
		return endM;
	}
	public void setEndM(byte endM) {
		this.endM = endM;
	}
	public byte getStartR() {
		return startR;
	}
	public void setStartR(byte startR) {
		this.startR = startR;
	}
	public byte getStartG() {
		return startG;
	}
	public void setStartG(byte startG) {
		this.startG = startG;
	}
	public byte getStartB() {
		return startB;
	}
	public void setStartB(byte startB) {
		this.startB = startB;
	}
	public byte getEndR() {
		return endR;
	}
	public void setEndR(byte endR) {
		this.endR = endR;
	}
	public byte getEndG() {
		return endG;
	}
	public void setEndG(byte endG) {
		this.endG = endG;
	}
	public byte getEndB() {
		return endB;
	}
	public void setEndB(byte endB) {
		this.endB = endB;
	}
	public byte[] getContentbuf() {
		return contentbuf;
	}
	public void setContentbuf(byte[] contentbuf) {
		this.contentbuf = contentbuf;
	}

	private byte[] contentbuf = new byte[6];
	public byte[] GetContentBuf(){
		return contentbuf;
	}
}
