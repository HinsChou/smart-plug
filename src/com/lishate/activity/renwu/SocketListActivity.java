package com.lishate.activity.renwu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.lishate.MainActivity;
import com.lishate.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.lishate.activity.FirstActivity;
import com.lishate.adapter.SocketListAdapter;
import com.lishate.application.switchApplication;
import com.lishate.data.GobalDef;
import com.lishate.data.LocalIpInfo;
import com.lishate.data.SharedPreferencesUtility;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.ServerItemModel;
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
import com.lishate.message.baseMessage;
import com.lishate.message.baseRspMessage;
import com.lishate.net.UdpProcess;
import com.lishate.utility.Utility;
import com.zxs.ptrmenulistview.PullToRefreshLayout;
import com.zxs.ptrmenulistview.PullToRefreshLayout.OnRefreshListener;
import com.zxs.ptrmenulistview.SwipeMenu;
import com.zxs.ptrmenulistview.SwipeMenuCreator;
import com.zxs.ptrmenulistview.SwipeMenuItem;
import com.zxs.ptrmenulistview.SwipeMenuListView;
import com.zxs.ptrmenulistview.SwipeMenuListView.OnMenuItemClickListener;

public class SocketListActivity extends FirstActivity {
	
	private static final String TAG = "SocketListActivity";
	private ProgressBar mProgressBar;
	private SwipeMenuListView mSocketList;
	public TextView mNetworkView;
	private SocketListAdapter mAdapter;
	private ImageButton mRefreshButton;
	private Button mCloseButton;
	public RefreshHandler handler;
	private Thread refreshTrhead;
	private boolean refreshrun = true;
	private static final int typeOpen = 0;
	private static final int typeClose = 1;
	public int typeIsOpen = 0;
	
	private static final int dialog_type_alert = 1;
	private static final int dialog_type_process = 2;
	
	private final int dialogType = dialog_type_process;
	
	private static final int update_ui_message = 3;
	
	private List<DeviceItemModel> devlist = new ArrayList<DeviceItemModel>();
	
	private boolean mScrollBusy = false;
	
	private ImageButton addplug_more;
	private PopupWindow popup;
	private PullToRefreshLayout mRefreshView;
	private PopupWindow popupseticon;
	private String dev_pic = "";
	public final static int capture =1;
	private static final int zoom = 2;
	private static final int code = 3;
	private Bitmap bmp = null;
	private String orginalUriStr = null;
	private Handler myhander;
	private Dao<DeviceItemModel, Integer> devicedatadao = null;
	
	private List<ConfigInfo> configinfos = new ArrayList<ConfigInfo>();
	private boolean getconfig_suc = false;
	//public ArrayList<ConfigInfo> configinfos = new ArrayList<ConfigInfo>();
	private ConfigInfo current_ci = null;
	
	private List<DeviceItemModel> initDevList(){
		List<DeviceItemModel> result = new ArrayList<DeviceItemModel>();
		try {
			
			//DeviceItemDao did = new DeviceItemDao(getHelper());
			result.clear();
			
			DeviceItemModel item = new DeviceItemModel();
			for (int i = 0; i < 8; i++) {
				result.add(item);
			}
			result.addAll(devicedatadao.queryForAll());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Log.i(TAG, "获得的名字是: " + result.get(0).getDeviceName());
		devlist = result;
		return result;
	}
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		//Log.d(TAG, "onKeyDown");
//		//return super.onKeyDown(keyCode, event);
//		finish();
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		Log.d(TAG, "dispatchKeyEvent");
		return super.dispatchKeyEvent(event);
	}

	private void findView(){
		mProgressBar = (ProgressBar)findViewById(R.id.socketlist_refresjing);
		mRefreshButton = (ImageButton)findViewById(R.id.socketlist_allopen);
		mCloseButton = (Button)findViewById(R.id.socketlist_allclose);
		addplug_more = (ImageButton)findViewById(R.id.socketlist_refresh);
		mSocketList = (SwipeMenuListView)findViewById(R.id.socketlist_listview);
		mNetworkView = (TextView)findViewById(R.id.socketlist_net_check);
		mRefreshView = (PullToRefreshLayout)findViewById(R.id.refresh_view);
		
	}
	
	private void initView(){
		handler = new RefreshHandler();
		try {
			devicedatadao = getHelper().getDao(DeviceItemModel.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		addplug_more.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popup.showAsDropDown(v);
				//popup.setOutsideTouchable(true);
			}
		});
		
		//帮助页面
		ImageView ivHelp = (ImageView) findViewById(R.id.ivHelp);
		ivHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent mintent = new Intent(getApplicationContext(), SpecificationAvtivity.class);
				startActivity(mintent);
			}
		});
		
		//下拉刷新
		mRefreshView.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				
				if(Utility.CheckNetwork(SocketListActivity.this) == true){
					mNetworkView.setVisibility(View.GONE);
					new RefreshTask().execute(new Void[0]);
//					UpdateLocalInfo();
				}
				else{
					mNetworkView.setVisibility(View.VISIBLE);
					Toast.makeText(SocketListActivity.this, R.string.unNetwork, Toast.LENGTH_SHORT).show();
				}
				
				new Handler(){
					@Override
					public void handleMessage(Message msg)
					{
						// 千万别忘了告诉控件刷新完毕了哦！
						mRefreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
						mNetworkView.setVisibility(View.GONE);
					}
				}.sendEmptyMessageDelayed(0, 3000);
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				
				new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						// 千万别忘了告诉控件加载完毕了哦！
						mRefreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					}
				}.sendEmptyMessageDelayed(0, 3000);
			}
			
		});
		
		
		//侧滑删除
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));
				// set item width
				deleteItem.setWidth(mSocketList.dp2px(72));
				// set a icon
				deleteItem.setIcon(R.drawable.device_del);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		mSocketList.setMenuCreator(creator);
		
		mSocketList.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		    @Override
		    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		        switch (index) {
		        case 0:
		            // delete
		        	final int pos = position;
		        	new AlertDialog.Builder(SocketListActivity.this, AlertDialog.THEME_HOLO_LIGHT)
		        		.setMessage(getResources().getString(R.string.wangt_del_device))
		        		.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
								DeviceItemModel dim = (DeviceItemModel)mAdapter.getItem(pos);
					        	SocketListActivity.this.OnRemoveItem(dim);
							}
						}).setNegativeButton(getString(R.string.no), new AlertDialog.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
								arg0.dismiss();
							}
						}).show();
		        }
		        // false : close the menu; true : not close the menu
		        return false;
		    }
		});
		
		
		mSocketList.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				
				Log.d(TAG, "socket scrolllistener");
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
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
				Log.d(TAG, "socket scrolllistener change， scrollState " + scrollState);
			}
			
		});
		
		mCloseButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if(Utility.CheckNetwork(SocketListActivity.this) == true){
					mNetworkView.setVisibility(View.GONE);
					//new RefreshTask().execute(new Void[0]);
					OpenCloseTask oat = new OpenCloseTask();
					oat.IsClose = 1;
					oat.execute(new DeviceItemModel[0]);
				}
				else{
					mNetworkView.setVisibility(View.VISIBLE);
					Toast.makeText(SocketListActivity.this, R.string.unNetwork, Toast.LENGTH_SHORT);
				}
			}
		});
		
		LinearLayout llDeviceNew = (LinearLayout) findViewById(R.id.llDeviceNew);
		llDeviceNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent mintent = new Intent(getApplicationContext(), MainActivity.class);
//				startActivityForResult(mintent, GobalDef.REQEST_SMART_CONFIG);
				startActivity(mintent);
			}
		});
		
		// 装载R.layout.popup对应的界面布局 用来选择增加设备或者更多功能
		View root = this.getLayoutInflater().inflate(R.layout.popup, null);
		// 创建PopupWindow对象
		popup = new PopupWindow(root, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//popup.setFocusable(false);
		popup.setOutsideTouchable(true);
		popup.setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));
		
		//一键配置
		root.findViewById(R.id.morefunc).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				// 关闭PopupWindow
				popup.dismiss(); //①
				Intent mintent = new Intent(getApplicationContext(), SocketNewConfigActivity.class);
				startActivityForResult(mintent, GobalDef.REQEST_SMART_CONFIG);
			}
		});
		
		//ap配置
		root.findViewById(R.id.adddevice).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				// 通过ap模式添加插座设备
				popup.dismiss(); //①
				Intent mintent = new Intent(getApplicationContext(), AddPlugActivity.class);
				startActivityForResult(mintent, GobalDef.REQEST_AP_CONFIG);
				
			}
		});
		
		//帮助
		root.findViewById(R.id.specif).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				popup.dismiss(); //①
				Intent mintent = new Intent(getApplicationContext(), SpecificationAvtivity.class);
				startActivity(mintent);
			}
		});
		
		// 装载R.layout.popup对应的界面布局 用来为设备选择图片
		View rooticon = this.getLayoutInflater().inflate(R.layout.popupicon, null);
		// 创建PopupWindow对象
		popupseticon = new PopupWindow(rooticon, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//popupseticon.setFocusable(false);
		popupseticon.setOutsideTouchable(true);
		popupseticon.setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));
		
		//相机选择
		rooticon.findViewById(R.id.morefunc).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				// 关闭PopupWindow
				//Log.i(TAG, "photoshop is starting...");
				popupseticon.dismiss(); //①
				Date now = new Date();
            	dev_pic = "device_" + now.getTime() + ".png";
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);  
                getImage.addCategory(Intent.CATEGORY_OPENABLE);  
                getImage.setType("image/jpeg");  
                startActivityForResult(getImage, code);  
			}
		});
		
		//相册拍照
		rooticon.findViewById(R.id.adddevice).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				// 添加插座设备
				popupseticon.dismiss(); //①
				Log.i(TAG, "photoshop is starting...");
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            	Date now = new Date();
            	dev_pic = "device_" + now.getTime() + ".png";
            	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), dev_pic)));
				startActivityForResult(intent, capture);
				
			}
		});	
	}
	
	class OpenCloseTask extends AsyncTask<DeviceItemModel, Integer, String>{

		private ProgressDialog progressDialog;
		public int IsClose = 1;
		private int fail = 0;
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			fail = 0;
			progressDialog = new ProgressDialog(SocketListActivity.this);
			if(IsClose == 1){
				progressDialog.setMessage(SocketListActivity.this.getString(R.string.renwu_socket_close));
			}
			else if(IsClose == 0){
				progressDialog.setMessage(SocketListActivity.this.getString(R.string.renwu_socket_open));
			}
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			Log.d(TAG, "OpenTask preexecute");
		}

		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			mAdapter.notifyDataSetChanged();
			progressDialog.dismiss();
			if(fail == 1){
				Toast.makeText(SocketListActivity.this, R.string.renwu_socketlist_send_fail, Toast.LENGTH_SHORT).show();
			}
		}

		private void UpdateMessage(baseRspMessage rspmsg){
			for(int i= 0; i<devlist.size(); i++){
				DeviceItemModel dim = devlist.get(i);
				if(rspmsg.getFromId() == dim.getDeviceId()){
					//dim.setDeviceStatus(GobalDef.DEVICE_STATUS_OPEN);
					dim.setOnline(DeviceItemModel.Online_On);
					if(IsClose == 0){
						dim.setOnoff(DeviceItemModel.OnOff_On);
					}else if(IsClose == 1){
						dim.setOnoff(DeviceItemModel.OnOff_Off);
					}
				}
			}
		}

		@Override
		protected String doInBackground(DeviceItemModel... params) {
			
			baseMessage orm = null;
			if(IsClose == 0){
				orm = new OpenReqMessage();
			}else if(IsClose == 1){
				orm = new CloseReqMessage();
			}
			else{
				orm = new OpenReqMessage();
			}
			ServerItemModel sim = new ServerItemModel();
			ServerItemModel localsim = new ServerItemModel();
			localsim.setPort(GobalDef.LOCAL_PORT);
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			orm.Direct = MessageDef.BASE_MSG_FT_REQ;
			orm.setFromId(GobalDef.MOBILEID);
			if(IsClose == 0){
				orm.MsgType = MessageDef.MESSAGE_TYPE_OPEN_REQ;
			}
			else if(IsClose == 1){
				orm.MsgType = MessageDef.MESSAGE_TYPE_CLOSE_REQ;
			}
			orm.Seq = MessageSeqFactory.GetNextSeq();
			orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			orm.ToType = MessageDef.BASE_MSG_FT_HUB;
			Log.d(TAG, "orm to type is: " + orm.ToType);
			
			DatagramSocket ds ;//= new DatagramSocket();
			try {
				ds = new DatagramSocket();
				ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
			} catch (SocketException e) {
				
				fail = 1;
				e.printStackTrace();
				return null;
			}
			
			for(int i=0; i<devlist.size(); i++){
				DeviceItemModel dim = devlist.get(i);
				orm.setToId(dim.getDeviceId());
				if(dim.getLocalIp() != null && dim.getLocalIp().length() > 0){
					localsim.setIpaddress(dim.getLocalIp());
					UdpProcess.SendMsg(ds, orm, localsim);
				}
				UdpProcess.SendMsg(ds, orm, sim);
				Log.d(TAG, "send id " + dim.getDeviceId() + " msg");
			}
			while(true){
				baseMessage bm = UdpProcess.RecvMsg(ds);
				if(bm != null){
					if(IsClose == 0){
						if(bm.MsgType == MessageDef.MESSAGE_TYPE_OPEN_RSP){
							OpenRspMessage orspm = (OpenRspMessage)bm;
							UpdateMessage(orspm);
						}
					}
					else if(IsClose == 1){
						if(bm.MsgType == MessageDef.MESSAGE_TYPE_CLOSE_RSP){
							CloseRspMessage orspm = (CloseRspMessage)bm;
							UpdateMessage(orspm);
						}
					}
				}
				else{
					break;
				}
			}
			//fail = 0;
			return "";
		}
		
	}
	
	class OpenTask extends AsyncTask<DeviceItemModel, Integer, String>{
		
		

		private ProgressDialog progressDialog;
		
		public int IsOpen = 0;
		private int fail = 0;
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			fail = 0;
			progressDialog = new ProgressDialog(SocketListActivity.this);
			progressDialog.setMessage(SocketListActivity.this.getString(R.string.renwu_socket_open));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			Log.d(TAG, "OpenTask preexecute");
		}
		
		


		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			mAdapter.notifyDataSetChanged();
			progressDialog.dismiss();
			if(fail == 1){
				Toast.makeText(SocketListActivity.this, R.string.renwu_socketlist_send_fail, Toast.LENGTH_SHORT);
			}
		}

		private void UpdateMessage(OpenRspMessage rspmsg){
			for(int i= 0; i<devlist.size(); i++){
				DeviceItemModel dim = devlist.get(i);
				if(rspmsg.getFromId() == dim.getDeviceId()){
					//dim.setDeviceStatus(GobalDef.DEVICE_STATUS_OPEN);
					dim.setOnline(DeviceItemModel.Online_On);
					dim.setOnoff(DeviceItemModel.OnOff_On);
				}
			}
		}

		@Override
		protected String doInBackground(DeviceItemModel... params) {
			
			OpenReqMessage orm = new OpenReqMessage();
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			orm.Direct = MessageDef.BASE_MSG_FT_REQ;
			orm.setFromId(GobalDef.MOBILEID);
			orm.MsgType = MessageDef.MESSAGE_TYPE_OPEN_REQ;
			orm.Seq = MessageSeqFactory.GetNextSeq();
			orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			orm.ToType = MessageDef.BASE_MSG_FT_END;
			
			DatagramSocket ds ;//= new DatagramSocket();
			try {
				ds = new DatagramSocket();
				ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
			} catch (SocketException e) {
				
				fail = 1;
				e.printStackTrace();
				return null;
			}
			
			for(int i=0; i<devlist.size(); i++){
				DeviceItemModel dim = devlist.get(i);
				orm.setToId(dim.getDeviceId());
				UdpProcess.SendMsg(ds, orm, sim);
				Log.d(TAG, "send id " + dim.getDeviceId() + " msg");
			}
			while(true){
				baseMessage bm = UdpProcess.RecvMsg(ds);
				if(bm != null){
					if(bm.MsgType == MessageDef.MESSAGE_TYPE_OPEN_RSP){
						OpenRspMessage orspm = (OpenRspMessage)bm;
						UpdateMessage(orspm);
					}
				}
				else{
					break;
				}
			}
			//fail = 0;
			return "";
		}
		
	}
	
	class OpenSingleTask extends AsyncTask<DeviceItemModel, Integer, Integer>{

		private ProgressDialog progressDialog;
		private DeviceItemModel dim;
		private Vibrator mVibrator;
		@Override
		protected Integer doInBackground(DeviceItemModel... params) {
			
			dim = params[0];
			baseMessage orm = null;
			baseMessage msg = null;
			if(dim.getOnoff() == DeviceItemModel.OnOff_Off){
				orm = new OpenReqMessage();
			}
			else{
				orm = new CloseReqMessage();
			}
			ServerItemModel sim = new ServerItemModel();
			ServerItemModel localsim = new ServerItemModel();
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
			
			if(dim.getLocalIp() != null && dim.getLocalIp().length() > 0){
				localsim.setIpaddress(dim.getLocalIp());
				localsim.setPort(GobalDef.LOCAL_PORT);
				msg = UdpProcess.GetMsgReturn(orm, localsim);
				
			}
			
			if(msg == null){
				msg = UdpProcess.GetMsgReturn(orm, sim);
			}
			
			if(msg != null){
				try{
					if(dim.getOnoff() == DeviceItemModel.OnOff_Off){
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
			
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result == 0){
				Toast.makeText(SocketListActivity.this, R.string.renwu_operation_fail, Toast.LENGTH_SHORT).show();
			}
			else{
				dim.setOnline(DeviceItemModel.Online_On);
				if(dim.getOnoff() == DeviceItemModel.OnOff_Off){
					dim.setOnoff(DeviceItemModel.OnOff_On);
				}
				else{
					dim.setOnoff(DeviceItemModel.OnOff_Off);
				}
				for(int i = 0; i<devlist.size(); i++){
					Log.d(TAG, "dim id " + devlist.get(i).getDeviceId() + " onoff " + devlist.get(i).getOnoff());
					
				}
				mAdapter.notifyDataSetChanged();
			}
		}
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			if (SharedPreferencesUtility.getVibrate() == true)
	         {
	            this.mVibrator = ((Vibrator)SocketListActivity.this.getSystemService(Service.VIBRATOR_SERVICE));
	            this.mVibrator.vibrate(50L);
	         }
			progressDialog = new ProgressDialog(SocketListActivity.this);
			progressDialog.setMessage(SocketListActivity.this.getString(R.string.loging));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		
	}
	
	class LoginToTask extends AsyncTask<DeviceItemModel, Integer, String>{

		private ProgressDialog progressDialog;
		private DeviceItemModel dim;
		@Override
		protected String doInBackground(DeviceItemModel... params) {
			
			String result = null;
			baseMessage bm = null;
			dim = params[0];
			//LoginReqMessage lrm = new LoginReqMessage();
			GetServerConfigReqMessage lrm = new GetServerConfigReqMessage();
			lrm.Direct = MessageDef.BASE_MSG_FT_REQ;
			lrm.setFromId(GobalDef.MOBILEID);
			lrm.setToId(dim.getDeviceId());
			Log.d(TAG, "to hid is: " + lrm.ToHID);
			Log.d(TAG, "to lid is: " + lrm.ToLID);
			Log.d(TAG, "dim deviceid is: " + dim.getDeviceId());
			lrm.MsgType = MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ;
			lrm.Seq = MessageSeqFactory.GetNextSeq();
			lrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			lrm.ToType = MessageDef.BASE_MSG_FT_HUB;
			
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			bm = UdpProcess.GetMsgReturn(lrm, sim);
			if(bm != null){
				GetServerConfigRspMessage gscrsp = (GetServerConfigRspMessage)bm;
				gscrsp.Content2Array();
				//这里取出时间信息从主机设备去获取。
				result = Utility.getConfigStringInfo(gscrsp.configinfos);
				dim.setTimeinfo(result);
				
			}
			else{
				result = null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result == null){//获取定时任务表失败
				Toast.makeText(SocketListActivity.this, R.string.timeout, Toast.LENGTH_SHORT).show();
				
//				switchApplication app = (switchApplication)getApplication();
//				app.SetDeviceItemModel(dim);
//				GobalDef.Instance.setDeviceItemModel(dim);
//				//dim = app.getItemModel();
//				Intent intent = new Intent();
//				intent.setClass(SocketListActivity.this, SocketInfoActivity.class);
//				SocketListActivity.this.startActivity(intent);
			}
			else
			{//获取定时任务表成功
				switchApplication app = (switchApplication)getApplication();
				app.SetDeviceItemModel(dim);
				GobalDef.Instance.setDeviceItemModel(dim);
				Intent intent = new Intent();
				intent.setClass(SocketListActivity.this, SocketTimerDetail.class);
				startActivity(intent);
			}
			//
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			progressDialog = new ProgressDialog(SocketListActivity.this);
			progressDialog.setMessage(SocketListActivity.this.getString(R.string.loging));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
	}
	
	class GetConfigsToTask extends AsyncTask<DeviceItemModel, Integer, String>{

		private DeviceItemModel dim;
		@Override
		protected String doInBackground(DeviceItemModel... params) {
			
			String result = null;
			baseMessage bm = null;
			dim = params[0];
			//LoginReqMessage lrm = new LoginReqMessage();
			GetServerConfigReqMessage lrm = new GetServerConfigReqMessage();
			lrm.Direct = MessageDef.BASE_MSG_FT_REQ;
			lrm.setFromId(GobalDef.MOBILEID);
			lrm.setToId(dim.getDeviceId());
			lrm.MsgType = MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ;
			lrm.Seq = MessageSeqFactory.GetNextSeq();
			lrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			lrm.ToType = MessageDef.BASE_MSG_FT_HUB;
			
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			bm = UdpProcess.GetMsgReturn(lrm, sim);
			if(bm != null){
				GetServerConfigRspMessage gscrsp = (GetServerConfigRspMessage)bm;
				gscrsp.Content2Array();
				//这里取出时间信息从主机设备去获取。
				result = Utility.getConfigStringInfo(gscrsp.configinfos);
				dim.setTimeinfo(result);
				ConfigInfo cfinfo = new ConfigInfo();
				gscrsp.configinfos.get(0).CopyTo(cfinfo);
				configinfos.add(cfinfo);
			}
			else{
				result = null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			//progressDialog.dismiss();
			if(null != result){
				dim.setOnline(DeviceItemModel.Online_On);
			}else
				dim.setOnline(DeviceItemModel.Online_Off);
//			try {
//				devicedatadao.update(dim);
//			} catch (SQLException e) {
//				
//				e.printStackTrace();
//			}
			getconfig_suc = true;
			//
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			getconfig_suc = false;
//			progressDialog = new ProgressDialog(SocketListActivity.this);
//			progressDialog.setMessage(SocketListActivity.this.getString(R.string.loging));
//			progressDialog.setCanceledOnTouchOutside(false);
//			progressDialog.show();
		}
	}
	

	class RefreshHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			if(Utility.CheckNetwork(SocketListActivity.this) == false){
				SocketListActivity.this.mNetworkView.setVisibility(View.GONE);
			}
			else
			{
				SocketListActivity.this.mNetworkView.setVisibility(View.GONE);
			}
			switch(msg.what){
			case update_ui_message:
				mAdapter.SetList(devlist);
				mAdapter.notifyDataSetChanged();
				break;
			}
			
		}
		
	}
	
	public baseMessage RecvMsg(DatagramSocket ds,LocalIpInfo ip){
		DatagramPacket packet = UdpProcess.Recv(ds);
		baseMessage bm = null;
		if(packet != null){
			/*
			if(Encryption.checkIsMsg(packet.getData()) == 1){
				bm = Encryption.Decryption(packet.getData());
			}
			*/
			
			bm = Encryption.GetMsg(packet.getData());
			if(bm != null){
				if(packet.getAddress() != null){
					ip.ip = packet.getAddress().toString();
					if(ip.ip.startsWith("/")){
						ip.ip = ip.ip.substring(1);
					}
					Log.d(TAG, "recvmsg ip:" + ip.ip);
				}
			}
		}
		return bm;
	}
	

	
	private void UpdateMessage(GetStatueRspMessage rspmsg){
		for(int i= 0; i<devlist.size(); i++){
			DeviceItemModel dim = devlist.get(i);
			if(rspmsg.getFromId() == dim.getDeviceId()){
				//dim.setDeviceStatus(GobalDef.DEVICE_STATUS_OPEN);
				dim.setOnline(DeviceItemModel.Online_On);
				if(rspmsg.getOpenClose() == 1){
					dim.setOnoff(DeviceItemModel.OnOff_On);
					Log.d(TAG, "rspmsg openclose true");
				}
				else{
					dim.setOnoff(DeviceItemModel.Online_Off);
					Log.d(TAG, "rspmsg openclose false");
				}
				
			}
		}
	}
	
	private void UpdateLocalMessage(GetStatueRspMessage rspmsg, LocalIpInfo ip){
		for(int i= 0; i < devlist.size(); i++){
			DeviceItemModel dim = devlist.get(i);
			String result = null;
			baseMessage bm = null;
			try {
				if(rspmsg.getFromId() == dim.getDeviceId()){
					//dim.setDeviceStatus(GobalDef.DEVICE_STATUS_OPEN);
					Log.e(TAG, "设备处于在线状态，哈哈");
					GobalDef.Instance.getLocalList().putLocalInfo(dim.getDeviceId(), ip.ip);
					if(dim.getOnline() != DeviceItemModel.Online_On){
					}
					dim.setOnline(DeviceItemModel.Online_On);
					if(rspmsg.getOpenClose() == 1){
						if(dim.getOnoff() != DeviceItemModel.OnOff_On){
						}
						dim.setOnoff(DeviceItemModel.OnOff_On);
						Log.d(TAG, "rspmsg openclose true");
					}
					else{
						if(dim.getOnoff() != DeviceItemModel.OnOff_Off){
						}
						dim.setOnoff(DeviceItemModel.OnOff_Off);
						Log.d(TAG, "rspmsg openclose false");
					}
					Log.d(TAG, "ip is: " + ip.ip);
					
					dim.setLocalIp(ip.ip);
					
					GetServerConfigReqMessage lrm = new GetServerConfigReqMessage();
					lrm.Direct = MessageDef.BASE_MSG_FT_REQ;
					lrm.setFromId(GobalDef.MOBILEID);
					lrm.setToId(dim.getDeviceId());
					Log.d(TAG, "to hid is: " + lrm.ToHID);
					Log.d(TAG, "to lid is: " + lrm.ToLID);
					Log.d(TAG, "dim deviceid is: " + dim.getDeviceId());
					lrm.MsgType = MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ;
					lrm.Seq = MessageSeqFactory.GetNextSeq();
					lrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
					lrm.ToType = MessageDef.BASE_MSG_FT_HUB;
					
					ServerItemModel sim = new ServerItemModel();
					sim.setIpaddress(GobalDef.SERVER_URL);
					sim.setPort(GobalDef.SERVER_PORT);
					bm = UdpProcess.GetMsgReturn(lrm, sim);
					if(bm != null){
						GetServerConfigRspMessage gscrsp = (GetServerConfigRspMessage)bm;
						gscrsp.Content2Array();
						//这里取出时间信息从主机设备去获取。
						result = Utility.getConfigStringInfo(gscrsp.configinfos);
						dim.setTimeinfo(result);
						if(gscrsp.configinfos.size() >0)
						{
							ConfigInfo ci = gscrsp.configinfos.get(0);
							if(ci.isenable)
							{
								if(dim.getSettime() != DeviceItemModel.SetTime_On){
								}
								dim.setSettime(DeviceItemModel.SetTime_On);
							}
							else
							{
								if(dim.getSettime() != DeviceItemModel.SetTime_Off){
								}
								dim.setSettime(DeviceItemModel.SetTime_Off);
							}
						}
						
					}
					
	//			if(true == isUpdate){
	//				try {
	//					devicedatadao.update(dim);
	//				} catch (SQLException e) {
	//					
	//					e.printStackTrace();
	//				}
	//			}	
					
				}
//				else{
//					
//				}
			} catch (Exception e) {
				Log.e(TAG, "设备处于离线状态，哈哈");
				dim.setOnline(DeviceItemModel.Online_Off);	
			}
		}
		Message msg = handler.obtainMessage();
		msg.what = update_ui_message;
		handler.sendMessage(msg);
		//mAdapter.SetList(devlist);
		//mAdapter.notifyDataSetChanged();
	}
	
	private void RefreshAllDevice(){
		Log.d(TAG, "refresh do background");
		GetStatueReqMessage orm = new GetStatueReqMessage();
		ServerItemModel sim = new ServerItemModel();
		sim.setIpaddress(GobalDef.SERVER_URL);
		sim.setPort(GobalDef.SERVER_PORT);
		
		//ServerItemModel localsim = new ServerItemModel();
		orm.Direct = MessageDef.BASE_MSG_FT_REQ;
		orm.setFromId(GobalDef.MOBILEID);
		
		orm.Seq = MessageSeqFactory.GetNextSeq();
		orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
		orm.ToType = MessageDef.BASE_MSG_FT_HUB;
		
		Log.d(TAG, "orm seq " + orm.Seq + " orm " + orm.FromType + " orm to type " + orm.ToType);
		
		//while(true){
		
		DatagramSocket ds = null ;//= new DatagramSocket();
		try {
			ds = new DatagramSocket();
			ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
		} catch (SocketException e) {
			
			e.printStackTrace();
		}
		
			try{
				for(int i=0; i<devlist.size(); i++){
					DeviceItemModel dim = devlist.get(i);
					orm.setToId(dim.getDeviceId());
					UdpProcess.SendMsg(ds, orm, sim);
					Log.d(TAG, "send id " + dim.getDeviceId() + " msg");
				}
				
				while(true){
					baseMessage bm = UdpProcess.RecvMsg(ds);
					if(bm != null){
						if(bm.MsgType == MessageDef.MESSAGE_TYPE_GET_STATUS_RSP){
							GetStatueRspMessage gsrm = (GetStatueRspMessage)bm;
							UpdateMessage(gsrm);
						}
					}
					else{
						break;
					}
				}
				
				//Thread.sleep(1000);
			}
			catch(Exception e){
				if(e.getMessage() != null){
					Log.d(TAG, e.getMessage());
				}
				e.printStackTrace();
			}
		
	}
	
	class RefreshAllThread extends Thread{

		@Override
		public void run() {
			
			super.run();
			while(true)
			{
				try{
					if(Utility.CheckNetwork(SocketListActivity.this) == true && refreshrun){
						//RefreshAllDevice();
						//mAdapter.notifyDataSetChanged();
						Log.i(TAG, "update local info");
						UpdateLocalInfo();
						//new RefreshTask().execute(new Void[0]);
					}
					//Message msg = Message.obtain();
					//SocketListActivity.this.handler.sendMessage(msg);
					
				}
				catch(Exception e){
					if(e.getMessage() != null){
						Log.d(TAG, e.getMessage());
					}
					e.printStackTrace();
				}finally {
					try {
						sleep(3000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	class RefreshTask extends AsyncTask<Void, Void, Void>{
		
		private List<baseMessage> al = new ArrayList<baseMessage>();
		
		private void UpdateMessage(GetStatueRspMessage rspmsg){
			for(int i= 0; i<devlist.size(); i++){
				DeviceItemModel dim = devlist.get(i);
				if(rspmsg.getFromId() == dim.getDeviceId()){
					//dim.setDeviceStatus(GobalDef.DEVICE_STATUS_OPEN);
					dim.setOnline(DeviceItemModel.Online_On);
					if(rspmsg.getOpenClose() == DeviceItemModel.OnOff_On){
						dim.setOnoff(DeviceItemModel.OnOff_On);
						Log.e(TAG, "rspmsg.getFromId()"+ rspmsg.getFromId() + "rspmsg openclose true");
						
					}
					else{
						dim.setOnoff(DeviceItemModel.OnOff_Off);
						Log.e(TAG, "rspmsg.getFromId()"+ rspmsg.getFromId() + "rspmsg openclose false");
					}
				}else{
					dim.setOnline(DeviceItemModel.Online_Off);
				}
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			
			
			//Encryption.EncriptionGetServerConfigsReq(null);
			//Log.e(TAG, "refresh do background");
			Void v = null;
			GetStatueReqMessage orm = new GetStatueReqMessage();
			ServerItemModel sim = new ServerItemModel();
			ServerItemModel localsim = new ServerItemModel();
			localsim.setPort(GobalDef.LOCAL_PORT);
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			orm.Direct = MessageDef.BASE_MSG_FT_REQ;
			orm.setFromId(GobalDef.MOBILEID);
			
			orm.Seq = MessageSeqFactory.GetNextSeq();
			orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			orm.ToType = MessageDef.BASE_MSG_FT_HUB;
			
			Log.d(TAG, "orm seq " + orm.Seq + " orm fromtype " + orm.FromType + " orm to type " + orm.ToType);
			
			DatagramSocket ds ;//= new DatagramSocket();
			try {
				ds = new DatagramSocket();
				ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
			} catch (SocketException e) {
				
				e.printStackTrace();
				return null;
			}
			
			for(int i=0; i<devlist.size(); i++){
				DeviceItemModel dim = devlist.get(i);
				orm.setToId(dim.getDeviceId());
				Log.d(TAG, "to hid is: " + orm.ToHID);
				Log.d(TAG, "to lid is: " + orm.ToLID);
				UdpProcess.SendMsg(ds, orm, sim);
				//Log.e(TAG, "send id " + dim.getDeviceId() + " msg");
				if(dim.getLocalIp() != null && dim.getLocalIp().length() > 0){
					localsim.setIpaddress(dim.getLocalIp());
					UdpProcess.SendMsg(ds, orm, localsim);
				}
			}
			
			al.clear();
			while(true){
				baseMessage bm = UdpProcess.RecvMsg(ds);
				if(bm != null){
					if(bm.MsgType == MessageDef.MESSAGE_TYPE_GET_STATUS_RSP){
						GetStatueRspMessage gsrm = (GetStatueRspMessage)bm;
						UpdateMessage(gsrm);
						al.add(bm);
					}
				}
				else{
					break;
				}
			}
			for(DeviceItemModel dim: devlist){
				boolean online = false;
				for(baseMessage tempbm : al){
					if(dim.getDeviceId() == tempbm.getFromId()){
						online = true;
						break;
					}
				}
				if(online == true){
					Log.d(TAG, "dim set online on");
					dim.setOnline(DeviceItemModel.Online_On);
				}
				else{
					Log.d(TAG, "dim set online 0ff");
					dim.setOnline(DeviceItemModel.Online_Off);
				}
			}
			return v;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
			Log.e(TAG, "on refersh post exec");
			//*
			//mAdapter.SetList(devlist);
			//mAdapter.notifyDataSetChanged();
			
			Message msg = handler.obtainMessage();
			msg.what = update_ui_message;
			handler.sendMessage(msg);
			
			//*/
			//SocketListActivity.this.setDataSource();
			mRefreshButton.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			//SocketListActivity.this.invalidate();
		}

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			mRefreshButton.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void UpdateLocalInfo(){
		
		GetStatueReqMessage orm = new GetStatueReqMessage();
		ServerItemModel sim = new ServerItemModel();
		sim.setIpaddress(GobalDef.LOCAL_URL);
		sim.setPort(GobalDef.LOCAL_PORT);
		orm.Direct = MessageDef.BASE_MSG_FT_REQ;
		orm.setFromId(GobalDef.MOBILEID);
		
		orm.Seq = MessageSeqFactory.GetNextSeq();
		orm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
		orm.ToType = MessageDef.BASE_MSG_FT_HUB;
		
		DatagramSocket ds ;//= new DatagramSocket();
		try {
			ds = new DatagramSocket();
			ds.setSoTimeout(GobalDef.Instance.getUdpReadTimeOut());
		} catch (SocketException e) {
			
			e.printStackTrace();
			return ;
		}
		
		//orm.setToId(0);
		//orm.setFromId(0);
//		UdpProcess.SendMsg(ds, orm, sim);
		for(int i=0; i<devlist.size(); i++){
			DeviceItemModel dim = devlist.get(i);
			orm.setToId(dim.getDeviceId());
			UdpProcess.SendMsg(ds, orm, sim);
		}
		
//		while(true){
			LocalIpInfo ip = new LocalIpInfo();
			baseMessage bm = RecvMsg(ds, ip);
			Log.d(TAG, "after recvmsg ip: " + ip.ip);
			if(bm != null){
				if(bm.MsgType == MessageDef.MESSAGE_TYPE_GET_STATUS_RSP){
					GetStatueRspMessage gsrm = (GetStatueRspMessage)bm;
					UpdateLocalMessage(gsrm, ip);
				}
			}else{
				GetStatueRspMessage gsrm = (GetStatueRspMessage)bm;
				UpdateLocalMessage(gsrm, ip);
			}
//		}
	}
	
	class OpenAllTask extends AsyncTask<Integer, Integer, Integer>{

		AlertDialog dialog = null;
		ProgressDialog procDialog = null;
		LinearLayout mLayout;
		LayoutInflater layoutinflat;
		ProgressBar bar;
		
		@Override
		protected Integer doInBackground(Integer... params) {
			
			//return null;
			int flag = 0;
			while(true){
				if(flag == 10){
					break;
				}
				else{
					flag ++;
					
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			return (Integer)flag;
		}

		@SuppressWarnings("unused")
		@Override
		protected void onCancelled() {
			
			super.onCancelled();
			if(dialogType == dialog_type_alert){
				if(dialog != null){
					dialog.dismiss();
				}
			}
			else if(dialogType == dialog_type_process){
				if(procDialog != null){
					procDialog.dismiss();
				}
			}
			
		}

		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(Integer result) {
			
			super.onPostExecute(result);
			//mLayout = new LinearLayout(SocketListActivity.this);
			if(dialogType == dialog_type_alert){
				bar.setProgress(result);
				if(dialog != null){
					dialog.dismiss();
				}
			}
			else if(dialogType == dialog_type_process){
				if(procDialog != null){
					procDialog.dismiss();
				}
			}
		}

		@SuppressWarnings("unused")
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			if(dialogType == dialog_type_alert){
				layoutinflat = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
				mLayout = (LinearLayout) layoutinflat.inflate(R.layout.socketlist_openalert, null);
				bar = (ProgressBar)mLayout.findViewById(R.id.socketlistopen_progress);
				bar.setMax(100);
				dialog = new AlertDialog.Builder(SocketListActivity.this)
							.setView(mLayout)
							.setNegativeButton("yess", new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									
								}
								
							})
							.setPositiveButton("noo", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									
								}
							})
							.create();
				dialog.show();
			}
			else if(dialogType == dialog_type_process){
				procDialog = new ProgressDialog(SocketListActivity.this);
				if(typeIsOpen == typeOpen){
					procDialog.setMessage(SocketListActivity.this.getString(R.string.renwu_socketlist_openall));
				}
				else if(typeIsOpen == typeClose){
					procDialog.setMessage(SocketListActivity.this.getString(R.string.renwu_socketlist_openall));
				}
				procDialog.setCanceledOnTouchOutside(false);
				procDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				procDialog.show();
				//procDialog.setMessage(R.string.renwu_s)
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			
			super.onProgressUpdate(values);
		}
		
		
	}
	
	public void OnItemOpenClick(DeviceItemModel dim){
		Log.d(TAG, "SocketListActivity OnItemClick");
		if(Utility.CheckNetwork(SocketListActivity.this) == true){
			mNetworkView.setVisibility(View.GONE);
			//new RefreshTask().execute(new Void[0]);
			OpenSingleTask ost = new OpenSingleTask();
			ost.execute(new DeviceItemModel[]{dim});
		}
		else{
			mNetworkView.setVisibility(View.VISIBLE);
			Toast.makeText(SocketListActivity.this, R.string.unNetwork, Toast.LENGTH_SHORT);
		}
		
		
	}
	
	public void OnItemClick(DeviceItemModel dim){
		boolean flag = false;
		for(int i=0; i<devlist.size(); i++){
			DeviceItemModel tdim = devlist.get(i);
			if(tdim != dim){
				if(tdim.isUi_del() == true){
					tdim.setUi_del(false);
					flag = true;
				}
			}
		}
		if(flag == true){
			mAdapter.notifyDataSetChanged();
			return;
		}
		if(mScrollBusy == false){
			if(Utility.CheckNetwork(SocketListActivity.this) == true){
				mNetworkView.setVisibility(View.GONE);
				//new RefreshTask().execute(new Void[0]);
				LoginToTask ltt = new LoginToTask();
				ltt.execute(new DeviceItemModel[]{dim});
			}
			else{
				mNetworkView.setVisibility(View.VISIBLE);
				Toast.makeText(SocketListActivity.this, R.string.unNetwork, Toast.LENGTH_SHORT);
			}
		}
	}
	
	public void OnItemLongClick(DeviceItemModel dim){
		
	}
	
	public void OnRemoveItem(DeviceItemModel dim){
		try {
			DeviceItemDao did = new DeviceItemDao(getHelper());
			did.delete(dim);
			devlist.remove(dim);
			mAdapter.notifyDataSetChanged();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		
		super.onCreate(paramBundle);
		setContentView(R.layout.socketlist);
		int id = 2;
		id |= (1 << 7);
		Log.d(TAG, "id is: " + id);
		id = ((id >> 7) & 0x000000FF);
		Log.d(TAG, "id is: " + id);
		findView();
		initView();
		initDevList();
		
		mAdapter = new SocketListAdapter(this, devlist);
		mSocketList.setAdapter(mAdapter);
		
		refreshTrhead = new RefreshAllThread();
		refreshTrhead.start();
	}

	private DeviceItemModel getFromDevList(DeviceItemModel dim){
		for(int i=0; i<devlist.size(); i++){
			DeviceItemModel tempDev = devlist.get(i);
			if(dim.getDeviceId() == tempDev.getDeviceId()){
				dim.setOnline(tempDev.getOnline());
				dim.setOnoff(tempDev.getOnoff());
				//return devlist.get(i);
			}
		}
		return null;
	}

	@Override
	protected void onResume() {
		
		
		super.onResume();
		Log.i(TAG, "onResume----------------------");
		refreshrun = true;
		setDataSource();
		GetConfigFromDevice gcfd = new GetConfigFromDevice();
		gcfd.start();
	}



	 class GetConfigFromDevice extends Thread{
		
		
		
		@Override
		public void run() {
			
			super.run();
			configinfos.clear();
			for(DeviceItemModel dim: devlist){
				if(mScrollBusy == false){
					if(Utility.CheckNetwork(SocketListActivity.this) == true){
						mNetworkView.setVisibility(View.GONE);
						//new RefreshTask().execute(new Void[0]);
						GetConfigsToTask ltt = new GetConfigsToTask();
						ltt.execute(new DeviceItemModel[]{dim});
						getconfig_suc=false; 
						while(false == getconfig_suc){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}
						}
					}
	//				else{
	//					mNetworkView.setVisibility(View.VISIBLE);
	//					Toast.makeText(SocketListActivity.this, R.string.unNetwork, Toast.LENGTH_SHORT);
	//				}
				}
			}
		}
	}
	 
	@Override
	protected void onPause() {
		
		super.onPause();
		refreshrun = false;
	}

//	@Override
//	protected void onRestart() {
//		
//		super.onRestart();
//		refreshrun = false;
//	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		refreshrun = false;
		
	}

	public void SetIconForDevice(final DeviceItemModel dim, View v) {
		
		
		myhander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				super.handleMessage(msg);
				if(msg.what == 0)
				{
					Log.i(TAG, "orginalUriStr=" + orginalUriStr);
					try {
					if(null != orginalUriStr)
						dim.setDeviceIcon(orginalUriStr);
					devicedatadao.update(dim);
					devicedatadao.refresh(dim);
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
					setDataSource();
				}
			}
		};			
//		popupseticon.showAsDropDown(v);
		switchApplication app = (switchApplication)getApplication();
		app.SetDeviceItemModel(dim);
		GobalDef.Instance.setDeviceItemModel(dim);
		Intent intent = new Intent();
		intent.setClass(SocketListActivity.this, SocketInfoActivity.class);
		startActivity(intent);
		
	}
	protected void setDataSource() {
		
		List<DeviceItemModel> result = initDevList();
		for(int i=0; i< result.size(); i++){
			getFromDevList(result.get(i));
		}
		devlist = result;
		mAdapter.SetList(devlist);
		mAdapter.notifyDataSetChanged();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		// ContentResolver contentResolver  =getContentResolver();  
         /** 
         * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法， 
         * 所以为了区别到底选择了那个方式获取图片要进行判断，这里的requestCode跟startActivityForResult里面第二个参数对应 
         */  
		 if(resultCode == Activity.RESULT_OK){
			 if(data == null){
				 Log.d(TAG, "data is null");
				 if(requestCode == capture){
					 startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), dev_pic)));
				 }
				 else if(requestCode == zoom){
					 Log.d(TAG, "zoom");
				 }
			 }
			 else{
				 Log.d(TAG, "data is not null");
				 Bundle extras = data.getExtras();
				 if(requestCode == capture){
						//Bitmap 
						
						Bitmap photo = extras.getParcelable("data");  
						startPhotoZoom(photo);
						
					}
					else if(requestCode == zoom){
						Log.d(TAG, "zoom");
						Bitmap photo = extras.getParcelable("data");  
						File pic = new File(GobalDef.Instance.getCachePath() + "/" + dev_pic);
						orginalUriStr = GobalDef.Instance.getCachePath() + "/" + dev_pic;
						FileOutputStream fs;
						try {
							fs = new FileOutputStream(pic.toString());
							photo.compress(Bitmap.CompressFormat.JPEG, 75, fs);
							fs.flush();
							fs.close();
							//mScenePicture.setImageBitmap(photo);
							//orginalUriStr = GobalDef.Instance.getCachePath() + "/" + dev_pic;
							//SetDetailBitmap(photo);
							//detail.setImageBitmap(photo);
							//dev_icon = "dev_" + dim.getDeviceId();
							//dim.setDeviceIcon("dev_" + dim.getDeviceId());
						} catch (FileNotFoundException e) {
							
							e.printStackTrace();
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						 myhander.sendEmptyMessage(0);
						
					}
					else if(requestCode == code){
						 Uri orginalUri = data.getData(); 
			             orginalUriStr = orginalUri.toString();
			             startPhotoZoom(orginalUri);
			            
					}
			 }
		 }
	
	}
	public void startPhotoZoom(Bitmap bmp)
	  {
	    Intent localIntent = new Intent("com.android.camera.action.CROP");
	    localIntent.setType("image/*");
	    localIntent.putExtra("data", bmp);
	    localIntent.putExtra("crop", "true");
	    localIntent.putExtra("aspectX", 1);
	    localIntent.putExtra("aspectY", 1);
	    localIntent.putExtra("outputX", 150);
	    localIntent.putExtra("outputY", 150);
	    localIntent.putExtra("return-data", true);
	    localIntent.putExtra("scale", true);
	    localIntent.putExtra("scaleUpIfNeeded", true);
	    startActivityForResult(localIntent, zoom);
	  }
	
	public void startPhotoZoom(Uri paramUri){
		Intent localIntent = new Intent("com.android.camera.action.CROP");
	    localIntent.setDataAndType(paramUri, "image/*");
	    localIntent.putExtra("data", bmp);
	    localIntent.putExtra("crop", "true");
	    localIntent.putExtra("aspectX", 1);
	    localIntent.putExtra("aspectY", 1);
	    localIntent.putExtra("outputX", 150);
	    localIntent.putExtra("outputY", 150);
	    localIntent.putExtra("return-data", true);
	    localIntent.putExtra("scale", true);
	    localIntent.putExtra("scaleUpIfNeeded", true);
	    startActivityForResult(localIntent, zoom);
	}



	public void SetTimerForDevice(DeviceItemModel dim, View v) {
		
		switchApplication app = (switchApplication)getApplication();
		app.SetDeviceItemModel(dim);
		GobalDef.Instance.setDeviceItemModel(dim);
		Intent intent = new Intent();
		intent.setClass(SocketListActivity.this, ScoketTaskEditActivity.class);
		startActivity(intent);
	}



	public void SetTimerOnOff(DeviceItemModel dim2, boolean isChecked) {
		//Log.e(TAG, "该设备号是" + dim2.getDeviceId());
		if(dim2.getOnline() == DeviceItemModel.Online_On){
			int onlinedevice_index=0;
			for(DeviceItemModel dim: devlist){
				if(dim.getOnline() == DeviceItemModel.Online_On){
					onlinedevice_index++;
					//重新设置
					if(dim.getDeviceId() == dim2.getDeviceId()){
						//if()
						current_ci = configinfos.get(onlinedevice_index-1);
						if(isChecked)
							current_ci.isenable=true;
						else
							current_ci.isenable = false;
						SaveConfigInfoTask sct = new SaveConfigInfoTask();
						sct.execute(new DeviceItemModel[]{dim});
						break;
					}
				}
			}
		}
		else{
			Toast.makeText(getApplicationContext(), getString(R.string.deviceoffline), Toast.LENGTH_SHORT).show();
		}
	}

private class SaveConfigInfoTask extends  AsyncTask<DeviceItemModel,Integer, Integer>{
		
		private static final String TAG = "SocketTaskEditActivity";
		private ProgressDialog processDialog;
		private DeviceItemModel dim;
		private List<ConfigInfo> configslist = new ArrayList<ConfigInfo>();

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			processDialog.dismiss();
			if(result == 0){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.renwu_socket_offline), Toast.LENGTH_SHORT).show();
//				if(timeCount == 0){
//					configinfos.remove(current_ci);
//				}
//				else{
//					if(tempci != null){
//						configinfos.set(0, tempci);
//					}
//				}
				return;
			}
			
//			switchApplication app = (switchApplication)getApplication();
//			app.SetDeviceItemModel(dim);
//			dim.setTimeinfo(Utility.getConfigStringInfo(configinfos));
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			if(timeCount > 0){
				//tempci = configinfos.get(timeCount - 1);
				//configinfos.set(timeCount - 1, ci);
			configslist.clear();
			configslist.add(current_ci);
processDialog = new ProgressDialog(SocketListActivity.this);
			processDialog.setMessage(getString(R.string.loging));
			processDialog.setCanceledOnTouchOutside(false);
			processDialog.show();
			Log.d(TAG, "OpenTask preexecute");
		}

		@Override
		protected Integer doInBackground(DeviceItemModel... params) {
			dim = params[0];
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
			scrm.configinfos = (ArrayList<ConfigInfo>) configslist;
			scrm.Array2Content();
			byte[] buf = scrm.getIndexBuf();
//			if(timeCount == 0){
//				buf[0] = (byte) configslist.size();
//			}
//			else{
//				buf[0] = (byte) (timeCount - 1);
//				
//			}
			buf[0]=0;
			current_ci.writeByte(buf, 1);
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
}
