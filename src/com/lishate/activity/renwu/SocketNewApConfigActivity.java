package com.lishate.activity.renwu;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import wlt_Config.Config;
import wlt_ns.w_ns;

import com.lishate.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lishate.activity.BaseActivity;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.zxs.ptrmenulistview.CircleProgress;

public class SocketNewApConfigActivity extends BaseActivity {

	protected static final String TAG = "SocketNewApConfigActivity";
	private final static int TIME_MSG = 1;
	private final static int TIME_OK = 2;
	private final static int TIME_FAIL = 3;

	private final static int WIFI_CONNECT_START = 0;
	private final static int WIFI_CONNECT_SELFAP = 1;
	private final static int WIFI_CONNECT_WANTAP = 2;
	private final static int WIFI_CONNECT_FAIL = 4;

	private final static int connect_times = 60 * 25;// 20;

	private int wifi_connect_statue = 0;
	private LinearLayout back;
	private Config config = new Config();
	ArrayList<String> list_ns = new ArrayList<String>();
	w_ns ns;
	Context context;
	private ProgressDialog progressDialog;
	private ImageView showpass;

	private EditText ssid;
	private EditText password;
	private Button start;
	private Timer handTimer = new Timer();
	private int selapflag = 0;
	private String currentSSid;
	private static final String COMPARESSID = "SmartPlug";
	private int sendflag = 1;
	private GetInfoTask git = new GetInfoTask();
	private SharedPreferences rgbSharedPreferences;
	private SharedPreferences.Editor rgbEditor;
	private CircleProgress pbStart;
	WifiManager mWifiManager;
	private int timeout_count;
	private TextView tvConfigFail;
	private ImageView ivConfigList;
	private boolean bShow = false;
	private String[] wifis;
	
	private void findView() {
		rgbSharedPreferences = getSharedPreferences("SSIDPWD", MODE_PRIVATE);
		rgbEditor = rgbSharedPreferences.edit();
		ssid = (EditText) findViewById(R.id.socketnewconfig_edit_ssid);
		password = (EditText) findViewById(R.id.socketnewconfig_edit_pass);
		back = (LinearLayout) findViewById(R.id.socketnewconfig_back);
		start = (Button) findViewById(R.id.socketnewconfig_edit_config);
		showpass = (ImageView) findViewById(R.id.ivShowPass);
		pbStart = (CircleProgress) findViewById(R.id.pbStart);
		tvConfigFail = (TextView) findViewById(R.id.tvConfigFail);
		ivConfigList = (ImageView) findViewById(R.id.ivConfigList);
	}

	private void initView() {
		pbStart.setTitle(getResources().getString(R.string.device_new_ap));

		tvConfigFail.setText(getResources().getString(R.string.device_new_aphelp));
		tvConfigFail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SocketNewApConfigActivity.this, AddPlugActivity.class);
				startActivity(intent);
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (start.isEnabled() == true) {
					Log.i(TAG, "back");
					finish();
				} else {
					Toast.makeText(getApplicationContext(), R.string.renwu_config_back, Toast.LENGTH_SHORT).show();
				}
			}

		});

		View view = this.getLayoutInflater().inflate(R.layout.popupicon, null);
		final LinearLayout llWifi = (LinearLayout) findViewById(R.id.llWifi);
		final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));

		ListView lvConfigList = (ListView) view.findViewById(R.id.lvConfigList);
		lvConfigList.setAdapter(
				new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wifis));
		lvConfigList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ivConfigList.setImageResource(R.drawable.config_down);
				popupWindow.dismiss();
				bShow = false;
				ssid.setText(wifis[arg2]);
				password.setText(rgbSharedPreferences.getString(wifis[arg2], ""));
			}
		});

		ivConfigList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bShow) {
					bShow = false;
					ivConfigList.setImageResource(R.drawable.config_down);
					popupWindow.dismiss();
				} else {
					bShow = true;
					ivConfigList.setImageResource(R.drawable.config_up);
					popupWindow.showAsDropDown(llWifi);
				}
			}
		});

		showpass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.i(TAG, "password" + password.getInputType());
				if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
					showpass.setImageResource(R.drawable.password_hide);
					password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				} else {
					showpass.setImageResource(R.drawable.password_show);
					password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
			}
		});

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				rgbEditor.putString(ssid.getText().toString(), password.getText().toString());
				rgbEditor.commit();
				WifiInfo info = mWifiManager.getConnectionInfo();
				
				if(info == null){
					Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
					startActivity(wifiSettingsIntent);
				}else{
					String tempssid = info.getSSID();
					if (tempssid.startsWith("\"") && tempssid.endsWith("\"")) 
						tempssid = tempssid.substring(1, tempssid.length() - 1);
					
					if(!tempssid.equals(COMPARESSID)){
						Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
						startActivity(wifiSettingsIntent);
					}else{
						start.setEnabled(false);
						wifi_connect_statue = WIFI_CONNECT_START;
						if (git != null) {
							// git.loopflag = false;
							git = new GetInfoTask();
						}
						// GetInfoTask git = new GetInfoTask();
						git.execute(new Integer[0]);
						timeout_count = 0;
						StartTimer();
					}
				}
			}

		});
	}

	private TimerTask showTask = new TimerTask() {

		@Override
		public void run() {

			timeout_count++;

			Message msg = Message.obtain();
			msg.what = TIME_MSG;
			showHandler.sendMessage(msg);
		}

	};

	public boolean compareSsid(String ssid1, String ssid2) {
		if (ssid1.startsWith("\"") && ssid1.endsWith("\"") && ssid1.length() > 2) {
			ssid1 = ssid1.substring(1, ssid1.length() - 1);
		}
		if (ssid2.startsWith("\"") && ssid2.endsWith("\"") && ssid2.length() > 2) {
			ssid2 = ssid2.substring(1, ssid2.length() - 1);
		}
		return ssid1.equals(ssid2);
	}

	private void StartTimer() {
		if (handTimer == null) {
			handTimer = new Timer();
		}

//		if (showTask == null) {
			showTask = new TimerTask() {

				@Override
				public void run() {

					timeout_count++;
					pbStart.setProgress(timeout_count * 100f / connect_times);
					Message msg = Message.obtain();
					msg.what = TIME_MSG;
					showHandler.sendMessage(msg);
					// msg.what = TIME_MSG;
					// testswitchssid.sendMessage(msg);
				}
			};
//		}
		if (handTimer != null && showTask != null)
			handTimer.schedule(showTask, 0, 40);
	}

	private void StopTimer() {
		if (handTimer != null) {
			handTimer.cancel();
			handTimer = null;
		}

		if (showTask != null) {
			showTask.cancel();
			showTask = null;

		}
		timeout_count = 0;
		pbStart.setProgress(timeout_count);
	}

	protected void StopConfig() {

		// handTimer.cancel();
		// Utility.StopTimer(handTimer, showTask);
		StopTimer();
	}

	private Handler showHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case TIME_MSG:
//				Log.i(TAG, "timeout_count: " + timeout_count + ",wifi_connect_statue: " + wifi_connect_statue);
				if (timeout_count > connect_times) {
					Log.w(TAG, "ÂíÉÏÍË³ö");
					StopConfig();
					Message tmsg = Message.obtain();
					tmsg.what = TIME_FAIL;
					showHandler.sendMessage(tmsg);
				}
				WifiInfo info = mWifiManager.getConnectionInfo();
				if (info != null) {
					 if (wifi_connect_statue == WIFI_CONNECT_START) {
						Log.i(TAG, "info ssid:" + info.getSSID());
						String tempssid = info.getSSID();

						if (tempssid.startsWith("\"") && tempssid.endsWith("\"")) {
							tempssid = tempssid.substring(1, tempssid.length() - 1);
						}
						Log.i(TAG, "tempssid is: " + tempssid);
						if (tempssid.equals(COMPARESSID)) {
							Log.d(TAG, "tempssid startwith");
							wifi_connect_statue = WIFI_CONNECT_SELFAP;
							selapflag = 0;
						}
					} else if (wifi_connect_statue == WIFI_CONNECT_SELFAP) {
						selapflag++;
						if (selapflag >= connect_times) {
							wifi_connect_statue = WIFI_CONNECT_FAIL;
							selapflag = 0;
							Message tmsg = Message.obtain();
							tmsg.what = TIME_FAIL;
							showHandler.sendMessage(tmsg);
						} else {
							List<ScanResult> wifiscanlist = mWifiManager.getScanResults();
							boolean isfind = false;
							for (ScanResult sr : wifiscanlist) {
//								Log.i(TAG, "ssid: " + sr.SSID);
								String wifitempssid = sr.SSID;
								if (wifitempssid.startsWith("\"") && wifitempssid.endsWith("\"")) {
									wifitempssid = wifitempssid.substring(1, wifitempssid.length() - 1);
								}
								if (wifitempssid.equals(COMPARESSID)) {
									isfind = true;
									break;
								}
							}
							if (isfind == false && sendflag == 2) {
								wifi_connect_statue = WIFI_CONNECT_WANTAP;
							}
						}
					} else if (wifi_connect_statue == WIFI_CONNECT_WANTAP) {
						Log.i(TAG, "info ssid: " + info.getSSID() + " currentssid:" + currentSSid);
						// if(info.getSSID().equals(currentSSid) ){
						if (compareSsid(info.getSSID(), currentSSid) == true) {
							Message tmsg = Message.obtain();
							tmsg.what = TIME_OK;
							showHandler.sendMessage(tmsg);
							wifi_connect_statue = WIFI_CONNECT_START;
						} else {
							List<WifiConfiguration> wificonfigs = mWifiManager.getConfiguredNetworks();
							for (int i = 0; i < wificonfigs.size(); i++) {
								WifiConfiguration wc = wificonfigs.get(i);
								if (wc.SSID.equals(currentSSid)) {
									mWifiManager.enableNetwork(wc.networkId, true);
								}
							}
						}
					}else if (wifi_connect_statue == WIFI_CONNECT_FAIL) {
						if (!info.getSSID().equals(currentSSid)) {
							List<WifiConfiguration> wificonfigs = mWifiManager.getConfiguredNetworks();
							for (int i = 0; i < wificonfigs.size(); i++) {
								WifiConfiguration wc = wificonfigs.get(i);
								if (wc.SSID.equals(currentSSid)) {
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

	private class GetInfoTask extends AsyncTask<Integer, Integer, Integer> {

		public boolean loopflag = true;

		@Override
		protected Integer doInBackground(Integer... arg0) {

			try {

				while (true) {
					if (wifi_connect_statue != WIFI_CONNECT_SELFAP) {
						Thread.sleep(100);
					} else {
						break;
					}
				}

				Log.d(TAG, "getinfo do back");

				byte[] rcvbuf = new byte[128];
				InetAddress address = InetAddress.getByName("255.255.255.255");

				String tempssid = currentSSid.substring(1);
				tempssid = tempssid.substring(0, tempssid.length() - 1);
				byte[] ssidbytes = tempssid.getBytes();
				byte[] passbytes = password.getText().toString().getBytes();
				byte[] buf = new byte[ssidbytes.length + passbytes.length + 6 + 2];
				DatagramPacket dp = new DatagramPacket(buf, buf.length, address, 1025);
				buf[0] = 0x00;
				buf[1] = (byte) buf.length;
				buf[2] = 0x01;
				buf[3] = (byte) (ssidbytes.length + 1);
				buf[4] = (byte) (passbytes.length + 1);
				System.arraycopy(ssidbytes, 0, buf, 5, ssidbytes.length);
				System.arraycopy(passbytes, 0, buf, 5 + ssidbytes.length + 1, passbytes.length);
				buf[buf.length - 1] = (byte) 0xFF;
				DatagramPacket rcvpack = new DatagramPacket(rcvbuf, rcvbuf.length);
				DatagramSocket ds = new DatagramSocket();
				sendflag = 1;
				while (loopflag == true) {
					if (wifi_connect_statue == WIFI_CONNECT_FAIL) {
						return 0;
					}
					try {
						Log.d(TAG, "sendflag:" + sendflag);
						if (sendflag == 1) {
							ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
							ds.send(dp);
							ds.receive(rcvpack);
							Log.d(TAG, "rcvlen is: " + rcvpack.getLength());
							if (rcvpack.getLength() == 10) {
								if (rcvbuf[0] == 0x00 && rcvbuf[9] == (byte) 0xFF) {
									sendflag = 2;
									long deviceid = 0;
									int i = 0;
									for (i = 0; i < 8; i++) {
										int tempid = (int) (rcvbuf[i + 1] & 0xFF);
										deviceid = deviceid | (long) ((tempid & 0xFFFFFFFFFFFFFFFFL) << ((i) * 8));
									}

									Log.d(TAG, "deviceid is: " + deviceid);

									List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
									try {
										DeviceItemDao did = new DeviceItemDao(getHelper());
										DeviceItemModel dim = null;
										devlist.addAll(did.queryForAll());
										for (i = 0; i < devlist.size(); i++) {
											DeviceItemModel tempdim = devlist.get(i);
											if (tempdim.getDeviceId() == deviceid) {
												dim = tempdim;
												break;
											}
										}
										if (dim == null) {
											dim = new DeviceItemModel();
											dim.setDeviceId(deviceid);
											dim.setDeviceName(getString(R.string.renwu_socket) + (devlist.size() + 1));
											dim.setParentId(0);
											// devlist.add(dim);
											did.create(dim);
										}
									} catch (SQLException e) {

										e.printStackTrace();
									}
								}
							}
						} else if (sendflag == 2) {
							buf[0] = 0x00;
							buf[1] = 4;
							buf[2] = 0x02;
							buf[3] = (byte) 0xFF;
							ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
							for (int i = 0; i < 50; i++) {
								ds.send(dp);
								Thread.sleep(10);
							}
							ds.receive(rcvpack);
							if (rcvpack.getLength() == 2) {
								return 1;
							}
						}
					} catch (Exception e) {
						if (sendflag == 2) {

							return 1;
						}
					}
				}
			} catch (Exception e) {

			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			sendflag = 1;
			if (result == 1) {
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
		if (handTimer != null) {
			handTimer.cancel();
			handTimer = null;
		}
	}

	@Override
	protected void onCreate(Bundle paramBundle) {

		super.onCreate(paramBundle);
		setContentView(R.layout.socketnewapconfig);
		
		findView();
		Map<String, ?> all = rgbSharedPreferences.getAll();
		wifis = new String[all.size()];
		int i = 0;
		for (String key : all.keySet()) {
			wifis[i] = key;
			i++;
		}
		initView();

		progressDialog = new ProgressDialog(this);
		context = this.getApplicationContext();
		String ssid_data = null;
		ssid_data = config.initData(context);
		config.initData(context);

		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiManager.getDhcpInfo();

		if (ssid_data != null) {
			ssid.setText(ssid_data);
			// if(!(rgbSharedPreferences.getString(ssid_data,
			// "").equalsIgnoreCase("")));
			password.setText(rgbSharedPreferences.getString(ssid_data, ""));

			currentSSid = "\"" + ssid_data + "\"";
		} else {
			Toast.makeText(getApplicationContext(), R.string.unNetwork, Toast.LENGTH_LONG).show();
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}

		wifi_connect_statue = WIFI_CONNECT_START;

	}

}
