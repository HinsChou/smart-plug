package com.lishate.config;

import java.io.IOException;
//import com.lishate.wificonnect.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wlt_Config.Config;
import wlt_ns.w_ns;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.integrity_project.smartconfiglib.FirstTimeConfig;
import com.integrity_project.smartconfiglib.FirstTimeConfigListener;
import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.utility.Utility;
import com.lishate.config.*;


public class ScoketConfigActivity extends BaseActivity implements FirstTimeConfigListener, configInterface {

	protected static final String TAG = "SocketConfigActivity";
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
	private Button search;
	private int data_ret = 0;
	private Timer handTimer=new Timer();
	
	private String baute = "115200";//"57600";
	private String dhcp = "动态";
	private String ip = "192.167.1.2";
	private String mask = "255.255.255.0";
	private String gateway = "192.167.1.1";
	private String dns = "192.167.1.1";
	private String pattern="UDP Client";
	private String w_port="32000";
	private String m_ip=GobalDef.SERVER_URL;
	private String m_port = "12188";
	
	private String ap="OPEN";
	private String cmd_ssid="TPLINK";
	private String cmd_passwd="123456";
	private Socket s;
	private int flag = 4;
	private int findflag = 0;
	private int findMax = 180;
	
	private final String devicekey = "devid";
	
	
	private final static int timeout = 1;
	private final static int sus = 2;
	private final static int fail = 3;
	private final static int ok = 4;
	private final static int addDevice = 5;
	//private String gateway = "";
	
	private boolean broadcastrcv = true;
	
	private Thread listenThread = null;
	
	private DatagramSocket ds = null;
	
	private esp8266smartconfig e8266config;
	
	
	private TimerTask showTask = new TimerTask(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			msg.what = 1;
			//showHandler.sendMessage(msg);
		}
		
	};
	private Handler showHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			case timeout:
				progressDialog.dismiss();
				start.setEnabled(true);
				Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_unfind, Toast.LENGTH_SHORT).show();
				stopPacketData();
				break;
			case sus:
				/*
				stopPacketData();
				Log.d(TAG, " sus ");
				recvMsg();
				queryTask qt = new queryTask();
				qt.execute(new Integer[]{0});
				*/
				
				//return;
				break;
			case fail:
				progressDialog.dismiss();
				Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_fail, Toast.LENGTH_SHORT).show();
				start.setEnabled(true);
				stopPacketData();
				break;
			case addDevice:
				String devid = msg.getData().getString(devicekey);
				AddDevice(devid);
				showHandler.sendEmptyMessage(ok);
				break;
			case ok:
				progressDialog.dismiss();
				Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_sus, Toast.LENGTH_SHORT).show();
				start.setEnabled(true);
				stopPacketData();
				break;
				/*
				if(flag == 0){
					if(ns.getName().size() > 0){
						//Toast.makeText(ScoketConfigActivity.this, "发现设备", Toast.LENGTH_SHORT).show();
						findflag = 0;
						progressDialog.setMessage(ScoketConfigActivity.this.getString(R.string.renwu_config_find));
						Log.d(TAG, "list_ns 0 " + list_ns.get(0) + " 1 " + list_ns.get(1));
						
						s = ns.Start(list_ns.get(1));
						Log.d(TAG, "ns start after");
						if(s != null){
							ap = list.get(1);
							cmd_ssid=list.get(0);
							cmd_passwd=  password.getText().toString().trim();
							if(ns.sendPackage(s, baute, dhcp, ip, mask, gateway, dns, ap, cmd_ssid, cmd_passwd, pattern, w_port, m_ip, m_port) > 0){
								List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
								String macstr = list_ns.get(0);
								String[] mac = macstr.split(":");
								long deviceid = 0;
								for(int i=mac.length-1; i>=0; i--){
									int temp = Integer.parseInt(mac[i], 16);
									//long result = (long)((temp & 0xFFFFFFFFFFFFFFFFL) << (7 * 8));
									deviceid |= (long)((temp & 0xFFFFFFFFFFFFFFFFL)<< ((7 - (mac.length -1 - i)) * 8));
									Log.d(TAG, "deviceid is: " + deviceid);
								}
								Log.d(TAG, "deviceid is: " + deviceid);
								
								try {
									DeviceItemDao did = new DeviceItemDao(getHelper());
									DeviceItemModel dim = null;
									devlist.addAll(did.queryForAll());
									for(int i=0; i<devlist.size(); i++){
										DeviceItemModel tempdim = devlist.get(i);
										if(tempdim.getDeviceId() == deviceid){
											dim = tempdim;
											break;
										}
									}
									if(dim == null){
										dim = new DeviceItemModel();
										dim.setDeviceId(deviceid);
										dim.setDeviceName("新增设备");
										dim.setParentId(0);
										devlist.add(dim);
										did.create(dim);
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								progressDialog.dismiss();
								Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_sus, Toast.LENGTH_SHORT).show();
								flag =3;
								return;
							}
							else{
								progressDialog.dismiss();
								Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_fail, Toast.LENGTH_SHORT).show();
								start.setEnabled(true);
							}
						}
						else{
							Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_fail, Toast.LENGTH_SHORT).show();
							start.setEnabled(true);
						}
						flag = 1;
						
					}
					else{
						findflag++;
						if(findflag >= findMax){
							findflag = 0;
							flag = 1;
							progressDialog.dismiss();
							start.setEnabled(true);
							Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_unfind, Toast.LENGTH_SHORT).show();
						}
						
					}
					
				}
				else if(flag == 3){
					ns.reset(s);
					flag = 1;
					start.setEnabled(true);
				}
				*/
			}
		}
		
	};
	
	private void recvMsg(){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					InetAddress wildcardAddr = null;
					MulticastSocket listenSocket = new MulticastSocket(null);
					InetSocketAddress localAddr = new InetSocketAddress(wildcardAddr, 5353);
					listenSocket.setReuseAddress(true);
				    listenSocket.bind(localAddr);
				    listenSocket.setTimeToLive(255);
				    listenSocket.joinGroup(InetAddress.getByName("224.0.0.251"));
				    listenSocket.setBroadcast(true);
					//DatagramSocket ds = new DatagramSocket(5353);
					byte[] buf = new byte[512];
					DatagramPacket dp = new DatagramPacket(buf,buf.length);
					Log.d(TAG, "ds recv packet");
					while(true){
						listenSocket.receive(dp);
						
						Log.d(TAG, "dp length is: " + dp.getLength() + " ip " + dp.getAddress() + " port " + dp.getPort());
						dp.setPort(12188);
						
						String s = "AT+WAUTO=TP-LINK_5D53B,3,7777711111777\n";
						byte[] temp = s.getBytes("UTF-8");
						dp.setLength(temp.length);
						System.arraycopy(temp, 0, buf, 0, temp.length);
						
						listenSocket.send(dp);
						
					}
					
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		t.start();
	}
	
	private void findView(){
		back =(ImageButton)findViewById(R.id.socketnewdetail_back);
		ssid = (EditText)findViewById(R.id.socketdetail_edit_ssid);
		password = (EditText)findViewById(R.id.socketdetail_edit_pass);
		start = (Button)findViewById(R.id.socketdetail_edit_config);
		showpass = (CheckBox)findViewById(R.id.socketdetail_edit_showpass);
		search = (Button)findViewById(R.id.socketdetail_search_config);
	}
	
	private class ConfigAsyncTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String apSsid = params[0];
			String apPassword = params[1];
			e8266config = new esp8266smartconfig(apSsid,  apPassword);
			e8266config.setConfigInterface(ScoketConfigActivity.this);
			e8266config.startConfig();
			return null;
		}
		
	}
	
	private class EsptouchAsyncTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog mProgressDialog;

		//private IEsptouchTask mEsptouchTask;

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String apSsid = params[0];
			String apPassword = params[1];
			//mEsptouchTask = new EsptouchTask(apSsid, apPassword);
			//boolean result = mEsptouchTask.execute();
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result == true){
				//showHandler.sendEmptyMessage(sus);
				
			}
			else{
				//showHandler.sendEmptyMessage(fail);
			}
			
			queryTask qt = new queryTask();
			qt.execute(new Integer[]{0});
		}
	}
	
	private boolean islisten = false;
	private boolean isRcv = false;
	private void _listenInfo(){
		new Thread(){
			public void run(){
				islisten = true;
				isRcv = false;
				DatagramSocket ds = null;
				try {
					ds = new DatagramSocket(10000);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					ds = null;
					e.printStackTrace();
				}
				if(ds != null){
					try {
						ds.setSoTimeout(1000);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte[] recvbuf = new byte[10];
					DatagramPacket dp = new DatagramPacket(recvbuf,recvbuf.length);
					while(islisten == true){
						
						try {
							ds.receive(dp);
							if(dp.getLength() == 10){
								long deviceid = 0;
								Log.d(TAG, "now get deviceid");
								if((recvbuf[0] == 0x00) && (recvbuf[9] == ((byte)0xFF))){
									for(int i=0; i<8; i++){
										int tempid = (int)(recvbuf[i+1] & 0xFF);
										deviceid = deviceid | (long)((tempid & 0xFFFFFFFFFFFFFFFFL)<< (( i) * 8));
									}
									Log.d(TAG, "deviceid is: " + deviceid);
								}
								List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
								try {
									DeviceItemDao did = new DeviceItemDao(getHelper());
									DeviceItemModel dim = null;
									devlist.addAll(did.queryForAll());
									for(int i=0; i<devlist.size(); i++){
										DeviceItemModel tempdim = devlist.get(i);
										if(tempdim.getDeviceId() == deviceid){
											dim = tempdim;
											break;
										}
									}
									if(dim == null){
										dim = new DeviceItemModel();
										dim.setDeviceId(deviceid);
										dim.setDeviceName("新增设备");
										dim.setParentId(0);
										devlist.add(dim);
										did.create(dim);
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								isRcv = true;
								islisten = false;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.d(TAG, "rcv error");
							e.printStackTrace();
						}
					}
				}
				
			}
			
		}.start();
	}
	
	public void SendEspTouchConnect(){
		String apSsid = ssid.getText().toString().trim();;
		String apPassword = password.getText().toString().trim();
		Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid + ", "
				+ " mEdtApPassword = " + apPassword);
		new EsptouchAsyncTask().execute(apSsid, apPassword);
		_listenInfo();
	}
	
	public void SendConfigConnect(){
		String apSsid = ssid.getText().toString().trim();;
		String apPassword = password.getText().toString().trim();
		Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid + ", "
				+ " mEdtApPassword = " + apPassword);
		new ConfigAsyncTask().execute(apSsid, apPassword);
	}
	
	private int QueryAck(){
		try{
			
			byte[] buf = new byte[5];
			byte[] rcvbuf = new byte[128];
			InetAddress address = InetAddress.getByName("255.255.255.255");
			DatagramPacket dp = new DatagramPacket(buf,buf.length,address,GobalDef.LOCAL_PORT);
			buf[0] = 0x01;
			buf[1] = 5;
			buf[2] = 0x11;
			buf[3] = 0x01;
			buf[4] = (byte) 0xFE;
			DatagramPacket rcvpack = new DatagramPacket(rcvbuf, rcvbuf.length);
			DatagramSocket ds = new DatagramSocket();
			//ds.setBroadcast(true);
			int wile_flag = 120;
			while(wile_flag > 0){
				try{
					wile_flag --;
					ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
					ds.send(dp);
					ds.receive(rcvpack);
					long deviceid = 0;
					int i = 0;
					if(rcvpack.getLength() == 16){
						if(rcvbuf[0] == 0x01 && rcvbuf[15] == (byte)0xFE && rcvbuf[1] == 16
								&& rcvbuf[3] == 2 ){
							
							for(i=0; i<8; i++){
								int tempid = (int)(rcvbuf[i+4] & 0xFF);
								deviceid = deviceid | (long)((tempid & 0xFFFFFFFFFFFFFFFFL)<< (( i) * 8));
							}
							Log.d(TAG, "deviceid is: " + deviceid);
							buf[3] = 3;
							ds.send(dp);
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
									dim.setDeviceName("新增设备");
									dim.setParentId(0);
									devlist.add(dim);
									did.create(dim);
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							return 1;
						}
						
					}
				}
				catch(Exception e){
					e.printStackTrace();
					
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	private void GetBroadCastAck(){
		
		try {
			if(ds == null){
				ds = new DatagramSocket(GobalDef.BROADCAST_PORT);
			}
			byte[] buf = new byte[128];
			DatagramPacket dp = new DatagramPacket(buf,buf.length);
			while(broadcastrcv){
				try{
					//ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
					ds.receive(dp);
					long deviceid = 0;
					
					if(dp.getLength() == 16){
						broadcastrcv = false;
						List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
						for(int i=0; i<8; i++){
							int tempid = (int)(buf[i] & 0xFF);
							deviceid = deviceid | (long)((tempid & 0xFFFFFFFFFFFFFFFFL)<< (( i) * 8));
							Log.d(TAG, "recvbuf " + i + " " + tempid + " deviceid " + deviceid );
						}
						Log.d(TAG, "deviceid is: " + deviceid);
						
						try {
							DeviceItemDao did = new DeviceItemDao(getHelper());
							DeviceItemModel dim = null;
							devlist.addAll(did.queryForAll());
							for(int i=0; i<devlist.size(); i++){
								DeviceItemModel tempdim = devlist.get(i);
								if(tempdim.getDeviceId() == deviceid){
									dim = tempdim;
									break;
								}
							}
							if(dim == null){
								dim = new DeviceItemModel();
								dim.setDeviceId(deviceid);
								dim.setDeviceName("新增设备");
								dim.setParentId(0);
								devlist.add(dim);
								did.create(dim);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			ds.close();
			ds = null;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try{
				if(ds != null){
					ds.close();
					ds = null;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	class queryTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			
			return QueryAck();
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if(result == 1){
				showHandler.sendEmptyMessage(ok);
			}
			else{
				showHandler.sendEmptyMessage(fail);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
		
	}
	
	public void AddDevice(String devicestrid){
		
		long deviceid = Long.parseLong(devicestrid, 16);
		List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
		try {
			DeviceItemDao did = new DeviceItemDao(getHelper());
			DeviceItemModel dim = null;
			devlist.addAll(did.queryForAll());
			for(int i=0; i<devlist.size(); i++){
				DeviceItemModel tempdim = devlist.get(i);
				if(tempdim.getDeviceId() == deviceid){
					dim = tempdim;
					break;
				}
			}
			if(dim == null){
				dim = new DeviceItemModel();
				dim.setDeviceId(deviceid);
				dim.setDeviceName("新增设备");
				dim.setParentId(0);
				devlist.add(dim);
				did.create(dim);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	class SearchTask extends AsyncTask<Integer, Integer, String>{

		private ProgressDialog progressDialog;
		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			DatagramSocket ds;
			InetAddress address;
			byte[] recvbuf = new byte[128];
			byte[] buf = new byte[5];
			for(int i=0; i <buf.length; i++){
				buf[i] = (byte) i;
			}
			try{
				DatagramPacket recvpacket = new DatagramPacket(recvbuf,recvbuf.length);
				ds = new DatagramSocket(4288);
				ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
				while(true){
					try{
						ds.receive(recvpacket);
						Log.d(TAG, "recvpacket length is: " + recvpacket.getLength());
						long deviceid = 0;
						
						if(recvpacket.getLength() == 16){
							List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
							for(int i=0; i<8; i++){
								int tempid = (int)(recvbuf[i] & 0xFF);
								deviceid = deviceid | (long)((tempid & 0xFFFFFFFFFFFFFFFFL)<< (( i) * 8));
								Log.d(TAG, "recvbuf " + i + " " + tempid + " deviceid " + deviceid );
							}
							Log.d(TAG, "deviceid is: " + deviceid);
							
							try {
								DeviceItemDao did = new DeviceItemDao(getHelper());
								DeviceItemModel dim = null;
								devlist.addAll(did.queryForAll());
								for(int i=0; i<devlist.size(); i++){
									DeviceItemModel tempdim = devlist.get(i);
									if(tempdim.getDeviceId() == deviceid){
										dim = tempdim;
										break;
									}
								}
								if(dim == null){
									dim = new DeviceItemModel();
									dim.setDeviceId(deviceid);
									dim.setDeviceName("新增设备");
									dim.setParentId(0);
									devlist.add(dim);
									did.create(dim);
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return "";
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
						
				}
					
			}catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			try {
				address = InetAddress.getByName("255.255.255.255");
				DatagramPacket dp = new DatagramPacket(buf, buf.length, address, 80);
				DatagramPacket recvpacket = new DatagramPacket(recvbuf,recvbuf.length);
				ds = new DatagramSocket(4288);
				
				Log.d(TAG, " ds port is: %d\r\n" + ds.getLocalPort());
				ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
				while(true){
					ds.send(dp);
					try{
						ds.receive(recvpacket);
						Log.d(TAG, "recvpacket length is: " + recvpacket.getLength());
						long deviceid = 0;
						
						if(recvpacket.getLength() == 16){
							List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
							for(int i=0; i<8; i++){
								int tempid = (int)(recvbuf[i] & 0xFF);
								deviceid = deviceid | (long)((tempid & 0xFFFFFFFFFFFFFFFFL)<< (( i) * 8));
								Log.d(TAG, "recvbuf " + i + " " + tempid + " deviceid " + deviceid );
							}
							Log.d(TAG, "deviceid is: " + deviceid);
							
							try {
								DeviceItemDao did = new DeviceItemDao(getHelper());
								DeviceItemModel dim = null;
								devlist.addAll(did.queryForAll());
								for(int i=0; i<devlist.size(); i++){
									DeviceItemModel tempdim = devlist.get(i);
									if(tempdim.getDeviceId() == deviceid){
										dim = tempdim;
										break;
									}
								}
								if(dim == null){
									dim = new DeviceItemModel();
									dim.setDeviceId(deviceid);
									dim.setDeviceName("新增设备");
									dim.setParentId(0);
									devlist.add(dim);
									did.create(dim);
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return "";
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//progressDialog.dismiss();
			showHandler.sendEmptyMessage(ok);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//progressDialog = new ProgressDialog(ScoketConfigActivity.this);
			//progressDialog.setCanceledOnTouchOutside(false);
			//progressDialog.show();
			
			
		}
		
		
		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handTimer.cancel();
		if(listenThread != null){
			try {
				listenThread.join(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listenThread = null;
		}
	}





	private void initView(){
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(start.isEnabled() == true){
					ScoketConfigActivity.this.finish();
				}
				else{
					Toast.makeText(ScoketConfigActivity.this, R.string.renwu_config_back, Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		
		
		showpass.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(!showpass.isChecked()){
					password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				else{
					password.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}
			
		});
		
		search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchTask st = new SearchTask();
				st.execute(new Integer[0]);
			}
			
		});
		
		
		start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!Utility.CheckNetwork(ScoketConfigActivity.this)){
					Toast.makeText(ScoketConfigActivity.this, R.string.unNetwork, Toast.LENGTH_LONG).show();
					return;
				}
				/*
				listenThread = new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						GetBroadCastAck();
					}
					
				});
				*/
				//queryTask qt = new queryTask();
				//qt.execute(new Integer[]{0});
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setMessage(ScoketConfigActivity.this.getString(R.string.renwu_config_finding));
				progressDialog.show();
				isCalled = false;
				try {
					//sendPacketData();
					//SendEspTouchConnect();
					//ConfigAsyncTask();
					SendConfigConnect();
					//e8266config = new esp8266smartconfig(ssid.getText().toString().trim(),  password.getText().toString().trim());
					//e8266config.setConfigInterface(ScoketConfigActivity.this);
					//e8266config.startConfig();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//listenThread.start();
				/*
				ns = new w_ns();
				if(data_ret == 1){
					start.setEnabled(false);
					String passwd = password.getText().toString().trim();
					list = config.SendMeg(context, passwd);
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.setMessage(ScoketConfigActivity.this.getString(R.string.renwu_config_finding));
					progressDialog.show();
					list_ns = ns.ns_wlt(context);
					flag = 0;
					
				}
				*/
			}
			
		});
	}
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.socketnewdetail);
		findView();
		initView();
		progressDialog = new ProgressDialog(this);
		context=this.getApplicationContext();
		String ssid_data=null;
		ssid_data=config.initData(context);
		config.initData(context);
		
		WifiManager mWifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		DhcpInfo di = mWifiManager.getDhcpInfo();
		if(di != null){
			int gatwayVal = di.gateway;
			gateway =  (String.format("%d.%d.%d.%d", (gatwayVal & 0xff),(gatwayVal >> 8 & 0xff),(gatwayVal >> 16 & 0xff),	(gatwayVal >> 24 & 0xff))).toString();
		}
		/*
		String macstr = "00:00:04:00:61:BC";
		String[] mac = macstr.split(":");
		long deviceid = 0;
		for(int i=mac.length-1; i>=0; i--){
			int temp = Integer.parseInt(mac[i], 16);
			//long result = (long)((temp & 0xFFFFFFFFFFFFFFFFL) << (7 * 8));
			deviceid |= (long)((temp & 0xFFFFFFFFFFFFFFFFL)<< ((7 - (mac.length -1 - i)) * 8));
			Log.d(TAG, "deviceid is: " + deviceid);
		}
		Log.d(TAG, "deviceid is: " + deviceid);
		*/
		if(ssid_data != null){
			ssid.setText(ssid_data);
			data_ret = 1;
		}
		else{
			Toast toast;
			toast=Toast.makeText(ScoketConfigActivity.this, R.string.unNetwork, Toast.LENGTH_LONG);
			//toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		handTimer.schedule(showTask, 1000, 1000);
		
	}
	
	private FirstTimeConfig firstConfig = null;
	
	public boolean isCalled = false;
	
	private void stopPacketData() 
	{
		if(isCalled)
		{		
			try 
			{
				isCalled = false;
	
				//mSendDataPackets.setText(getResources().getString(R.string.start_label));
	
				//mSendDataPackets.setBackgroundResource(R.drawable.selection);
				//if (mconfigProgress != null) 
				//{
				//	mconfigProgress.setVisibility(ProgressBar.INVISIBLE);
				//}
				firstConfig.stopTransmitting();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void sendPacketData() throws Exception
	{
		if (!isCalled)
		{
			isCalled = true;
			//mSendDataPackets.setText(getResources().getString(R.string.stop_label));
			try 
			{
				firstConfig = getFirstTimeConfigInstance(ScoketConfigActivity.this);
			} catch (Exception e) 
			{
				
			}

			Log.d(TAG, "transmitSetings");
			firstConfig.transmitSettings();
			//mSendDataPackets.setBackgroundResource(R.drawable.selection_focus_btn);
			//mconfigProgress.setVisibility(ProgressBar.VISIBLE);
		} 
		else 
		{
			if (firstConfig != null) 
			{
				stopPacketData();
			}
		}
	}


	private FirstTimeConfig getFirstTimeConfigInstance(FirstTimeConfigListener apiListener) throws Exception 
	{
		String ssidFieldTxt = ssid.getText().toString().trim();//mSSIDInputField.getText().toString().trim();
		String passwdText = password.getText().toString().trim();//mPasswordInputField.getText().toString().trim();
		String deviceInput = "CC3000";//mDeviceNameInputField.getText().toString().trim();
		

		byte[] totalBytes = null;
		
		return new FirstTimeConfig(apiListener, passwdText, totalBytes,	gateway,
				ssidFieldTxt,deviceInput);
	}


	@Override
	public void onFirstTimeConfigEvent(FtcEvent fe, Exception e) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onFirstTimeConfigEvent");
		try{
			e.printStackTrace();
		}
		catch(Exception ev){
			
		}
		
		switch(fe){
		case FTC_ERROR:
			showHandler.sendEmptyMessage(fail);
			break;
		case FTC_SUCCESS:
			showHandler.sendEmptyMessage(sus);
			break;
		case FTC_TIMEOUT:
			showHandler.sendEmptyMessage(timeout);
			break;
		}
	}

	@Override
	public void isStartConfig() {
		// TODO Auto-generated method stub
		Log.d(TAG, "isStartConfig");
	}

	@Override
	public void isStopConfig() {
		// TODO Auto-generated method stub
		Log.d(TAG, "isStopConfig");
	}

	@Override
	public void completeConfig(String parameter) {
		// TODO Auto-generated method stub
		Log.d(TAG, "iscompleteConfig");
		Message msg = Message.obtain();
		msg.what = addDevice;
		Bundle bund = new Bundle();
		bund.putString(devicekey, parameter);
		msg.setData(bund);
		showHandler.sendMessage(msg);
	}

	@Override
	public void configtimeout() {
		// TODO Auto-generated method stub
		showHandler.sendEmptyMessage(timeout);
	}

	
}
