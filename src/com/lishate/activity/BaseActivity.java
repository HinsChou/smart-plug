package com.lishate.activity;

import java.io.File;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lishate.GobalInfo;
import com.lishate.R;
import com.lishate.application.switchApplication;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.DatabaseHelper;


import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;

public class BaseActivity extends ActivityGroup {

	private volatile DatabaseHelper mHelper;
	private switchApplication mApplication;
	private SDReciver mReciver = new SDReciver() ;
	
	public DatabaseHelper getHelper(){
		if(mHelper == null){
			mHelper = (DatabaseHelper)OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		return mHelper;
	}
	
	protected void onCreate(Bundle paramBundle){
		//mGobalInfo = (GobalInfo)getApplication();
		mApplication = (switchApplication)getApplication();
		mApplication.getActivityList().add(this);
		super.onCreate(paramBundle);
	}
	
	protected void onDestroy(){
		super.onDestroy();
		if(mHelper != null){
			OpenHelperManager.releaseHelper();
			this.mHelper = null;
		}
		mApplication.getActivityList().remove(this);
	}
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mReciver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		IntentFilter localIntentFilter = new IntentFilter("android.intent.action.MEDIA_MOUNTED");
	    localIntentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
	    localIntentFilter.addAction("android.intent.action.MEDIA_EJECT");
	    localIntentFilter.addAction("android.intent.action.MEDIA_REMOVED");
	    localIntentFilter.addAction("android.intent.action.MEDIA_SHARED");
	    localIntentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
	    localIntentFilter.addDataScheme("file");
	    registerReceiver(mReciver, localIntentFilter);
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_SEARCH:
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}



	private class SDReciver extends BroadcastReceiver{

		private AlertDialog mDialog;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(GobalDef.Instance.getCachePath().contains(Environment.getExternalStorageDirectory().getPath())){
				if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){
					if(mDialog != null && mDialog.isShowing()){
						mDialog.dismiss();
						mDialog = null;
					}
				}
				else{
					mDialog = new AlertDialog.Builder(BaseActivity.this)
						.setTitle(R.string.app_name)
						.setMessage(R.string.remove)
						.setCancelable(false)
						.setPositiveButton(R.string.rom, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								String str = Environment.getDataDirectory().getPath() + GobalDef.SWITCH_DIR;
								GobalDef.Instance.setCachePath(str + GobalDef.CACHE_DIR);
								GobalDef.Instance.setUpdatePath(str + GobalDef.UPDATE_DIR);
								File f =new File(GobalDef.Instance.getCachePath());
								f.mkdirs();
								f = new File(GobalDef.Instance.getUpdatePath());
								f.mkdirs();
							}
						})
						.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if(mDialog != null){
									mDialog.dismiss();
								}
								BaseActivity.this.mApplication.finish();
							}
							
						})
						.show();
				}
			}
		}
		
	}
}
