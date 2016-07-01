package com.lishate.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lishate.data.dao.SocketItemDao;

@DatabaseTable(daoClass=SocketItemDao.class, tableName="socket_table")
public class SocketItemModel {

	@DatabaseField(id = true)
	private long socketId;
	@DatabaseField
	private int sockettype;
	@DatabaseField
	private String socketName;
	@DatabaseField
	private long parentId;
	@DatabaseField
	private String socketIp;
	@DatabaseField
	private long socketPort;
	@DatabaseField
	private String deviceIcon;
	public long getSocketId() {
		return socketId;
	}
	public void setSocketId(long socketId) {
		this.socketId = socketId;
	}
	public int getSockettype() {
		return sockettype;
	}
	public void setSockettype(int sockettype) {
		this.sockettype = sockettype;
	}
	public String getSocketName() {
		return socketName;
	}
	public void setSocketName(String socketName) {
		this.socketName = socketName;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getSocketIp() {
		return socketIp;
	}
	public void setSocketIp(String socketIp) {
		this.socketIp = socketIp;
	}
	public long getSocketPort() {
		return socketPort;
	}
	public void setSocketPort(long socketPort) {
		this.socketPort = socketPort;
	}
	public String getDeviceIcon() {
		return deviceIcon;
	}
	public void setDeviceIcon(String deviceIcon) {
		this.deviceIcon = deviceIcon;
	}
	
	
}
