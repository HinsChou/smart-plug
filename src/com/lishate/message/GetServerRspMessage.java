package com.lishate.message;

public class GetServerRspMessage extends baseRspMessage {
	
	public GetServerRspMessage(){
		super();
		//setMsgType(MessageDef.MESSAGE_TYPE_GETSERVER_RSP);
		MsgType = MessageDef.MESSAGE_TYPE_GETSERVER_RSP;
	}
	
	//private int _count = 0;
	public int getCount(){
		if(_serverinfo != null){
			return _serverinfo.length / 6;
		}
		return 0;
	}
	/*
	public void setCount(int v){
		_count = v;
	}
	*/
	
	private byte[] _serverinfo = null;
	public byte[] getServerInfo(){
		return _serverinfo;
	}
	public void setServerInfo(byte[] serverinfo){
		_serverinfo = serverinfo;
	}
}
