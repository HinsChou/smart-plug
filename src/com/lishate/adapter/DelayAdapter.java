package com.lishate.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lishate.R;
import com.lishate.adapter.SocketDetailAdapter.SocketDetailHolder;
import com.lishate.data.model.TimeTaskItemModel;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DelayAdapter extends BaseAdapter {

	private Context mContext;
	private int[] values;
	private LayoutInflater mInflater;
	
	public DelayAdapter(Context context, int[] val){
		mContext = context;
		values = val;
		mInflater = LayoutInflater.from(context);
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
		return values[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.delay_list, null);
			
			//convertView.setTag(holder);
		}
		else{
			//holder = (SockeDetailtHolder) convertView.getTag();
		}
		return convertView;
	}

}
