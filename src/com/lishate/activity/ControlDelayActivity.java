package com.lishate.activity;

import com.lishate.R;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ControlDelayActivity extends BaseActivity {

	private CheckBox cb_turn;
	private String[] delaytime = new String[10];
	private EditText et_other;
	private ImageButton mBackButton;
	//private ManageDevice mDeviceData;
	private ImageButton mSaveButton;
	private TextView mStatusText;
	private TextView mTitleView;
	private String[] nTimer;
	private RadioButton radio1;
	private RadioButton radio2;
	private RadioButton radio3;
	private RadioButton radio4;
	private RadioButton radio5;
	private RadioButton radio6;
	private RadioButton radio7;
	private RadioGroup radioGroup1;
	private RadioGroup radioGroup2;
	private RadioGroup radioGroup3;
	private RadioGroup radioGroup4;
	private String updataTimer;
	
	private void findView(){
		
	}
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.controldelay);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
}
