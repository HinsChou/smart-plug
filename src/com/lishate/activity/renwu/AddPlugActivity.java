package com.lishate.activity.renwu;

import com.lishate.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddPlugActivity extends Activity {

	private Button startconfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adddevice);
		initView();
	}

	private void initView() {
		startconfig = (Button) findViewById(R.id.startconfig);
		startconfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
