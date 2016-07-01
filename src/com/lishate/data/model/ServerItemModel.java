package com.lishate.data.model;


import java.io.Serializable;

import com.j256.ormlite.field.*; 
import com.j256.ormlite.table.DatabaseTable;
import com.lishate.data.dao.ServerItemDao;

@DatabaseTable(daoClass=ServerItemDao.class, tableName="server_item_table")
public class ServerItemModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 229489863167224371L;
	
	@DatabaseField(generatedId = true)
	private long id;
	@DatabaseField
	String ipaddress;
	@DatabaseField
	int port;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean CheckIsEqual(ServerItemModel sim){
		if(ipaddress.equals(sim.getIpaddress()) && port == sim.getPort()){
			return true;
		}
		return false;
	}
}
