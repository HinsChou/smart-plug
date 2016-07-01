package com.lishate.activity;

import java.util.ArrayList;
import java.util.List;

import com.lishate.R;
import com.lishate.adapter.ControlSetAdapter;
import com.lishate.data.model.DeviceItemModel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ControlSetActivity extends BaseActivity {

	private ImageButton mBackButton;
	private ControlSetAdapter mAdapter;
	private int mCount;
	private DeviceItemModel dim;
	private ListView mListView;
	private ImageButton mRefreshButton;
	private List<String> mTimeList = new ArrayList();
	
	private void findView(){
		mBackButton = (ImageButton)findViewById(R.id.controlset_back);
		mListView = (ListView)findViewById(R.id.controlset_list_view);
		mRefreshButton = (ImageButton)findViewById(R.id.controlset_refresh);
	}
	
	private void initView(){
		View listHeadView = getLayoutInflater().inflate(R.layout.controlsetlist_title,null);
		mListView.addHeaderView(listHeadView);
		mBackButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		mRefreshButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RefreshTask rt = new RefreshTask();
				rt.execute(new Void[0]);
			}
			
		});
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent;
				if(pos == 0){
					int i = mCount + 1;
					if( i== 1){
						i = 2;
					}
					if(i > 8){
						Toast.makeText(ControlSetActivity.this, getString(R.string.error_max_7_count_list), Toast.LENGTH_SHORT);
					}
					intent = new Intent();
					intent.putExtra("INTENT_TIME_ADD", "INTENT_ADD_TIME");
			        intent.putExtra("INTENT_TIME_COUNT", i);
			        intent.setClass(ControlSetActivity.this, ControlSetEditActivity.class);
			        ControlSetActivity.this.startActivity(intent);
			        return;
				}
				intent = new Intent();
				intent.putExtra("INTENT_TIME_ADD", "INTENT_ADD_TIME");
		        intent.putExtra("INTENT_TIME_COUNT", pos + 1);
		        intent.setClass(ControlSetActivity.this, ControlSetEditActivity.class);
		        ControlSetActivity.this.startActivity(intent);
			}
			
		});
		
		mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(pos != 0){
					new AlertDialog.Builder(ControlSetActivity.this)
						.setMessage(R.string.deltask)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Integer[] ints = new Integer[1];
								ints[0] = arg1 + 1;
								DeleteTimeTask  dtt = new DeleteTimeTask();
								dtt.execute(ints);
							}
							
						})
						.setNegativeButton(R.string.no, null).show();
				}
				return false;
			}
			
		});
	}
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.controlset);
		
		findView();
		initView();
		mTimeList.add("1");
		mTimeList.add("2");
		mAdapter = new ControlSetAdapter(this, mTimeList);
		//*
		mListView.setAdapter(mAdapter);
		//*/
	}
	
	class RefreshTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	class DeleteTimeTask extends AsyncTask<Integer, Void, String>{

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
