package com.lishate.data.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lishate.data.dao.ApplicationDao;

@DatabaseTable(daoClass=ApplicationDao.class, tableName="application_table")
public class applicationContent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8611122621710186466L;

	@DatabaseField
	private long deviceId;

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
