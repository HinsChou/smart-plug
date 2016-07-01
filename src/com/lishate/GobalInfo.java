package com.lishate;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class GobalInfo  {

private List<Activity> mActivityList = new ArrayList<Activity>();
	
	public List<Activity> getActivityList(){
		return mActivityList;
	}
	
	public void clearActivityList(){
		for(int i=0; i<mActivityList.size(); i++){
			this.mActivityList.get(i).finish();
		}
		mActivityList.clear();
	}
	
	public void finish(){
		clearActivityList();
		System.gc();
	}
	
	/*
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
	*/
	public void Start(){
		
	}
}
