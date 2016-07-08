package com.lishate.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

import com.lishate.data.GobalDef;
import com.lishate.data.model.ServerItemModel;
import com.lishate.encryption.Encryption;
import com.lishate.message.GetServerConfigReqMessage;
import com.lishate.message.SetConfigReqMessage;
import com.lishate.message.baseMessage;


public class UdpProcess {
	
	public final static int SEND_SUCESS = 0;
	public final static int SEND_FAIL = 1;
	public final static int RECV_SUCESS = 2;
	public final static int RECV_FAIL = 3;
	public final static int DATA_UDP_FAIL = 4;
	private static final String TAG = "UdpProcess";

	public static int Send(DatagramSocket ds, String destip, int destport, byte[] buf){
		int result = SEND_SUCESS;
		InetAddress address ;
		try {
			address = InetAddress.getByName(destip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = SEND_FAIL;
			return result;
		}
		DatagramPacket dp = new DatagramPacket(buf, buf.length, address, destport);
		try {
			Log.d(TAG, "send buf length is: " + buf.length + " address " + address.toString() + " Port is: " + destport);
			ds.send(dp);
			result = SEND_SUCESS;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = SEND_FAIL;
			e.printStackTrace();
		}
		return result;
	}
	
	public static DatagramPacket Recv(DatagramSocket ds){
		
		byte[] buf = new byte[128];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			ds.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			packet = null;
		}
		return packet;
		
	}
	
	public static int SendMsg(DatagramSocket ds, baseMessage msg, ServerItemModel si){
		byte[] buf = Encryption.GetMsg2Buf(msg);// Encryption.Encryption(msg);
		return UdpProcess.Send(ds, si.getIpaddress(), si.getPort(), buf);
	}
	
	public static baseMessage RecvMsg(DatagramSocket ds){
		DatagramPacket packet = Recv(ds);
		baseMessage bm = null;
		if(packet != null){
			/*
			if(Encryption.checkIsMsg(packet.getData()) == 1){
				bm = Encryption.Decryption(packet.getData());
			}
			*/
			bm = Encryption.GetMsg(packet.getData());
		}
		return bm;
	}
	
	public static baseMessage GetMsgReturn(baseMessage sendMsg, ServerItemModel si, int timeout, int repeatflag){
		DatagramSocket ds ;//= new DatagramSocket();
		int flag = 12;
		if(timeout == 0){
			timeout = GobalDef.Instance.getUdpReadTimeOut();
		}
		if(repeatflag != 0) {
			flag = repeatflag;
		}
		try {
			ds = new DatagramSocket();
			ds.setSoTimeout(timeout);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		baseMessage bm = null;
		while(flag > 0){
			if(SendMsg(ds, sendMsg,si) == SEND_SUCESS){
				try {
					ds.setSoTimeout(timeout);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bm = RecvMsg(ds);
				while(true){
					if(bm != null){
						if(bm.Seq != sendMsg.Seq){
							try {
								ds.setSoTimeout(timeout);
							} catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bm = RecvMsg(ds);
						}else{
							break;
						}
					}else{
						break;
					}
				}
				flag--;
				Log.d(TAG, "flag is: " + flag);
				if(bm != null){
					Log.d(TAG, "bm is not null");
					break;
				}
				
		}
			/*
			while((bm != null) && (bm.getSeq() != sendMsg.getSeq())){
				try {
					ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				bm = RecvMsg(ds);
			}
			*/
			//return bm;
		}
		return bm;
	}
	public static baseMessage GetMsgReturn(baseMessage scrm,
			ServerItemModel sim) {
		// TODO Auto-generated method stub
		
		return GetMsgReturn(scrm, sim, 0, 0);
	}
	
	
}
