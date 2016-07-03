package com.lishate.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.lishate.data.GobalDef;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.encryption.Encryption;
import com.lishate.message.ConfigInfo;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utility {

	private static final String TAG = "Utility";

	public static String CheckVersion(){
		String result = null;
		
		return result;
	}
	
	public static short GetShortFromBuf(byte[] buf, int index){
		short result = 0;
		result = (short)(buf[index] & 0x000000FF);
		result += ((buf[index + 1] & 0x00FF) << 8);
		return result;
	}
	
	public static int GetIpPortFromBuf(byte[] buf, int index){
		int port = 0;
		port = buf[index] & 0x000000FF;
		port += (buf[index + 1] & 0x000000FF) << 8;
		return port;
	}
	
	public static String GetIpFromBuf(byte[] buf, int index){
		String result = "";
		int temp = buf[0] & 0x000000FF;
		result = result + String.valueOf(temp) + ".";
		temp = buf[1] & 0xFF;
		result = result + String.valueOf(temp) + ".";
		temp = buf[2] & 0xFF;
		result = result + String.valueOf(temp) + ".";
		temp = buf[3] & 0xFF;
		result = result + String.valueOf(temp);
		return result;
	}
	
	public static String GetMacFormID(long id){
		int temp = 0;
		String result = "";
		String stemp = "";
		for(int i = 0; i<6; i++){
			temp = (int) ((id >> ((5 - i) * 8)) & 0x000000FF);
			stemp = Integer.toHexString(temp);
			if(stemp.length() < 2){
				stemp = "0" + stemp;
			}
			
			result = result + stemp ;
			if(i != 5){
				result = result + ":";
			}
		}
		
		return result;
	}
	
	 public static byte[] longToByte(long number) { 
	    long temp = number; 
	    byte[] b = new byte[8]; 
	    for (int i = 0; i < b.length; i++) { 
	        b[i] = new Long(temp & 0xff).byteValue();
	        temp = temp >> 8; // 向右移8位 
	    } 
	    return b; 
	 } 
	 
	 
	 
	 public static long byteToLong(byte[] b) { 
	        long s = 0; 
	        for(int i=0; i<b.length; i++)
	        {
	        	b[i] &= 0xff; 
	        	s |= ( long)(b[i]&0xff) << (8*(b.length-1 - i));
	        }
//	        long s0 = b[0] & 0xff;// 最低位 
//	        long s1 = b[1] & 0xff; 
//	        long s2 = b[2] & 0xff; 
//	        long s3 = b[3] & 0xff; 
//	        long s4 = b[4] & 0xff;// 最低位 
//	        long s5 = b[5] & 0xff; 
//	        long s6 = b[6] & 0xff; 
//	        long s7 = b[7] & 0xff; 
	 
	        // s0不变 
	      
//	        s1 <<= 8; 
//	        s2 <<= 16; 
//	        s3 <<= 24; 
//	        s4 <<= 8 * 4; 
//	        s5 <<= 8 * 5; 
//	        s6 <<= 8 * 6; 
//	        s7 <<= 8 * 7; 
//	        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7; 
	        return s; 
	    } 
	 
	 public static long StringToLong(String s){
		 byte[] buf = hexStringToBytes(s);
		 return byteToLong(buf);
	 }
	 
	 public static String LongToString(long l){
		 byte[] buf = longToByte(l);
		 return bytesToHexString(buf);
	 }
	 
	 public static String bytesToHexString(byte[] src){  
		    StringBuilder stringBuilder = new StringBuilder("");  
		    if (src == null || src.length <= 0) {  
		        return null;  
		    }  
		    for (int i = 0; i < src.length; i++) {  
		        int v = src[i] & 0xFF;  
		        String hv = Integer.toHexString(v);  
		        if (hv.length() < 2) {  
		            stringBuilder.append(0);  
		        }  
		        stringBuilder.append(hv);  
		    }  
		    return stringBuilder.toString();  
		}  
	 
	 public static byte[] hexStringToBytes(String hexString) {  
		    if (hexString == null || hexString.equals("")) {  
		        return null;  
		    }  
		    if(hexString.length()%2 ==1){
		    	hexString = "0" +hexString;
		    }
		    hexString = hexString.toUpperCase();  
		    int length = hexString.length() / 2;  
		    char[] hexChars = hexString.toCharArray();  
		    byte[] d = new byte[length];  
		    for (int i = 0; i < length; i++) {  
		        int pos = i * 2;  
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		    }  
		    return d;  
		}  
	 
	 private static byte charToByte(char c) {  
		    return (byte) "0123456789ABCDEF".indexOf(c);  
		}  
	
	public static Drawable gravy(Drawable dr){
		dr.mutate();
		ColorMatrix cm = new ColorMatrix();  
        cm.setSaturation(0);       
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);       
        dr.setColorFilter(cf);  
		return dr;
	}
	
	
	
	/*
	public static int GetLowIntFromLong(long l){
		int temp = 0;
		if(l >= 0){
			
		}
	}
	*/
	public static boolean CheckNetwork(Context context){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mInfo = mConnectivityManager.getActiveNetworkInfo();
		if(mInfo != null){
//			Log.d(TAG, "CheckNetwork is not null");
			return mInfo.isConnected();
		}
		Log.d(TAG, "CheckNetwork is null");
		return false;
	}
	
	public static boolean IsWifiConnected(Context context){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(mInfo != null){
			return mInfo.isConnected();
		}
		return false;
	}
	
	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i=0; i < b.length; i++) {
		    result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		 }
		return result;
	}
	
	public static byte[] getByteArray(String hexString) {
		String temp = "00";
		byte[] buf = new byte[5];
		for(int i=0; i<5; i++){
			temp = hexString.substring(i * 2, i * 2 + 2);
			buf[i] = (byte) Integer.parseInt(temp, 16);
		}
		return buf;
	}
	
	public static String GetTimeString(int h, int m){
		String temp = String.valueOf(h);
		String result = "";
		if(temp.length() < 2){
			temp = "0"+temp;
		}
		result += temp;
		temp = String.valueOf(m);
		if(temp.length() < 2){
			temp = "0" + temp;
		}
		result += temp;
		return result;
	}
	
	public static String getDeviceInfo(DeviceItemModel dim){
		String str = LongToString(dim.getDeviceId());
		StringInt si = new StringInt();
		si.setContent(str);
		String result = si.getContentString();
		si.setContent(dim.getDeviceName());
		result = result + si.getContentString();
		return result;
	}
	
	public static String setDeviceInfo(DeviceItemModel dim, String p){
		StringInt si = new StringInt();
		int len = si.parseContent(p);
		dim.setDeviceId(StringToLong(si.getContent()));
		p = p.substring(len);
		len = si.parseContent(p);
		dim.setDeviceName(si.getContent());
		p = p.substring(len);
		return p;
	}
	
	public static boolean Init(Context context){
		Encryption.init();
		
		return true;
	}
	/*
	public static int GetHttpLength(String url){
		int result = -1;
		HttpURLConnection conn = null;
		try{
			conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setRequestMethod("HEAD");
			conn.setConnectTimeout(5000);
			result = conn.getContentLength();
		}
		catch(Throwable te){
			System.out.println(te.getMessage());
			te.printStackTrace();
		}
		finally{
			if(conn != null){
				conn.disconnect();
			}
		}
		return result;
	}
	*/
	public static boolean HttpDownload(String url, String path){
		try{
			//int length = GetHttpLength(url);
			HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setConnectTimeout(5000);
			int length = conn.getContentLength();
			InputStream  is = conn.getInputStream();
			
			File f = new File(path);
			byte[] buf = new byte[1024];
			FileOutputStream os = new FileOutputStream(f);
			try{
				int i = 0;
				while(true){
					int j = is.read(buf);
					if(j < 0){
						if(i >= length){
							return true;
						}
						else{
							return false;
						}
					}
					else{
						i = i+j;
						os.write(buf,0,j);
					}
				}
			}
			catch(Throwable te){
				
			}
			finally{
				is.close();
				os.close();
				
			}
		}
		catch(Throwable te){
			System.out.println(te.getMessage());
			te.printStackTrace();
		}
		return false;
	}
	
	public static List<ConfigInfo> getConfigInfo(String s){
		ArrayList<ConfigInfo> infos = new ArrayList<ConfigInfo>();
		try{
			String result = s;
			String info = "";
			infos.clear();
			while(result.length() > 9){
				info = result.substring(0, 10);
				result = result.substring(10);
				ConfigInfo ci = new ConfigInfo();
				if(!info.equals(GobalDef.TIME_TASK_EMPTY)){
					ci.SetStringToInfo(info);
					infos.add(ci);
				}
			}
		}
		catch(Exception e){
			if(e.getMessage() != null){
			Log.d(TAG, e.getMessage());
			}
			e.printStackTrace();
		}
		return infos;
	}
	
	public static String getTimeString(int hour, int min){
		String result = "";
		if(hour < 10){
			result = result + "0";
		}
		result = result + hour + ":";
		if(min < 10){
			result = result + "0";
		}
		result = result + min;
		return result;
	}
	
	public static String getConfigStringInfo(List<ConfigInfo> s){
		String result = "";
		for(int i=0; i<7; i++){
			if(i < s.size()){
				ConfigInfo ci = s.get(i);
				result = result + ci.GetStringFromInfo();
			}
			else{
				result = result + GobalDef.TIME_TASK_EMPTY;
			}
		}
		return result;
	}
	
	public static byte setByteIndex(byte src, int index){
		int i = 1;
		i = i << (7 - index);
		src = (byte) (src | i);
		return src;
	}
	
	public static byte clearByteIndex(byte src, int index){
		int i = 1;
		i = i << (7 - index);
		i = ~i;
		src = (byte)(src & i);
		return src;
	}
	
	public static boolean getByteIndex(byte src, int index){
		int i=1; 
		i = i<< (7 - index);
		src = (byte) (src & i); 
		if(src != 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
