package com.lishate.activity.renwu;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wlt_Config.Config;
import wlt_ns.w_ns;

import com.lishate.R;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lishate.activity.BaseActivity;
import com.lishate.config.configInterface;
import com.lishate.config.esp8266smartconfig;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.utility.Utility;
import com.zxs.ptrmenulistview.CircleProgress;
public class SocketNewConfigActivity extends BaseActivity implements configInterface {

	protected static final String TAG = "SocketConfigActivity";
	private final static int TIME_MSG = 1;
	private final static int TIME_OK = 2;
	private final static int TIME_FAIL = 3;
	
	private final static int WIFI_CONNECT_START = 0;
	private final static int WIFI_CONNECT_SELFAP = 1;
	private final static int WIFI_CONNECT_WANTAP = 2;
	private final static int WIFI_CONNECT_FAIL = 4;
	private final static int ADDDEVICE = 6;
	
	private final static int TIMEROUT = 7;
	private final static int CONNECTING = 8;
	private final static int CONNECTING_SEC = 9;
	
	private final static int connect_times = 60;//20;
	
	private int wifi_connect_statue = 0;
	private LinearLayout back;
	private Config config = new Config();
	private ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> list_ns=new ArrayList<String>();
	w_ns ns;
	Context context;
	private ProgressDialog progressDialog;
	private ImageView showpass;
	private CircleProgress pbStart;
	private EditText ssid;
	private EditText password;
	private Button start;
	private Button apconfig;
	private Timer handTimer;
	private TimerTask showTask;
	private int selapflag = 0;
	
	private String currentSSid;
	private static final String COMPARESSID = "Aura";
	 private final static String SMARTCONFIGURATESSID = "\"" + "Aura" + "\"";
	private int sendflag = 1;
	private GetInfoTask git = new GetInfoTask();
	private SharedPreferences rgbSharedPreferences;
	private SharedPreferences.Editor rgbEditor;
	
	WifiManager mWifiManager;
	private BroadcastReceiver wifiConnectReceiver;
	private esp8266smartconfig e8266config;
	private String DEVICEID = "devid";
	private int timeout_count;
	private void findView(){
		rgbSharedPreferences = getSharedPreferences("SSIDPWD", MODE_PRIVATE);
		rgbEditor = rgbSharedPreferences.edit();
		ssid = (EditText)findViewById(R.id.socketnewconfig_edit_ssid);
		password = (EditText)findViewById(R.id.socketnewconfig_edit_pass);
		back =(LinearLayout)findViewById(R.id.socketnewconfig_back);
		pbStart = (CircleProgress)findViewById(R.id.pbStart);
		start = (Button)findViewById(R.id.socketnewconfig_edit_config);
		showpass = (ImageView)findViewById(R.id.ivShowPass);
		apconfig = (Button)findViewById(R.id.socketdetail_search_config);
	}
	
	private void initView(){
		pbStart.setTitle(getResources().getString(R.string.device_new_onekey));
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if(start.isEnabled() == true){
					SocketNewConfigActivity.this.finish();
				}
				else{
					Toast.makeText(SocketNewConfigActivity.this, R.string.renwu_config_back, Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		showpass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Log.i(TAG, "password" + password.getInputType());
				if(password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
					showpass.setImageResource(R.drawable.password_hide);
					password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					showpass.setImageResource(R.drawable.password_show);
					password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
			}
		});
		
		apconfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent mintent = new Intent(getApplicationContext(), SocketNewApConfigActivity.class);
				startActivity(mintent);
//				finish();
			}
		});
		
		
		start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				rgbEditor.putString(ssid.getText().toString(), password.getText().toString());
				rgbEditor.commit();
//				Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");   
//				startActivity(wifiSettingsIntent);
				
				StartTimer();
				
				start.setEnabled(false);
//				wifi_connect_statue = WIFI_CONNECT_START;
//				processstatus.setText(getText(R.string.configstart));
//				processstatus.setBackgroundResource(R.drawable.configstart);
//				if(git != null){
//					git.loopflag = false;
//					git = new GetInfoTask();
//				}
//				//GetInfoTask git = new GetInfoTask();
//				git.execute(new Integer[0]);
				Log.e(TAG, "配置开始------------------");
				SendConfigConnect();
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setMessage(SocketNewConfigActivity.this.getString(R.string.renwu_config_finding));
//				progressDialog.show();
			}
			
		});
	}
	private void StartTimer(){
		if(handTimer == null){
			handTimer = new Timer();
		}
		
		if(showTask == null){
			showTask = new TimerTask() {
				@Override
				public void run() {
					
					Message msg = Message.obtain();
//					msg.what = TIME_MSG;
//					showHandler.sendMessage(msg);
//					Log.e(TAG, "定时计数中*********timeout_count=" + timeout_count + "******");
					timeout_count ++;
					msg.what = CONNECTING_SEC;
					if(timeout_count == 25 * 60)
						msg.what = TIMEROUT;
					
					pbStart.setProgress(timeout_count * 100f / (25 * 60));
					showHandler.sendMessage(msg);
//					msg.what = TIME_MSG;
//					testswitchssid.sendMessage(msg);
				}
			};
		}
		if(handTimer != null && showTask != null )  
			handTimer.schedule(showTask, 0, 40);  
	}
	
	private void StopTimer(){
		if(handTimer != null){
			handTimer.cancel();
			handTimer = null;
		}
		
		if(showTask != null){
			showTask.cancel();
			showTask = null;
			
		}
		timeout_count = 0;
		pbStart.setProgress(timeout_count);
	}
	
	public void SendConfigConnect(){
		String apSsid = ssid.getText().toString().trim();;
		String apPassword = password.getText().toString().trim();
		Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid + ", "
				+ " mEdtApPassword = " + apPassword);
		new ConfigAsyncTask().execute(apSsid, apPassword);
	}
	private class ConfigAsyncTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			
			String apSsid = params[0];
			String apPassword = params[1];
			e8266config = new esp8266smartconfig(apSsid,  apPassword);
			e8266config.setConfigInterface(SocketNewConfigActivity.this);
			e8266config.timeout = 100000;
			e8266config.startConfig();
			return null;
		}
		
	}
	@Override
	public void isStartConfig() {
		
		Log.e(TAG, "isStartConfig");
	}

	@Override
	public void isStopConfig() {
		
		Log.e(TAG, "isStopConfig");
		
	}

	@Override
	public void completeConfig(String parameter) {
		
		Log.d(TAG, "iscompleteConfig");
		Message msg = Message.obtain();
		msg.what = ADDDEVICE;
		Bundle bund = new Bundle();
		bund.putString(DEVICEID, parameter);
		msg.setData(bund);
		showHandler.sendMessage(msg);
	}

	@Override
	public void configtimeout() {
		
		showHandler.sendEmptyMessage(TIMEROUT);
		
	}
	
	
	
	public  class WifiConnectReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo notewokInfo = manager.getActiveNetworkInfo();
				if (notewokInfo != null) {
					System.out.println("notewokInfo.getExtraInfo()------->"
							+ notewokInfo.getExtraInfo());
					WifiInfo currentWifiInfo = mWifiManager.getConnectionInfo();
					System.out.println("currentWifiInfo.getSSID()----->"
							+ currentWifiInfo.getSSID());
					if (currentWifiInfo.getSSID().equals(currentSSid) || currentWifiInfo.getSSID().equals(SMARTCONFIGURATESSID)) {
					}
					Log.e(TAG, "切换到"+ currentWifiInfo.getSSID());
				} else {
					System.out.println("notewokInfo is null");
				}

			}
		}

	}
	public boolean compareSsid(String ssid1, String ssid2){
		if(ssid1.startsWith("\"") && ssid1.endsWith("\"") && ssid1.length() > 2){
			ssid1 = ssid1.substring(1, ssid1.length() - 1);
		}
		if(ssid2.startsWith("\"") && ssid2.endsWith("\"") && ssid2.length() > 2){
			ssid2 = ssid2.substring(1, ssid2.length() - 1);
		}
		return ssid1.equals(ssid2);
	}
	private Handler showHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			switch(msg.what){
			case TIME_MSG:
				WifiInfo info = mWifiManager.getConnectionInfo();
				//Log.d(TAG, "wifi_connect statue:" + wifi_connect_statue);
				if(info != null){
					//Log.d(TAG, "info ssid " + info.getSSID());
					if(wifi_connect_statue == WIFI_CONNECT_WANTAP){
						Log.d(TAG, "info ssid: " + info.getSSID() + " currentssid:" + currentSSid);
						//if(info.getSSID().equals(currentSSid) ){
						if(compareSsid(info.getSSID(), currentSSid) ){
							Log.e(TAG,"连接信息为：" + info.getSupplicantState());
							if(info.getSupplicantState() == SupplicantState.COMPLETED ){
								//Log.e(TAG,"连接信息为：" + info.getSupplicantState());
								Message tmsg = Message.obtain(); 
							tmsg.what = TIME_OK;//配置完成
							Toast.makeText(getApplicationContext(), "已经连上家庭路由" + currentSSid, Toast.LENGTH_SHORT).show();
							showHandler.sendMessage(tmsg);
							handTimer.cancel();
							//wifi_connect_statue = WIFI_CONNECT_START;//状态重新连回到家庭路由器
							}
						}
						else{
							List<WifiConfiguration> wificonfigs = mWifiManager.getConfiguredNetworks();
							for(int i = 0; i<wificonfigs.size(); i++){
								WifiConfiguration wc = wificonfigs.get(i);
								if(wc.SSID.equals(currentSSid)){
									mWifiManager.enableNetwork(wc.networkId, true);
								}
							}
						}
					}
					else if(wifi_connect_statue == WIFI_CONNECT_START){
						Log.d(TAG, "info ssid:" + info.getSSID());
						//if(info.getSSID().length() > 3){
							String tempssid = info.getSSID();
							
							if(tempssid.startsWith("\"") && tempssid.endsWith("\"")){
								tempssid = tempssid.substring(1, tempssid.length() - 1);
							}
							//tempssid = tempssid.substring(0, 3);
							Log.d(TAG, "tempssid is: " + tempssid);
							if(tempssid.equals(COMPARESSID)){
							//if(tempssid.startsWith(COMPARESSID)){
								Log.d(TAG, "tempssid startwith");
								wifi_connect_statue = WIFI_CONNECT_SELFAP;
								selapflag = 0;
							}
						//}
					}
					else if(wifi_connect_statue == WIFI_CONNECT_SELFAP){
						selapflag++;
						if(selapflag >= connect_times){
								wifi_connect_statue = WIFI_CONNECT_FAIL;
								selapflag = 0;
								Message tmsg = Message.obtain();
								tmsg.what = TIME_FAIL;
								showHandler.sendMessage(tmsg);
						}
						else{
							List<ScanResult> wifiscanlist = mWifiManager.getScanResults();
							boolean isfind = false;
							for(ScanResult sr : wifiscanlist){
								String wifitempssid = sr.SSID;
								if(wifitempssid.startsWith("\"") && wifitempssid.endsWith("\"")){
									wifitempssid = wifitempssid.substring(1, wifitempssid.length() - 1);
								}
								if(wifitempssid.equals(COMPARESSID)){
									isfind = true;
									break;
								}
							}
							if(isfind == false && sendflag == 2){
								wifi_connect_statue = WIFI_CONNECT_WANTAP;
							}
						}
					}
					else if(wifi_connect_statue == WIFI_CONNECT_FAIL){
						if(!info.getSSID().equals(currentSSid) )
						{
							List<WifiConfiguration> wificonfigs = mWifiManager.getConfiguredNetworks();
							for(int i = 0; i<wificonfigs.size(); i++){
								WifiConfiguration wc = wificonfigs.get(i);
								if(wc.SSID.equals(currentSSid)){
									mWifiManager.enableNetwork(wc.networkId, true);
								}
							}
						}
					}
				}
				break;
			case TIME_OK:
				Log.d(TAG, "wifi_connect ok");
				progressDialog.dismiss();
				start.setEnabled(true);
				Toast.makeText(SocketNewConfigActivity.this, R.string.renwu_config_sus, Toast.LENGTH_SHORT).show();
				setResult(GobalDef.CONFIG_DEVICE_SMARTCONFIG_OK);
				finish();
				break;
			case TIME_FAIL:
				Log.d(TAG, "wifi_connect fail");
				progressDialog.dismiss();
				start.setEnabled(true);
				Toast.makeText(SocketNewConfigActivity.this, R.string.renwu_config_fail, Toast.LENGTH_SHORT).show();
				break;
			case ADDDEVICE:
				e8266config.stopConfig();
				Bundle msgbundle = msg.getData();
				String deviid = msgbundle.getString(DEVICEID);
				Log.e(TAG, "配置成功，准备增加插座--------设备ID号是："+ deviid);
				progressDialog.dismiss();
				start.setEnabled(true);
				StopTimer();
				AddPlug(deviid);
				setResult(GobalDef.CONFIG_DEVICE_SMARTCONFIG_OK);
				finish();
				break;
			case CONNECTING:
				break;
			case CONNECTING_SEC:
				break;
			case TIMEROUT:
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.renwu_config_fail), Toast.LENGTH_SHORT).show();
				e8266config.stopConfig();
				progressDialog.dismiss();
				start.setEnabled(true);
				StopTimer();
				Log.e(TAG, "配置超时---------------------");
				break;
			default:
				break;
			}
		}
		
	};
	private void AddPlug(String deviceid){
		long id = 0;
		id = Utility.StringToLong(deviceid);
	//	String tempssid = deviceid.substring(1);
	//	tempssid = tempssid.substring(0,tempssid.length() - 1);
		//long  id = Long.p
		Log.e(TAG, deviceid + "  设备ID号是：" + Long.toHexString(id));
		List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
		try {
			DeviceItemDao did = new DeviceItemDao(getHelper());
			DeviceItemModel dim = null;
			devlist.addAll(did.queryForAll());
			for(int i=0; i<devlist.size(); i++){
				DeviceItemModel tempdim = devlist.get(i);
				if(tempdim.getDeviceId() == id){
					dim = tempdim;
					break;
				}
			}
			if(dim == null){
				
				dim = new DeviceItemModel();
				dim.setDeviceId(id);
				dim.setDeviceName(getString(R.string.renwu_socket) + devlist.size() );
				dim.setParentId(0);
				
				//devlist.add(dim);
				did.create(dim);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private class GetInfoTask extends AsyncTask<Integer, Integer, Integer>{
		
		public boolean loopflag = true;

		@Override
		protected Integer doInBackground(Integer... arg0) {
			
			try{
				
				while(true){
					if(wifi_connect_statue != WIFI_CONNECT_SELFAP){
						Thread.sleep(100);
					}
					else{
						break;
					}
				}
				
				Log.d(TAG, "getinfo do back");
				
				byte[] rcvbuf = new byte[128];
				InetAddress address = InetAddress.getByName("255.255.255.255");
				
				String tempssid = currentSSid.substring(1);
				tempssid = tempssid.substring(0,tempssid.length() - 1);
				byte[] ssidbytes = tempssid.getBytes();
				byte[] passbytes = password.getText().toString().getBytes();
				byte[] buf = new byte[ssidbytes.length + passbytes.length + 6 + 2];
				DatagramPacket dp = new DatagramPacket(buf,buf.length,address,1025);
				buf[0] = 0x00;
				buf[1] = (byte)buf.length;
				buf[2] = 0x01;
				buf[3] = (byte) (ssidbytes.length + 1);
				buf[4] = (byte) (passbytes.length + 1);
				System.arraycopy(ssidbytes, 0, buf, 5, ssidbytes.length);
				System.arraycopy(passbytes, 0, buf, 5 + ssidbytes.length + 1, passbytes.length);
				buf[buf.length - 1] = (byte) 0xFF;
				DatagramPacket rcvpack = new DatagramPacket(rcvbuf, rcvbuf.length);
				DatagramSocket ds = new DatagramSocket();
				//int sendflag = 1;
				sendflag = 1;
				while(loopflag == true){
					if(wifi_connect_statue == WIFI_CONNECT_FAIL){
						return 0;
					}
					try{
						Log.d(TAG, "sendflag:" + sendflag);
						if(sendflag == 1){
							ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
							ds.send(dp);
							ds.receive(rcvpack);
							Log.d(TAG, "rcvlen is: " + rcvpack.getLength());
							if(rcvpack.getLength() == 10){
								if(rcvbuf[0] == 0x00 && rcvbuf[9] == (byte)0xFF){
									sendflag = 2;
									long deviceid = 0;
									int i = 0;
									for(i=0; i<8; i++){
										int tempid = (int)(rcvbuf[i+1] & 0xFF);
										deviceid = deviceid | (long)((tempid & 0xFFFFFFFFFFFFFFFFL)<< (( i) * 8));
									}
									
									Log.d(TAG, "deviceid is: " + deviceid);
									
									List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
									try {
										DeviceItemDao did = new DeviceItemDao(getHelper());
										DeviceItemModel dim = null;
										devlist.addAll(did.queryForAll());
										for(i=0; i<devlist.size(); i++){
											DeviceItemModel tempdim = devlist.get(i);
											if(tempdim.getDeviceId() == deviceid){
												dim = tempdim;
												break;
											}
										}
										if(dim == null){
											dim = new DeviceItemModel();
											dim.setDeviceId(deviceid);
											dim.setDeviceName(getString(R.string.renwu_socket) +devlist.size() );
											dim.setParentId(0);
											//devlist.add(dim);
											did.create(dim);
										}
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						else if(sendflag == 2){
							buf[0] = 0x00;
							buf[1] = 4;
							buf[2] = 0x02;
							buf[3] = (byte)0xFF;
							ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
							for(int i=0; i<50; i++)
							{
								ds.send(dp);
								Thread.sleep(10);
							}
							ds.receive(rcvpack);
							if(rcvpack.getLength() ==2){
								return 1;
							}
						}
					}
					catch(Exception e){
						if(sendflag == 2){
						
						
							return 1;
						}
					}
				}
			}
			catch(Exception e){
				
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			
			super.onPostExecute(result);
			sendflag = 1;
			if(result == 1){
				wifi_connect_statue = WIFI_CONNECT_WANTAP;
			}
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		if(handTimer != null){
			handTimer.cancel();
			handTimer = null;
		}
		unregisterReceiver(wifiConnectReceiver);
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		
		super.onCreate(paramBundle);
		setContentView(R.layout.socketnewconfig);
		findView();
		initView();
		
		progressDialog = new ProgressDialog(this);
		context=this.getApplicationContext();
		String ssid_data=null;
		ssid_data=config.initData(context);
		config.initData(context);
		
		mWifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		if(!mWifiManager.isWifiEnabled()){
			Toast.makeText(getApplicationContext(), "请连接到家庭路由器", Toast.LENGTH_SHORT).show();
			Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");   
			startActivity(wifiSettingsIntent); 
		}
		if(!mWifiManager.isWifiEnabled())
		{
			finish();
		}else{
			
			while(mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED)
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		mWifiManager.getDhcpInfo();
		
		wifiConnectReceiver = new WifiConnectReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(wifiConnectReceiver, filter);
		if(ssid_data != null){
			ssid.setText(ssid_data);
			if(!(rgbSharedPreferences.getString(ssid_data, "").equalsIgnoreCase("")));
			{
				password.setText(rgbSharedPreferences.getString(ssid_data, ""));
			}
			
			currentSSid = "\"" + ssid_data + "\"";
		}
		else{
			Toast toast;
			toast=Toast.makeText(SocketNewConfigActivity.this, R.string.unNetwork, Toast.LENGTH_LONG);
			//toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
		wifi_connect_statue = WIFI_CONNECT_START;
	}
	
	
}
