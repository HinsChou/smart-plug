package com.lishate.data;

public class baseMacData {

	private long _mac_id;
	public long getMacID(){return _mac_id;}
	public void setMacID(long macid){
		_mac_id = macid;
	}
	
	private long _parent_id;
	public long getParentId(){
		return _parent_id;
	}
	public void setParentId(long parentid){
		_parent_id = parentid;
	}
	
	private int _type_id;
	public int getTypeId(){
		return _type_id;
	}
	public void setTypeId(int typeid){
		_type_id = typeid;
	}
	
	
}
