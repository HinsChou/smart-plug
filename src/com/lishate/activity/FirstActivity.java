package com.lishate.activity;

import com.lishate.R;
import android.app.Dialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FirstActivity extends BaseActivity {

	private static final String TAG = "FirstActivity";

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			//Toast.makeText(this, "≤‚ ‘", Toast.LENGTH_LONG);
			Log.i(TAG, "test");
			//*
			final Dialog dialog = new Dialog(this, R.style.exitdialog);
			dialog.setContentView(R.layout.exit_dialog);
			dialog.show();
			
			Button yes = (Button)dialog.findViewById(R.id.exitdialog_yes);
			Button no = (Button)dialog.findViewById(R.id.exitdialog_no);
			yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					System.exit(1);
				}
			});
			no.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
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
