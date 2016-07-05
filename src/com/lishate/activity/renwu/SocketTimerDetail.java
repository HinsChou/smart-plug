package com.lishate.activity.renwu;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.client.protocol.RequestAddCookies;

import com.lishate.R;
import com.lishate.activity.renwu.ScoketTaskEditActivity.SaveConfigInfoTask;
import com.lishate.adapter.AuraSocketTimerAdapter;
import com.lishate.application.switchApplication;
import com.lishate.data.GobalDef;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.ServerItemModel;
import com.lishate.message.ConfigInfo;
import com.lishate.message.GetServerConfigReqMessage;
import com.lishate.message.MessageDef;
import com.lishate.message.MessageSeqFactory;
import com.lishate.message.PublicTimezoneSetReqMessage;
import com.lishate.message.SetConfigReqMessage;
import com.lishate.message.baseMessage;
import com.lishate.net.UdpProcess;
import com.lishate.utility.ImageTools;
import com.lishate.utility.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SocketTimerDetail extends Activity{
	private final String TAG = "SocketTimerDetail";
	private final int REQUESTMODIFYTIMER = 10001;
	private final int REQUESTADDTIMERTASK = 10002;
	private LinearLayout backkey;
	private LinearLayout addtask;
//	private ImageView   device_icon;
//	private EditText	device_name;
	private ListView	timerlist;
	private DeviceItemModel dim;
	private enum RequestCode{
		MODIFYTASK,
		ADDTIMETASK		
	}
	private List<ConfigInfo> configinfos ;
	private ConfigInfo oldtimerinfo;
	//用来存储上一次的定时信息
	private List<ConfigInfo> oldconfiginfos=new ArrayList<ConfigInfo>() ;
	private ConfigInfo ci = null;
	private int timeCount = 0;
	
	public boolean[] dayschecked;
	private boolean[] days_week;
	private AuraSocketTimerAdapter mtimeradapter;
	
	private List<TimerSubItem> timerdatatask = new ArrayList<TimerSubItem>();
	
	private Bitmap myBitmap;
	
	public void setConfigInfo(){
//		if(null !=oldtimerinfo)	
//		{
//			if(oldtimerinfo.isenable)
				ci = oldtimerinfo;
				ci.isenable = true;
//			else
//				ci.isenable = false;
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
		
//		String sresult = (String) endTime.getText();
//		String[] times = sresult.split(":");
//		ci.endHour = (byte) Integer.parseInt(times[0]);
//		ci.endMin = (byte) Integer.parseInt(times[1]);
//		sresult = (String)startTime.getText();
//		times = sresult.split(":");
//		ci.startHour = (byte)Integer.parseInt(times[0]);
//		ci.startMin = (byte)Integer.parseInt(times[1]);
		
	}
	

	protected boolean equalCurrenttimeWithLastTimer() {
		// TODO Auto-generated method stub
		if(oldconfiginfos.size() != configinfos.size())
			return true;
		else{
			for(int i=0; i<configinfos.size(); i++){
				ConfigInfo cnew = configinfos.get(i);
				ConfigInfo cold = oldconfiginfos.get(i);
				if(cnew.isenable != cold.isenable || cnew.startenable != cold.startenable ||cnew.endenable!= cold.endenable
						 || cnew.startHour!= cold.startHour || cnew.startMin!= cold.startMin || cnew.endHour != cold.endHour 
						 || cnew.startMin!= cold.startMin || cnew.week != cold.week){
					return false;
				}
			}
		}
		return  true;
		
	}	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.socketimer);
		switchApplication app = (switchApplication)getApplication();
		dim = app.getItemModel();
		configinfos = Utility.getConfigInfo(dim.getTimeinfo());
		SaveOldConfig();
		findView();
		initView();
		mtimeradapter = new AuraSocketTimerAdapter(this,  configinfos);
		timerlist.setAdapter(mtimeradapter);
		
		
	}

	private void SaveOldConfig() {
		// TODO Auto-generated method stub
		oldconfiginfos.clear();
		for(int i =0; i<configinfos.size(); i++){
			ConfigInfo ci = new ConfigInfo();
			ci.isenable = configinfos.get(i).isenable;
			ci.startenable = configinfos.get(i).startenable;
			ci.endenable = configinfos.get(i).endenable;
			ci.startHour = configinfos.get(i).startHour;
			ci.startMin = configinfos.get(i).startMin;
			ci.endHour = configinfos.get(i).endHour;
			ci.endMin = configinfos.get(i).endMin;
			ci.week = configinfos.get(i).week;
			oldconfiginfos.add(ci);
		}
	}


	private void initView() {
		// TODO Auto-generated method stub
		
		
		backkey.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean ismodify =false;
			
//					ismodify = !Arrays.equals(dayschecked, days_week);
//				//else
					//days_week = Arrays.copyOf(dayschecked, dayschecked.length);
					//ismodify = equalCurrenttimeWithLastTimer();
				Log.e(TAG, "ismodify=" +  ismodify);	
//				if(true == ismodify )
//				{
//					//提示是否要保存修改的数据
//					Toast.makeText(getApplicationContext(), "正在保存修改的数据", Toast.LENGTH_SHORT).show();
//					
//					//setConfigInfo();
//					SaveConfigInfoTask sct = new SaveConfigInfoTask();
//					sct.execute(new Void[0]);
//				}
//				else
					finish();
			}


			
		});
//		device_name.setText(dim.getDeviceName());
//		device_name.setFocusable(false);
//		if(dim.getDeviceIcon().startsWith("p")){
//			String str = dim.getDeviceIcon() + "_on_xxl";
//			device_icon.setImageResource(GetresIdByName(str));
//			
//		}else if(!dim.getDeviceIcon().equalsIgnoreCase(null)){
//			myBitmap = BitmapFactory.decodeFile(dim.getDeviceIcon());
//			myBitmap= ImageTools.toRoundCorner(myBitmap, myBitmap.getDensity());       
//			device_icon.setImageBitmap(myBitmap);
//		}
		addtask.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mintent = new Intent();
				mintent.setClass(getApplicationContext(), SetTimerSelectDay.class);
				int reqcode = REQUESTADDTIMERTASK<<8;
//				Bundle mBundle = new Bundle();
//				mBundle.putSerializable("MODIFYTIMER", ci2);//帮当前要修改的时间参数传递进去
//				mintent.putExtras(mBundle);
				startActivityForResult(mintent, reqcode);
			}
		});
//		if(configinfos.size()>0)
//		{
//			oldtimerinfo = configinfos.get(0);
//			
//			startTime.setText(Utility.getTimeString(oldtimerinfo.startHour,oldtimerinfo.startMin));
//			endTime.setText(Utility.getTimeString(oldtimerinfo.endHour, oldtimerinfo.endMin));
//			String repeat = "";
//			String[] ss = getResources().getStringArray(R.array.array_date);
//			
//			for(int i=0; i<7; i++){
//				if(Utility.getByteIndex(oldtimerinfo.week, i + 1)){
//					repeat = repeat + "  " + ss[i];
//					dayschecked[i] = true;
//					days_week[i] = true;
//				}else
//				{
//					dayschecked[i] = false;
//					days_week[i] = false;
//				}
//				
//					
//			}
//			cycle_days.setText(repeat);
//		}else{
//			
//			oldtimerinfo = new ConfigInfo();
//			
//			for(int i=0; i<7; i++){
//				dayschecked[i] = false;
//				days_week[i] = false;
//			}
//			timeCount = 1;
//			configinfos = new ArrayList<ConfigInfo>();
//			configinfos.add(oldtimerinfo);
//		}
		
	}

	private void findView() {
		// TODO Auto-generated method stub
		backkey = (LinearLayout)findViewById(R.id.socketinfo_back);
		addtask = (LinearLayout)findViewById(R.id.sockettimer_add);
//		device_icon = (ImageView)findViewById(R.id.socketinfo_detail);
//		device_name = (EditText)findViewById(R.id.device_name);
		timerlist  = (ListView)findViewById(R.id.socket_timer_aura);
	}
class SaveConfigInfoTask extends  AsyncTask<Void,Integer, Integer>{
		
		//private static final String TAG = "SocketTaskEditActivity";
		private ProgressDialog processDialog;
		private int fail = 0;
		private ConfigInfo tempci;

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			processDialog.dismiss();
			if(result == 0){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.renwu_socket_offline), Toast.LENGTH_SHORT).show();
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
			//finish();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			if(timeCount > 0){
//				tempci = configinfos.get(timeCount - 1);
//				//configinfos.set(timeCount - 1, ci);
//				configinfos.set(0, ci);
//			}
//			else if(timeCount == 0){
//				//configinfos.add(ci);
//				configinfos.set(0, ci);
//			}
			fail = 0;
			processDialog = new ProgressDialog(SocketTimerDetail.this);
			processDialog.setMessage(getString(R.string.loging));
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
//			byte[] buf = scrm.getIndexBuf();
//			if(timeCount == 0){
//				buf[0] = (byte) configinfos.size();
//			}
//			else{
//				buf[0] = (byte) (timeCount - 1);
//			}
//			ci.writeByte(buf, 1);
//			scrm.setIndexBuf(buf);
			//UdpProcess.Send(ds, destip, destport, buf)
			baseMessage bm = UdpProcess.GetMsgReturn(scrm, sim,0,0);
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
				
				
				bm = UdpProcess.GetMsgReturn(lrm, sim,0,0);
				if(bm != null){
					return 1;
				}
			}
			return 0;
		}
		
	}
	
	private int GetresIdByName(String str){
		int resId = 0;
		Field field;
		
		try {
			field = R.drawable.class.getField(str);
			resId= field.getInt(new R.drawable()); 
			
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resId;
		
	} 
	private class TimerSubItem{
		private int timepoint;
		private boolean isOnoff;
		private boolean[] dayChecked;
	}

	public void Update() {
		// TODO Auto-generated method stub
		mtimeradapter.notifyDataSetChanged();
		Log.e(TAG, "准备更新时间参数 有" + configinfos.size() + "项任务表");
		timeCount = configinfos.size();
		//if(timeCount>0)
		{
			SaveConfigInfoTask sct = new SaveConfigInfoTask();
			sct.execute(new Void[0]);
		}
	}


	public void EditTaskReq(ConfigInfo ci2, int pos) {
		// TODO Auto-generated method stub
		//Log.e(TAG, "准备修改时间参数");
		
		Intent mintent = new Intent(this, SetTimerSelectDay.class);
		int reqcode = (REQUESTMODIFYTIMER<<8) + (pos & 0xff);
		Log.e(TAG, "准备修改的定时信息是：reqcode=" + (reqcode>>8) );
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("MODIFYTIMER", ci2);//帮当前要修改的时间参数传递进去
		mintent.putExtras(mBundle);
		startActivityForResult(mintent, reqcode);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, "处理返回参数RESULT_OK=" +resultCode );
		ConfigInfo mconfig ;
		if(resultCode == RESULT_OK)
		{
			mconfig = (ConfigInfo)data.getSerializableExtra("BACKMODIFY");
			switch(requestCode>>8)
			{
				case REQUESTMODIFYTIMER:
					
					Refresh(mconfig, requestCode&0xff);
					break;
				case REQUESTADDTIMERTASK:
					AddTimerTask(mconfig);
					break;
				default:
					Log.e(TAG, "处理返回参数  请求参数无效" + (requestCode>>8));
					break;
			}
			if((requestCode>>8 == REQUESTMODIFYTIMER) ||  (requestCode>>8 == REQUESTADDTIMERTASK)){
				//发送时区
				///DeviceItemModel[] dimlist= new DeviceItemModel()[0];
				TimeZoneSetTask  settimezone= new TimeZoneSetTask();
				settimezone.execute(new DeviceItemModel[]{dim});
			}
		}
	}


	private void AddTimerTask(ConfigInfo newconfig) {
		// TODO Auto-generated method stub
		ConfigInfo mmconfig =new ConfigInfo();
		mmconfig.endenable = newconfig.endenable;
		mmconfig.startenable = newconfig.startenable;
		mmconfig.isenable = newconfig.isenable;
		mmconfig.startHour = newconfig.startHour;
		mmconfig.startMin = newconfig.startMin;
		mmconfig.endHour = newconfig.endHour;
		mmconfig.endMin = newconfig.endMin;
		mmconfig.week = newconfig.week;
		Log.e(TAG, "刷新数据");
		configinfos.add(mmconfig);
		Update();
	}
	class TimeZoneSetTask extends AsyncTask<DeviceItemModel, Integer, Integer>{
		
		@Override
		protected Integer doInBackground(DeviceItemModel... params) {
			// TODO Auto-generated method stub
			
			dim = params[0];
			PublicTimezoneSetReqMessage  orm = null;
			baseMessage msg = null;
			orm = new PublicTimezoneSetReqMessage();
//			TimeZone tz = TimeZone.getDefault();
//			String str = tz.getDisplayName(tz.useDaylightTime(), TimeZone.SHORT);
//			
//			if(!str.contains("GMT"))
//				str = createGmtOffsetString(true,true,tz.getRawOffset());
//			int timezoneH = Integer.parseInt(str.substring(4, 6));
//			int timezoneM = Integer.parseInt(str.substring(7, 9));
//			if(str.charAt(3) == '-'){
//				timezoneH = 0 - timezoneH;
//			}
			
			Date mdate = new Date();
			Calendar mcalend = Calendar.getInstance();
		
			long time_UTC = Date.UTC(mdate.getYear(), mdate.getMonth(), mdate.getDate(), mdate.getHours(), mdate.getMinutes(),  mdate.getSeconds());
			long  diff = (time_UTC - mcalend.getTime().getTime());
			if(diff<0)
				diff= (diff-900)/900*900;
			else
				diff= (diff+900)/900*900;
			
			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
			long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
			
			orm.setTimeZone((int)hours+12);
			orm.setTimeZoneMin((int)minutes);
			orm.setToId(dim.getDeviceId());
			//orm.setServerIp((115)|(28<<8)|(45<<16)|(50<<24));
			//orm.setServerPort(20480);
			//orm.setUpgradeUrl("fm/pub/1");
			ServerItemModel sim = new ServerItemModel();
			ServerItemModel localsim = new ServerItemModel();
			//sim.setIpaddress("255.255.255.255");
			sim.setIpaddress(GobalDef.SERVER_URL);
			
			sim.setPort(GobalDef.SERVER_PORT);
			orm.Direct = MessageDef.BASE_MSG_FT_REQ;
			orm.setFromId(GobalDef.MOBILEID);
			//orm.MsgType = MessageDef.MESSAGE_TYPE_PUBLIC_UPGRADE_REQ;
			
			orm.Seq = MessageSeqFactory.GetNextSeq();
			orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			orm.ToType = MessageDef.BASE_MSG_FT_HUB;
			orm.setToId(dim.getDeviceId());
			
			
			if(msg == null){
				msg = UdpProcess.GetMsgReturn(orm, sim);
			}
			
			if(msg != null){
				Log.e(TAG, "设置时区   not  null");
			}
			else{
				Log.e(TAG, "设置时区   msg is null");
			}
			return 1;
		}
		
	}


	private void Refresh(ConfigInfo newconfig, int posion) {
		// TODO Auto-generated method stub
		Log.e(TAG, "数组总长度是：" +timerdatatask.size() + "刷新数据位置是："+ posion);
		
		if(configinfos.size()>posion){
			ConfigInfo mmconfig =(ConfigInfo) configinfos.get(posion);
			mmconfig.endenable = newconfig.endenable;
			mmconfig.startenable = newconfig.startenable;
			mmconfig.isenable = newconfig.isenable;
			mmconfig.startHour = newconfig.startHour;
			mmconfig.startMin = newconfig.startMin;
			mmconfig.endHour = newconfig.endHour;
			mmconfig.endMin = newconfig.endMin;
			mmconfig.week = newconfig.week;
			Log.e(TAG, "刷新数据");
			Update();
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mtimeradapter.notifyDataSetChanged();
	}


}
