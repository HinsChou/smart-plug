package com.lishate.activity.renwu;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.activity.FirstActivity;
import com.lishate.data.GobalDef;
import com.lishate.data.SharedPreferencesUtility;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.net.BroadCast;
import com.lishate.utility.Utility;

public class MoreListActivity extends FirstActivity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		finish();
		return super.onKeyDown(keyCode, event);
	}
	public static final String TAG = "MoreListActivity";
	private TextView configText;
	private TextView appdownload;
	private TextView inputData;
	private TextView outData;
	private TextView language;
	private TextView help;
	private TextView key;
	private TextView about;
	private ImageView vibrat;
	
	private RelativeLayout rela_download;
	private RelativeLayout rela_config;
	private RelativeLayout rela_language;
	private RelativeLayout rela_intdata;
	private RelativeLayout rela_outdata;
	private RelativeLayout rela_help;
	
	private boolean isContinue = true;
	
	private Button appdownload_yes;
	
	private Dialog appDownloadDialog;
	
	private List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
	
	
	private void findView(){
		configText = (TextView)findViewById(R.id.morehelp_addnew);
		appdownload = (TextView)findViewById(R.id.morehelp_appdownload);
		language = (TextView)findViewById(R.id.morehelp_language);
		vibrat = (ImageView)findViewById(R.id.morehelp_vibrat);
		
		rela_download = (RelativeLayout)findViewById(R.id.morehlep_rela_download);
		rela_config = (RelativeLayout)findViewById(R.id.morehlep_rela_addnew);
		rela_language = (RelativeLayout)findViewById(R.id.morehlep_rela_language);
		rela_intdata = (RelativeLayout)findViewById(R.id.morehlep_rela_inputdata);
		rela_outdata = (RelativeLayout)findViewById(R.id.morehlep_rela_outdata);
		rela_help = (RelativeLayout)findViewById(R.id.morehlep_rela_help);
		
		appDownloadDialog = new Dialog(this);
		appDownloadDialog.setTitle(getString(R.string.renwu_morehelp_appdownload));
		appDownloadDialog.setContentView(R.layout.appdialog);
		appdownload_yes = (Button)appDownloadDialog.findViewById(R.id.appdialog_yes);
		
		if(SharedPreferencesUtility.getVibrate() == true){
			vibrat.setImageResource(R.drawable.on_check);
		}
		else{
			vibrat.setImageResource(R.drawable.offline_off);
		}
		
	}
	
	private void initView(){
		/*
		configText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MoreListActivity.this, ScoketConfigActivity.class);
				MoreListActivity.this.startActivity(intent);
			}
			
		});
		*/
		
		/*
		appdownload.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appDownloadDialog.show();
			}
			
		});
		*/
		
		vibrat.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "vibrat is onclick");
				if(SharedPreferencesUtility.getVibrate() == true){
					SharedPreferencesUtility.putVibrate(false);
					vibrat.setImageResource(R.drawable.offline_off);
				}
				else{
					SharedPreferencesUtility.putVibrate(true);
					vibrat.setImageResource(R.drawable.on_check);
				}
			}
			
		});
		
		rela_help.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MoreListActivity.this, SocketNewConfigActivity.class);
				MoreListActivity.this.startActivity(intent);
			}
			
		});
		
		rela_outdata.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BroadCastTask bct = new BroadCastTask();
				bct.execute(new Integer[]{0});
			}
			
		});
		
		rela_intdata.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BroadRecvTask brt = new BroadRecvTask();
				brt.execute(new Integer[]{0});
			}
			
		});
		
		rela_config.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MoreListActivity.this, SocketNewConfigActivity.class);
				MoreListActivity.this.startActivity(intent);
			}
			
		});
		
		rela_download.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appDownloadDialog.show();
			}
			
		});
		
		appdownload_yes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appDownloadDialog.dismiss();
			}
			
		});
		
		rela_language.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//*
				//LayoutInflater inflater = (LayoutInflater)MoreListActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//View layout = inflater.inflate(R.layout.exit_dialog, null);
				
				final Dialog dialog = new Dialog(MoreListActivity.this, R.style.exitdialog);
				dialog.setContentView(R.layout.languagedialog);
				
				Button yes = (Button)dialog.findViewById(R.id.languagedialog_yes);
				Button no = (Button)dialog.findViewById(R.id.languagedialog_no);
				RadioButton ch = (RadioButton)dialog.findViewById(R.id.languagedialog_chinese);
				RadioButton en = (RadioButton)dialog.findViewById(R.id.languagedialog_english);
				switch(SharedPreferencesUtility.getLanguage()){
				case 0:
					ch.setChecked(true);
					break;
				case 1:
					en.setChecked(true);
					break;
				}
				
				yes.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Resources resources =getResources();
						Configuration config = resources.getConfiguration();
						DisplayMetrics dm = resources.getDisplayMetrics();// 閼惧嘲绶辩仦蹇撶閸欏倹鏆熼敍姘瘜鐟曚焦妲搁崚鍡氶哺閻滃浄绱濋崓蹇曠缁涘锟�
						
						
						RadioButton temps = (RadioButton)dialog.findViewById(R.id.languagedialog_chinese);
						if(temps.isChecked() == true){
							config.locale = Locale.CHINESE;
							SharedPreferencesUtility.putLanguage(0);
						}
						temps = (RadioButton)dialog.findViewById(R.id.languagedialog_english);
						if(temps.isChecked() == true){
							config.locale = Locale.ENGLISH;
							SharedPreferencesUtility.putLanguage(1);
						}
						resources.updateConfiguration(config, dm);
						Toast.makeText(MoreListActivity.this, 
								MoreListActivity.this.getString(R.string.renwu_config_languagechange), 
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
					
				});
				
				no.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				
				ch.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						RadioButton rb = (RadioButton)dialog.findViewById(R.id.languagedialog_english);
						rb.setChecked(false);
					}
					
				});
				
				en.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						RadioButton chs = (RadioButton)dialog.findViewById(R.id.languagedialog_chinese);
						chs.setChecked(false);
					}
					
				});
				dialog.show();
				
			}
			
		});
	}
	
	
	
	public void UpdateListInfo(List<DeviceItemModel> devices){
		List<DeviceItemModel> result = new ArrayList<DeviceItemModel>();
		DeviceItemDao did = null;
		try {
			
			did = new DeviceItemDao(getHelper());
			result.clear();
			result.addAll(did.queryForAll());
			//*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<devices.size(); i++){
			DeviceItemModel dim = devices.get(i);
			boolean flag = false;
			for(int n = 0; n < result.size(); n++){
				DeviceItemModel tempdim = result.get(n);
				if(tempdim.getDeviceId() == dim.getParentId()){
					tempdim.setDeviceName(dim.getDeviceName());
					try {
						did.update(tempdim);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					flag = true;
				}
			}
			if(flag == false){
				result.add(dim);
				try {
					did.createOrUpdate(dim);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	class BroadRecvTask extends AsyncTask<Integer, Integer, Integer>{

		//public boolean IsContinue = true;
		public Dialog dialog;
		
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			byte[] buf = new byte[1024];
			List<DeviceItemModel> result = new ArrayList<DeviceItemModel>();
			result.clear();
			while(MoreListActivity.this.isContinue == true){
				DatagramSocket socket = null; 
				try{
			        socket = new DatagramSocket(GobalDef.BROADCAST_PORT);  
			        DatagramPacket pack = new DatagramPacket(buf,buf.length);
			        socket.setSoTimeout(1000);
			        socket.receive(pack);
			        byte[] tempbuf = new byte[pack.getLength()];
			        System.arraycopy(buf, 0, tempbuf, 0, pack.getLength());
			        String s = new String(tempbuf,GobalDef.STR_CODE);
			        while(s.length() > 0){
			        	try{
				        	DeviceItemModel dim = new DeviceItemModel();
				        	s = Utility.setDeviceInfo(dim, s);
				        	result.add(dim);
			        	}
			        	catch(Exception e){
			        		if(e.getMessage() != null){
			        			Log.d(TAG, e.getMessage());
			        		}
			        		e.printStackTrace();
			        	}
			        }
			        Log.d(TAG, "length is: " + pack.getLength());
			        if(result.size() > 0){
			        	break;
			        }
	        	}
	        	catch(Exception e){
	        		
	        	}
	        	finally{
	        		if(socket != null){
	        			socket.close();
	        		}
	        	}
			}
			if(result.size() > 0){
				UpdateListInfo(result);
				return 1;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result == 0){
				Toast.makeText(MoreListActivity.this, MoreListActivity.this.getString(R.string.renwu_more_recv_fail), Toast.LENGTH_SHORT).show();
			}
			else if(result == 1){
				Toast.makeText(MoreListActivity.this, MoreListActivity.this.getString(R.string.renwu_more_recv_sus), Toast.LENGTH_SHORT).show();
			}
			if(dialog != null){
				dialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			MoreListActivity.this.isContinue = true;
			dialog = new Dialog(MoreListActivity.this, R.style.exitdialog);
			dialog.setContentView(R.layout.alertdialog);
			dialog.show();
			
			Button yes = (Button)dialog.findViewById(R.id.alertdialog_yes);
			yes.setText(R.string.no);
			//Button no = (Button)dialog.findViewById(R.id.exitdialog_no);
			TextView text = (TextView)dialog.findViewById(R.id.alertdialog_text);
			text.setText(R.string.renwu_more_indata);
			yes.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isContinue = false;
				}
				
			});
			
			
		}
		
		
	}
	
	class BroadCastTask extends AsyncTask<Integer, Integer, Integer>{

		//public boolean IsContinue = true;
		public Dialog dialog;
		public BroadCast bc = new BroadCast();
		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			List<DeviceItemModel> result = new ArrayList<DeviceItemModel>();
			try {
				
				DeviceItemDao did = new DeviceItemDao(getHelper());
				result.clear();
				result.addAll(did.queryForAll());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			String tempresult = "";
			for(int i=0; i<result.size(); i++){
				DeviceItemModel dim = result.get(i);
				tempresult = tempresult + Utility.getDeviceInfo(dim);
			}
			
			byte[] buf = null;
			try {
				buf = tempresult.getBytes(GobalDef.STR_CODE);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/*
			String s = null;
			try {
				s = new String(buf,GobalDef.STR_CODE);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        while(s.length() > 0){
	        	DeviceItemModel dim = new DeviceItemModel();
	        	s = Utility.setDeviceInfo(dim, s);
	        	result.add(dim);
	        }
			//*/
			
			while(isContinue == true){
				Log.d(TAG, "IsContinue is: " + isContinue);
				bc.SendDevice(buf,1000);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog != null){
				dialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isContinue = true;
			dialog = new Dialog(MoreListActivity.this, R.style.exitdialog);
			dialog.setContentView(R.layout.alertdialog);
			dialog.show();
			
			Button yes = (Button)dialog.findViewById(R.id.alertdialog_yes);
			//Button no = (Button)dialog.findViewById(R.id.exitdialog_no);
			yes.setText(R.string.no);
			TextView text = (TextView)dialog.findViewById(R.id.alertdialog_text);
			text.setText(R.string.renwu_more_outdata);
			yes.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isContinue = false;
				}
				
			});
			
			
		}
		
		
	}
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.morehelp);
		findView();
		initView();
		
	}

	
}
