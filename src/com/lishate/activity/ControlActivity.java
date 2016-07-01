package com.lishate.activity;

import com.lishate.R;
import com.lishate.application.switchApplication;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.utility.Utility;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ControlActivity extends BaseActivity {

	private DeviceItemModel dim;
	private ImageButton mBackButton;
	private RelativeLayout mMenuButton;
	private TextView mNoticeDelay;
	private TextView mNoticeTime;
	private Thread mReflshTimeThread;
	private ImageButton mRefreshButton;
	private ImageButton mSetStatusButton;
	private TextView mTitle;
	private ProgressBar refresjing;
	
	public void findView(){
		mTitle = (TextView)findViewById(R.id.control_title_view);
		mBackButton = (ImageButton)findViewById(R.id.control_back);
		mRefreshButton = (ImageButton)findViewById(R.id.control_refresh);
		mSetStatusButton = (ImageButton)findViewById(R.id.control_set_status);
		mMenuButton = (RelativeLayout)findViewById(R.id.control_bom_bar_button);
		mNoticeTime = (TextView)findViewById(R.id.control_notice_time);
		mNoticeDelay = (TextView)findViewById(R.id.control_notice_delay);
		refresjing = (ProgressBar)findViewById(R.id.control_refresjing);
	}
	
	private void initView(){
		mBackButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		mMenuButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openOptionsMenu();
			}
			
		});
		mSetStatusButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utility.CheckNetwork(ControlActivity.this)){
					new ControlSwitchTask().execute(new Void[0]);
				}
				else{
					Toast.makeText(ControlActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT);
				}
			}
			
		});
		mRefreshButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utility.CheckNetwork(ControlActivity.this)){
					new RefreshTask().execute(new Void[0]);
				}
				else{
					Toast.makeText(ControlActivity.this, getString(R.string.timeout), Toast.LENGTH_SHORT);
				}
			}
			
		});
	}
	
	private void RefreshView(){
		this.mTitle.setText(dim.getDeviceName());
		if(dim.getDeviceStatus() == 0){
			mSetStatusButton.setImageResource(R.drawable.set_cfg_off);
		}
		else{
			mSetStatusButton.setImageResource(R.drawable.set_cfg_on);
		}
	}
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.control);
		switchApplication app = (switchApplication)getApplication();
		dim = app.getItemModel();
		findView();
		initView();
		RefreshView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST+1, 1, R.string.timeTask).setIcon(R.drawable.emoji_35);
		menu.add(Menu.NONE,Menu.FIRST+2,2,R.string.delayDo).setIcon(R.drawable.emoji_149);
		menu.add(Menu.NONE,Menu.FIRST+3,3,R.string.deviceInfo).setIcon(R.drawable.emoji_104);
		return super.onCreateOptionsMenu(menu);
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch(item.getItemId()){
		case Menu.FIRST+1:
			intent.setClass(this, ControlSetActivity.class);
			break;
		case Menu.FIRST+2:
			intent.setClass(this, ControlDelayActivity.class);
			break;
		case Menu.FIRST+3:
			intent.setClass(this, DeviceEditActivity.class);
			break;
		}
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}



	class ControlSwitchTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	class RefreshTask extends AsyncTask<Void, Void,String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
