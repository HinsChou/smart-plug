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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lishate.activity.BaseActivity;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;

public class SocketNewApConfigActivity extends BaseActivity {

	protected static final String TAG = "SocketConfigActivity";
	private final static int TIME_MSG = 1;
	private final static int TIME_OK = 2;
	private final static int TIME_FAIL = 3;
	
	private final static int WIFI_CONNECT_START = 0;
	private final static int WIFI_CONNECT_SELFAP = 1;
	private final static int WIFI_CONNECT_WANTAP = 2;
	private final static int WIFI_CONNECT_SELAPGO = 3;
	private final static int WIFI_CONNECT_FAIL = 4;
	
	private final static int connect_times = 60;//20;
	
	private int wifi_connect_statue = 0;
	private ImageButton back;
	private Config config = new Config();
	private ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> list_ns=new ArrayList<String>();
	w_ns ns;
	Context context;
	private ProgressDialog progressDialog;
	private CheckBox showpass;
	
	private EditText ssid;
	private EditText password;
	private Button start;
	private Timer handTimer=new Timer();
	private int selapflag = 0;
	
	private String currentSSid;
	private static final String COMPARESSID = "SmartPlug";
	private int sendflag = 1;
	private GetInfoTask git = new GetInfoTask();
	private SharedPreferences rgbSharedPreferences;
	private SharedPreferences.Editor rgbEditor;
	
	WifiManager mWifiManager;
	
	private int timeout_count;
	private void findView(){
		rgbSharedPreferences = getSharedPreferences("SSIDPWD", MODE_PRIVATE);
		rgbEditor = rgbSharedPreferences.edit();
		ssid = (EditText)findViewById(R.id.socketnewconfig_edit_ssid);
		password = (EditText)findViewById(R.id.socketnewconfig_edit_pass);
		back =(ImageButton)findViewById(R.id.socketnewconfig_back);
		start = (Button)findViewById(R.id.socketnewconfig_edit_config);
		showpass = (CheckBox)findViewById(R.id.socketnewconfig_edit_showpass);
	}
	
	private void initView(){
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(start.isEnabled() == true){
					finish();
				}
				else{
					Toast.makeText(getApplicationContext(), R.string.renwu_config_back, Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		showpass.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(!showpass.isChecked()){
					password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				else{
					password.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}
			
		});
		
		start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rgbEditor.putString(ssid.getText().toString(), password.getText().toString());
				rgbEditor.commit();
				Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");   
				startActivity(wifiSettingsIntent); 
				start.setEnabled(false);
				wifi_connect_statue = WIFI_CONNECT_START;
				if(git != null){
					//git.loopflag = false;
					git = new GetInfoTask();
				}
				//GetInfoTask git = new GetInfoTask();
				git.execute(new Integer[0]);
				timeout_count =0;
				StartTimer();
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setMessage(SocketNewApConfigActivity.this.getString(R.string.renwu_config_finding));
				progressDialog.show();
			}
			
		});
	}
	
	private TimerTask showTask = new TimerTask(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			timeout_count ++;

			Message msg = Message.obtain();
			msg.what = TIME_MSG;
			showHandler.sendMessage(msg);
		}
		
	};
	
	public boolean compareSsid(String ssid1, String ssid2){
		if(ssid1.startsWith("\"") && ssid1.endsWith("\"") && ssid1.length() > 2){
			ssid1 = ssid1.substring(1, ssid1.length() - 1);
		}
		if(ssid2.startsWith("\"") && ssid2.endsWith("\"") && ssid2.length() > 2){
			ssid2 = ssid2.substring(1, ssid2.length() - 1);
		}
		return ssid1.equals(ssid2);
	}
	private void StartTimer(){
		if(handTimer == null){
			handTimer = new Timer();
		}
		
		if(showTask == null){
			showTask = new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					timeout_count ++;

					Message msg = Message.obtain();
					msg.what = TIME_MSG;
					showHandler.sendMessage(msg);
//					msg.what = TIME_MSG;
//					testswitchssid.sendMessage(msg);
				}
			};
		}
		if(handTimer != null && showTask != null )  
			handTimer.schedule(showTask, 10, 1000);  
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
	}
	protected void StopConfig() {
		// TODO Auto-generated method stub
		//handTimer.cancel();
		//Utility.StopTimer(handTimer, showTask);
		StopTimer();
	}
	private Handler showHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case TIME_MSG:
				Log.e(TAG, "定时信息为timeout_count-----------"+ timeout_count+ "wifi_connect_statue=" + wifi_connect_statue);
				if(timeout_count >120)
				{
					
					Log.e(TAG, "马上退出-----------");
					StopConfig();
					Message tmsg = Message.obtain();
					tmsg.what = TIME_FAIL;
					showHandler.sendMessage(tmsg);
					
				}
				WifiInfo info = mWifiManager.getConnectionInfo();
				//Log.d(TAG, "wifi_connect statue:" + wifi_connect_statue);
				if(info != null){
					//Log.d(TAG, "info ssid " + info.getSSID());
					if(wifi_connect_statue == WIFI_CONNECT_WANTAP){
						Log.d(TAG, "info ssid: " + info.getSSID() + " currentssid:" + currentSSid);
						//if(info.getSSID().equals(currentSSid) ){
						if(compareSsid(info.getSSID(), currentSSid) == true){
							Message tmsg = Message.obtain();
							tmsg.what = TIME_OK;
							showHandler.sendMessage(tmsg);
							wifi_connect_statue = WIFI_CONNECT_START;
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
				Toast.makeText(getApplicationContext(), R.string.renwu_config_sus, Toast.LENGTH_SHORT).show();
				setResult(GobalDef.CONFIG_DEVICE_APCONFIG_OK);
				finish();
				break;
			case TIME_FAIL:
				Log.d(TAG, "wifi_connect fail");
				progressDialog.dismiss();
				start.setEnabled(true);
				Toast.makeText(getApplicationContext(), R.string.renwu_config_fail, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};
	
	private class GetInfoTask extends AsyncTask<Integer, Integer, Integer>{
		
		public boolean loopflag = true;

		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
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
											dim.setDeviceName("插座" +devlist.size() );
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			sendflag = 1;
			if(result == 1){
				wifi_connect_statue = WIFI_CONNECT_WANTAP;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(handTimer != null){
			handTimer.cancel();
			handTimer = null;
		}
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
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
		DhcpInfo di = mWifiManager.getDhcpInfo();
		
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
			toast=Toast.makeText(getApplicationContext(), R.string.unNetwork, Toast.LENGTH_LONG);
			//toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
		wifi_connect_statue = WIFI_CONNECT_START;
		
	}
	
	
}
