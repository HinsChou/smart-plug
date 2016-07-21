package com.lishate.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lishate.activity.renwu.ChangeLampProperty;
import com.lishate.activity.renwu.SocketListActivity;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class SocketListAdapter extends BaseAdapter {

	protected static final String TAG = "SocketListAdapter";
	private SocketListActivity mContext;
	private List<DeviceItemModel> mSwitchList = new ArrayList<DeviceItemModel>();
	private LayoutInflater mInflater;
	private Bitmap myBitmap;
	private int state = 0;
	// private boolean isDelete = false;

	public SocketListAdapter(Context context, List<DeviceItemModel> list) {
		mContext = (SocketListActivity) context;
		mSwitchList = list;
		mInflater = LayoutInflater.from(context);
	}

	public void SetList(List<DeviceItemModel> list) {
		mSwitchList = list;
		for (DeviceItemModel dim : list) {
			Log.d(TAG, "dim id:" + dim.getDeviceId() + " online:" + dim.getOnline());
		}
	}

	@Override
	public int getCount() {

		return mSwitchList.size();
	}

	@Override
	public Object getItem(int pos) {

		if (pos < 0 || pos >= mSwitchList.size()) {
			return null;
		} else {
			return mSwitchList.get(pos);
		}
	}

	@Override
	public long getItemId(int pos) {

		Log.d(TAG, "get item id");
		return pos;
	}


	private void InitViewHolder(SocketHolder viewHolder, int pos) {

		DeviceItemModel dim = mSwitchList.get(pos);

		viewHolder.switchName.setText(String.valueOf(dim.getDeviceName()));
		viewHolder.onoff.setOnClickListener(new OnImgOnOffClickListener(pos, mContext));
		viewHolder.socketDelete.setOnClickListener(new OnDeleteClickListener(pos, mContext));
		viewHolder.timeronoff.setOnClickListener(new OnCheckedTimerOnOff(pos));
		viewHolder.switchIcon.setOnClickListener(new SetDeviceIcon(pos));
		viewHolder.timerset.setOnClickListener(new SetTimerTask(pos));
		viewHolder.switchLock.setOnClickListener(new ShowTimerTask(pos));
		viewHolder.switchName.setOnClickListener(new RenameDevice(pos));
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		SocketHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new SocketHolder();
			Log.d(TAG, "create convertview");
			convertView = mInflater.inflate(R.layout.subplug, null);

			convertView.setTag(viewHolder);

			viewHolder.switchIcon = (ImageView) convertView.findViewById(R.id.setplugscene);
			viewHolder.switchLock = (ImageButton) convertView.findViewById(R.id.timerlist);
			viewHolder.switchName = (TextView) convertView.findViewById(R.id.plugname);
			viewHolder.onoff = (ToggleButton) convertView.findViewById(R.id.plugonoff);
			viewHolder.timeronoff = (ToggleButton) convertView.findViewById(R.id.timeronoff);
			viewHolder.timerset = (Button) convertView.findViewById(R.id.timerset);
			viewHolder.socketDelete = (Button) convertView.findViewById(R.id.delete_device);
			viewHolder.connect = (ImageView) convertView.findViewById(R.id.ivConnect);

			InitViewHolder(viewHolder, pos);

		} else {
			Log.d(TAG, "on exits convertview");
			viewHolder = (SocketHolder) convertView.getTag();
			if (viewHolder.pos != pos) {
				InitViewHolder(viewHolder, pos);
			}
		}

		viewHolder.pos = pos;
		// GoneDelete(holder);
		DeviceItemModel dim = mSwitchList.get(pos);

		if (dim != null) {
			// 名称
			viewHolder.switchName.setText(dim.getDeviceName());
			// int intdraw = R.drawable.p1;
			// 图片
			if (dim.getDeviceIcon() != null) {
				try {
					if (dim.getDeviceIcon().startsWith("file:///android_asset/")) {
						AssetManager am = mContext.getResources().getAssets();
						InputStream is = am.open(dim.getDeviceIcon().replace("file:///android_asset/", ""));
						myBitmap = BitmapFactory.decodeStream(is);
						is.close();
					} else
						myBitmap = BitmapFactory.decodeFile(dim.getDeviceIcon());
//					Log.i(TAG, dim.getDeviceIcon());
					viewHolder.switchIcon.setImageBitmap(myBitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				viewHolder.switchIcon.setImageResource(R.drawable.socket);
			}

			// holder.socketId.setText(Utility.GetMacFormID(dim.getDeviceId()));
			// 在线
			if (dim.getOnline() == DeviceItemModel.Online_Off) {
				state++;
				Log.w(TAG, "dim get online off, state:" + state);
				if (state == 3) {
					viewHolder.connect.setImageResource(R.drawable.device_disconnect);
					viewHolder.onoff.setClickable(false);
					viewHolder.timeronoff.setClickable(false);
					viewHolder.switchIcon.setClickable(true);
					viewHolder.switchName.setClickable(true);
					viewHolder.switchLock.setClickable(false);

					viewHolder.onoff.setChecked(false);
					viewHolder.timeronoff.setChecked(false);
					viewHolder.switchLock.setImageResource(R.drawable.timer_off);
				}
			} else if (dim.getOnline() == DeviceItemModel.Online_On) {
				state = 0;
				Log.w(TAG, "dim id " + dim.getDeviceId() + " onoff " + dim.getOnoff());
				viewHolder.connect.setImageResource(R.drawable.device_connect);
				viewHolder.onoff.setClickable(true);
				viewHolder.timeronoff.setClickable(true);
				viewHolder.switchIcon.setClickable(true);
				viewHolder.switchName.setClickable(true);
				viewHolder.switchLock.setClickable(true);

				if (dim.getOnoff() == DeviceItemModel.OnOff_On) {
					Log.e(TAG, "dim id " + dim.getDeviceId() + " set on");
					viewHolder.onoff.setChecked(true);// .setImageResource(R.drawable.on);
				} else {
					Log.e(TAG, "dim id " + dim.getDeviceId() + " set off");
					viewHolder.onoff.setChecked(false);// .setImageResource(R.drawable.off);
				}

				if (dim.getSettime() == DeviceItemModel.SetTime_On) {
					viewHolder.timeronoff.setChecked(true);
					viewHolder.switchLock.setImageResource(R.drawable.timer_off);
				} else {
					viewHolder.timeronoff.setChecked(false);
					viewHolder.switchLock.setImageResource(R.drawable.timer_off);
				}
			}
		} else {
			Log.i(TAG, "dim is null");
		}

		// convertView.setOnTouchListener(new OnTouchListener(){
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// Log.d(TAG, "socketlistadpater on touch ");
		//
		// final SocketHolder holder = (SocketHolder)v.getTag();
		// switch(event.getAction()){
		// case MotionEvent.ACTION_CANCEL:
		// v.setBackgroundColor(Color.TRANSPARENT);
		// //*
		// Log.d(TAG, "action cancel");
		//
		// ux = event.getX();
		// //Log.d(TAG, "x " + x + " us " + ux);
		// if(Math.abs(x-ux) > 30){
		// if(x > ux){
		// VisibleDelete(holder);
		// }
		// else{
		// //VisibleDelete(holder);
		// GoneDelete(holder);
		// }
		// //VisibleDelete(holder);
		// Log.d(TAG, "Visible delete");
		// }
		// else{
		// //暂时把它注释掉，因为现在不需要跳转到socketDail
		// GoneDelete(holder);
		//// int ppos = holder.pos;
		//// DeviceItemModel tempdim =
		// (DeviceItemModel)SocketListAdapter.this.getItem(ppos);
		//// mContext.OnItemClick(tempdim);
		// }
		// break;
		// case MotionEvent.ACTION_DOWN:
		// v.setBackgroundColor(Color.GREEN);
		// x = event.getX();
		// GoneDelete(holder);
		// break;
		// case MotionEvent.ACTION_UP:
		// Log.d(TAG, "action up");
		// v.setBackgroundColor(Color.TRANSPARENT);
		// ux = event.getX();
		// Log.d(TAG, "x " + x + " us " + ux);
		// if(Math.abs(x-ux) > 30){
		// if(x > ux){
		// VisibleDelete(holder);
		// }
		// else{
		// GoneDelete(holder);
		// }
		// Log.d(TAG, "Visible delete");
		// }
		// else{
		// GoneDelete(holder);
		//// int ppos = holder.pos;
		//// Log.d(TAG, "ppos is: " + ppos);
		//// DeviceItemModel tempdim =
		// (DeviceItemModel)SocketListAdapter.this.getItem(ppos);
		//// mContext.OnItemClick(tempdim);
		// }
		// break;
		// }
		// return true;
		// }
		// });

		return convertView;
	}

	private class RenameDevice implements OnClickListener {

		private int position;

		public RenameDevice(int pos) {

			this.position = pos;
		}

		@Override
		public void onClick(View v) {

			DeviceItemModel dim = (DeviceItemModel) getItem(position);
			if (null != dim) {

				// mContext.RenameDeviceForPlug(dim, position);
				Intent intent = new Intent(mContext, ChangeLampProperty.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("renamedevice", dim);
				intent.putExtras(mBundle);

				mContext.startActivity(intent);
			}
		}

	}

	private class OnCheckedTimerOnOff implements OnClickListener {
		private int position;

		public OnCheckedTimerOnOff(int pos) {

			this.position = pos;
		}

		@Override
		public void onClick(View v) {

			ToggleButton timeronoff = (ToggleButton) v;
			DeviceItemModel dim = (DeviceItemModel) getItem(position);
			if (null != dim)
				mContext.SetTimerOnOff(dim, timeronoff.isChecked());
		}

	}

	private class SetDeviceIcon implements OnClickListener {
		private int postion;

		public SetDeviceIcon(int pos) {

			this.postion = pos;
		}

		@Override
		public void onClick(View v) {

			DeviceItemModel dim = (DeviceItemModel) getItem(postion);
			if (null != dim)
				mContext.SetIconForDevice(dim, v);
		}

	}

	private class SetDeviceName implements OnClickListener {

		public SetDeviceName(int pos) {

		}

		@Override
		public void onClick(View v) {

		}

	}

	private class SetTimerTask implements OnClickListener {
		private int postion;

		public SetTimerTask(int pos) {

			this.postion = pos;
		}

		@Override
		public void onClick(View v) {

			DeviceItemModel dim = (DeviceItemModel) getItem(postion);
			if (null != dim) {
				// mContext.SetTimerForDevice(dim, v);
				Log.d(TAG, "ppos is: " + postion);
				DeviceItemModel tempdim = (DeviceItemModel) getItem(postion);
				mContext.OnItemClick(tempdim);
			}
		}

	}

	private class ShowTimerTask implements OnClickListener {
		private int postion;

		public ShowTimerTask(int pos) {
			this.postion = pos;
		}

		@Override
		public void onClick(View v) {

			DeviceItemModel dim = (DeviceItemModel) getItem(postion);
			if (null != dim) {
				// mContext.SetTimerForDevice(dim, v);
				// Log.d(TAG, "ppos is: " + postion);
				DeviceItemModel tempdim = (DeviceItemModel) getItem(postion);
				mContext.OnItemClick(tempdim);
			}
		}
	}

	class OnDeleteClickListener implements OnClickListener {

		private int ppos = 0;
		private SocketListActivity mContext;

		public OnDeleteClickListener(int pos, SocketListActivity text) {
			ppos = pos;
			mContext = text;
		}

		@Override
		public void onClick(View v) {

			Log.d(TAG, "SocketListAdapter onclick");
			DeviceItemModel dim = (DeviceItemModel) SocketListAdapter.this.getItem(ppos);
			mContext.OnRemoveItem(dim);
		}

	}

	class OnImgOnOffClickListener implements OnClickListener {

		private int ppos = 0;
		private SocketListActivity mContext;

		public OnImgOnOffClickListener(int pos, SocketListActivity text) {
			ppos = pos;
			mContext = text;
		}

		@Override
		public void onClick(View v) {

			Log.d(TAG, "SocketListAdapter onclick");
			DeviceItemModel dim = (DeviceItemModel) SocketListAdapter.this.getItem(ppos);
			mContext.OnItemOpenClick(dim);
			// Toast.makeText(mContext, "hello", Toast.LENGTH_LONG);
		}

	}

	class SocketHolder {
		Button socketDelete;
		ImageView switchIcon;
		ImageButton switchLock;
		TextView switchId;
		TextView switchName;
		ImageView connect;
		TextView type;

		ToggleButton onoff;
		ToggleButton timeronoff;
		Button timerset;
		int pos = 0;

		SocketHolder() {

		}
	}
}
