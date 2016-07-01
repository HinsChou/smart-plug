package com.lishate.application;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lishate.data.GobalDef;
import com.lishate.data.SharedPreferencesUtility;
import com.lishate.data.dao.DatabaseHelper;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.ServerItemModel;
import com.lishate.utility.CrashHandler;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.util.Log;

public class switchApplication extends Application {

	private static final String TAG = "application";
	private List<Activity> mActivityList = new ArrayList<Activity>();
	private List<ServerItemModel> mServerList = new ArrayList();
	private volatile DatabaseHelper mHelper;
	
	public DatabaseHelper getHelper(){
		if(mHelper == null){
			mHelper = (DatabaseHelper)OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		return mHelper;
	}
	
	public List<Activity> getActivityList(){
		return mActivityList;
	}
	
	public List<ServerItemModel> getServerList(){
		return mServerList;
	}
	
	public void clearActivityList(){
		for(int i=0; i<mActivityList.size(); i++){
			this.mActivityList.get(i).finish();
		}
		mActivityList.clear();
	}
	
	private DeviceItemModel _mDeviceModel;
	public void SetDeviceItemModel(DeviceItemModel dim){
		Log.d(TAG, "SetDeviceItemModel");
		if(dim != null){
			SharedPreferencesUtility.putDeviceId(dim.getDeviceId());
		}
		else{
			SharedPreferencesUtility.putDeviceId(0);
		}
		_mDeviceModel  = dim;
	}
	public DeviceItemModel getItemModel(){
		Log.d(TAG, "GetDeviceItemModel");
		if(_mDeviceModel == null){
			long temp = SharedPreferencesUtility.getDeviceId();
			try {
				DeviceItemDao did = new DeviceItemDao(getHelper());
				List<DeviceItemModel> dims = did.queryForAll();
				for(DeviceItemModel dim : dims){
					if(dim.getDeviceId() == temp){
						_mDeviceModel = dim;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return _mDeviceModel;
	}
	
	public void dataItemInit(){
		
	}
	
	public void finish(){
		clearActivityList();
		System.gc();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "application oncreate");
		SharedPreferencesUtility.Init(this);
		Start();
		
		CrashHandler.Instance.setSavePath(GobalDef.Instance.getCachePath());
		CrashHandler.Instance.Init(this);
		
	}
	
	public void Start(){
		GobalDef.Instance.setPHeight(getResources().getDisplayMetrics().heightPixels);
		GobalDef.Instance.setPWidth(getResources().getDisplayMetrics().widthPixels);
		//GobalDef.Instance.setSHeight(getResources())
		String str = "";
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
			str = Environment.getExternalStorageDirectory().getPath() + GobalDef.SWITCH_DIR;
		}
		else{
			str = Environment.getDataDirectory().getPath() + GobalDef.SWITCH_DIR;
		}
		GobalDef.Instance.setCachePath(str + GobalDef.CACHE_DIR);
		GobalDef.Instance.setUpdatePath(str + GobalDef.UPDATE_DIR);
		GobalDef.Instance.setDeviceIcon(str + GobalDef.ICON_DIR);
		File f =new File(GobalDef.Instance.getCachePath());
		f.mkdirs();
		f = new File(GobalDef.Instance.getUpdatePath());
		f.mkdirs();
		f = new File(GobalDef.Instance.getDeviceIcon());
		f.mkdirs();
	}
}
