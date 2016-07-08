package com.lishate.activity;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.lishate.R;
import com.lishate.data.GobalDef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ControlSetAlertActivity extends Activity {

	private Button cancel;
	private String hour = "1";
	private WheelView hours;
	private WheelView mins;
	private String min = "1";
	private Button save;
	
	public void findView(){
		cancel = (Button)findViewById(R.id.controlalert_cancel);
		save = (Button)findViewById(R.id.controlalert_save);
		hours = (WheelView)findViewById(R.id.controlalert_hour);
		mins = (WheelView)findViewById(R.id.controlalert_mins);
	}
	
	private void initView(){
		save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.putExtra(GobalDef.INTENT_TIME_HOUR, String.valueOf(ControlSetAlertActivity.this.hours.getCurrentItem()));
		        intent.putExtra(GobalDef.INTENT_TIME_MIN, String.valueOf(ControlSetAlertActivity.this.mins.getCurrentItem()));
		        ControlSetAlertActivity.this.setResult(1, intent);
		        ControlSetAlertActivity.this.finish();
			}
			
		});
		
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ControlSetAlertActivity.this.finish();
			}
			
		});
		
		hours.setViewAdapter(new NumericWheelAdapter(this, 0, 23));
		mins.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlalert);
		Intent intent = getIntent();
		int hour = Integer.parseInt(intent.getExtras().getString(GobalDef.INTENT_TIME_HOUR));
		int min = Integer.parseInt(intent.getExtras().getString(GobalDef.INTENT_TIME_MIN));
		findView();
		initView();
		if( hour < 24){
			hours.setCurrentItem(hour);
		}
		if( min < 60){
			mins.setCurrentItem(min);
		}
	}

	
}
