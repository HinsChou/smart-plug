package com.lishate.data;

import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;

public class NetMacData {

	public long getMacId(){
		if(_baseData != null){
			return _baseData.getDeviceId();
		}
		else{
			return 0;
		}
	}
	
	private DeviceItemModel _baseData;
	public DeviceItemModel getMacData(){
		return _baseData;
	}
	public void setMacData(DeviceItemModel data){
		_baseData = data;
	}
	
	private String _localIp = "";
	public String getLocalIp(){
		return _localIp;
	}
	public void setLocalIp(String localIp){
		_localIp = localIp;
	}
	
	private int _localPort = 0;
	public int getLocalPort(){
		return _localPort;
	}
	public void setLocalPort(int localport){
		_localPort = localport;
	}
	
	private String _serverIp = "";
	public String getServerIp(){
		return _serverIp;
	}
	public void setServerIp(String sip){
		_serverIp = sip;
	}
	
	private int _serverPort = 0;
	public int getServerPort(){
		return _serverPort;
	}
	public void setServerPort(int sp){
		_serverPort = sp;
	}
	
	
}
