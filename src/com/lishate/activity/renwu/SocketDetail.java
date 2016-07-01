package com.lishate.activity.renwu;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.activity.ControlSetAlertActivity;
import com.lishate.adapter.SocketDetailAdapter;
import com.lishate.application.switchApplication;
import com.lishate.data.DeviceLocalInfo;
import com.lishate.data.GobalDef;
import com.lishate.data.SharedPreferencesUtility;
import com.lishate.data.dao.TimeTaskItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.ServerItemModel;
import com.lishate.data.model.TimeTaskItemModel;
import com.lishate.encryption.Encryption;
import com.lishate.message.CloseReqMessage;
import com.lishate.message.CloseRspMessage;
import com.lishate.message.ConfigInfo;
import com.lishate.message.GetServerConfigReqMessage;
import com.lishate.message.GetServerConfigRspMessage;
import com.lishate.message.GetStatueReqMessage;
import com.lishate.message.GetStatueRspMessage;
import com.lishate.message.MessageDef;
import com.lishate.message.MessageSeqFactory;
import com.lishate.message.OpenReqMessage;
import com.lishate.message.OpenRspMessage;
import com.lishate.message.SetConfigReqMessage;
import com.lishate.message.SocketDelayReqMessage;
import com.lishate.message.SocketDelayRspMessage;
import com.lishate.message.baseMessage;
import com.lishate.net.UdpProcess;
import com.lishate.utility.Utility;

public class SocketDetail extends BaseActivity {

	protected static final String TAG = "SocketDetail";
	private ImageButton backButton;
	private TextView name;
	private ProgressBar bar;
	private ImageButton refresh;
	private ImageView socketimg;
	private ImageView openclose;
	private ImageView delayimg;
	private DeviceItemModel dim;
	private SocketDetailAdapter sda ;
	private ListView tasklist;
	private TextView tip;
	private TextView delayInfo;
	private ServerItemModel localsim = new ServerItemModel();
	
	private boolean mScrollBusy = false;
	
	private boolean refreshrun = true;
	
	private List<ConfigInfo> configinfos = new ArrayList<ConfigInfo>();
	
private List<TimeTaskItemModel> devlist = new ArrayList<TimeTaskItemModel>();

	private ConfigInfo GetCompareConfigInfo(ConfigInfo c1, ConfigInfo c2, int direct){
		if(direct == 0){
			if(c1.startHour < c2.startHour){
				return c1;
			}
			else if(c1.startHour == c2.startHour){
				if(c1.startMin < c2.startMin){
					return c1;
				}
			}
		}
		else if(direct == 1){
			if(c1.endHour < c2.endHour){
				return c1;
			}
			else if(c1.endHour == c2.endHour){
				if(c1.endMin < c2.endMin){
					return c1;
				}
			}
		}
		return c2;
	}
	
	public void InitDevice(DeviceItemModel dim){
		if(dim.getOnoff() == DeviceItemModel.Online_Off){
			openclose.setImageResource(R.drawable.off);
		}else{
			openclose.setImageResource(R.drawable.on);
		}
		
		initDevList();
		sda.setConfigInfo(configinfos);
		sda.notifyDataSetChanged();
		tip.setText(getNextString());//getNextString();
	}
	
	private String getNextString(){
		Date now = new Date();
		int week = now.getDay();
		ConfigInfo tempci = null;
		String result = "";
		try{
			for(int i=0; i<configinfos.size(); i++){
				week = now.getDay();
				ConfigInfo ci = configinfos.get(i);
				//int index = 0;
				if(week > 0){
					week = 8-week;
				}
				else{
					week = 1;
				}
				if(Utility.getByteIndex(ci.week, week)){
					if(dim.getOnoff() == DeviceItemModel.OnOff_On){
						if(ci.endenable == true){
							if(ci.endHour > now.getHours()){
								if(tempci != null){
									tempci = GetCompareConfigInfo(ci, tempci, 1);
								}
								else{
									tempci = ci;
								}
							}
							else if(ci.endHour == now.getHours()){
								if(ci.endMin >= now.getMinutes()){
									if(tempci != null){
										tempci = GetCompareConfigInfo(ci, tempci, 1);
									}
									else{
										tempci = ci;
									}
								}
							}
						}
					}
					else if(dim.getOnoff() == DeviceItemModel.OnOff_Off){
						if(ci.startenable == true){
							if(ci.startHour > now.getHours()){
								if(tempci != null){
									tempci = GetCompareConfigInfo(ci,tempci,0);
								}
								else{
									tempci = ci;
								}
							}
							else if(ci.startHour == now.getHours()){
								if(ci.startMin >= now.getMinutes()){
									if(tempci != null){
										tempci = GetCompareConfigInfo(ci,tempci, 0);
									}
									else{
										tempci = ci;
									}
								}
							}
						}
					}
				}
			}
			if(tempci != null){
				if(dim.getOnoff() == DeviceItemModel.OnOff_On){
					result =Utility.getTimeString(tempci.endHour, tempci.endMin) + this.getResources().getString(R.string.renwu_socket_detail_close);
				}
				else{
					result = Utility.getTimeString(tempci.startHour, tempci.startMin) + getResources().getString(R.string.renwu_socket_detail_open);
				}
			}
		}
		catch(Exception e){
			if(e.getMessage() != null){
				Log.d(TAG,e.getMessage());
			}
			e.printStackTrace();
			
		}
		return result;
	}
	
	private void initDevList(){
		try {
			String s = dim.getTimeinfo();
			configinfos = Utility.getConfigInfo(s);
			
			/*
			TimeTaskItemDao did = new TimeTaskItemDao(getHelper());
			devlist.clear();
			devlist.addAll(did.queryForAll());
			if(devlist.size() == 0){
				TimeTaskItemModel dim = new TimeTaskItemModel();
				
				devlist.add(dim);
				
				did.create(dim);
				//did.
			}
			TimeTaskItemModel dim = new TimeTaskItemModel();
			for(int i=0; i<7; i++){
				devlist.add(dim);
			}
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private void findView(){
		backButton = (ImageButton)findViewById(R.id.socketdetail_back);
		name = (TextView)findViewById(R.id.socketdetail_name);
		bar = (ProgressBar)findViewById(R.id.socketdetail_refresjing);
		refresh = (ImageButton)findViewById(R.id.socketdetail_refresh);
		socketimg = (ImageView)findViewById(R.id.socketdetial_img);
		openclose = (ImageView)findViewById(R.id.socketdetail_open);
		tasklist = (ListView)findViewById(R.id.socketdetail_task);
		delayimg = (ImageView)findViewById(R.id.socketdetail_delay);
		tip = (TextView)findViewById(R.id.socketdetail_tip);
		delayInfo = (TextView)findViewById(R.id.socketdetail_delayinfo);
	}
	
	private void InitView(){
		View v = getLayoutInflater().inflate(R.layout.socketdetail_task_head, null);
		tasklist.addHeaderView(v);
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		tip.setText(getNextString());
		name.setText(dim.getDeviceName());
		socketimg.setImageDrawable(getResources().getDrawable(R.drawable.p1));
		
		tasklist.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch(scrollState){
				case OnScrollListener.SCROLL_STATE_IDLE:
					mScrollBusy = false;
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					mScrollBusy = true;
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					mScrollBusy = true;
					break;
				}
			}
			
		});
		
		socketimg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SocketDetail.this, SocketInfoActivity.class);
				SocketDetail.this.startActivity(intent);
			}
			
		});
		
		refresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RefreshTask rt = new RefreshTask();
				rt.execute(new DeviceItemModel[]{dim});
			}
			
		});
		
		openclose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				opencloseTask oct = new opencloseTask();
				oct.execute(new DeviceItemModel[]{dim});
			}
			
		});
		
		//*
		tasklist.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adaView, View paramView, int index,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d(TAG, "the itemindex is: " + index);
				Intent intent = new Intent();
				if(index == 0){
					if(configinfos.size() >= 7){
						Toast.makeText(SocketDetail.this, R.string.renwu_socket_dtail_maxtime, Toast.LENGTH_SHORT).show();
						return;
					}
				}
				intent.putExtra(GobalDef.INTENT_TIME_INDEX, index);
				intent.setClass(SocketDetail.this, ScoketTaskEditActivity.class);
				startActivity(intent);
			}
			
		});
		//*/
		
		tasklist.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch(scrollState){
				case OnScrollListener.SCROLL_STATE_IDLE:
					mScrollBusy = false;
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					mScrollBusy = true;
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					mScrollBusy = true;
					break;
				}
				Log.d(TAG, "socket scrolllistener change£¬ scrollState " + scrollState);
			}
			
		});
		
		delayimg.setOnClickListener(new OnClickListener(){

			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				AlertDialog dialog = new AlertDialog.Builder(SocketDetail.this).setTitle(R.string.renwu_socket_dealy_title)
						.setSingleChoiceItems(R.array.array_delay, 0, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								
							}
							
						})
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								
							}
							
						})
						.setNegativeButton(null, null)
						.create();
				
				dialog.show();
				*/
				Intent intent = new Intent();
				intent.putExtra(GobalDef.INTENT_TIME_HOUR, "00");
				intent.putExtra(GobalDef.INTENT_TIME_MIN, "0");
				intent.setClass(SocketDetail.this, ControlSetAlertActivity.class);
				startActivityForResult(intent,0);
			}
			
		});
	}
	
	class opencloseTask extends AsyncTask<DeviceItemModel, Integer, Integer>{

		private ProgressDialog progressDialog;
		private Vibrator mVibrator;
		@Override
		protected Integer doInBackground(DeviceItemModel... params) {
			// TODO Auto-generated method stub
			//dim = params[0];
			baseMessage orm = null;
			if(dim.getOnoff() == DeviceItemModel.Online_Off){
				orm = new OpenReqMessage();
			}
			else{
				orm = new CloseReqMessage();
			}
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			orm.Direct = MessageDef.BASE_MSG_FT_REQ;
			orm.setFromId(GobalDef.MOBILEID);
			if(dim.getOnoff() == DeviceItemModel.Online_Off){
				orm.MsgType = MessageDef.MESSAGE_TYPE_OPEN_REQ;
			}else{
				orm.MsgType = MessageDef.MESSAGE_TYPE_CLOSE_REQ;
			}
			orm.Seq = MessageSeqFactory.GetNextSeq();
			orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			orm.ToType = MessageDef.BASE_MSG_FT_HUB;
			orm.setToId(dim.getDeviceId());
			
			baseMessage msg = null;
			DeviceLocalInfo dli = GobalDef.Instance.getLocalList().getDeviceLocalInfo(dim.getDeviceId());
			if(dli != null){
				if(dli.getLocalIp() != null && dli.getLocalIp().length() > 0){
					localsim.setIpaddress(dli.getLocalIp());
					msg = UdpProcess.GetMsgReturn(orm, localsim);
				}
			};
			if(msg == null){
				GobalDef.Instance.getLocalList().deleteDeviceLocalInfo(dim.getDeviceId());
				msg = UdpProcess.GetMsgReturn(orm, sim);
			}
			
			if(msg != null){
				try{
					if(dim.getOnoff() == DeviceItemModel.Online_Off){
						OpenRspMessage orspm = (OpenRspMessage)msg;
						if(orspm.getRspStatue() == MessageDef.RSP_OK){
						return 1;
						}
					}
					else{
						CloseRspMessage orspm = (CloseRspMessage)msg;
						if(orspm.getRspStatue() == MessageDef.RSP_OK){
						return 1;
						}
					}
				}
				catch(Exception e){
					Log.d(TAG, e.getMessage());
					e.printStackTrace();
				}
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result == 0){
				Toast.makeText(SocketDetail.this, R.string.renwu_operation_fail, Toast.LENGTH_SHORT);
			}
			else{
				if(dim.getOnoff() == DeviceItemModel.Online_Off){
					dim.setOnoff(DeviceItemModel.OnOff_On);
					openclose.setImageResource(R.drawable.on);
				}
				else{
					dim.setOnoff(DeviceItemModel.OnOff_Off);
					openclose.setImageResource(R.drawable.off);
				}
				//mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (SharedPreferencesUtility.getVibrate() == true)
	         {
	            this.mVibrator = ((Vibrator)SocketDetail.this.getSystemService(Service.VIBRATOR_SERVICE));
	            this.mVibrator.vibrate(50L);
	         }
			progressDialog = new ProgressDialog(SocketDetail.this);
			progressDialog.setMessage(SocketDetail.this.getString(R.string.loging));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
			
		
	}
	
	class DeleteTask extends AsyncTask<Integer, Integer, Integer>{

		private ProgressDialog progressDialog;
		//private DeviceItemModel dim;
		private ConfigInfo tempci = null;
		private int tempindex = 0;
		
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int index = params[0];
			tempindex = index;
			if(index >= configinfos.size()){
				return 0;
			}else{
				tempci = configinfos.get(index);
				configinfos.remove(index);
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
				
				baseMessage bm = null;
				DeviceLocalInfo dli = GobalDef.Instance.getLocalList().getDeviceLocalInfo(dim.getDeviceId());
				if(dli != null){
					if(dli.getLocalIp() != null && dli.getLocalIp().length() > 0){
						localsim.setIpaddress(dli.getLocalIp());
						bm = UdpProcess.GetMsgReturn(scrm, localsim);
					}
				}
				
				if(bm == null){
					GobalDef.Instance.getLocalList().deleteDeviceLocalInfo(dim.getDeviceId());
					UdpProcess.GetMsgReturn(scrm, sim);
				}
				GetServerConfigReqMessage lrm = new GetServerConfigReqMessage();
				lrm.Direct = MessageDef.BASE_MSG_FT_REQ;
				lrm.setFromId(GobalDef.MOBILEID);
				lrm.setToId(dim.getDeviceId());
				
				lrm.MsgType = MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ;
				lrm.Seq = MessageSeqFactory.GetNextSeq();
				lrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
				lrm.ToType = MessageDef.BASE_MSG_FT_HUB;
				
				//DeviceLocalInfo dli = GobalDef.Instance.getLocalList().getDeviceLocalInfo(dim.getDeviceId());
				if(dli != null){
					if(dli.getLocalIp() != null && dli.getLocalIp().length() > 0){
						localsim.setIpaddress(dli.getLocalIp());
						bm = UdpProcess.GetMsgReturn(lrm, localsim);
					}
				}
				//bm = UdpProcess.GetMsgReturn(lrm, localsim);
				if(bm == null){
					GobalDef.Instance.getLocalList().getDeviceLocalInfo(dim.getDeviceId());
					bm = UdpProcess.GetMsgReturn(lrm, sim);
				}
				if(bm != null){
					GetServerConfigRspMessage gscrsp = (GetServerConfigRspMessage)bm;
					gscrsp.Content2Array();
					
					String result = Utility.getConfigStringInfo(gscrsp.configinfos);
					dim.setTimeinfo(result);
					return 1;
				}
				
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result == 0){
				try{
					configinfos.add(tempindex, tempci);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				Toast.makeText(SocketDetail.this, R.string.renwu_socketlist_send_fail, Toast.LENGTH_SHORT).show();
			}
			else{
				InitDevice(dim);
				//sda.notifyDataSetChanged();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(SocketDetail.this);
			progressDialog.setMessage(SocketDetail.this.getString(R.string.loging));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		
		
		
	}
	
	class SendDelayTask extends AsyncTask<Integer, Integer, Integer>{

		private ProgressDialog progressDialog;
		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			int min = arg0[0];
			SocketDelayReqMessage sdr = new SocketDelayReqMessage();
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			sdr.Direct = MessageDef.BASE_MSG_FT_REQ;
			sdr.setFromId(GobalDef.MOBILEID);
			sdr.Seq = MessageSeqFactory.GetNextSeq();
			sdr.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			sdr.ToType = MessageDef.BASE_MSG_FT_SERVER;
			sdr.MsgType = MessageDef.MESSAGE_TYPE_SET_DELAY_ONOFF_REQ;
			
			sdr.OnOff = dim.getOnoff();
			if(dim.getOnoff() == DeviceItemModel.OnOff_On){
				sdr.OnOff = DeviceItemModel.OnOff_Off;
			}
			else{
				sdr.OnOff = DeviceItemModel.OnOff_On;
			}
			sdr.TimeSpan = 60 * min;
			sdr.setToId(dim.getDeviceId());
			
			baseMessage bm = UdpProcess.GetMsgReturn(sdr, sim);
			if(bm != null){
				SocketDelayRspMessage sdrm = (SocketDelayRspMessage)bm;
				return 1;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result == 0){
				Toast.makeText(SocketDetail.this, R.string.renwu_socketlist_send_fail, Toast.LENGTH_SHORT).show();
			}
			else{
				//sda.notifyDataSetChanged();
				Toast.makeText(SocketDetail.this, R.string.renwu_detail_send_sus, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(SocketDetail.this);
			progressDialog.setMessage(SocketDetail.this.getString(R.string.loging));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		
		
		
	}
	
	class RefreshTask extends AsyncTask<DeviceItemModel, Integer, Integer>{

		@Override
		protected Integer doInBackground(DeviceItemModel... params) {
			// TODO Auto-generated method stub
			int flag = 0;
			GetStatueReqMessage orm = new GetStatueReqMessage();
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			orm.Direct = MessageDef.BASE_MSG_FT_REQ;
			orm.setFromId(GobalDef.MOBILEID);
			
			orm.Seq = MessageSeqFactory.GetNextSeq();
			orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			orm.ToType = MessageDef.BASE_MSG_FT_HUB;
			orm.MsgType = MessageDef.MESSAGE_TYPE_GET_STATUS_REQ;
			
			orm.setToId(dim.getDeviceId());
			
			baseMessage bm = null;
			DeviceLocalInfo dli = GobalDef.Instance.getLocalList().getDeviceLocalInfo(dim.getDeviceId());
			if(dli != null){
				if(dli.getLocalIp() != null && dli.getLocalIp().length() > 0){
					localsim.setIpaddress(dli.getLocalIp());
					bm = UdpProcess.GetMsgReturn(orm, localsim);
				}
			}
			if(bm == null){
				GobalDef.Instance.getLocalList().deleteDeviceLocalInfo(dim.getDeviceId());
				bm = UdpProcess.GetMsgReturn(orm, sim);
			}
			if(bm != null){
				GetStatueRspMessage gsrm = (GetStatueRspMessage)bm;
				if(gsrm.getOpenClose() == 1){
					dim.setOnoff(DeviceItemModel.OnOff_On);
				}
				else{
					dim.setOnoff(DeviceItemModel.OnOff_Off);
				}
				GetServerConfigReqMessage lrm = new GetServerConfigReqMessage();
				lrm.Direct = MessageDef.BASE_MSG_FT_REQ;
				lrm.setFromId(GobalDef.MOBILEID);
				lrm.setToId(dim.getDeviceId());
				
				lrm.MsgType = MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ;
				lrm.Seq = MessageSeqFactory.GetNextSeq();
				lrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
				lrm.ToType = MessageDef.BASE_MSG_FT_HUB;
				
				//DeviceLocalInfo dli = GobalDef.Instance.getLocalList().getDeviceLocalInfo(dim.getDeviceId());
				if(dli != null){
					if(dli.getLocalIp() != null && dli.getLocalIp().length() > 0){
						localsim.setIpaddress(dli.getLocalIp());
						bm = UdpProcess.GetMsgReturn(lrm, localsim);
					}
				}
				//bm = UdpProcess.GetMsgReturn(lrm, localsim);
				if(bm == null){
					GobalDef.Instance.getLocalList().deleteDeviceLocalInfo(dim.getDeviceId());
					bm = UdpProcess.GetMsgReturn(lrm, sim);
				}
				if(bm != null){
					GetServerConfigRspMessage gscrsp = (GetServerConfigRspMessage)bm;
					gscrsp.Content2Array();
					
					String result = Utility.getConfigStringInfo(gscrsp.configinfos);
					dim.setTimeinfo(result);
					flag = 1;
				}
				else{
					flag = 0;
				}
			}
			else{
				dim.setOnline(DeviceItemModel.Online_Off);
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result == 0){
				Toast.makeText(SocketDetail.this, R.string.timeout, Toast.LENGTH_SHORT).show();
			}
			else{
				//dim.setTimeinfo(timeinfo)
				InitDevice(dim);
			}
			refresh.setVisibility(View.VISIBLE);
			bar.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			refresh.setVisibility(View.GONE);
			bar.setVisibility(View.VISIBLE);
		}
		
		
		
	}
	
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.socketdetail);
		switchApplication app = (switchApplication)getApplication();
		dim = app.getItemModel();
		initDevList();
		findView();
		InitView();
		localsim.setIpaddress(GobalDef.LOCAL_URL);
		localsim.setPort(GobalDef.LOCAL_PORT);
		//tasklist.addHeaderView(v)
		sda = new SocketDetailAdapter(this,configinfos);
		tasklist.setAdapter(sda);
		
		InitDevice(dim);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		switchApplication app = (switchApplication)getApplication();
		dim = app.getItemModel();
		Log.d(TAG, "time info is: " + dim.getTimeinfo());
		updateInfo();
		initDevList();
		//configinfos.add(new ConfigInfo());
		//configinfos.add(new ConfigInfo());
		sda.setConfigInfo(configinfos);
		sda.notifyDataSetChanged();
		tip.setText(getNextString());
		
	}
	
	
	
	public void updateInfo(){
		name.setText(dim.getDeviceName());
		String dev_icon = dim.getDeviceIcon();
		if(dev_icon == null || dev_icon == ""){
			dev_icon = "p1";
		}
		if(dev_icon.startsWith("dev_")){
			try{
				Bitmap tbmp = BitmapFactory.decodeFile(GobalDef.Instance.getCachePath() + "/" + dim.getDeviceIcon() + ".jpg");
				socketimg.setImageBitmap(tbmp);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		else{
			if(dev_icon.equals("p1")){
				socketimg.setImageResource(R.drawable.p1);
			}
			else if(dev_icon.equals("p2")){
				socketimg.setImageResource(R.drawable.p2);
			}
			else if(dev_icon.equals("p3")){
				socketimg.setImageResource(R.drawable.p3);
			}
			else if(dev_icon.equals("p4")){
				socketimg.setImageResource(R.drawable.p4);
			}
			else if(dev_icon.equals("p5")){
				socketimg.setImageResource(R.drawable.p5);
			}
			else if(dev_icon.equals("p6")){
				socketimg.setImageResource(R.drawable.p6);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
	public void OnItemClick(int pos){
		//*
		Log.d(TAG, "the itemindex is: " + pos);
		if(mScrollBusy == false){
			Intent intent = new Intent();
			/*
			if(pos == 0){
				if(configinfos.size() >= 7){
					Toast.makeText(SocketDetail.this, R.string.renwu_socket_dtail_maxtime, Toast.LENGTH_SHORT).show();
					return;
				}
			}
			*/
			pos = pos + 1;
			intent.putExtra(GobalDef.INTENT_TIME_INDEX, pos);
			intent.setClass(SocketDetail.this, ScoketTaskEditActivity.class);
			startActivity(intent);		
		}
		//*/
	}
	
	public void OnRemoveItemClick(int pos){
		DeleteTask dt= new DeleteTask();
		dt.execute(new Integer[]{pos});
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
			
			String result = getString(R.string.renwu_config_delay_delay);
			result = result + hour + getString(R.string.renwu_config_delay_hour) + min + getString(R.string.renwu_config_delay_min);
			if(dim.getOnoff() == DeviceItemModel.OnOff_Off){
				result = result + getString(R.string.renwu_config_delay_open);
			}
			else{
				result = result + getString(R.string.renwu_config_delay_close);
			}
			
			SendDelayTask sdt = new SendDelayTask();
			int temphour = Integer.parseInt(hour);
			temphour = temphour * 60;
			temphour = temphour + Integer.parseInt(min);
			sdt.execute(new Integer[]{temphour});
			delayInfo.setText(result);
		}
	}

	

	
}
