package com.lishate.adapter;

import com.lishate.R;
import com.lishate.activity.renwu.ScoketTaskDaysActivity;
import com.lishate.adapter.SocketDetailAdapter.SocketDetailHolder;
import com.lishate.adapter.SocketListAdapter.SocketHolder;

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

public class SocketTaskAdapter extends BaseAdapter {

	private static final String TAG = "SocketTaskAdapter";
	private ScoketTaskDaysActivity mContext;
	private String[] values;
	private boolean[] dayschecked;
	private LayoutInflater mInflater;
	
	public SocketTaskAdapter(Context context, String[] val, boolean[] dayschecked){
		mContext = (ScoketTaskDaysActivity)context;
		values = val;
		mInflater = LayoutInflater.from(context);
		this.dayschecked = dayschecked;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return values[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TaskSelDayHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new TaskSelDayHolder();
			convertView = mInflater.inflate(R.layout.socketnewtask_list, null);
			viewHolder.mDayName = (TextView) convertView.findViewById(R.id.socketnewtask_list_name);
			viewHolder.daycheckicon = (ImageView) convertView.findViewById(R.id.socketnewtask_list_check);
			
			convertView.setTag(viewHolder);
		}else
		{
			viewHolder = (TaskSelDayHolder)convertView.getTag();
			
		}
		viewHolder.pos = position;
		viewHolder.mDayName.setText(getItem(position).toString());
		viewHolder.mDayName.setOnClickListener(new OnCheckedDay(position, dayschecked[position]));
		if(!dayschecked[position])
			viewHolder.daycheckicon.setBackgroundResource(R.drawable.unchecked_day);
		else
			viewHolder.daycheckicon.setBackgroundResource(R.drawable.checked_day);
		return convertView;
	}
	public class OnCheckedDay implements OnClickListener{
		int pos;
		boolean mchecked = false;
		public OnCheckedDay(int position, boolean dayschecked) {
			// TODO Auto-generated constructor stub
			this.pos = position;
			this.mchecked = dayschecked;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i(TAG, "timer week is selected pos="+ pos + " checked=" + mchecked);	
			
			mchecked = !mchecked;
			dayschecked[pos]=mchecked;
			mContext.UpdateSelWeeks(pos, mchecked);
			}
		
	}
	
	class TaskSelDayHolder{
		
		TextView mDayName;
		ImageView daycheckicon;
		boolean mchecked = false;
		int pos = 0;
		
	}
}
