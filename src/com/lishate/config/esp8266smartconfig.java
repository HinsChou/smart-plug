package com.lishate.config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.util.Log;

import com.lishate.config.ScoketConfigActivity;
import com.iot.espressif.esptouch.j;
public class esp8266smartconfig extends baseConfig {
	
	public final String TAG = "SMARTCONFIG";
	
	public j _mEsptouchTask;
	
	public String ssid = "";
	public String password = "";
	
	public boolean islisten = false;
	public boolean isRcv = false;
	
	public int timeout;
	
	public esp8266smartconfig(String ssid, String pw){
		this.ssid = ssid;
		password = pw;
		timeout = 120000;
		_mEsptouchTask = new j(ssid, pw);
	}
	
	public boolean startConfig(){
		_listenInfo();
		configInterface ci = this.getConfigInterface();
		if(ci != null){
			ci.isStartConfig();
		}
		_mEsptouchTask.b();
		
		
		
		return true;
	}
	
	public boolean stopConfig(){
		_mEsptouchTask.a();
		islisten = false;
		configInterface ci = this.getConfigInterface();
		if(ci != null){
			ci.isStopConfig();
		}
		return false;
	}
	
	private void _listenInfo(){
		new Thread(){
			public void run(){
				DatagramSocket ds = null;
				try{
					islisten = true;
					isRcv = false;
					
					long starttime = System.currentTimeMillis();
					try {
						ds = new DatagramSocket(10024);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						ds = null;
						e.printStackTrace();
					}
					if(ds != null){
						try {
							ds.setSoTimeout(200);
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						byte[] recvbuf = new byte[10];
						DatagramPacket dp = new DatagramPacket(recvbuf,recvbuf.length);
						while(islisten == true){
							if((System.currentTimeMillis() - starttime) > timeout){
								islisten = false;
								configInterface ci = esp8266smartconfig.this.getConfigInterface();
								ci.configtimeout();
								break;
							}
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
									String result = Long.toHexString(deviceid);
									configInterface ci = esp8266smartconfig.this.getConfigInterface();
									if(ci != null){
										ci.completeConfig(result);
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
					
					configInterface ci = esp8266smartconfig.this.getConfigInterface();
					if(ci != null){
						ci.isStopConfig();
					}
				}
				catch(Throwable e){
					Log.d(TAG, "error:" + e.getMessage());
				}
				finally{
					if(ds != null){
						ds.close();
						ds = null;
					}
					
				}
			}
			
			
		}.start();
	}

}
