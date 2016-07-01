package com.lishate.activity;

import com.lishate.R;
import com.lishate.data.SharedPreferencesUtility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class MainMenuActivity extends BaseActivity {

	private static boolean hasTask;
	private static boolean isExit = false;
	private ViewFlipper mBody;
	private LinearLayout mMenuBG;
	private LinearLayout mMenuSwitchList;
	private LinearLayout mMenuConfig;
	private LinearLayout mMenuHelp;
	private ImageView mMenuSwitch_img;
	private ImageView mMenuConfig_img;
	private ImageView mMenuHelp_img;
	
	private ImageView[] menuImg;
	private int[] ImgSelID;
	private int[] ImgUnSelID;
	
	private int mSelectedMenu = 0;
	
	private final static String TAG = "MainMenuActivity";
	
	
	private void findViews(){
		menuImg = new ImageView[3];
		ImgSelID = new int[3];
		ImgUnSelID = new int[3];
		mBody = ((ViewFlipper)findViewById(R.id.menu_body));
		mMenuBG = (LinearLayout)findViewById(R.id.menu_bg);
		mMenuSwitchList = (LinearLayout)findViewById(R.id.menu_switch_layout);
		mMenuConfig = (LinearLayout)findViewById(R.id.menu_config_layout);
		mMenuHelp = (LinearLayout)findViewById(R.id.menu_help_layout);
		/*
		mMenuSwitch_img = (ImageView)findViewById(R.id.menu_switch_img);
		mMenuConfig_img = (ImageView)findViewById(R.id.menu_config_img);
		mMenuHelp_img = (ImageView)findViewById(R.id.menu_help_img);
		*/
		menuImg[0] = (ImageView)findViewById(R.id.menu_switch_img);
		ImgSelID[0] = R.drawable.icon_home_sel;
		ImgUnSelID[0] = R.drawable.icon_home_nor;
		menuImg[1] = (ImageView)findViewById(R.id.menu_config_img);
		ImgSelID[1] = R.drawable.icon_more_sel;
		ImgUnSelID[1] = R.drawable.icon_home_nor;
		menuImg[2] = (ImageView)findViewById(R.id.menu_help_img);
		ImgSelID[2] = R.drawable.icon_selfinfo_sel;
		ImgUnSelID[2] = R.drawable.icon_selfinfo_nor;
		
	}
	
	private void InitViews(){
		
		mMenuSwitchList.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSelectedMenu != 0){
					toMenuActivity(0);
				}
			}});
		
		mMenuConfig.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSelectedMenu != 1){
					toMenuActivity(1);
				}
			}
			
		});
		
		mMenuHelp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSelectedMenu != 2){
					toMenuActivity(2);
				}
			}
			
		});
	}
	
	private void toMenuActivity(int paramInt){
		Intent localInt = new Intent();
		switch(paramInt){
		case 0:
			localInt.setClass(this, SwtichListActivity.class);
			switchActivity(localInt,paramInt);
			break;
		case 1:
			localInt.setClass(this, SwitchConfigActivity.class );
			switchActivity(localInt, paramInt);
			break;
		case 2:
			localInt.setClass(this, HelpActivity.class);
			switchActivity(localInt, paramInt);
			break;
		default:
			break;
		}
	}
	
	
	private void switchActivity(Intent menuIntent, int menuid){
		if(mSelectedMenu != menuid){
			mSelectedMenu = menuid;
			SetSelectedMenuBG(mSelectedMenu);
			
		}
		View localView = getLocalActivityManager().startActivity(String.valueOf(mSelectedMenu), menuIntent).getDecorView();
		mBody.removeAllViews();
		mBody.addView(localView);
		mBody.showNext();
		SharedPreferencesUtility.putMenuPosition(this.mSelectedMenu);
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}
	
	private void SetSelectedMenuBG(int menuid){
		for(int i=0; i<ImgSelID.length; i++){
			if(i == menuid){
				menuImg[i].setBackgroundResource(ImgSelID[i]);
			}
			else{
				menuImg[i].setBackgroundResource(ImgUnSelID[i]);
			}
		}
	}
	
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.mainmenu);
		Intent intent= new Intent(this,com.lishate.server.SwitchServer.class);
		startService(intent);
		
		findViews();
		InitViews();
		toMenuActivity(SharedPreferencesUtility.getMenuPosition());
		
		Log.d(TAG, "theadid " + Thread.currentThread().getId());
	}
	
	

}
