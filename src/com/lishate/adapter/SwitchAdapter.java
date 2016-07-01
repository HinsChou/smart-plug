package com.lishate.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lishate.R;
import com.lishate.data.model.DeviceItemModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SwitchAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<DeviceItemModel> mSwitchList = new ArrayList<DeviceItemModel>();
	private List<DeviceItemModel> mLocalSwitchList = new ArrayList<DeviceItemModel>();
	private LayoutInflater mInflater;
	
	public SwitchAdapter(Context context, List<DeviceItemModel> devlist, List<DeviceItemModel> locallist){
		mContext = context;
		mSwitchList = devlist;
		mLocalSwitchList = locallist;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSwitchList.size() + mLocalSwitchList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position < mSwitchList.size()){
			return mSwitchList.get(position);
			
		}else if(position >= mSwitchList.size()){
			position = position - mSwitchList.size();
			if(position < mLocalSwitchList.size()){
				return mLocalSwitchList.get(position);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SwitchHolder viewHolder= null;
		if(convertView == null){
			viewHolder = new SwitchHolder();
			convertView = mInflater.inflate(R.layout.subplug, null);
//			viewHolder.switchIcon = (ImageView)convertView.findViewById(R.id.devicelist_list_left);
//			viewHolder.switchLock = (ImageView)convertView.findViewById(R.id.devicelist_list_lock);
//			viewHolder.netFrom = (ImageView)convertView.findViewById(R.id.devicelist_list_net_from);
//			viewHolder.switchId = (TextView)convertView.findViewById(R.id.devicelist_list_id);
//			viewHolder.switchName = (TextView)convertView.findViewById(R.id.devicelist_list_name);
			//viewHolder.type = (TextView)convertView.findViewById(R.id.devicel)
			viewHolder.switchIcon = (ImageView)convertView.findViewById(R.id.setplugscene);
			viewHolder.switchName = (TextView) convertView.findViewById(R.id.plugname);
			viewHolder.onoff = (ToggleButton) convertView.findViewById(R.id.plugonoff);
			viewHolder.timeronoff = (ToggleButton) convertView.findViewById(R.id.timeronoff);
			//viewHolder.timerset = (Button) convertView.findViewById(R.id.settimer);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (SwitchHolder)convertView.getTag();
		}
		DeviceItemModel nmd = (DeviceItemModel)getItem(position);
		if(nmd != null){

			
			viewHolder.switchName.setText(nmd.getDeviceName());
			//viewHolder.switchId.setText(String.valueOf(nmd.getDeviceId()));
			//判断是否在线和是否开启
			if(DeviceItemModel.Online_Off == nmd.getOnline())
			{//离线状态显示不可用
				
			}else
			{
				if(DeviceItemModel.OnOff_On == nmd.getOnoff())
				{
					viewHolder.onoff.setChecked(true);
				}else
				{
					viewHolder.onoff.setChecked(false);
				}
			}
			//判断定时器是否开启
			if(DeviceItemModel.SetTime_On == nmd.getSettime())
			{
				viewHolder.timeronoff.setChecked(true);
			}else
			{
				viewHolder.timeronoff.setChecked(false);
			}
		}
		return convertView;
	}
	
	class SwitchHolder{
		ImageView switchIcon;
		ImageView switchLock;
		TextView switchId;
		TextView switchName;
		ImageView netFrom;
		TextView type;
		
		ToggleButton onoff;
		ToggleButton timeronoff;
		Button timerset;
		public SwitchHolder(){
			
		}
	}
	

}
