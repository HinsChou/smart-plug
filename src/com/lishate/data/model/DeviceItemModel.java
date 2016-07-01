package com.lishate.data.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lishate.data.dao.DeviceItemDao;

@DatabaseTable(daoClass=DeviceItemDao.class, tableName="device_table")
public class DeviceItemModel implements Serializable {

	public static final int Online_On = 1;
	public static final int Online_Off = 2;
	public static final int Online_Un = 3;
	
	public static final int OnOff_On = 1;
	public static final int OnOff_Off = 2;
	
	public static final int SetTime_On = 1;
	public static final int SetTime_Off = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4219003487036716047L;
	@DatabaseField(id = true)
	private long deviceId;
	@DatabaseField
	private int deviceType;
	@DatabaseField
	private String deviceName;
	@DatabaseField
	private long parentId;
	@DatabaseField
	private String serverIp;
	@DatabaseField
	private long serverPort;
	@DatabaseField
	private String deviceIcon;
	@DatabaseField
	private String timeinfo;
	@DatabaseField
	private boolean timeronoff;
	
	public String getTimeinfo() {
		return timeinfo;
	}
	public void setTimeinfo(String timeinfo) {
		this.timeinfo = timeinfo;
	}
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public long getServerPort() {
		return serverPort;
	}
	public void setServerPort(long serverPort) {
		this.serverPort = serverPort;
	}
	public String getDeviceIcon() {
		return this.deviceIcon;
	}
	public void setDeviceIcon(String deviceIcon) {
		this.deviceIcon = deviceIcon;
	}
	
	private int deviceStatus = 0;
	public int getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(int deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	
	private boolean ui_del = false;
	private int online = Online_Off;
	@DatabaseField
	private int onoff = OnOff_Off;
	@DatabaseField
	private int settime = SetTime_Off;
	private String localIp = "";
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	public int getOnoff() {
		return onoff;
	}
	public void setOnoff(int onoff) {
		this.onoff = onoff;
	}
	public int getSettime() {
		return settime;
	}
	public void setSettime(int settime) {
		this.settime = settime;
	}
	public boolean isUi_del() {
		return ui_del;
	}
	public void setUi_del(boolean ui_del) {
		this.ui_del = ui_del;
	}
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	
	
}
