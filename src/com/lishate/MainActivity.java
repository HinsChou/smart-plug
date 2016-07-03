package com.lishate;

import android.os.Bundle;


import com.lishate.activity.renwu.SocketNewConfigActivity;
import com.lishate.activity.renwu.SpecificationAvtivity;
import com.lishate.data.GobalDef;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		LinearLayout llBack = (LinearLayout) findViewById(R.id.llBack);
		llBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		TextView tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		TextView tvConfig = (TextView) findViewById(R.id.tvConfig);
		tvConfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SocketNewConfigActivity.class);
				startActivity(intent);
			}
		});
		
		TextView tvHelp = (TextView) findViewById(R.id.tvHelp);
		tvHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SpecificationAvtivity.class);
				startActivity(intent);
			}
		});
	}


}
