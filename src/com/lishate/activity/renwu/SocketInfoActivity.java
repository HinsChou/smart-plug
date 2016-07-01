package com.lishate.activity.renwu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.application.switchApplication;
import com.lishate.data.GobalDef;
import com.lishate.data.dao.DeviceItemDao;
import com.lishate.data.model.DeviceItemModel;

public class SocketInfoActivity extends BaseActivity {

	private ImageView back;
	private EditText name;
	private DeviceItemModel dim;
	private ImageView p1;
	private ImageView p2;
	private ImageView p3;
	private ImageView p4;
	private ImageView p5;
	private ImageView p6;
	private ImageView detail;
	private ImageView modify;
	private ImageView camera;
	private TableLayout table;
	private Bitmap bmp = null;
	
	public final static int capture =1;
	private static final int zoom = 2;
	private static final int code = 3;
	private static final String TAG = "SocketInfo";
	
	private String dev_icon = "";
	
	public void findView(){
		back = (ImageView)findViewById(R.id.socketinfo_back);
		name = (EditText)findViewById(R.id.socketinfo_name);
		p1 = (ImageView)findViewById(R.id.socketinfo_p1);
		p2 = (ImageView)findViewById(R.id.socketinfo_p2);
		p3 = (ImageView)findViewById(R.id.socketinfo_p3);
		p4 = (ImageView)findViewById(R.id.socketinfo_p4);
		p5 = (ImageView)findViewById(R.id.socketinfo_p5);
		p6 = (ImageView)findViewById(R.id.socketinfo_p6);
		detail = (ImageView)findViewById(R.id.socketinfo_detail);
		modify = (ImageView)findViewById(R.id.socketinfo_modify);
		camera = (ImageView)findViewById(R.id.socketinfo_camera);
		table = (TableLayout)findViewById(R.id.socketinfo_table);
	}
	
	public void SetDetailBitmap(Bitmap inbmp){
		detail.setImageBitmap(inbmp);
		if(bmp != null){
			bmp.recycle();
			bmp = null;
		}
		bmp = inbmp;
		
	}
	
	public void initView(){
		//*
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SocketInfoActivity.this.finish();
			}
			
		});
		if(name == null){
			Log.d(TAG, "name is null");
		}
		
		if(dim == null){
			Log.d(TAG, "dim is null");
		}
		name.setText(dim.getDeviceName());
		dev_icon = dim.getDeviceIcon();
		if(dev_icon == null || dev_icon == ""){
			dev_icon = "p1";
		}
		if(dev_icon.startsWith("dev_")){
			try{
				Bitmap tbmp = BitmapFactory.decodeFile(GobalDef.Instance.getCachePath() + "/" + dev_icon + ".jpg");
				SetDetailBitmap(tbmp);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		else{
			if(dev_icon.equals("p1")){
				detail.setImageResource(R.drawable.p1);
			}
			else if(dev_icon.equals("p2")){
				detail.setImageResource(R.drawable.p2);
			}
			else if(dev_icon.equals("p3")){
				detail.setImageResource(R.drawable.p3);
			}
			else if(dev_icon.equals("p4")){
				detail.setImageResource(R.drawable.p4);
			}
			else if(dev_icon.equals("p5")){
				detail.setImageResource(R.drawable.p5);
			}
			else if(dev_icon.equals("p6")){
				detail.setImageResource(R.drawable.p6);
			}
		}
		
		p1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detail.setImageResource(R.drawable.p1);
				dev_icon = "p1";
			}
			
		});
		
		p2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detail.setImageResource(R.drawable.p2);
				dev_icon = "p2";
			}
			
		});
		
		p3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detail.setImageResource(R.drawable.p3);
				dev_icon = "p3";
			}
			
		});
		
		p4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detail.setImageResource(R.drawable.p4);
				dev_icon = "p4";
			}
			
		});
		
		p5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detail.setImageResource(R.drawable.p5);
				dev_icon = "p5";
			}
			
		});
		
		p6.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detail.setImageResource(R.drawable.p6);
				dev_icon = "p6";
			}
			
		});
		
		modify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					DeviceItemDao did = new DeviceItemDao(getHelper());
					dim.setDeviceName(name.getText().toString());
					dim.setDeviceIcon(dev_icon);
					did.createOrUpdate(dim);
					
					SocketInfoActivity.this.finish();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		//*/
		camera.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "device_" + dim.getDeviceId() + ".png")));
				startActivityForResult(intent, capture);
				
			}
			
		});
		
		
	}
	
	class UpdateTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.socketinfo);
		switchApplication app = (switchApplication)getApplication();
		dim = app.getItemModel();
		//dim = GobalDef.Instance.getDeviceItemModel();
		findView();
		initView();
	}


	public void startPhotoZoom(Bitmap bmp)
	  {
	    Intent localIntent = new Intent("com.android.camera.action.CROP");
	    localIntent.setType("image/*");
	    localIntent.putExtra("data", bmp);
	    localIntent.putExtra("crop", "true");
	    localIntent.putExtra("aspectX", 1);
	    localIntent.putExtra("aspectY", 1);
	    localIntent.putExtra("outputX", 150);
	    localIntent.putExtra("outputY", 150);
	    localIntent.putExtra("return-data", true);
	    localIntent.putExtra("scale", true);
	    localIntent.putExtra("scaleUpIfNeeded", true);
	    startActivityForResult(localIntent, zoom);
	  }
	
	public void startPhotoZoom(Uri paramUri){
		Intent localIntent = new Intent("com.android.camera.action.CROP");
	    localIntent.setDataAndType(paramUri, "image/*");
	    localIntent.putExtra("data", bmp);
	    localIntent.putExtra("crop", "true");
	    localIntent.putExtra("aspectX", 1);
	    localIntent.putExtra("aspectY", 1);
	    localIntent.putExtra("outputX", 150);
	    localIntent.putExtra("outputY", 150);
	    localIntent.putExtra("return-data", true);
	    localIntent.putExtra("scale", true);
	    localIntent.putExtra("scaleUpIfNeeded", true);
	    startActivityForResult(localIntent, zoom);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "on ActivityResult");
		if(resultCode == Activity.RESULT_OK){
			if(data == null){
				Log.d(TAG, "data is null");
				if(requestCode == capture){
					startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "device_" + dim.getDeviceId() + ".png")));
				}
				else if(requestCode == zoom){
					Log.d(TAG, "zoom");
				}
			}
			else{
				Log.d(TAG, "data is not null");
				Bundle extras = data.getExtras();
				if(requestCode == capture){
					//Bitmap 
					
					Bitmap photo = extras.getParcelable("data");  
					startPhotoZoom(photo);
					
				}
				else if(requestCode == zoom){
					Log.d(TAG, "zoom");
					Bitmap photo = extras.getParcelable("data");  
					File pic = new File(GobalDef.Instance.getCachePath() + "/" + "dev_" + dim.getDeviceId() + ".jpg");
					FileOutputStream fs;
					try {
						fs = new FileOutputStream(pic.toString());
						photo.compress(Bitmap.CompressFormat.JPEG, 75, fs);
						fs.flush();
						fs.close();
						SetDetailBitmap(photo);
						//detail.setImageBitmap(photo);
						dev_icon = "dev_" + dim.getDeviceId();
						//dim.setDeviceIcon("dev_" + dim.getDeviceId());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
		}
		
	}
	
	
	
}
