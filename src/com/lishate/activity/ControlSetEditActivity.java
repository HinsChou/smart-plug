package com.lishate.activity;

import com.lishate.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ControlSetEditActivity extends BaseActivity {

	private int OpRange;
	private final int SELECT_FINISH_TIME = 1;
	private final int SELECT_START_TIME = 0;
	private String[] array = new String[7];
	private boolean[] flags = new boolean[7];
	private boolean isAddNew;
	private ImageButton mBackButton;
	private RelativeLayout mCircleChoose;
	private TextView mCircleText;
	//private ManageDevice mDeviceData;
	private CheckBox mEnable;
	private ImageButton mEndButton;
	private boolean mEndEnable = true;
	private TextView mEndTime;
	private ImageButton mSaveButton;
	private String mSelectedTime = "";
	private ImageButton mStartButton;
	private boolean mStartEnable = true;
	private TextView mStartTime;
	private String mTimeCycle = "";
	private TextView mTitle;
	private String mUpdataTimer;
	private String[] nTimer;
	private String[] newArray = new String[7];
	
	private void findView(){
		
		this.mBackButton = ((ImageButton)findViewById(R.id.controlsetedit_back));
	    this.mTitle = ((TextView)findViewById(R.id.controlsetedit_title_view));
	    this.mSaveButton = ((ImageButton)findViewById(R.id.controlsetedit_start_button));
	    this.mEnable = ((CheckBox)findViewById(R.id.controlsetedit_g_enable));
	    this.mCircleChoose = ((RelativeLayout)findViewById(R.id.controlsetedit_circle_choose));
	    this.mCircleText = ((TextView)findViewById(R.id.controlsetedit_circle_text));
	    this.mStartButton = ((ImageButton)findViewById(R.id.controlsetedit_start_button));
	    this.mStartTime = ((TextView)findViewById(R.id.controlsetedit_start_time));
	    this.mEndButton = ((ImageButton)findViewById(R.id.controlsetedit_finish_button));
	    this.mEndTime = ((TextView)findViewById(R.id.controlsetedit_finish_time));
	}
	
	public void initView(){
		mBackButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
		mStartButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mStartEnable){
					mStartButton.setImageResource(R.drawable.group_edit_checkbox);
					mStartEnable = false;
				}
				else{
					mStartButton.setImageResource(R.drawable.group_edit_checkbox_checked);
					mStartEnable = true;
				}
			}
			
		});
		
		mEndButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mEndEnable){
					mEndButton.setImageResource(R.drawable.group_edit_checkbox);
					mEndEnable = false;
				}
				else{
					mEndButton.setImageResource(R.drawable.group_edit_checkbox_checked);
					mEndEnable = true;
				}
			}
			
		});
		
		mCircleChoose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ControlSetEditActivity.this).setTitle(R.string.repeat_circle)
				    .setMultiChoiceItems(ControlSetEditActivity.this.array, flags, new DialogInterface.OnMultiChoiceClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1,
								boolean arg2) {
							// TODO Auto-generated method stub
							
						}
				    	
				    })
				    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
				    	
				    })
				    .setNegativeButton(R.string.no, null).show();
			}
			
		});
		
		mStartTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int result = 0;
				Intent intent = new Intent();
				intent.setClass(ControlSetEditActivity.this, ControlSetAlertActivity.class);
				startActivityForResult(intent,result);
			}
			
		});
		
		mEndTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int result = 0;
				Intent intent = new Intent();
				intent.setClass(ControlSetEditActivity.this, ControlSetAlertActivity.class);
				startActivityForResult(intent,result);
			}
			
		});
		
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.controlsetedit);
		array = this.getResources().getStringArray(R.array.array_day);
		newArray = this.getResources().getStringArray(R.array.array_date);
		findView();
		initView();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
}
