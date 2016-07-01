package com.lishate.activity.renwu;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class ScoketTaskEditActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "ScoketTaskEditActivity";
	private ImageButton back;
	private TextView title;
	private ImageButton save;
	

	private TextView startTime;
	private TextView endTime;
	
	private int timeCount = 0;
	private DeviceItemModel dim;
	private List<ConfigInfo> configinfos ;
	private ConfigInfo ci = null;

	

	
	private int UnSelectedColor = Color.BLACK;
	private int SelectedColor = Color.BLUE;
	
	private String[] val = new String[]{"周一","周一","周一","周一","周一","周一","周一"};
	
	private TextView cycle_days;
	private TextView repeat_days;
	
	private ConfigInfo oldtimerinfo;
	public boolean[] dayschecked;
	private boolean[] days_week;
	private Byte week_temp;
	private RelativeLayout start_time_layout;
	private RelativeLayout end_time_layout;
	
	private void findView(){
		
		back = (ImageButton)findViewById(R.id.socketnewtask_back);
	
		save = (ImageButton)findViewById(R.id.socketnewtask_save);
	
		startTime = (TextView)findViewById(R.id.socketnewtask_starttime);
		startTime.setOnClickListener(this);
		endTime = (TextView)findViewById(R.id.socketnewtask_endtime);
		endTime.setOnClickListener(this);
		
		start_time_layout = (RelativeLayout)findViewById(R.id.renwu_socket_task_rela_opentime); 
		end_time_layout = (RelativeLayout)findViewById(R.id.renwu_socket_task_rela_closetime);
		start_time_layout.setOnClickListener(this);
		end_time_layout.setOnClickListener(this);
		
		cycle_days = (TextView)findViewById(R.id.sel_days);
		cycle_days.setOnClickListener(this);
		repeat_days = (TextView)findViewById(R.id.repeat_days);
		repeat_days.setOnClickListener(this);
	}
	
	public void setConfigInfo(){
//		if(null !=oldtimerinfo)	
//		{
//			
				ci = oldtimerinfo;
			if(oldtimerinfo.isenable)
				ci.isenable = true;
			else
				ci.isenable = false;
//		}
		
		
			ci.startenable = true;
			ci.endenable = true;
		
		if(days_week[0] == true){
			ci.week = Utility.setByteIndex(ci.week, 1);
			
		}
		else{
			ci.week = Utility.clearByteIndex(ci.week, 1);
		}
		if(days_week[1] == true){
			ci.week = Utility.setByteIndex(ci.week, 7);
		}
		else{
			ci.week = Utility.clearByteIndex(ci.week, 7);
		}
		if(days_week[2] == true){
			ci.week = Utility.setByteIndex(ci.week, 6);
		}
		else{
			ci.week = Utility.clearByteIndex(ci.week, 6);
		}
		if(days_week[3] == true){
			ci.week = Utility.setByteIndex(ci.week, 5);
		}
		else{
			ci.week = Utility.clearByteIndex(ci.week, 5);
		}
		if(days_week[4] == true){
			ci.week = Utility.setByteIndex(ci.week, 4);
		}
		else{
			ci.week = Utility.clearByteIndex(ci.week, 4);
		}
		if(days_week[5] == true){
			ci.week = Utility.setByteIndex(ci.week, 3);
		}
		else{
			ci.week = Utility.clearByteIndex(ci.week, 3);
		}
		if(days_week[6] == true){
			ci.week = Utility.setByteIndex(ci.week, 2);
		}else{
			ci.week = Utility.clearByteIndex(ci.week, 2);
		}
		
		String sresult = (String) endTime.getText();
		String[] times = sresult.split(":");
		ci.endHour = (byte) Integer.parseInt(times[0]);
		ci.endMin = (byte) Integer.parseInt(times[1]);
		sresult = (String)startTime.getText();
		times = sresult.split(":");
		ci.startHour = (byte)Integer.parseInt(times[0]);
		ci.startMin = (byte)Integer.parseInt(times[1]);
		
	}

	

	protected boolean equalCurrenttimeWithLastTimer() {
		// TODO Auto-generated method stub
		ConfigInfo cii = new ConfigInfo();
		String sresult = (String) endTime.getText();
		String[] times = sresult.split(":");
		cii.endHour = (byte) Integer.parseInt(times[0]);
		cii.endMin = (byte) Integer.parseInt(times[1]);
		sresult = (String)startTime.getText();
		times = sresult.split(":");
		cii.startHour = (byte)Integer.parseInt(times[0]);
		cii.startMin = (byte)Integer.parseInt(times[1]);
		Log.i(TAG, "老数据：开始=" +  oldtimerinfo.startHour + ":" + oldtimerinfo.startMin + "\n" 
				+ "结束=" +  oldtimerinfo.endHour + ":" + oldtimerinfo.endMin + "\n"
				+ "最新数据：开始" + cii.startHour + ":" + cii.startMin + "\n"
					+ "结束" + cii.endHour + ":" + cii.endMin + "\n");
		return(oldtimerinfo.endHour != cii.endHour 
				|| oldtimerinfo.endMin != cii.endMin 
				|| oldtimerinfo.startHour != cii.startHour 
				|| oldtimerinfo.startMin != cii.startMin );
		
	}

	private void initView(){
		if(timeCount == 0){
			//editname.setText(R.string.renwu_config_newtask);
		}
		else{
		//	editname.setText(R.string.renwu_config_modifytask);
		}
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean ismodify =false;
				Log.i(TAG, "days_week=" +  days_week);
				//Log.i(TAG, "days_week'length=" + days_week.length);
				//if(null != days_week)
					ismodify = !Arrays.equals(dayschecked, days_week);
				//else
					//days_week = Arrays.copyOf(dayschecked, dayschecked.length);
					ismodify |= equalCurrenttimeWithLastTimer();
				Log.i(TAG, "ismodify=" +  ismodify);	
				if(true == ismodify )
				{
					//提示是否要保存修改的数据
					Toast.makeText(getApplicationContext(), getString(R.string.saving_data), Toast.LENGTH_SHORT).show();
					
					setConfigInfo();
					SaveConfigInfoTask sct = new SaveConfigInfoTask();
					sct.execute(new Void[0]);
				}
				else
					finish();
			}


			
		});
		
		save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScoketTaskEditActivity.this.setConfigInfo();
				/*
				if(timeCount == 0){
					configinfos.add(ci);
				}
				else{
					configinfos.set(timeCount - 1, ci);
				}
				*/
//				SaveConfigInfoTask sct = new SaveConfigInfoTask();
//				sct.execute(new Void[0]);
			}
			
		});
		

		
		//ci = new ConfigInfo();
		
		dayschecked = new boolean[7];
		days_week = new boolean[7];
		if(configinfos.size()>0)
		{
			oldtimerinfo = configinfos.get(0);
			
			startTime.setText(Utility.getTimeString(oldtimerinfo.startHour,oldtimerinfo.startMin));
			endTime.setText(Utility.getTimeString(oldtimerinfo.endHour, oldtimerinfo.endMin));
			String repeat = "";
			String[] ss = getResources().getStringArray(R.array.array_date);
			
			week_temp = oldtimerinfo.week;
			week_temp = (byte) ((1<<6 & week_temp) |(1 & week_temp>>5) |(1 <<1 & week_temp>>3)| (1<<2 & week_temp>>1)|(1<<3 & week_temp<<1)
						| (1<<4 & week_temp<<3) | (1<<5 & week_temp<<5));
			for(int i=0; i<7; i++){
				if(Utility.getByteIndex(week_temp, i + 1)){
					repeat = repeat + "  " + ss[i];
					dayschecked[i] = true;
					days_week[i] = true;
				}else
				{
					dayschecked[i] = false;
					days_week[i] = false;
				}
				
					
			}
			cycle_days.setText(repeat);
		}else{
			
			oldtimerinfo = new ConfigInfo();
			
			for(int i=0; i<7; i++){
				dayschecked[i] = false;
				days_week[i] = false;
			}
			timeCount = 1;
			configinfos = new ArrayList<ConfigInfo>();
			configinfos.add(oldtimerinfo);
		}
		
			
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
			//Toast.makeText(getApplicationContext(), "定时信息任务量为"+ timeCount + "个", Toast.LENGTH_SHORT).show();
			if(result == 0){
				Toast.makeText(ScoketTaskEditActivity.this, ScoketTaskEditActivity.this.getResources().getString(R.string.renwu_socket_offline), Toast.LENGTH_SHORT).show();
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
			ScoketTaskEditActivity.this.finish();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(timeCount > 0){
				tempci = configinfos.get(timeCount - 1);
				//configinfos.set(timeCount - 1, ci);
				configinfos.set(0, ci);
			}
			else if(timeCount == 0){
				//configinfos.add(ci);
				configinfos.set(0, ci);
			}
			fail = 0;
			processDialog = new ProgressDialog(ScoketTaskEditActivity.this);
			processDialog.setMessage(ScoketTaskEditActivity.this.getString(R.string.loging));
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
		setContentView(R.layout.socketnewtask);
		//timeCount = getIntent().getIntExtra(GobalDef.INTENT_TIME_INDEX, 0);
		switchApplication app = (switchApplication)getApplication();
		dim = app.getItemModel();
		configinfos = Utility.getConfigInfo(dim.getTimeinfo());
		timeCount = configinfos.size();
		findView();
		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "back here onActivityResult resultCode=" + resultCode);
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
		}else if(resultCode == Activity.RESULT_OK)
		{
			if(requestCode == 2)
			{
				days_week = data.getExtras().getBooleanArray(GobalDef.INTENT_TIME_WEEK);
				SetDaysSelet(days_week);
				Log.i(TAG, "back here onActivityResult resultCode == 2");
			}
		}
		
		
	}

	private void SetDaysSelet(boolean[] days_week) {
		// TODO Auto-generated method stub
		String repeat = "";
		String[] ss = getResources().getStringArray(R.array.array_date);
		for(int i=0; i<7; i++){
			if(days_week[i]){
				repeat = repeat + "  " + ss[i];
				
			}
				
		}
		cycle_days.setText(repeat);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == startTime || (v == start_time_layout))  
		{
			int result = 0;
			
			Intent intent = new Intent();
			intent.putExtra(GobalDef.INTENT_TIME_HOUR, "00");
			intent.putExtra(GobalDef.INTENT_TIME_MIN, "0");
			intent.setClass(getApplicationContext(), ControlSetAlertActivity.class);
			startActivityForResult(intent,result);
		}
		if(v == endTime || (v == end_time_layout))
		{
			int result = 1;
			Intent intent = new Intent();
			intent.putExtra(GobalDef.INTENT_TIME_HOUR, "00");
			intent.putExtra(GobalDef.INTENT_TIME_MIN, "0");
			intent.setClass(getApplicationContext(), ControlSetAlertActivity.class);
			startActivityForResult(intent,result);
		}
		if((v == cycle_days) || (v == repeat_days) )
		{
			
			//跳转选择一周的天数
			Intent intent = new Intent();
			intent.putExtra(GobalDef.INTENT_TIME_WEEK, week_temp);
			intent.setClass(getApplicationContext(), ScoketTaskDaysActivity.class);
			startActivityForResult(intent,2);
		}
	}

	
}
