package com.lishate.adapter;

import java.util.List;

import com.lishate.R;
import com.lishate.data.TimerInfo;
import com.lishate.utility.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ControlSetAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mDateList;
	private LayoutInflater mInflate;
	
	public ControlSetAdapter(Context context, List<String> paramList){
		mContext = context;
		mDateList = paramList;
		mInflate = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDateList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Integer.valueOf(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		TimerInfo ti;
		int h;
		int m;
		if(convertView == null){
			vh = new ViewHolder();
			convertView = mInflate.inflate(R.layout.controlsetlist, null);
			vh.circal = (TextView)convertView.findViewById(R.id.controlsetlist_circal);
			vh.do_status = (TextView)convertView.findViewById(R.id.controlsetlist_do_status);
			vh.finish_time = (TextView)convertView.findViewById(R.id.controlsetlist_finish_time);
			vh.iv = (ImageView)convertView.findViewById(R.id.controlsetlist_iv);
			vh.start_time = (TextView)convertView.findViewById(R.id.controlsetlist_start_time);
			
			convertView.setTag(vh);
		}
		else{
			vh = (ViewHolder)convertView.getTag();
		}
		
		
		ti = TimerInfo.ParseInStr(this.mDateList.get(position));
		if(ti.getEnable().equals("01")){
			vh.iv.setImageResource(R.drawable.status_enable);
		}
		else{
			vh.iv.setImageResource(R.drawable.status_disable);
		}
		h = Integer.parseInt(ti.getOn_hour());
		m = Integer.parseInt(ti.getOff_min());
		if(h >= 0 && h<=24 && m>= 0 && m<60){
			vh.start_time.setText(Utility.GetTimeString(h, m));
		}
		else{
			vh.start_time.setText("-- --");
		}
		h = Integer.parseInt(ti.getOff_hour());
		m = Integer.parseInt(ti.getOff_min());
		if(h >= 0 && h<=24 && m>= 0 && m<60){
			vh.finish_time.setText(Utility.GetTimeString(h, m));
		}
		else{
			vh.finish_time.setText("-- --");
		}
		vh.circal.setText(TimerInfo.GetWeekLan(ti.getWeek(), this.mContext));
		
		return convertView;
	}

	class ViewHolder{
		TextView circal;
		TextView do_status;
		TextView finish_time;
		ImageView iv;
		TextView start_time;
	}
}
