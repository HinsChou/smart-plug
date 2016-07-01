package com.lishate.server;

import com.lishate.message.baseMessage;
import com.lishate.net.MessageRecvInterface;
import com.lishate.net.MessageSendInterface;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SwitchServer extends Service implements MessageRecvInterface, MessageSendInterface
{
	
	private static final String TAG = "switchserver";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "theadid " + Thread.currentThread().getId());
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeSendMsg(baseMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSendMsg(baseMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failSendMsg(baseMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvMsg(baseMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
