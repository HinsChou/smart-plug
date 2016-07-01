package com.lishate.data;

import com.lishate.R;

import android.content.Context;

public class TimerInfo {

	public String done = "00";
	public String enable = "00";
	public String off_hour = "00";
	public String off_min = "00";
	public String on_hour = "00";
	public String on_min = "00";
	public String start = "00";
	public String week = "00";
	public String getDone() {
		return done;
	}
	public void setDone(String done) {
		this.done = done;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getOff_hour() {
		return off_hour;
	}
	public void setOff_hour(String off_hour) {
		this.off_hour = off_hour;
	}
	public String getOff_min() {
		return off_min;
	}
	public void setOff_min(String off_min) {
		this.off_min = off_min;
	}
	public String getOn_hour() {
		return on_hour;
	}
	public void setOn_hour(String on_hour) {
		this.on_hour = on_hour;
	}
	public String getOn_min() {
		return on_min;
	}
	public void setOn_min(String on_min) {
		this.on_min = on_min;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	  
	public static TimerInfo ParseInStr(String s){
		TimerInfo t = new TimerInfo();
		
		return t;
	}
	
	public static String GetWeekLan(String week, Context context){
		String[] arrayOfString = { context.getString(R.string.monday_mini) + ",", context.getString(R.string.tuesday_mini) + ",", context.getString(R.string.wednesday_mini) + ",", context.getString(R.string.thursday_mini) + ",", context.getString(R.string.friday_mini) + ",", context.getString(R.string.saturday_mini) + ",", context.getString(R.string.sunday_mini) + ",", "" };
		String result = "";
		int i = 0;
		try{
			i = Integer.parseInt(week);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		for(int t=0; t<7; t++){
			int temp = ((i >> t) & 0x01);
			if(temp == 1){
				result = result + arrayOfString[t];
			}
		}
		if(result.length() == 0){
			return context.getString(R.string.nerver);
		}
		else{
			result = result.substring(0,result.length() - 1);
		}
		return result;
	}
	  
}
