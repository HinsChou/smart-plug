package com.lishate.activity;

import java.sql.SQLException;

import com.lishate.R;
import com.lishate.data.dao.DeviceItemDao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends BaseActivity {

	private static final String TAG = "FirstActivity";

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			//Toast.makeText(this, "≤‚ ‘", Toast.LENGTH_LONG);
			Log.i(TAG, "test");
			//*
			final Dialog dialog = new Dialog(this, R.style.exitdialog);
			dialog.setContentView(R.layout.exit_dialog);
			dialog.show();
			
			Button yes = (Button)dialog.findViewById(R.id.exitdialog_yes);
			Button no = (Button)dialog.findViewById(R.id.exitdialog_no);
			TextView text = (TextView)dialog.findViewById(R.id.exitdialog_text);
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
			//*/
			/*
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
            View view=inflater.inflate(R.layout.exit_dialog, null);  
            builder.setView(view);
			//builder.setMessage(R.string.wangt_del_device);
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					System.exit(1);
				}
				
			});
			builder.setNegativeButton(R.string.no, null);
			builder.create().show();
			//*/
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
