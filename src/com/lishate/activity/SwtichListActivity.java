package com.lishate.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lishate.R;
import com.lishate.adapter.SwitchAdapter;
import com.lishate.application.switchApplication;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.dao.ServerItemDao;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.ServerItemModel;
import com.lishate.message.GetServerReqMessage;
import com.lishate.message.GetServerRspMessage;
import com.lishate.message.LoginReqMessage;
import com.lishate.message.MessageDef;
import com.lishate.message.MessageSeqFactory;
import com.lishate.message.baseMessage;
import com.lishate.net.UdpProcess;
import com.lishate.utility.Utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SwtichListActivity extends BaseActivity {

	private ProgressBar mProgressBar;
	private ImageButton mRefreshButton;
	private ListView mDeviceListview;
	private TextView mNetworkView;
	private SwitchAdapter mAdapter;
	
	private List<DeviceItemModel> mNewDeviceList = new ArrayList();
	
	private List<DeviceItemModel> deviceList = new ArrayList();
	
	private void InitDeviceList(){
		try {
			DeviceItemDao did = new DeviceItemDao(getHelper());
			deviceList.clear();
			deviceList.addAll(did.queryForAll());
			if(deviceList.size() == 0){
				DeviceItemModel dim = new DeviceItemModel();
				dim.setDeviceName("≤‚ ‘");
				dim.setDeviceType(1);
				dim.setParentId(0);
				deviceList.add(dim);
				did.create(dim);
				//did.
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void findView(){
		mProgressBar = (ProgressBar)findViewById(R.id.devlist_refresjing);
		mRefreshButton = (ImageButton)findViewById(R.id.devlist_refresh);
		mDeviceListview = (ListView)findViewById(R.id.devlist_listview);
		mNetworkView = (TextView)findViewById(R.id.devlist_net_check);
	}
	
	private void initView(){
		mRefreshButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Utility.CheckNetwork(SwtichListActivity.this) == true){
					new RefreshTask().execute(new Void[0]);
				}
				else{
					mNetworkView.setVisibility(View.VISIBLE);
					Toast.makeText(SwtichListActivity.this, R.string.unNetwork, Toast.LENGTH_SHORT);
				}
			}
			
		});
		
		mDeviceListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				DeviceItemModel dim = (DeviceItemModel)(view.getAdapter()).getItem(pos);
				LogintoServerTask lst = new LogintoServerTask();
				lst.execute(new DeviceItemModel[]{dim});
			}
		});
		
		mDeviceListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> view, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				final DeviceItemModel dim = (DeviceItemModel)(view.getAdapter()).getItem(pos);
				AlertDialog.Builder builder = new AlertDialog.Builder(SwtichListActivity.this);
				builder.setMessage(R.string.wangt_del_device);
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						try {
							DeviceItemDao did = new DeviceItemDao(getHelper());
							did.delete(dim);
							deviceList.remove(dim);
							SwtichListActivity.this.mAdapter.notifyDataSetChanged();
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				builder.setNegativeButton(R.string.no, null);
				builder.create().show();
				return false;
			}
		});
	}
	
	class LogintoServerTask extends AsyncTask<DeviceItemModel,Integer,String>{

		private ProgressDialog progressDialog;
		private DeviceItemModel dim;
		@Override
		protected String doInBackground(DeviceItemModel... params) {
			// TODO Auto-generated method stub
			String result = null;
			baseMessage bm = null;
			dim = params[0];
			LoginReqMessage lrm = new LoginReqMessage();
			lrm.Direct = MessageDef.BASE_MSG_FT_REQ;
			lrm.setFromId(GobalDef.MOBILEID);
			lrm.setToId(dim.getDeviceId());
			lrm.MsgType = MessageDef.MESSAGE_TYPE_LOGIN_REQ;
			lrm.Seq = MessageSeqFactory.GetNextSeq();
			lrm.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			lrm.ToType = MessageDef.BASE_MSG_FT_END;
			
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			bm = UdpProcess.GetMsgReturn(lrm, sim);
			if(bm == null){
				result = null;
			}
			else{
				result = "";
			}
			result = "";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result == null){
				Toast.makeText(SwtichListActivity.this, R.string.timeout, Toast.LENGTH_SHORT).show();
			}
			else{
				switchApplication app = (switchApplication)getApplication();
				app.SetDeviceItemModel(dim);
				Intent intent = new Intent();
				intent.setClass(SwtichListActivity.this, ControlActivity.class);
				startActivity(intent);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(SwtichListActivity.this);
			progressDialog.setMessage(SwtichListActivity.this.getString(R.string.loging));
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		
		
	}
	
	class RefreshTask extends AsyncTask<Void, Void, Void>{

		baseMessage rcvMsg = null;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			GetServerReqMessage gsr = new GetServerReqMessage();
			gsr.setFromId(GobalDef.MOBILEID);
			gsr.setToId(GobalDef.SERVERID);
			gsr.FromType = MessageDef.BASE_MSG_FT_MOBILE;
			gsr.ToType = MessageDef.BASE_MSG_FT_SERVER;
			gsr.Seq = MessageSeqFactory.GetNextSeq();
			//gsr.setFromType(MessageDef.BASE_MSG_FT_MOBILE);
			//gsr.setToType(MessageDef.BASE_MSG_FT_SERVER);
			//gsr.setSeq(MessageSeqFactory.GetNextSeq());
			ServerItemModel sim = new ServerItemModel();
			sim.setIpaddress(GobalDef.SERVER_URL);
			sim.setPort(GobalDef.SERVER_PORT);
			rcvMsg = UdpProcess.GetMsgReturn(gsr, sim);
			if(rcvMsg != null && rcvMsg.MsgType == MessageDef.MESSAGE_TYPE_GETSERVER_RSP){
				GetServerRspMessage gsrsp = (GetServerRspMessage)rcvMsg;
				ServerItemDao sid;
				try {
					sid = new ServerItemDao(getHelper());
					if(gsrsp.getCount() > 0){
						sid.clearTable();
						switchApplication sa = (switchApplication)getApplication();
						sa.getServerList().clear();
						for(int i=0; i<gsrsp.getCount(); i++){
							ServerItemModel tsim = new ServerItemModel();
							tsim.setIpaddress(Utility.GetIpFromBuf(gsrsp.getServerInfo(), i * 6));
							tsim.setPort(Utility.GetIpPortFromBuf(gsrsp.getServerInfo(), i * 6 + 4));
							sa.getServerList().add(tsim);
						}
						sid.InsertTable(sa.getServerList());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//UdpProcess.SendMsg(ds, msg, si);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mRefreshButton.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			String rcv = "";
			if(rcvMsg == null){
				rcv = "basemsg is null";
			}
			else{
				rcv = "basemsg is ok";
			}
			Toast.makeText(SwtichListActivity.this, rcv, Toast.LENGTH_LONG);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mRefreshButton.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		switchApplication sa = (switchApplication)getApplication();
		if(deviceList.size() == 0){
			InitDeviceList();
		}
		mDeviceListview.setAdapter(mAdapter);
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.devicelist);
		findView();
		initView();
		InitDeviceList();
		mAdapter = new SwitchAdapter(this, mNewDeviceList, deviceList);
		mDeviceListview.setAdapter(mAdapter);
		
	}
	
	//class Refresh
}
