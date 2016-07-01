package com.lishate.message;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class baseMessage implements Parcelable {

	/*
	private int _length = 0;
	public int getLength(){
		return _length;
	}
	public void setLength(int length)
	{
		_length = length;
	}
	*/
	private final String TAG = "basemsg";
	
	public int Seq = 0;
	public int FromType = MessageDef.BASE_MSG_FT_MOBILE;
	public int ToType = MessageDef.BASE_MSG_FT_SERVER;
	public int Direct = MessageDef.BASE_MSG_FT_REQ;
	public int FromHID = 0;
	public int FromLID = 0;
	public int ToHID = 0;
	public int ToLID = 0;
	public int MsgType = 0;
	
	/*
	private int _seq = 0;
	public int getSeq(){
		return _seq;
	}
	public void setSeq(int seq){
		_seq = seq;
	}
	
	private int _fromtype = 0;
	public int getFromType(){
		return _fromtype;
	}
	public void setFromType(int ft){
		_fromtype = ft;
	}
	
	private int _totype = 0;
	public int getToType(){
		return _totype;
	}
	public void setToType(int totype){
		_totype = totype;
	}
	
	private int _direct = 0;
	public int getDirect(){
		return _direct;
	}
	public void setDirect(int direct){
		_direct = direct;
	}
	*/
	//private long _fromid = 0;
	//public long MMFromID = 1234567890;//9204514244744040552L;
	public long getFromId(){
		//Log.d(TAG, "bm get form id " + _fromid);
		long fromid = 0;
		fromid += (FromLID & 0xFFFFFFFFL) << 0;
		fromid += (FromHID & 0xFFFFFFFFL) << 32;
		return fromid;
	}
	public void setFromId(long fromid){
		//Log.d(TAG, "bm set form id " + fromid);
		//_fromid = fromid;
		long tfromid  = fromid >> 32 & 0xFFFFFFFFL;
		FromHID = (int)tfromid;
		FromLID = (int)fromid;
		//FromHID = (int)(fromid >> 32) ;
	}
	
	//private long _toid = 0;
	public long getToId(){
		long toid = 0;
		toid += (ToLID & 0xFFFFFFFFL) << 0;
		toid += (ToHID & 0xFFFFFFFFL) << 32;
		return toid;
	}
	public void setToId(long toid){
		long ttoid  = toid >> 32 & 0xFFFFFFFFL;
		ToHID = (int)ttoid;
		ToLID = (int)toid;
	}
	
	/*
	private int _msgType = 0;
	public int getMsgType(){
		return _msgType;
	}
	public void setMsgType(int msgtype){
		//Log.d(TAG, "bm set msgtype " + msgtype);
		_msgType = msgtype;
	}
	*/
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		/*
		dest.writeInt(_seq);
		dest.writeInt(_fromtype);
		dest.writeInt(_totype);
		dest.writeInt(_direct);
		dest.writeLong(_toid);
		dest.writeLong(_fromid);
		dest.writeInt(_msgType);
		*/
	}
	
	
	/*
	private int _command = 0;
	public int getCommand(){
		return _command;
	}
	public void setCommand(int command){
		_command = command;
	}
	
	private byte[] content;
	public byte[] getContent(){
		return content;
	}
	public void setContent(byte[] buf){
		content = buf;
	}
	*/
}
