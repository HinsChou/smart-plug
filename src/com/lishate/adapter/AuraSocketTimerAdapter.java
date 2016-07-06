package com.lishate.adapter;

import java.util.List;



import com.lishate.R;
import com.lishate.activity.renwu.ScoketTaskDaysActivity;
import com.lishate.activity.renwu.SocketTimerDetail;
import com.lishate.adapter.SocketDetailAdapter.SocketDetailHolder;
import com.lishate.adapter.SocketListAdapter.SocketHolder;
import com.lishate.message.ConfigInfo;
import com.lishate.utility.Utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AuraSocketTimerAdapter extends BaseAdapter {

	private static final String TAG = "AuraSocketTimerAdapter";
	private SocketTimerDetail mContext;
	private String[] values = new String[]{"周日","周一","周二","周三","周四","周五","周六"};
	private List<ConfigInfo> configinfos;
	private boolean[] dayschecked;
	private LayoutInflater mInflater;
	
	public AuraSocketTimerAdapter(Context context,  List<ConfigInfo> list){
		mContext = (SocketTimerDetail)context;
		//values = val;
		mInflater = LayoutInflater.from(context);
		configinfos = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return configinfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return configinfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ConfigInfo config = configinfos.get(position);
		TimerTaskHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new TimerTaskHolder();
			convertView = mInflater.inflate(R.layout.socketsubtime_aura, null);
			viewHolder.mDayName = (TextView) convertView.findViewById(R.id.timertaskcycle);
			viewHolder.timepiont = (TextView)convertView.findViewById(R.id.action_timer);
			viewHolder.isOnOff = (TextView)convertView.findViewById(R.id.tip_Onoff);
			viewHolder.deletButton = (ImageButton)convertView.findViewById(R.id.delettimertask);
			viewHolder.activeButton = (ImageButton)convertView.findViewById(R.id.timer_activate);
			convertView.setTag(viewHolder);
		}else
		{
			viewHolder = (TimerTaskHolder)convertView.getTag();
			
		}
		viewHolder.pos = position;
		String str = "";
		if(config.week != 0){
			byte weekselect = (byte) (config.week & 0x7f);
			weekselect = (byte) ((weekselect & 1<<6) | (1 & weekselect>>5) | (1<<1 & weekselect>>3) | (1<<2 & weekselect>>1)
						| (1<<3 & weekselect<<1) | (1<<4 & weekselect<<3 ) | (1<<5 & weekselect<<5));
			
			String[] ss = mContext.getResources().getStringArray(R.array.array_date);
			
			for(int i=0; i<7; i++){
				if(Utility.getByteIndex(weekselect, i + 1)){
					str = str + "  " + ss[i];
					//Log.e(TAG, "选择的周期"+ weekselect + str);
				}
				
		}
		}
		viewHolder.mDayName.setText(str);
		str = "00:00";
		Log.e(TAG, "starttimer=" + config.startHour + ":" + config.startMin + "-----------endtime=" + config.endHour + ":" +config.endMin);
		if(config.startenable){
			if((config.startHour & 0x1f) <10){
				str = "0" + config.startHour ;
			}
			else{
				str =""+ config.startHour;
			}
			if((config.startMin& 0x3f)<10){
				str =str+ ":0" + config.startMin;
			}else{
				str =str+ ":" + config.startMin;
			}
		}else //if((config.endHour & 0x80) == 0x80)
		{
			if((config.endHour & 0x1f) <10){
				str = "0" + config.endHour ;
			}
			else{
				str =""+ config.endHour;
			}
			if((config.endMin& 0x3f)<10){
				str =str+ ":0" + config.endMin;
			}else{
				str =str+ ":" + config.endMin;
			}
		}
		viewHolder.timepiont.setText(str);
		if(config.startenable && !config.endenable){
			viewHolder.isOnOff.setBackgroundResource(R.drawable.plug_on);
		}else if(!config.startenable && config.endenable){
			viewHolder.isOnOff.setBackgroundResource(R.drawable.plug_off);
		}
		if(config.isenable){
			viewHolder.activeButton.setImageResource(R.drawable.switch_on);
		}else{
			viewHolder.activeButton.setImageResource(R.drawable.switch_off);
		}
		viewHolder.activeButton.setOnClickListener(new ActiveTimer(position));
		viewHolder.deletButton.setOnClickListener(new DeletTask(position));
		convertView.setOnClickListener(new EditTask(position));
//		viewHolder.mDayName.setText(getItem(position).toString());
//		viewHolder.mDayName.setOnClickListener(new OnCheckedDay(position, dayschecked[position]));
//		if(!dayschecked[position])
//			viewHolder.daycheckicon.setBackgroundResource(R.drawable.unchecked_day);
//		else
//			viewHolder.daycheckicon.setBackgroundResource(R.drawable.checked_day);
		return convertView;
	}
	public class EditTask implements OnClickListener{
		int pos;
		//boolean mchecked = false;
		public EditTask(int position) {
			// TODO Auto-generated constructor stub
			this.pos = position;
			//this.mchecked = dayschecked;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "timer week is selected pos="+ pos + " checked=" + mchecked);	
			ConfigInfo ci = configinfos.get(pos);
			mContext.EditTaskReq(ci , pos);
			}
		
	}
	public class ActiveTimer implements OnClickListener{
		int pos;
		//boolean mchecked = false;
		public ActiveTimer(int position) {
			// TODO Auto-generated constructor stub
			this.pos = position;
			//this.mchecked = dayschecked;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "timer week is selected pos="+ pos + " checked=" + mchecked);	
			ConfigInfo ci = configinfos.get(pos);
			ci.isenable =!ci.isenable;
			mContext.Update();
			}
		
	}
	public class DeletTask implements OnClickListener{
		int pos;
		//boolean mchecked = false;
		public DeletTask(int position) {
			// TODO Auto-generated constructor stub
			this.pos = position;
			//this.mchecked = dayschecked;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "timer week is selected pos="+ pos + " checked=" + mchecked);	
			configinfos.remove(pos);
			mContext.Update();

			}
		
	}	
	class TimerTaskHolder{
		
		TextView mDayName;
		ImageView daycheckicon;
		TextView timepiont;
		TextView isOnOff;
		ImageButton deletButton;
		ImageButton activeButton;
		boolean mchecked = false;
		
		int pos = 0;
		
	}
}
