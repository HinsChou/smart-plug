package com.lishate.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lishate.R;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.ServerItemDao;
import com.lishate.data.model.ServerItemModel;
import com.lishate.encryption.Encryption;
import com.lishate.message.LoginReqMessage;
import com.lishate.message.LoginRspMessage;
import com.lishate.message.MessageDef;
import com.lishate.message.MessageSeqFactory;
import com.lishate.message.messageTest;
import com.lishate.server.SwitchServer;
import com.lishate.update.VersionItem;
import com.lishate.utility.Utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LoadActivity extends BaseActivity {
	
	private static final String TAG = "LoadActivity";
	private final static int MsgNoVersionID = 1;
	private final static int MsgUpdateVersionID = 2;
	private final static int MsgDownloadFail = 3;
	private final static int MsgNoNet = 4;
	
	private VersionItem getVI = null;
	
	private void LoadList(){
		List<ServerItemModel> list = new ArrayList();
		try {
			ServerItemDao sid = new ServerItemDao(getHelper());
			list.clear();
			list.addAll(sid.queryForAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.load);
		Utility.Init(this);
		//Encryption.Test();
		//messageTest mt = new messageTest();
		//Encryption.MsgTest(mt);
		//LoadList();
		/*
		int hid = 0x80000002;
		int lid = 0x90000000;
		long iOutcome = 0;  
        byte bLoop;  
        byte[] bRefArr = new byte[]{-3,-3,-3,-3,-3,-3,-3,-3};
        for ( int i =0; i<8 ; i++) {  
            bLoop = bRefArr[i];  
            iOutcome+= (bLoop & 0xFFL) << (8 * i);  
            
        }    
		byte b = 0;
		long loid = 0x7000000280000002L;
		long tloid = loid;
		long ttloid = 0;
		tloid = tloid >> 32 & 0xFFFFFFFFL;
		hid = (int)tloid;
		lid = (int)loid;
		tloid = 0;
		tloid += (lid & 0xFFFFFFFFL) << 0;
		tloid += (hid & 0xFFFFFFFFL) << 32;
		tloid = hid;
		
		tloid = tloid << 32;
		ttloid = hid;
		tloid = ttloid + tloid;
		ttloid = 0x8F000000;
		tloid = 0x7F00000000000000L;
		tloid = tloid | ttloid;
		b = (byte)(loid >> 0);
		b = (byte)(loid >> 8);
		loid = loid << 2;
		loid = loid << 32;
		
		loid = loid | lid;
		
		tloid = loid & 0xFFFFFFFF00000000L;
		hid = (int)tloid;
		lid = (int)(long)(loid & 0xFFFFFFFF);
		//*/
		new Thread(new CheckVersionTask()).start();
		/*
		Log.d(TAG, "theadid " + Thread.currentThread().getId());
		Log.d(TAG, "inti value is: " + Encryption.init());
		
		
		messageTest mt = new messageTest();
		Encryption.MsgTest(mt);
		LoginReqMessage lreqm = new LoginReqMessage();
		//lreqm.setDirect(MessageDef.DIRECT_REQ);
		lreqm.setSeq(MessageSeqFactory.GetNextSeq());
		lreqm.setFromId(189273434);
		lreqm.setToId(452255);
		lreqm.setFromType(MessageDef.BASE_MSG_FT_MOBILE);
		lreqm.setToType(MessageDef.BASE_MSG_FT_END);
		lreqm.setMsgType(MessageDef.MESSAGE_TYPE_LOGIN_REQ);
		byte[] reqbuf = Encryption.GetMsg2Buf(lreqm);
		LoginReqMessage lreqm2 = new LoginReqMessage();
		lreqm2 = (LoginReqMessage)Encryption.GetMsg(reqbuf);
		
		LoginRspMessage lrspm = new LoginRspMessage();
		lrspm.setSeq(MessageSeqFactory.GetNextSeq());
		lrspm.setFromId(2424234);
		lrspm.setToId(35656);
		lrspm.setFromType(MessageDef.BASE_MSG_FT_MOBILE);
		lrspm.setToType(MessageDef.BASE_MSG_FT_END);
		//lrspm.setMsgType(MessageDef.MESSAGE_TYPE_LOGIN_REQ);
		lrspm.setRspStatue(MessageDef.RSP_FAIL);
		byte[] rspbuf = Encryption.GetMsg2Buf(lrspm);
		LoginRspMessage lrspm2 = new LoginRspMessage();
		lrspm2 = (LoginRspMessage)Encryption.GetMsg(rspbuf);
		int i = 0;
		i++;
		if(i < 0){
			i = 0;
		}
		*/
		//StartActivity();
		//*/
		/*
		byte[] buf = new byte[128];
		buf[0] = 0x00;
		buf[1] = -2;
		Log.d(TAG, "checkIsMsg value: " + Encryption.checkIsMsg(buf));
		LoginRspMessage lrm = new LoginRspMessage();
		Log.d(TAG, "lrm seq is: " + lrm.getSeq());
		Log.d(TAG, "encript " + Encryption.DecryptionLoginRsp(buf, lrm));
		Log.d(TAG, "lrm seq is: " + lrm.getSeq());
		LoginReqMessage lreqm = new LoginReqMessage();
		lreqm.setDirect(MessageDef.DIRECT_REQ);
		lreqm.setSeq(1);
		lreqm.setFromId(189273434);
		lreqm.setToId(452255);
		lreqm.setFromType(MessageDef.BASE_MSG_FT_MOBILE);
		lreqm.setToType(MessageDef.BASE_MSG_FT_END);
		lreqm.setMsgType(MessageDef.MESSAGE_TYPE_LOGIN_REQ);
		
		byte[] reqbuf = Encryption.EncriptionLoginReq(lreqm);
		int length = reqbuf.length;
		Log.d(TAG, "length is: " + length);
		lreqm = new LoginReqMessage();
		int result = Encryption.checkIsMsg(reqbuf);
		Log.d(TAG, "result is: " + result);
		*/
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void StartActivity(){
		Intent intent = new Intent();
		intent.setClass(LoadActivity.this, MainMenuActivity.class);
		startActivity(intent);
		finish();
	}
	
	private class CheckVersionTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			if(Utility.CheckNetwork(LoadActivity.this) == false){
				msg.what = MsgNoNet;
				LoadActivity.this.taskHandler.sendMessage(msg);
			}
			else{
				if(com.lishate.update.Utility.CheckServerVersionUpdate()){
					VersionItem vi = com.lishate.update.Utility.GetVersionItem();
					if(vi == null){
						msg.what = MsgNoVersionID;
						LoadActivity.this.taskHandler.sendMessage(msg);
					}
					else{
						getVI = vi;
						msg.what = MsgUpdateVersionID;
						LoadActivity.this.taskHandler.sendMessage(msg);
					}
				}
				else{
					msg.what = MsgNoVersionID;
					LoadActivity.this.taskHandler.sendMessage(msg);
				}
			}
		}
		
	}
	
	
	
	private Handler taskHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case MsgNoVersionID:
				StartActivity();
				break;
			case MsgNoNet:
				StartActivity();
				break;
			case MsgDownloadFail:
				AlertDialog tmpdialog = new AlertDialog.Builder(LoadActivity.this).setTitle(R.string.app_name)
						.setMessage(R.string.updatefail)
						.setCancelable(false)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								StartActivity();
							}
							
						}).create();
				tmpdialog.show();
				break;
			case MsgUpdateVersionID:
				AlertDialog dialog = new AlertDialog.Builder(LoadActivity.this).setTitle(R.string.app_name)
						.setMessage(R.string.update_new)
						.setCancelable(false)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								DownloadTask dt = new DownloadTask();
								dt.execute(new Void[0]);
							}
							
						})
						.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								StartActivity();
							}
							
						}).create();
				dialog.show();
				break;
			}
		}
		
	};
	
	class DownloadTask extends AsyncTask<Void, Integer, File>{

		ProgressDialog progressDialog;
		
		@Override
		protected File doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			File f = null;
			try{
				VersionItem vi = getVI;
				String url = vi.getUrl();//"http://115.28.45.50/update/2/switch.apk";
				//int length = Utility.GetHttpLength(url);
				HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				int length = conn.getContentLength();
				progressDialog.setMax(length);
				f = new File(GobalDef.Instance.getUpdatePath(), "update.apk");
				FileOutputStream os = new FileOutputStream(f);
				try{
					
					byte[] buf = new byte[1024];
					int i = 0;
					while(true){
						int j = is.read(buf);
						if(j <= 0){
							if(i >= length){
								return f;
							}
							else{
								return null;
							}
							
						}
						os.write(buf,0,j);
						i+=j;
						Integer[] temps = new Integer[1];
						temps[0] = Integer.valueOf(i);
						publishProgress(temps);
					}
				}
				catch(Throwable t){
					t.printStackTrace();
				}
				finally{
					is.close();
					os.close();
					
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(File result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result != null){
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
				//installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				LoadActivity.this.startActivity(installIntent);
				//LoadActivity.this.finish();
				//System.exit(0);
			}
			else{
				Message msg = Message.obtain();
				msg.what = MsgDownloadFail;
				LoadActivity.this.taskHandler.sendMessage(msg);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(LoadActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(LoadActivity.this.getString(R.string.apkdownload));
			progressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			progressDialog.setProgress(values[0].intValue());
			super.onProgressUpdate(values);
		}
		
		
		
	}
	
}
