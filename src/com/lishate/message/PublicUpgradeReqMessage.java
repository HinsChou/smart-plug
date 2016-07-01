package com.lishate.message;

import java.io.UnsupportedEncodingException;

public class PublicUpgradeReqMessage extends baseReqMessage {
	public PublicUpgradeReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_PUBLIC_UPGRADE_REQ;
	}
	
	private int serverIp = 0;
	public int getServerIp(){
		return serverIp;
	}
	public void setServerIp(int ip){
		serverIp = ip;
	}
	
	private int serverPort = 0;
	public int getServerPort(){
		return serverPort;
	}
	public void setServerPort(int port){
		serverPort = port;
	}
	
	private byte type = 0;
	public byte getType(){
		return type;
	}
	public void setType(byte t){
		type = t;
	}
	
	private byte[] upgradeUrl = new byte[0];
	public byte[] getUpgradeUrl(){
		return upgradeUrl;
	}
	public void setUpgradeUrl(String url){
		try {
			upgradeUrl = url.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
