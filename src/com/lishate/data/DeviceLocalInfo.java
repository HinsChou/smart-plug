package com.lishate.data;

import java.util.Date;

public class DeviceLocalInfo {

	private long devid = 0;
	private String localIp = null;
	private Date lastLocalTime;
	public long getDevid() {
		return devid;
	}
	public void setDevid(long devid) {
		this.devid = devid;
	}
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public Date getLastLocalTime() {
		return lastLocalTime;
	}
	public void setLastLocalTime(Date lastLocalTime) {
		this.lastLocalTime = lastLocalTime;
	}
	
	
	
}
