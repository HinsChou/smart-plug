package com.lishate.message;

import java.io.Serializable;

import com.lishate.data.GobalDef;
import com.lishate.utility.Utility;

public class ConfigInfo implements Serializable {

	public boolean isenable = false;
	public boolean startenable = false;
	public boolean endenable = false;
	public byte startHour = 0;
	public byte endHour = 0;
	public byte startMin = 0;
	public byte endMin = 0;
	public byte week = 0;
	
	public ConfigInfo(){
		
	}
	
	public void CopyTo(ConfigInfo c){
		c.isenable = isenable;
		c.startenable = startenable;
		c.endenable = endenable;
		c.startHour = startHour;
		c.endHour = endHour;
		c.startMin = startMin;
		c.endMin = endMin;
		c.week = week;
	}
	
	
	
	public String GetStringFromInfo(){
		byte[] buf = new byte[5];
		writeByte(buf,0);
		try {
			return Utility.getHexString(buf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GobalDef.TIME_TASK_EMPTY;
	}
	
	public void SetStringToInfo(String hex){
		byte[] buf = Utility.getByteArray(hex);
		readByte(buf, 0);
	}
	
	public void writeByte(byte[] buf, int index){
		buf[index] = 0;
		if(isenable){
			buf[index] = Utility.setByteIndex(buf[index], 0);
			//buf[index] |= (1 << 7);
		}
		else{
			buf[index] = Utility.clearByteIndex(buf[index], 0);
		}
		//buf[index] = Utility.setByteIndex(buf[index], 1);
		buf[index] |= week;
		
		buf[index + 1] = (byte)startHour;
		if(startenable){
			//buf[index + 1] |= 1 << 5;
			buf[index + 1] = Utility.setByteIndex(buf[index + 1], 0);
		}
		else{
			buf[index + 1] = Utility.clearByteIndex(buf[index + 1], 0);
		}
		//buf[index + 1] = Utility.setByteIndex(buf[index + 1], 1);
		buf[index + 2] = (byte)startMin;
		buf[index + 3] = (byte)endHour;
		if(endenable){
			buf[index + 3] = Utility.setByteIndex(buf[index + 3], 0);
			//buf[index + 3] |= 1 << 5;
		}
		else{
			buf[index + 3] = Utility.clearByteIndex(buf[index + 3], 0);
		}
		//buf[index + 3] = Utility.setByteIndex(buf[index + 3], 1);
		buf[index + 4] = (byte)endMin;
	}
	
	public void readByte(byte[] buf, int index){
		isenable = Utility.getByteIndex(buf[index], 0);
		/*
		if(((buf[index] >> 7) & 0x000000FF) == 0){
			isenable = false;
		}
		else{
			isenable = true;
		}
		*/
		week = (byte) (buf[index] & 0x7F);
		startenable = Utility.getByteIndex(buf[index + 1], 0);
		/*
		if((buf[index + 1] & 0x20) == 1){
			startenable = true;
		}
		else{
			startenable = false;
		}
		*/
		startHour = (byte) (buf[index + 1] & 0x1F);
		startMin = (byte)(buf[index + 2] & 0x3F);
		endenable = Utility.getByteIndex(buf[index + 3], 0);
		/*
		if((buf[index + 3] & 0x20) == 1 ){
			endenable = true;
		}
		else{
			endenable = false;
		}
		*/
		endHour = (byte) (buf[index + 3] &  0x1F);
		endMin = (byte)(buf[index + 4] & 0x3F);
	}
}
