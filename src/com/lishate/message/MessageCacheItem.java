package com.lishate.message;

import java.util.Date;

public class MessageCacheItem {

	private baseMessage msg = null;
	public baseMessage getMsg(){
		return msg;
	}
	public void setMsg(baseMessage bmsg){
		msg = bmsg;
	}
	
	private int index = 0;
	public int getIndex(){
		return index;
	}
	public void setIndex(int temp){
		index = temp;
	}
	
	private long lasttime = 0;//Date.this.getTime();
	public long getLastTime(){
		return lasttime;
	}
	public void setLastTime(long time){
		lasttime = time;
	}
	public void UpdateTime(){
		
	}
}
