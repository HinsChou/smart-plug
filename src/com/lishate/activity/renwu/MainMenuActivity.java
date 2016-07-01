package com.lishate.activity.renwu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.data.SharedPreferencesUtility;

public class MainMenuActivity extends BaseActivity {

	private static final String TAG = "MainMenuActivity";
	private ViewFlipper mBody;
	private LinearLayout menu_bg;
	private LinearLayout menu_socket;
	private LinearLayout menu_switch;
	private LinearLayout menu_control;
	private LinearLayout menu_help;
	private ImageView img_socket;
	private ImageView img_switch;
	private ImageView img_control;
	private ImageView img_help;
	
	private ImageView[] menuImg;
	private int[] ImgSelID;
	private int[] ImgUnSelID;
	
	private int mSelectedMenu = 0;
	
	
	private void findViews(){
		menuImg = new ImageView[4];
		ImgSelID = new int[4];
		ImgUnSelID = new int[4];
		
		mBody = (ViewFlipper)findViewById(R.id.renwu_menu_body);
		menu_bg = (LinearLayout)findViewById(R.id.renwu_menu_bg);
		menu_socket = (LinearLayout)findViewById(R.id.renwu_menu_socket_layout);
		menu_switch = (LinearLayout)findViewById(R.id.renwu_menu_switch_layout);
		menu_control = (LinearLayout)findViewById(R.id.renwu_menu_control_layout);
		menu_help = (LinearLayout)findViewById(R.id.renwu_menu_help_layout);
		
		menuImg[0] = (ImageView)findViewById(R.id.renwu_menu_socket_img);
		//ImgSelID[0] = R.drawable.icon_socket_sel;
		//ImgUnSelID[0] = R.drawable.icon_socket_nor;
		ImgSelID[0] = R.drawable.mainsocket;
		ImgUnSelID[0] = R.drawable.mainsocket;
		menuImg[1] = (ImageView)findViewById(R.id.renwu_menu_switch_img);
		//ImgSelID[1] = R.drawable.icon_switch_sel;
		//ImgUnSelID[1] = R.drawable.icon_switch_nor;
		ImgSelID[1] = R.drawable.mainswitch;
		ImgUnSelID[1] = R.drawable.mainswitch;
		menuImg[2] = (ImageView)findViewById(R.id.renwu_menu_control_img);
		//ImgSelID[2] = R.drawable.icon_control_sel;
		//ImgUnSelID[2] = R.drawable.icon_control_nor;
		ImgSelID[2] = R.drawable.maincontrol;
		ImgUnSelID[2] = R.drawable.maincontrol;
		menuImg[3] = (ImageView)findViewById(R.id.renwu_menu_help_img);
		//ImgSelID[3] = R.drawable.icon_more_sel;
		//ImgUnSelID[3] = R.drawable.icon_more_nor;
		ImgSelID[3] = R.drawable.more;
		ImgUnSelID[3] = R.drawable.more;
		
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onKeyDown");
		final Dialog dialog = new Dialog(this, R.style.exitdialog);
		dialog.setContentView(R.layout.exit_dialog);
		dialog.show();
		
		Button yes = (Button)dialog.findViewById(R.id.exitdialog_yes);
		Button no = (Button)dialog.findViewById(R.id.exitdialog_no);
		TextView text = (TextView)dialog.findViewById(R.id.exitdialog_text);
		text.setText(R.string.renwu_exit);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.exit(1);
			}
		});
		no.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
			
		});
		return super.onKeyDown(keyCode, event);
	}



	public void InitView(){
		menu_socket.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSelectedMenu != 0){
					//mSelectedMenu = 0;
					toMenuActivity(0);
				}
			}
			
		});
		
		menu_switch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSelectedMenu != 1){
					//mSelectedMenu = 1;
					toMenuActivity(1);
				}
			}
			
		});
		
		menu_control.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSelectedMenu != 2){
					//mSelectedMenu = 2;
					toMenuActivity(2);
				}
			}
			
		});
		
		menu_help.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mSelectedMenu != 3){
					//mSelectedMenu = 3;
					toMenuActivity(3);
				}
			}
			
		});
	}
	
	private void toMenuActivity(int paramInt){
		Intent intent = new Intent();
		switch(paramInt){
		case 0:
			intent.setClass(this, SocketListActivity.class);
			SwitchActivity(intent, paramInt);
			break;
		case 1:
			intent.setClass(this, SwitchListActivity.class);
			SwitchActivity(intent, paramInt);
			break;
		case 2:
			intent.setClass(this, ControlListActivity.class);
			SwitchActivity(intent, paramInt);
			break;
		case 3:
			intent.setClass(this, MoreListActivity.class);
			SwitchActivity(intent, paramInt);
		default:
			break;
		}
	}
	
	private void SwitchActivity(Intent menuIntent ,int menuid){
		Log.d(TAG, "mSelectedMenu " + mSelectedMenu + " menuid " + menuid);
		if(mSelectedMenu != menuid){
			
			mSelectedMenu = menuid;
			SetSelectedMenuBG(menuid);
		}
		View localView = getLocalActivityManager().startActivity(String.valueOf(mSelectedMenu), menuIntent).getDecorView();
		mBody.removeAllViews();
		mBody.addView(localView);
		mBody.showNext();
		SharedPreferencesUtility.putRenwuMenuPosotion(menuid);
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}
	
	private void SetSelectedMenuBG(int menuid){
		for(int i=0; i<ImgSelID.length; i++){
			if(i == menuid){
				menuImg[i].setImageResource(ImgSelID[i]);
			}
			else{
				menuImg[i].setImageResource(ImgUnSelID[i]);
			}
		}
	}
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.mainmenu_renwu);
		findViews();
		InitView();
		toMenuActivity(SharedPreferencesUtility.getRenwuMenuPosition());
	}

	
}
