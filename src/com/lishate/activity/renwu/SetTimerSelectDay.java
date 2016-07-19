package com.lishate.activity.renwu;


import com.aigestudio.wheelpicker.AbstractWheelPicker.OnWheelChangeListener;
import com.aigestudio.wheelpicker.WheelTimePicker;
import com.lishate.R;
import com.lishate.message.ConfigInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class SetTimerSelectDay extends Activity implements OnScrollListener, Formatter, OnValueChangeListener, OnClickListener, OnFocusChangeListener{
	private final String TAG = "SetTimerSelectDay";
	private CheckBox openofclose;
	private CheckBox checksunday;
	private CheckBox checkmonday;
	private CheckBox checktuesday;
	private CheckBox checkwedday;
	private CheckBox checkthirday;
	private CheckBox checkfriday;
	private CheckBox checksatday;	
	private Byte hour;
	private Byte minute;
	private NumberPicker hourpick;
	private NumberPicker minutepick;
	private LinearLayout back;
	private LinearLayout complete;
	private ConfigInfo mconfig;
	private WheelTimePicker wtpSelect;
	private enum RequestCode{
		MODIFYTASK,
		ADDTIMETASK,
		NONE
	}
	RequestCode mrequestcode;
	//NumberPicker np1;
	//int minPrice = 25, maxPrice = 75;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settimerselectday);
		findView();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		hourpick.setMinValue(0);
		hourpick.setMaxValue(23);
		hourpick.setValue(12);
		minutepick.setMinValue(0);
		minutepick.setMaxValue(59);
		minutepick.setValue(0);
		hourpick.setFocusable(false);
		minutepick.setFocusable(false);
		
		hourpick.setFormatter(this);
		hourpick.setOnScrollListener(this);
		hourpick.setOnValueChangedListener(this);
		hourpick.setOnFocusChangeListener(this);
		minutepick.setOnFocusChangeListener(this);
		minutepick.setOnScrollListener(this);
		minutepick.setOnValueChangedListener(this);
		back.setOnClickListener(this);
		complete.setOnClickListener(this);
		
		if(mrequestcode == RequestCode.MODIFYTASK){
			if(mconfig.startenable)
			{
				wtpSelect.setCurrentTime(mconfig.startHour&0x1f, mconfig.startMin&0x3f);
				hour = mconfig.startHour;
				minute = mconfig.startMin;
				hourpick.setValue(mconfig.startHour&0x1f);
				minutepick.setValue(mconfig.startMin&0x3f);
				openofclose.setChecked(true);
				mconfig.endenable = false;
			}else if(mconfig.endenable){
				wtpSelect.setCurrentTime(mconfig.endHour&0x1f, mconfig.endMin&0x3f);
				hour = mconfig.endHour;
				minute = mconfig.endMin;
				hourpick.setValue(mconfig.endHour&0x1f);
				minutepick.setValue(mconfig.endMin&0x3f);
				openofclose.setChecked(false);
				mconfig.startenable = false;
			}
			if((mconfig.week& 1<<6) == (1<<6) )
			{
				checksunday.setChecked(true);
			}else{
				checksunday.setChecked(false);
			}
			if((mconfig.week& 1<<5) == (1<<5) )
			{
				checksatday.setChecked(true);
			}else{
				checksatday.setChecked(false);
			}
			if((mconfig.week& 1<<4) == (1<<4) )
			{
				checkfriday.setChecked(true);
			}else{
				checkfriday.setChecked(false);
			}
			if((mconfig.week& 1<<3) == (1<<3) )
			{
				checkthirday.setChecked(true);
			}else{
				checkthirday.setChecked(false);
			}
			if((mconfig.week& 1<<2) == (1<<2) )
			{
				checkwedday.setChecked(true);
			}else{
				checkwedday.setChecked(false);
			}
			if((mconfig.week& 1<<1) == (1<<1) )
			{
				checktuesday.setChecked(true);
			}else{
				checktuesday.setChecked(false);
			}
			if((mconfig.week& 1<<0) == (1<<0) )
			{
				checkmonday.setChecked(true);
			}else{
				checkmonday.setChecked(false);
			}
		}
		
		int padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        wtpSelect.setItemCount(5);
        wtpSelect.setItemSpace(padding * 3);
        wtpSelect.setTextSize(padding * 4);
        wtpSelect.setCurrentTextColor(getResources().getColor(R.color.blue));
        wtpSelect.setOnWheelChangeListener(new OnWheelChangeListener() {
			
			@Override
			public void onWheelScrolling(float deltaX, float deltaY) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onWheelSelected(int index, String data) {
				// TODO Auto-generated method stub
				hour = (byte) Integer.parseInt(data.split(":")[0]);
				minute = (byte) Integer.parseInt(data.split(":")[1]);
			}

			@Override
			public void onWheelScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void findView() {
		// TODO Auto-generated method stub
		wtpSelect = (WheelTimePicker) findViewById(R.id.wtpSelect);
		hourpick = (NumberPicker)findViewById(R.id.hour);
		minutepick = (NumberPicker)findViewById(R.id.minute);
		openofclose = (CheckBox)findViewById(R.id.openorclose);
		checksunday = (CheckBox)findViewById(R.id.sundaycheck);
		checkmonday = (CheckBox)findViewById(R.id.mondaycheck);
		checktuesday = (CheckBox)findViewById(R.id.tuesdaycheck);
		checkwedday = (CheckBox)findViewById(R.id.wednesdaycheck);	
		checkthirday = (CheckBox)findViewById(R.id.thurdaycheck);
		checkfriday = (CheckBox)findViewById(R.id.fridaycheck);	
		checksatday = (CheckBox)findViewById(R.id.satdaycheck);
		
		back = (LinearLayout)findViewById(R.id.socketinfo_back);
		complete = (LinearLayout)findViewById(R.id.sockettimer_complete);
		
		mrequestcode = RequestCode.NONE;
		mconfig = (ConfigInfo) getIntent().getSerializableExtra("MODIFYTIMER");
		if(mconfig !=null){
			mrequestcode = RequestCode.MODIFYTASK;
		}else{
			mconfig = new ConfigInfo();
			mrequestcode = RequestCode.ADDTIMETASK;
		}
	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
		// TODO Auto-generated method stub
		if(view == hourpick)
		{
			Log.e(TAG, "时间  HOUR选择器被按下"+ scrollState);
			
		}else if(view == minutepick){
			Log.e(TAG, "时间  MINUTE选择器被按下"+ scrollState);
		}
	}

	@Override
	public String format(int value) {
		// TODO Auto-generated method stub
		 String tmpStr = String.valueOf(value);
	        if (value < 10) {
	            tmpStr = "0" + tmpStr;
	        }
	        return tmpStr;
	}

	@Override
	public void onValueChange(NumberPicker view, int arg1, int arg2) {
		// TODO Auto-generated method stub
		//Log.i(TAG, "时间发生变化--------------");
		if(view == hourpick){
			hour = (byte) hourpick.getValue();
		}else if(view == minutepick){
			minute = (byte) minutepick.getValue();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		hourpick.clearFocus();
		minutepick.clearFocus();
		if(view == back){
			//Intent intent = new Intent();
			setResult(RESULT_CANCELED);
			//setResult(, intent);
			finish();
		}else if(view == complete){
//			hour = Byte.parseByte((String) ((TextView) hourpick).getText());
//			minute = Byte.parseByte((String) ((TextView) minutepick).getText());
			mconfig.isenable = true;
			mconfig.startenable = openofclose.isChecked();
			mconfig.endenable = !openofclose.isChecked();
			if(mconfig.startenable){
//				mconfig.startHour = (byte) hourpick.getValue();
//				mconfig.startMin = (byte) minutepick.getValue();
				mconfig.startHour = hour;
				mconfig.startMin = minute;
			}else{
//				mconfig.endHour = (byte) hourpick.getValue();
//				mconfig.endMin = (byte) minutepick.getValue();
				mconfig.endHour = hour;
				mconfig.endMin = minute;
			}
			if(checksunday.isChecked())
			{
				mconfig.week |= 1<<6;
			}else
				mconfig.week &= ~(1<<6);
			
			if(checksatday.isChecked())
				mconfig.week |= 1<<5;
			else
				mconfig.week &=~(1<<5);
			
			if(checkfriday.isChecked())
				mconfig.week |= 1<<4;
			else
				mconfig.week &= ~(1<<4);
			
			if(checkthirday.isChecked())
				mconfig.week |= 1<<3;
			else
				mconfig.week &= ~(1<<3);
			
			if(checkwedday.isChecked())
				mconfig.week |= 1<<2;
			else
				mconfig.week &= ~(1<<2);
			
			if(checktuesday.isChecked())
				mconfig.week |= 1<<1;
			else
				mconfig.week &= ~(1<<1);
			
			if(checkmonday.isChecked())
				mconfig.week |= 1<<0;
			else
				mconfig.week &= ~(1<<0);
			Log.e(TAG, "------一共选择了那几天" + mconfig.week);
			Intent intent = new Intent();
			Bundle mbundle = new Bundle();
			mbundle.putSerializable("BACKMODIFY", mconfig);
			intent.putExtras(mbundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
//		Log.i(TAG, "onFocusChange----开始编辑定时信息");
		if(!hasFocus)
		{
			if(view == hourpick){
				hour = Byte.parseByte((String) ((TextView) view).getText());
				Log.i(TAG, "hour==" + hour);
			}else if(view == minutepick){
				minute = Byte.parseByte((String) ((TextView) view).getText());
				Log.i(TAG, "minute==" + minute);
			}
		}
	}

}
