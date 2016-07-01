package com.lishate.data;

import java.util.ArrayList;
import java.util.Random;

import com.lishate.data.model.DeviceItemModel;

public class GobalDef {
	
	public final static String UPDATE_XML_NAME = "apkupdate.xml";
	public final static String UPDATE_URL = "http://115.28.45.50/update/update.xml";//"http://www.hg125.com/update/update.xml";
	public final static String SWITCH_DIR = "/switch";
	public final static String CACHE_DIR = "/cache";
	public final static String UPDATE_DIR = "/update";
	public final static String ICON_DIR = "/icon";
	public final static String SERVER_URL = "115.28.45.50";//"218.244.129.177";//"115.28.45.50";//"192.168.2.101";//"115.28.45.50";
	public final static int SERVER_PORT = 12188;
	public final static int BROADCAST_PORT = 4288;//8300;
	public final static String STR_CODE = "UTF-8";
	public final static String LOCAL_URL = "255.255.255.255";
	public final static int LOCAL_PORT = 1025;//80;
	//public final static int BROADCAST_
	
	public final static String INTENT_TIME_INDEX = "TIME_INDEX";
	public final static String INTENT_TIME_HOUR = "TIME_HOUR";
	public final static String INTENT_TIME_MIN = "TIME_MIN";
	public final static String INTENT_TIME_WEEK = "TIME_WEEK";
	public final static String TIME_TASK_EMPTY = "0000000000";
	
	public static final int DEVICE_STATUS_OPEN = 1;
	public static final int DEVICE_STATUS_CLOSE = 0;
	
	public final static long MOBILEID = new Random().nextLong();//0x8000000390000005L;//9204514244744040552L;//new Random().nextLong();
	public final static long SERVERID = 0x7000000490000006L;//FFFF;

	//ADD by qinshaoxu 20150505
	public static final int CONFIG_DEVICE_OK = 1111;
	public static final int CONFIG_DEVICE_SMARTCONFIG_OK = 1112;
	public static final int CONFIG_DEVICE_APCONFIG_OK = 1113;
	
	public static final int REQEST_AP_CONFIG = 1001;
	public static final int REQEST_SMART_CONFIG = 1002;
	private GobalDef(){
		
	}
	
	public static final GobalDef Instance = new GobalDef();
	
	private int _soTimeout = 300;
	public int getUdpReadTimeOut(){
		return _soTimeout;
	}
	public void setUdpReadTimeOut(int timeout){
		_soTimeout = timeout;
	}
	
	private SynLocalList localList = new SynLocalList();
	public SynLocalList getLocalList(){
		return localList;
	}
	
	private String _deviceIcon = "";
	public String getDeviceIcon(){
		return _deviceIcon;
	}
	public void setDeviceIcon(String icon){
		_deviceIcon = icon;
	}
	
	private String _updatePath = "";
	public String getUpdatePath(){
		return _updatePath;
	}
	public void setUpdatePath(String path){
		_updatePath = path;
	}
	
	private String _cachePath = "";
	public String getCachePath(){
		return _cachePath;
	}
	public void setCachePath(String path){
		_cachePath = path;
	}
	
	
	
	private int _ApkVer = 1;
	public int GetApkVer(){
		return _ApkVer;
	}
	public void setApkVer(int ver){
		_ApkVer = ver;
	}
	
	private ArrayList<NetMacData> _localSwitchList = new ArrayList<NetMacData>();
	public ArrayList<NetMacData> getLocalSwitchList(){
		return _localSwitchList;
	}
	public void setLocalSwitchList(ArrayList<NetMacData> list){
		_localSwitchList = list;
	}
	
	private int _p_height = 0;
	public int getPHeight(){
		return _p_height;
	}
	public void setPHeight(int ph){
		_p_height = ph;
	}
	
	private int _p_width = 0;
	public int getPWidth(){
		return _p_width;
	}
	public void setPWidth(int pw){
		_p_width = pw;
	}
	
	private int _s_height = 0;
	public int getSHeight(){
		return _s_height;
	}
	public void setSHeight(int s){
		_s_height = s;
	}
	
	private DeviceItemModel _dim;
	public DeviceItemModel getDeviceItemModel(){
		return _dim;
	}
	public void setDeviceItemModel(DeviceItemModel dm){
		_dim = dm;
	}
}
