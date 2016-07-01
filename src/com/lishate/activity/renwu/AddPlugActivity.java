package com.lishate.activity.renwu;


import com.lishate.R;
import com.lishate.data.GobalDef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class AddPlugActivity extends Activity implements OnClickListener{

	private ImageButton backmain;
	private Button startconfig;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adddevice);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		startconfig = (Button) findViewById(R.id.startconfig);
		startconfig.setOnClickListener(this);
		backmain = (ImageButton)findViewById(R.id.ImageButton01);
		backmain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == startconfig)
		{
			Intent mintent = new Intent(getApplicationContext(), SocketNewApConfigActivity.class);
			startActivityForResult(mintent, 0);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if( resultCode == GobalDef.CONFIG_DEVICE_APCONFIG_OK)
		{	
//			long newdeviceid = data.getLongExtra("newdeviceid", -1);
//			Intent minten = new Intent();
//			minten.putExtra("newdeviceid", newdeviceid);
//			setResult(GobalDef.CONFIG_DEVICE_APCONFIG_OK, minten);
			
			finish();
	}
		
	}
}
