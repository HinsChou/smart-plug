package com.lishate.activity;


import com.lishate.R;
import com.lishate.utility.Utility;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SwitchConfigActivity extends BaseActivity {

	private int max_time = 60;
	private SharedPreferences mWifiSharedPreferences;
	private LinearLayout mBodyLayout;
	private TextView mConfigResult;
	private boolean mIsConfigSuccess = false;
	private TextView mErrorWifi;
	private ImageButton helpBtn;
	private int mNewProgress;
	private EditText mPassword;
	private PopupWindow mPopAlert;
	private ProgressBar mProgressBar;
	private ImageButton mRefreshButton;
	private String mSSID;
	private CheckBox mShowPassword;
	private EditText mSsidValue;
	private Button mSubmitButton;
	private String mWifiGateway;
	
	private static final String SWITCH_CONFIG_WIFI  = "config_wifi";
	private static final String SWITCH_CONFIG_WIFI_KEY = "ssid";
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.config);
		findView();
		initView();
		initMenu();
		mWifiSharedPreferences = getSharedPreferences(SWITCH_CONFIG_WIFI,0);
	}
	
	private void findView(){
		mBodyLayout = (LinearLayout)findViewById(R.id.config_body_layout);
		mRefreshButton = (ImageButton)findViewById(R.id.config_refresh);
		helpBtn = (ImageButton)findViewById(R.id.config_help);
		mErrorWifi = (TextView)findViewById(R.id.config_errorWifi);
		mSsidValue = (EditText)findViewById(R.id.config_ssid_value);
		mPassword = (EditText)findViewById(R.id.config_pass);
		mShowPassword = (CheckBox)findViewById(R.id.config_pass_show);
		mSubmitButton = (Button)findViewById(R.id.config_submit);
	}
	
	private void initView(){
		helpBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SwitchConfigActivity.this, SwitchConfigHelpActivity.class);
				SwitchConfigActivity.this.startActivity(intent);
			}
			
		});
		mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked == true){
					mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				else{
					mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
			
		});
		mRefreshButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CheckWifi();
			}
			
		});
		mSubmitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSSID = mSsidValue.getText().toString();
				if(checkSSID(mSSID) ==  true){
					SwitchConfigActivity.this.popShow();
				}
				else{
					Toast.makeText(SwitchConfigActivity.this, R.string.ssid_error, Toast.LENGTH_LONG);
				}
			}
		});
		
	}
	private void initMenu(){
		View alert = getLayoutInflater().inflate(R.layout.configalert, null);
		mConfigResult = (TextView)alert.findViewById(R.id.config_alert_title);
		mProgressBar  = (ProgressBar)alert.findViewById(R.id.config_alert_progressBar1);
		mProgressBar.setMax(max_time);
		Button btn = (Button)alert.findViewById(R.id.config_alert_close);
		btn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SwitchConfigActivity.this.mPopAlert.dismiss();
				if(SwitchConfigActivity.this.mIsConfigSuccess == true){
					SwitchConfigActivity.this.startActivity(new Intent(SwitchConfigActivity.this, MainMenuActivity.class));
					SwitchConfigActivity.this.finish();
				}
				else{
					
				}
			}
			
		});
		mPopAlert = new PopupWindow(alert,-1,-1);
	}
	
	private void popShow(){
		mNewProgress = 0;
		mPopAlert.showAtLocation(findViewById(R.id.config), Gravity.AXIS_SPECIFIED | Gravity.BOTTOM, 0, 0);
		mPopAlert.setFocusable(true);
		mPopAlert.setTouchable(true);
		mPopAlert.setOutsideTouchable(false);
		mConfigResult.setText(R.string.configing);
	}
	
	private boolean checkSSID(String ssid){
		int len = ssid.length();
		for(int i=0; i<len; i++){
			int j = ssid.charAt(i);
			if(j <33 || j > 126){
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private String GetGateWay(WifiManager localManager){
		return Formatter.formatIpAddress(localManager.getDhcpInfo().gateway);
	}

	private void CheckWifi(){
		mSSID = "";
		if(Utility.IsWifiConnected(this) == false){
			mErrorWifi.setVisibility(View.VISIBLE);
			mBodyLayout.setVisibility(View.GONE);
		}
		else{
			WifiManager localManager = null;
			try{
				localManager = (WifiManager)getSystemService(Service.WIFI_SERVICE);
				WifiInfo wifiInfo = localManager.getConnectionInfo();
				String strInfo = wifiInfo.toString();
				String strSSID = wifiInfo.getSSID().toString();
				strSSID = strSSID.replace("\"", "");
				mSSID= strSSID;
				
				mSsidValue.setText(mSSID);
				
				mErrorWifi.setVisibility(View.GONE);
				mBodyLayout.setVisibility(View.VISIBLE);
			}catch(Exception e){
				mErrorWifi.setVisibility(View.GONE);
				mBodyLayout.setVisibility(View.VISIBLE);
				mSsidValue.setText(mSSID);
				mPassword.setText(mWifiSharedPreferences.getString(SWITCH_CONFIG_WIFI_KEY, ""));
				mWifiGateway = GetGateWay(localManager);
			}
			
		}
	}
}
