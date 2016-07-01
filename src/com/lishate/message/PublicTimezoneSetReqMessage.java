package com.lishate.message;

public class PublicTimezoneSetReqMessage extends baseReqMessage {
	
	private int timezone = 0;
	public int getTimeZone(){
		return timezone;
	}
	public void setTimeZone(int temptimezone){
		timezone = temptimezone;
	}
	
	private int timezone_min = 0;
	public int getTimeZoneMin(){
		return timezone_min;
	}
	public void setTimeZoneMin(int v){
		timezone_min = v;
	}
	
	public PublicTimezoneSetReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_PUBLIC_TIMEZONE_SET_REQ;
	}

}
