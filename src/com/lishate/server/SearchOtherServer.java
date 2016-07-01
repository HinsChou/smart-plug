package com.lishate.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SearchOtherServer extends Service {

	public static final int SearchPort = 5008;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private DatagramSocket serverSocket;
	
	public void BroadCastDeviceData(){
		DatagramSocket socket;  
        DatagramPacket packet;  
        byte[] data={1,2,3,4};  
        
        while(true){
        	try{
		        socket = new DatagramSocket();  
		        socket.setBroadcast(true); 
		        packet = new DatagramPacket(data,data.length,InetAddress.getByName("255.255.255.255"),8300);  
		        
		        while(true){
		        	socket.send(packet);  
		        	Thread.sleep(1000);
		        }
        	}
        	catch(Exception e){
        		
        	}
        }
	}
	

}
