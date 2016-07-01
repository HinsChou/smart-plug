package com.lishate.activity.renwu;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.adapter.SwitchAdapter;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;

public class MainPlugActivity extends BaseActivity implements OnClickListener {
	private final static String TAG = "MainPlugActivity";
	private Button editplug;
	private Button addplug_more;
	private ListView pluglistview;
	private SwitchAdapter mswitchadapter;
	private List<DeviceItemModel> plugitemlist = new ArrayList<DeviceItemModel>();
	private  PopupWindow popup;
	private Dao<DeviceItemModel, Integer> plugdatadao= null ;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{	
			if(popup.isShowing())
			{
				popup.dismiss();
				return false;
			}
			else
			{
				return super.onKeyDown(keyCode, event);
			}
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.pluglist);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		editplug = (Button) findViewById(R.id.editplug);
		addplug_more = (Button) findViewById(R.id.addplug);
		addplug_more.setOnClickListener(this);
		editplug.setOnClickListener(this);
		pluglistview = (ListView) findViewById(R.id.socketlist_listview);
		mswitchadapter = new SwitchAdapter(getApplicationContext(), plugitemlist, plugitemlist);
		pluglistview.setAdapter(mswitchadapter);
		pluglistview.setDivider(null);
		
		// 装载R.layout.popup对应的界面布局
		View root = this.getLayoutInflater().inflate(R.layout.popup, null);
		// 创建PopupWindow对象
		popup = new PopupWindow(root, 280, 360);
		
		root.findViewById(R.id.morefunc).setOnClickListener(
		new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// 关闭PopupWindow
				//popup.dismiss(); //①
				Intent mintent = new Intent(getApplicationContext(), MoreListActivity.class);
				startActivity(mintent);
			}
		});
		root.findViewById(R.id.adddevice).setOnClickListener(
		new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// 添加插座设备
				//popup.dismiss(); //①
				Intent mintent = new Intent(getApplicationContext(), AddPlugActivity.class);
				startActivity(mintent);
				
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		setDataSource();
		super.onResume();
	}
	private void setDataSource(){
		initList();
		//mswitchadapter.setSceneList(SenceItemList);
		mswitchadapter.notifyDataSetChanged();
	}
	private void initList(){
		try{
			
			//TableUtils.dropTable(getHelper().getConnectionSource(), BridgeItemModel.class, true);
			TableUtils.createTableIfNotExists(getHelper().getConnectionSource(), DeviceItemModel.class);
			//TableUtils.createTableIfNotExists(getHelper().getConnectionSource(), EventItemModel.class);
				
			plugdatadao = getHelper().getDao(DeviceItemModel.class);
			
			
			DeviceItemDao did = new DeviceItemDao(getHelper());
			
			plugitemlist.clear();
			plugitemlist.addAll(did.queryForAll());
			for(DeviceItemModel dim : plugitemlist)
			{
				//检查一下所以得scene设置是否正常  设置color标志
				//Log.i(TAG, "simname " + sim.getName() );
				//这里做两件事情，查询插座知否在线，是否打开或者关闭 以及是否开启定时功能
				
			}	
		
		}
		catch(Exception e){
			if(e.getMessage() != null){
				Log.d(TAG, e.getMessage());
			}
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == editplug)
		{
			
		}
		else if(v == addplug_more)
		{
			popup.showAsDropDown(v);
		}
	}	
}
