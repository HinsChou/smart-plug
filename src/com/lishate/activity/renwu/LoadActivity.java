package com.lishate.activity.renwu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.activity.renwu.MainMenuActivity;
import com.lishate.data.GobalDef;
import com.lishate.data.SharedPreferencesUtility;
import com.lishate.data.dao.ServerItemDao;
import com.lishate.data.model.ServerItemModel;
import com.lishate.update.VersionItem;
import com.lishate.utility.Utility;

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
		Resources resources =getResources();
		Configuration config = resources.getConfiguration();
		DisplayMetrics dm = resources.getDisplayMetrics();
//		int i = SharedPreferencesUtility.getLanguage();
//		
		String langugue =config.locale.getLanguage();
		int i = langugue.endsWith("zh")?0:1;
		Log.e(TAG, "当前的预言是" + (i==0?"中文":"英文"));
		switch(i){
		case 0:
			config.locale = Locale.CHINESE;
			break;
		case 1:
			config.locale = Locale.ENGLISH;
			break;
		}
		resources.updateConfiguration(config, dm);
		new Thread(new CheckVersionTask()).start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void StartActivity(){
		Intent intent = new Intent();
		intent.setClass(LoadActivity.this, SocketListActivity.class);
		startActivity(intent);
		finish();
	}
	
	private class CheckVersionTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
//			Message msg = Message.obtain();
//			if(Utility.CheckNetwork(LoadActivity.this) == false){
//				msg.what = MsgNoNet;
//				LoadActivity.this.taskHandler.sendMessage(msg);
//			}
//			else{
//				if(com.lishate.update.Utility.CheckServerVersionUpdate()){
//					VersionItem vi = com.lishate.update.Utility.GetVersionItem();
//					if(vi == null){
//						msg.what = MsgNoVersionID;
//						LoadActivity.this.taskHandler.sendMessage(msg);
//					}
//					else{
//						getVI = vi;
//						msg.what = MsgUpdateVersionID;
//						LoadActivity.this.taskHandler.sendMessage(msg);
//					}
//				}
//				else{
//					msg.what = MsgNoVersionID;
//					LoadActivity.this.taskHandler.sendMessage(msg);
//				}
//			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = Message.obtain();
			msg.what = MsgNoVersionID;
			LoadActivity.this.taskHandler.sendMessage(msg);
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
								//tmpdialog.dismiss();
								StartActivity();
							}
							
						}).create();
				tmpdialog.show();
				break;
			case MsgUpdateVersionID:
				Dialog dialog = new Dialog(LoadActivity.this, R.style.exitdialog);
				dialog.setContentView(R.layout.exit_dialog);
				dialog.show();
				
				Button yes = (Button)dialog.findViewById(R.id.exitdialog_yes);
				Button no = (Button)dialog.findViewById(R.id.exitdialog_no);
				TextView text = (TextView)dialog.findViewById(R.id.exitdialog_text);
				text.setText(R.string.update_new);
				yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						DownloadTask dt = new DownloadTask();
						dt.execute(new Void[0]);
					}
				});
				no.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//dialog.dismiss();
						StartActivity();
					}
					
				});
				/*
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
				*/
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
