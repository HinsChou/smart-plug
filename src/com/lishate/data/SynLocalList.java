package com.lishate.data;

import java.util.ArrayList;
import java.util.Date;

public class SynLocalList {

	private Object obj = new Object();
	private ArrayList<DeviceLocalInfo> _list = new ArrayList<DeviceLocalInfo>();
	
	public void putLocalInfo(long devid, String ip){
		synchronized(obj){
			boolean result = false;
			for(DeviceLocalInfo li:_list){
				if(li.getDevid() == devid){
					result = true;
					li.setLocalIp(ip);
					li.setLastLocalTime(new Date());
				}
			}
			if(result == false){
				DeviceLocalInfo templi = new DeviceLocalInfo();
				templi.setDevid(devid);
				templi.setLocalIp(ip);
				templi.setLastLocalTime(new Date());
				_list.add(templi);
			}
		}
	}
	
	public DeviceLocalInfo getDeviceLocalInfo(long devid){
		synchronized(obj){
			for(DeviceLocalInfo li:_list){
				if(li.getDevid() == devid){
					return li;
				}
			}
			return null;
		}
	}
	
	public void deleteDeviceLocalInfo(long devid){
		synchronized(obj){
			for(DeviceLocalInfo li:_list){
				if(li.getDevid() == devid){
					_list.remove(li);
					return;
				}
			}
		}
	}
}
