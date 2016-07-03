package com.lishate.activity.renwu;

import com.lishate.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import android.widget.LinearLayout;

public class SpecificationAvtivity extends Activity{

	private LinearLayout backmain;
	private LinearLayout llFinish;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_aura_sub_speci);
		findView();
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		backmain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		llFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void findView() {
		// TODO Auto-generated method stub
		backmain = (LinearLayout)findViewById(R.id.llBack);
		llFinish = (LinearLayout)findViewById(R.id.llFinish);
	}

}
