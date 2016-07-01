package com.lishate.activity.renwu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.activity.ControlSetAlertActivity;
import com.lishate.activity.ControlSetEditActivity;
import com.lishate.adapter.SocketTaskAdapter;
import com.lishate.application.switchApplication;
import com.lishate.data.GobalDef;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.ServerItemModel;
import com.lishate.message.ConfigInfo;
import com.lishate.message.GetServerConfigReqMessage;
import com.lishate.message.MessageDef;
import com.lishate.message.MessageSeqFactory;
import com.lishate.message.SetConfigReqMessage;
import com.lishate.message.baseMessage;
import com.lishate.net.UdpProcess;
import com.lishate.utility.Utility;

public class ScoketTaskDaysActivity extends BaseActivity implements OnClickListener{

	private ImageButton back;
	private TextView title;
	private ImageButton save;
	
	private CheckBox openCheck;
	private CheckBox startCheck;
	private CheckBox endCheck;
	private TextView startTime;
	private TextView endTime;
	private ListView cryList;
	private SocketTaskAdapter adapter;
	private RelativeLayout relaopen;
	private RelativeLayout relaclose;
	private int timeCount = 0;
	private DeviceItemModel dim;
	private List<ConfigInfo> configinfos ;
	private ConfigInfo ci = null;
	private TextView editname;
	
	private CheckBox sun;
	private CheckBox mon;
	private CheckBox tue;
	private CheckBox wen;
	private CheckBox thu;
	private CheckBox fri;
	private CheckBox sat;
	
	private TextView tsun;
	private TextView tmon;
	private TextView ttue;
	private TextView twen;
	private TextView tthu;
	private TextView tfri;
	private TextView tsat;
	
	private int UnSelectedColor = Color.BLACK;
	private int SelectedColor = Color.BLUE;
	private byte week_days;
	private String[] val = new String[]{"每周日","每周一","每周二","每周三","每周四","每周五","每周六"};
	
	private TextView cycle_days;
	private TextView repeat_days;
	private ListView taskdayslist;
	private boolean[] dayschecked;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent mintent = new Intent();
			mintent.putExtra(GobalDef.INTENT_TIME_WEEK, dayschecked);
			setResult(Activity.RESULT_OK, mintent);
			finish();
			
		}
		return super.onKeyDown(keyCode, event);
	}

	private void findView(){
		
		back = (ImageButton)findViewById(R.id.sockettaskdays_back);
	
		save = (ImageButton)findViewById(R.id.sockettaskdays_save);
	
		cryList = (ListView) findViewById(R.id.socketdetaildays_task);
		dayschecked = new boolean[7];

	}
	
	public void setConfigInfo(){

		String sresult = (String) endTime.getText();
		String[] times = sresult.split(":");
		ci.endHour = (byte) Integer.parseInt(times[0]);
		ci.endMin = (byte) Integer.parseInt(times[1]);
		sresult = (String)startTime.getText();
		times = sresult.split(":");
		ci.startHour = (byte)Integer.parseInt(times[0]);
		ci.startMin = (byte)Integer.parseInt(times[1]);
		
	}
	
	private void initView(){
		if(configinfos.size()>0)
		{
			ConfigInfo ci = new ConfigInfo();
			ci = configinfos.get(0);
			dayschecked = new boolean[7];
			for(int i=0; i<7; i++){
				if(Utility.getByteIndex(week_days, i + 1)){
					
					dayschecked[i] = true;
				}else
					dayschecked[i] = false;
			}
		}
		
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//把当前选择的日期传给上一级
				Intent mintent = new Intent();
				mintent.putExtra(GobalDef.INTENT_TIME_WEEK, dayschecked);
				setResult(Activity.RESULT_OK, mintent);
				finish();
			}
			
		});
		
		save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScoketTaskDaysActivity.this.setConfigInfo();
				/*
				if(timeCount == 0){
					configinfos.add(ci);
				}
				else{
					configinfos.set(timeCount - 1, ci);
				}
				*/
				SaveConfigInfoTask sct = new SaveConfigInfoTask();
				sct.execute(new Void[0]);
			}
			
		});
		

	}
	
	
	private void InitItem(DeviceItemModel dim){
		
	}
	
	class SaveConfigInfoTask extends  AsyncTask<Void,Integer, Integer>{
		
		private static final String TAG = "SocketTaskEditActivity";
		private ProgressDialog processDialog;
		private int fail = 0;
		private ConfigInfo tempci;

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			processDialog.dismiss();
			if(result == 0){
				Toast.makeText(ScoketTaskDaysActivity.this, ScoketTaskDaysActivity.this.getResources().getString(R.string.renwu_socket_offline), Toast.LENGTH_SHORT).show();
				if(timeCount == 0){
					configinfos.remove(ci);
				}
				else{
					if(tempci != null){
						configinfos.set(timeCount - 1, tempci);
					}
				}
				return;
			}
			
			switchApplication app = (switchApplication)getApplication();
			app.SetDeviceItemModel(dim);
			dim.setTimeinfo(Utility.getConfigStringInfo(configinfos));
			ScoketTaskDaysActivity.this.finish();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(timeCount > 0){
				tempci = configinfos.get(timeCount - 1);
				configinfos.set(timeCount - 1, ci);
			}
			else if(timeCount == 0){
				configinfos.add(ci);
			}
			fail = 0;
			processDialog = new ProgressDialog(ScoketTaskDaysActivity.this);
			processDialog.setMessage(ScoketTaskDaysActivity.this.getString(R.string.loging));
			processDialog.setCanceledOnTouchOutside(false);
			processDialog.show();
			Log.d(TAG, "OpenTask preexecute");
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			SetConfigReqMessage scrm = new SetConfigReqMessage();
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			scrm.Direct = MessageDef.BASE_MSG_FT_REQ;
			scrm.setFromId(GobalDef.MOBILEID);
			scrm.MsgType = MessageDef.MESSAGE_TYPE_SET_CONFIG_REQ;
			scrm.Seq = MessageSeqFactory.GetNextSeq();
			scrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			scrm.ToType = MessageDef.BASE_MSG_FT_HUB;
			scrm.setToId(dim.getDeviceId());
			scrm.configinfos = (ArrayList<ConfigInfo>) configinfos;
			scrm.Array2Content();
			byte[] buf = scrm.getIndexBuf();
			if(timeCount == 0){
				buf[0] = (byte) configinfos.size();
			}
			else{
				buf[0] = (byte) (timeCount - 1);
				
			}
			ci.writeByte(buf, 1);
			scrm.setIndexBuf(buf);
			//UdpProcess.Send(ds, destip, destport, buf)
			baseMessage bm = UdpProcess.GetMsgReturn(scrm, sim);
			if(bm != null){
				return 1;
			}
			else{
				GetServerConfigReqMessage lrm = new GetServerConfigReqMessage();
				lrm.Direct = MessageDef.BASE_MSG_FT_REQ;
				lrm.setFromId(GobalDef.MOBILEID);
				lrm.setToId(dim.getDeviceId());
				
				lrm.MsgType = MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ;
				lrm.Seq = MessageSeqFactory.GetNextSeq();
				lrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
				lrm.ToType = MessageDef.BASE_MSG_FT_HUB;
				
				
				bm = UdpProcess.GetMsgReturn(lrm, sim);
				if(bm != null){
					return 1;
				}
			}
			return 0;
		}
		
	}
	
	private String getTimeText(int hour, int min){
		String result = "";
		if(hour < 10){
			result += "0";
		}
		
		result = result + hour + ":";
		if(min < 10){
			result = result + "0";
		}
		result = result + min;
		return result;
		
	}
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.socketdayslist);
		week_days = getIntent().getByteExtra(GobalDef.INTENT_TIME_WEEK,(byte) 0);
		switchApplication app = (switchApplication)getApplication();
		dim = app.getItemModel();
		configinfos = Utility.getConfigInfo(dim.getTimeinfo());
		findView();
		initView();
		adapter = new SocketTaskAdapter(this, val, dayschecked);
		cryList.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 1){
			String hour = data.getExtras().getString(GobalDef.INTENT_TIME_HOUR);
			String min = data.getExtras().getString(GobalDef.INTENT_TIME_MIN);
			
			if(hour.length() == 1){
				hour = "0" + hour;
			}
			if(min.length() == 1){
				min = "0" + min;
			}
			
			String result = hour + ":" + min;
			
			if(requestCode == 0){
				startTime.setText(result);
			}
			else if(requestCode == 1){
				endTime.setText(result);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void UpdateSelWeeks(int pos, boolean mchecked) {
		// TODO Auto-generated method stub
		dayschecked[pos] = mchecked;
		adapter.notifyDataSetChanged();
	}

	
}
