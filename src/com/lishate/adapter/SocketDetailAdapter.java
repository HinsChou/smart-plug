package com.lishate.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lishate.R;
import com.lishate.activity.renwu.SocketDetail;
import com.lishate.activity.renwu.SocketListActivity;
import com.lishate.adapter.SocketListAdapter.SocketHolder;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.TimeTaskItemModel;
import com.lishate.message.ConfigInfo;
import com.lishate.utility.Utility;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SocketDetailAdapter  extends BaseAdapter{

	private static final String TAG = null;
	private SocketDetail mContext;
	//private List<TimeTaskItemModel> mTaskList = new ArrayList<TimeTaskItemModel>();
	private List<ConfigInfo> mconfiglist = new ArrayList<ConfigInfo>();
	private LayoutInflater mInflater;
	private float x, ux;
	
	public SocketDetailAdapter(Context context, List<ConfigInfo> list){
		mContext = (SocketDetail)context;
		mconfiglist = list;
		mInflater = LayoutInflater.from(context);
	}
	
	public void setConfigInfo(List<ConfigInfo> list){
		mconfiglist = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mconfiglist.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		if(pos < 0 || pos >= mconfiglist.size())
		{
			return null;
		}
		else{
			return mconfiglist.get(pos);
		}
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}
	
	private void VisibleDelete(SocketDetailHolder holder){
		Log.d(TAG, " Visible delete");
		holder.delete.setVisibility(View.VISIBLE);// = View.VISIBLE;
		holder.enableIcon.setVisibility(View.GONE);
	}
	
	private void GoneDelete(SocketDetailHolder holder){
		Log.d(TAG, " Gone delete");
		holder.delete.setVisibility(View.GONE);
		holder.enableIcon.setVisibility(View.VISIBLE);
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SocketDetailHolder holder = null;
		if(convertView == null){
			holder = new SocketDetailHolder();
			convertView = mInflater.inflate(R.layout.socketdetail_list, null);
			holder.enableIcon = (ImageView)convertView.findViewById(R.id.socketdetail_list_enable);
			holder.starttime = (TextView)convertView.findViewById(R.id.socketdetail_list_start);
			holder.endtime = (TextView)convertView.findViewById(R.id.socketdetail_list_end);
			holder.repeat = (TextView)convertView.findViewById(R.id.socketdetail_list_repeat);
			holder.delete = (Button)convertView.findViewById(R.id.socketdetail_list_delete);
			convertView.setTag(holder);
		}
		else{
			holder = (SocketDetailHolder) convertView.getTag();
		}
		holder.pos = pos;
		ConfigInfo ci = mconfiglist.get(pos);
		if(ci.isenable){
			holder.enableIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.status_enable));
		}
		else{
			holder.enableIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.status_disable));
		}
		holder.starttime.setText(Utility.getTimeString(ci.startHour,ci.startMin));
		holder.endtime.setText(Utility.getTimeString(ci.endHour, ci.endMin));
		String repeat = "";
		String[] ss = mContext.getResources().getStringArray(R.array.array_date);
		for(int i=0; i<7; i++){
			if(Utility.getByteIndex(ci.week, i + 1)){
				repeat = repeat + " " + ss[i];
			}
		}
		holder.repeat.setText(repeat);
		GoneDelete(holder);
		holder.delete.setOnClickListener(new OnDeleteClickListener(holder.pos, mContext));
		/*
		DeviceItemModel dim = mSwitchList.get(pos);
		if(dim != null){
			holder.socketIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.p1));
			holder.socketId.setText(String.valueOf(dim.getDeviceId()));
			holder.socketName.setText(String.valueOf(dim.getDeviceName()));
			holder.socketOnline.setText(getOnLine(dim.getOnline()));
			holder.socketOfOff.setText(getOnOff(dim.getOnoff()));
			holder.socketSettime.setText(getSetTime(dim.getSettime()));
		}
		*/
		
		convertView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final SocketDetailHolder holder = (SocketDetailHolder)v.getTag();
				switch(event.getAction()){
				case MotionEvent.ACTION_CANCEL:
					Log.d(TAG, "action cancel");
					v.setBackgroundColor(Color.TRANSPARENT);
					ux = event.getX();
					//Log.d(TAG, "x " + x + " us " + ux);
					if(Math.abs(x-ux) > 30){
						if(x > ux){
							GoneDelete(holder);
						}
						else{
							VisibleDelete(holder);
						}
						//VisibleDelete(holder);
						Log.d(TAG, "Visible delete");
					}
					else{
						GoneDelete(holder);
						int ppos = holder.pos;
						//ConfigInfo tempdim = (ConfigInfo)SocketDetailAdapter.this.getItem(ppos);
						mContext.OnItemClick(ppos);
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
							GoneDelete(holder);
						}
						else{
							VisibleDelete(holder);
						}
						Log.d(TAG, "Visible delete");
					}
					else{
						GoneDelete(holder);
						
						int ppos = holder.pos;
						Log.d(TAG, "ppos is: " + ppos);
						//ConfigInfo tempdim = (ConfigInfo)SocketDetailAdapter.this.getItem(ppos);
						mContext.OnItemClick(ppos);
					}
					break;
				}
				return true;
			}
			
		});
		return convertView;
	}
	
	class OnDeleteClickListener implements OnClickListener{

		private int ppos = 0;
		private SocketDetail mContext;
		
		public OnDeleteClickListener(int pos, SocketDetail text){
			ppos = pos;
			mContext = text;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d(TAG, "SocketListAdapter onclick");
			//DeviceItemModel dim = (DeviceItemModel)SocketListAdapter.this.getItem(ppos);
			mContext.OnRemoveItemClick(ppos);
		}
		
	}
	
	class SocketDetailHolder{
		ImageView enableIcon;
		TextView starttime;
		TextView endtime;
		TextView repeat;
		Button delete;
		int pos;
		
		SocketDetailHolder(){
			
		}
	}

}
