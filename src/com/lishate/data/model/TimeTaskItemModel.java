package com.lishate.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lishate.data.dao.TimeTaskItemDao;

@DatabaseTable(daoClass=TimeTaskItemDao.class, tableName="socket_timetask_table")
public class TimeTaskItemModel {
	
	@DatabaseField(id = true)
	private int taskId = 0;
	@DatabaseField
	private int starthour=0;
	@DatabaseField
	private int startmin = 0;
	@DatabaseField
	private int endhour = 0;
	@DatabaseField
	private int endmin = 0;
	@DatabaseField
	private byte recycle = 0;
	@DatabaseField
	private int enable = 0;
	@DatabaseField
	private int startok = 0;
	@DatabaseField
	private int endok = 0;
	@DatabaseField
	private long deviceid = 0;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getStarthour() {
		return starthour;
	}
	public void setStarthour(int starthour) {
		this.starthour = starthour;
	}
	public int getStartmin() {
		return startmin;
	}
	public void setStartmin(int startmin) {
		this.startmin = startmin;
	}
	public int getEndhour() {
		return endhour;
	}
	public void setEndhour(int endhour) {
		this.endhour = endhour;
	}
	public int getEndmin() {
		return endmin;
	}
	public void setEndmin(int endmin) {
		this.endmin = endmin;
	}
	public byte getRecycle() {
		return recycle;
	}
	public void setRecycle(byte recycle) {
		this.recycle = recycle;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public int getStartok() {
		return startok;
	}
	public void setStartok(int startok) {
		this.startok = startok;
	}
	public int getEndok() {
		return endok;
	}
	public void setEndok(int endok) {
		this.endok = endok;
	}
	
	
}
