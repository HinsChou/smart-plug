package com.lishate.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lishate.activity.renwu.ChangeLampProperty;
import com.lishate.activity.renwu.SocketListActivity;
import com.lishate.data.GobalDef;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.utility.Utility;
import com.lishate.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SocketListAdapter extends BaseAdapter {

	protected static final String TAG = "SocketListAcapter";
	private SocketListActivity mContext;
	private List<DeviceItemModel> mSwitchList = new ArrayList<DeviceItemModel>();
	private LayoutInflater mInflater;
	private float x, ux;
	private byte[] mContent;  
	private Bitmap myBitmap;
	
	//private boolean isDelete = false;
	
	public SocketListAdapter(Context context, List<DeviceItemModel> list){
		mContext = (SocketListActivity)context;
		mSwitchList = list;
		mInflater = LayoutInflater.from(context);
	}
	
	public void SetList(List<DeviceItemModel> list){
		mSwitchList = list;
		for(DeviceItemModel dim:list){
			Log.d(TAG, "dim id:" + dim.getDeviceId() + " online:" + dim.getOnline());
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//Log.d(TAG, "get size is: " + mSwitchList.size());
		return mSwitchList.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		if(pos < 0 || pos >= mSwitchList.size())
		{
			return null;
		}
		else{
			return mSwitchList.get(pos);
		}
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		Log.d(TAG, "get item id");
		return pos;
	}
	
	private int getOnLine(int online){
		switch(online){
		case DeviceItemModel.Online_Off:
			return R.string.renwu_socketlist_online_off;
		case DeviceItemModel.Online_On:
			return R.string.renwu_socketlist_online_on;
		case DeviceItemModel.Online_Un:
			return R.string.renwu_socketlist_online_un;
		}
		return R.string.renwu_socketlist_online_off;
	}
	
	private int getOnOff(int onoff){
		switch(onoff){
		case DeviceItemModel.OnOff_On:
			return R.string.renwu_socketlist_onff_on;
		case DeviceItemModel.Online_Off:
			return R.string.renwu_socketlist_onff_off;
		}
		return R.string.renwu_socketlist_onff_off;
	}
	
	private int getSetTime(int settime){
		switch(settime){
		case DeviceItemModel.SetTime_On:
			return R.string.renwu_socketlist_settime_on;
		case DeviceItemModel.SetTime_Off:
			return R.string.renwu_socketlist_settime_off;
		}
		return R.string.renwu_socketlist_settime_off;
	}
	
	private void VisibleDelete(SocketHolder holder){
		Log.d(TAG, " Visible delete");
		holder.socketDelete.setVisibility(View.VISIBLE);// = View.VISIBLE;
		holder.onoff.setVisibility(View.GONE);
		holder.timeronoff.setVisibility(View.GONE);
		holder.timerset.setVisibility(View.GONE);
		DeviceItemModel dim = mSwitchList.get(holder.pos);
		dim.setUi_del(true);
	}
	
	private void GoneDelete(SocketHolder holder){
		Log.d(TAG, " Gone delete");
		holder.socketDelete.setVisibility(View.GONE);
		holder.onoff.setVisibility(View.VISIBLE);
		holder.timeronoff.setVisibility(View.GONE);
		holder.timerset.setVisibility(View.GONE);
		DeviceItemModel dim = mSwitchList.get(holder.pos);
		dim.setUi_del(false);
	}
	
	private void InitViewHolder(SocketHolder viewHolder, int pos ){
		
		DeviceItemModel dim = mSwitchList.get(pos);
		
		
		
		viewHolder.switchName.setText(String.valueOf(dim.getDeviceName()));
		viewHolder.onoff.setOnClickListener(new OnImgOnOffClickListener(pos, mContext));
		viewHolder.socketDelete.setOnClickListener(new OnDeleteClickListener(pos, mContext));
		viewHolder.timeronoff.setOnClickListener(new OnCheckedTimerOnOff(pos));
		viewHolder.switchIcon.setOnClickListener(new SetDeviceIcon(pos));
		viewHolder.switchName.setOnClickListener(new SetDeviceName(pos));
		viewHolder.timerset.setOnClickListener(new SetTimerTask(pos));
		viewHolder.switchLock.setOnClickListener(new ShowTimerTask(pos));
		viewHolder.switchName.setOnClickListener(new RenameDevice(pos));
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SocketHolder viewHolder = null;
		Log.e(TAG, "getView-------------------------------------------");
		
		//*
		if(convertView == null){
			viewHolder = new SocketHolder();
			Log.d(TAG, "create convertview");
			convertView = mInflater.inflate(R.layout.subplug, null);
			
			convertView.setTag(viewHolder);
			
			{
				viewHolder.switchIcon = (ImageButton)convertView.findViewById(R.id.setplugscene);
				viewHolder.switchLock = (ImageButton)convertView.findViewById(R.id.timerlist);
				viewHolder.switchName = (TextView) convertView.findViewById(R.id.plugname);
				viewHolder.onoff = (ToggleButton) convertView.findViewById(R.id.plugonoff);
				viewHolder.timeronoff = (ToggleButton) convertView.findViewById(R.id.timeronoff);
				viewHolder.timerset = (Button) convertView.findViewById(R.id.timerset);
				viewHolder.socketDelete= (Button) convertView.findViewById(R.id.delete_device);
			}
			
			InitViewHolder(viewHolder, pos);
			
		}
		else{
			Log.d(TAG, "on exits convertview");
			viewHolder = (SocketHolder) convertView.getTag();
			if(viewHolder.pos != pos){
				InitViewHolder(viewHolder, pos);
			}
		}
		
		viewHolder.pos = pos;
		//GoneDelete(holder);
		DeviceItemModel dim = mSwitchList.get(pos);
		
		if(dim != null){
			if(dim.isUi_del() == true){
				VisibleDelete(viewHolder);
			}else{
				GoneDelete(viewHolder);
			}
			viewHolder.switchName.setText(dim.getDeviceName());
			//int intdraw = R.drawable.p1;
			if(dim.getDeviceIcon() != null ){	
				   
				try { 
					//ContentResolver contentResolver  =mContext.getActivity().getContentResolver();
		                  //将图片内容解析成字节数组  
					//Log.i(TAG, "position="  + position +  sim.getPicuri() +  " " +  sim.getName());
					myBitmap = BitmapFactory.decodeFile(dim.getDeviceIcon());
		               
					viewHolder.switchIcon.setImageBitmap(myBitmap); 
		            } catch (Exception e) { 
		                e.printStackTrace(); 
		                // TODO: handle exception 
		            } 
			}else{
				viewHolder.switchIcon.setImageResource(R.drawable.setplugscene);
			}
			
			
			
			
			//holder.socketId.setText(Utility.GetMacFormID(dim.getDeviceId()));
			
			
			if(dim.getOnline() == DeviceItemModel.Online_Off){
				Log.d(TAG, "dim get online off");
				viewHolder.timeronoff.setBackgroundResource(R.drawable.offline_on);
				
				viewHolder.onoff.setClickable(true);
				viewHolder.timeronoff.setClickable(true);
				viewHolder.switchIcon.setClickable(true);
				viewHolder.switchName.setClickable(true);
			}
			else if(dim.getOnline() == DeviceItemModel.Online_On){
//				
				Log.e(TAG, "dim id " + dim.getDeviceId() + " onoff " + dim.getOnoff());
				viewHolder.onoff.setClickable(true);
				viewHolder.timeronoff.setClickable(true);
				viewHolder.switchIcon.setClickable(true);
				viewHolder.switchName.setClickable(true);
				if(dim.getOnoff() == DeviceItemModel.OnOff_On){
					Log.e(TAG, "dim id " + dim.getDeviceId() + " set on");
					viewHolder.onoff.setChecked(true);//.setImageResource(R.drawable.on);
				}
				else{
					Log.e(TAG, "dim id " + dim.getDeviceId() + " set off");
					viewHolder.onoff.setChecked(false);//.setImageResource(R.drawable.off);
				}
				if(dim.getSettime() == DeviceItemModel.SetTime_On)
				{
					viewHolder.timeronoff.setChecked(true);
				}else
					viewHolder.timeronoff.setChecked(false);
				
				
			}
			
		}
		
		
		
		convertView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.d(TAG, "socketlistadpater on touch ");
				
				final SocketHolder holder = (SocketHolder)v.getTag();
				switch(event.getAction()){
				case MotionEvent.ACTION_CANCEL:
					v.setBackgroundColor(Color.TRANSPARENT);
					//*
					Log.d(TAG, "action cancel");
					
					ux = event.getX();
					//Log.d(TAG, "x " + x + " us " + ux);
					if(Math.abs(x-ux) > 30){
						if(x > ux){
							VisibleDelete(holder);
							
						}
						else{
							//VisibleDelete(holder);
							GoneDelete(holder);
						}
						//VisibleDelete(holder);
						Log.d(TAG, "Visible delete");
					}
					else{
						//暂时把它注释掉，因为现在不需要跳转到socketDail
						GoneDelete(holder);
//						int ppos = holder.pos;
//						DeviceItemModel tempdim = (DeviceItemModel)SocketListAdapter.this.getItem(ppos);
//						mContext.OnItemClick(tempdim);
					}
					
					break;
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundColor(Color.GREEN);
					x = event.getX();
					GoneDelete(holder);
					break;
				case MotionEvent.ACTION_UP:
					Log.d(TAG, "action up");
					v.setBackgroundColor(Color.TRANSPARENT);
					ux = event.getX();
					Log.d(TAG, "x " + x + " us " + ux);
					if(Math.abs(x-ux) > 30){
						if(x > ux){
							VisibleDelete(holder);
							
						}
						else{
							GoneDelete(holder);
						}
						Log.d(TAG, "Visible delete");
					}
					else{
						GoneDelete(holder);
						
//						int ppos = holder.pos;
//						Log.d(TAG, "ppos is: " + ppos);
//						DeviceItemModel tempdim = (DeviceItemModel)SocketListAdapter.this.getItem(ppos);
//						mContext.OnItemClick(tempdim);
					}
					break;
				}
				
				return true;
			}
			
		});
		
		return convertView;
	}
	private class RenameDevice implements OnClickListener{
		
		private int position;
		public RenameDevice(int pos) {
			// TODO Auto-generated constructor stub
			this.position = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DeviceItemModel dim = (DeviceItemModel)getItem(position);
			if(null != dim)
			{
				
//				mContext.RenameDeviceForPlug(dim, position);
				
				
				Intent intent = new Intent(mContext, ChangeLampProperty.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("renamedevice", dim);
				intent.putExtras(mBundle);
				
				mContext.startActivity(intent);
			}
		}
		
	}
	private class OnCheckedTimerOnOff implements OnClickListener{
		private boolean mchecked;
		private int position;
		public OnCheckedTimerOnOff(int pos) {
			// TODO Auto-generated constructor stub
			this.position = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ToggleButton timeronoff = (ToggleButton) v;
			DeviceItemModel dim = (DeviceItemModel)getItem(position);
			if(null != dim)
				mContext.SetTimerOnOff(dim, timeronoff.isChecked());
		}
		
	}
	private class SetDeviceIcon implements OnClickListener{
		private int postion;
		public SetDeviceIcon(int pos) {
			// TODO Auto-generated constructor stub
			this.postion = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DeviceItemModel dim = (DeviceItemModel)getItem(postion);
			if(null != dim)
				mContext.SetIconForDevice(dim, v);
		}
		
	}	
	private class SetDeviceName implements OnClickListener{

		public SetDeviceName(int pos) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}	
	private class SetTimerTask implements OnClickListener{
		private int postion;
		public SetTimerTask(int pos) {
			// TODO Auto-generated constructor stub
			this.postion = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DeviceItemModel dim = (DeviceItemModel)getItem(postion);
			if(null != dim)
			{
				//mContext.SetTimerForDevice(dim, v);
				Log.d(TAG, "ppos is: " + postion);
				DeviceItemModel tempdim = (DeviceItemModel)getItem(postion);
				mContext.OnItemClick(tempdim);
			}
		}
		
	}	
	private class ShowTimerTask implements OnClickListener{
		private int postion;
		public ShowTimerTask(int pos) {
			// TODO Auto-generated constructor stub
			this.postion = pos;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DeviceItemModel dim = (DeviceItemModel)getItem(postion);
			if(null != dim)
			{
				//mContext.SetTimerForDevice(dim, v);
				//Log.d(TAG, "ppos is: " + postion);
				DeviceItemModel tempdim = (DeviceItemModel)getItem(postion);
				mContext.OnItemClick(tempdim);
			}
		}
		
	}	
	
	class OnDeleteClickListener implements OnClickListener{

		private int ppos = 0;
		private SocketListActivity mContext;
		
		public OnDeleteClickListener(int pos, SocketListActivity text){
			ppos = pos;
			mContext = text;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d(TAG, "SocketListAdapter onclick");
			DeviceItemModel dim = (DeviceItemModel)SocketListAdapter.this.getItem(ppos);
			mContext.OnRemoveItem(dim);
		}
		
	}
	
	class OnImgOnOffClickListener implements OnClickListener{

		private int ppos = 0;
		private SocketListActivity mContext;
		
		public OnImgOnOffClickListener(int pos, SocketListActivity text){
			ppos = pos;
			mContext = text;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d(TAG, "SocketListAdapter onclick");
			DeviceItemModel dim = (DeviceItemModel)SocketListAdapter.this.getItem(ppos);
			mContext.OnItemOpenClick(dim);
			//Toast.makeText(mContext, "hello", Toast.LENGTH_LONG);
		}
		
	}

	class SocketHolder{
		Button  socketDelete;
		ImageButton switchIcon;
		ImageButton switchLock;
		TextView switchId;
		TextView switchName;
		ImageView netFrom;
		TextView type;
		
		ToggleButton onoff;
		ToggleButton timeronoff;
		Button timerset;
		int pos = 0;
		SocketHolder(){
			
		}
	}
}
