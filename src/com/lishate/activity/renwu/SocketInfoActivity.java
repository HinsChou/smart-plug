package com.lishate.activity.renwu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;

import com.j256.ormlite.dao.Dao;
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
	private ImageView p7;
	private ImageView p8;
	private ImageView p9;
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
	private String dev_pic = "";
	
	public void findView(){
		back = (ImageView)findViewById(R.id.socketinfo_back);
		name = (EditText)findViewById(R.id.socketinfo_name);
		p1 = (ImageView)findViewById(R.id.socketinfo_p1);
		p2 = (ImageView)findViewById(R.id.socketinfo_p2);
		p3 = (ImageView)findViewById(R.id.socketinfo_p3);
		p4 = (ImageView)findViewById(R.id.socketinfo_p4);
		p5 = (ImageView)findViewById(R.id.socketinfo_p5);
		p6 = (ImageView)findViewById(R.id.socketinfo_p6);
		p7 = (ImageView)findViewById(R.id.socketinfo_p7);
		p8 = (ImageView)findViewById(R.id.socketinfo_p8);
		p9 = (ImageView)findViewById(R.id.socketinfo_p9);
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
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
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
			dev_icon = "file:///android_asset/p1.png";
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
			if(dev_icon.equals("file:///android_asset/p1.png")){
				p1.setImageResource(R.drawable.p1s);
			}
			else if(dev_icon.equals("file:///android_asset/p2.png")){
				p2.setImageResource(R.drawable.p2s);
			}
			else if(dev_icon.equals("file:///android_asset/p3.png")){
				p3.setImageResource(R.drawable.p3s);
			}
			else if(dev_icon.equals("file:///android_asset/p4.png")){
				p4.setImageResource(R.drawable.p4s);
			}
			else if(dev_icon.equals("file:///android_asset/p6.png")){
				p6.setImageResource(R.drawable.p6s);
			}
			else if(dev_icon.equals("file:///android_asset/p7.png")){
				p7.setImageResource(R.drawable.p7s);
			}else if(dev_icon.equals("file:///android_asset/p8.png")){
				p8.setImageResource(R.drawable.p8s);
			}else if(dev_icon.equals("file:///android_asset/p9.png")){
				p9.setImageResource(R.drawable.p9s);
			}
			
		}
		
		p1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				resetPhoto();
				p1.setImageResource(R.drawable.p1s);
				dev_icon = "file:///android_asset/p1.png";
			}
			
		});
		
		p2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				resetPhoto();
				p2.setImageResource(R.drawable.p2s);
				dev_icon = "file:///android_asset/p2.png";
			}
			
		});
		
		p3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				resetPhoto();
				p3.setImageResource(R.drawable.p3s);
				dev_icon = "file:///android_asset/p3.png";
			}
			
		});
		
		p4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				resetPhoto();
				p4.setImageResource(R.drawable.p4s);
				dev_icon = "file:///android_asset/p4.png";
			}
			
		});
		
		p6.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				resetPhoto();
				p6.setImageResource(R.drawable.p6s);
				dev_icon = "file:///android_asset/p6.png";
			}
			
		});
		
		
		p7.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				resetPhoto();
				p7.setImageResource(R.drawable.p7s);
				dev_icon = "file:///android_asset/p7.png";
			}
			
		});
		
		p8.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				resetPhoto();
				p8.setImageResource(R.drawable.p8s);
				dev_icon = "file:///android_asset/p8.png";
			}
			
		});
		
		p9.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Date now = new Date();
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);  
                getImage.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);  
                getImage.setType("image/jpeg");  
                startActivityForResult(getImage, code);  
			}
			
		});
		
		modify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				try {
					DeviceItemDao did = new DeviceItemDao(getHelper());
					dim.setDeviceName(name.getText().toString());
					dim.setDeviceIcon(dev_icon);
					did.createOrUpdate(dim);
					
					SocketInfoActivity.this.finish();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
			
		});

		camera.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "device_" + dim.getDeviceId() + ".png")));
				startActivityForResult(intent, capture);
			}
		});
	}
	
	public static String getPath(final Context context, final Uri uri) {  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
	        // ExternalStorageProvider  
	        if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {  

	            final String docId = DocumentsContract.getDocumentId(uri);  

	            final String[] split = docId.split(":");  

	            final String type = split[0];  

	            if ("primary".equalsIgnoreCase(type)) {  

	                return Environment.getExternalStorageDirectory() + "/" + split[1];  

	            }  

	        }  

	        // DownloadsProvider  

	        else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {  

	            final String id = DocumentsContract.getDocumentId(uri);  

	            final Uri contentUri = ContentUris.withAppendedId(  

	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  

	            return getDataColumn(context, contentUri, null, null);  

	        }  

	        // MediaProvider  

	        else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {  

	            final String docId = DocumentsContract.getDocumentId(uri);  

	            final String[] split = docId.split(":");  

	            final String type = split[0];  

	  

	            Uri contentUri = null;  

	            if ("image".equals(type)) {  

	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  

	            } else if ("video".equals(type)) {  

	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  

	            } else if ("audio".equals(type)) {  

	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  

	            }  

	            final String selection = "_id=?";  

	            final String[] selectionArgs = new String[] {  

	                    split[1]  

	            };  

	            return getDataColumn(context, contentUri, selection, selectionArgs);  

	        }  

	    }  

	    // MediaStore (and general)  

	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  

	        return getDataColumn(context, uri, null, null);  

	    }  

	    // File  

	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  

	        return uri.getPath();  

	    }  
	    return null;  

	}  
	
	public static String getDataColumn(Context context, Uri uri, String selection,  

	        String[] selectionArgs) {  

	  

	    Cursor cursor = null;  

	    final String column = "_data";  

	    final String[] projection = {  

	            column  

	    };  

	  

	    try {  

	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  

	                null);  

	        if (cursor != null && cursor.moveToFirst()) {  

	            final int column_index = cursor.getColumnIndexOrThrow(column);  

	            return cursor.getString(column_index);  

	        }  

	    } finally {  

	        if (cursor != null)  

	            cursor.close();  

	    }  

	    return null;  

	}  
	
	private void resetPhoto(){
		p1.setImageResource(R.drawable.p1);
		p2.setImageResource(R.drawable.p2);
		p3.setImageResource(R.drawable.p3);
		p4.setImageResource(R.drawable.p4);
		p5.setImageResource(R.drawable.p5);
		p6.setImageResource(R.drawable.p6);
		p7.setImageResource(R.drawable.p7);
		p8.setImageResource(R.drawable.p8);
		p9.setImageResource(R.drawable.p9);
	}
	
	class UpdateTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			
			return null;
		}
	}
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		
		super.onCreate(paramBundle);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.socketinfo);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
		
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "on ActivityResult");
		if(resultCode == Activity.RESULT_OK){
			if(data == null){
				Log.d(TAG, "data is null");
				if(requestCode == capture){
					startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), dev_pic)));
//					startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "device_" + dim.getDeviceId() + ".png")));
				}
				else if(requestCode == zoom){
					Log.d(TAG, "zoom");
				}
			}
			else{
				Log.d(TAG, "data is not null");
				Bundle extras = data.getExtras();
				if(requestCode == capture){
					Bitmap photo = extras.getParcelable("data");  
					startPhotoZoom(photo);
				}
				else if(requestCode == zoom){
					Log.d(TAG, "zoom");
					Bitmap photo = extras.getParcelable("data");  
					dev_pic = "device_" + new Date().getTime() + ".png";
					dev_icon = GobalDef.Instance.getCachePath() + "/" + dev_pic;
					Log.i(TAG, "dev_icon:" + dev_icon);
					File pic = new File(dev_icon);
					FileOutputStream fs;
					try {
						fs = new FileOutputStream(pic.toString());
						photo.compress(Bitmap.CompressFormat.JPEG, 75, fs);
						fs.flush();
						fs.close();
						resetPhoto();
						Bitmap bitmap = BitmapFactory.decodeFile(dev_icon); 
						p9.setImageBitmap(bitmap);
//						SetDetailBitmap(photo);
						//detail.setImageBitmap(photo);
//						dev_icon = "dev_" + dim.getDeviceId();
						//dim.setDeviceIcon("dev_" + dim.getDeviceId());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(requestCode == code){
					 Uri orginalUri = data.getData(); 
					 if(orginalUri.toString().startsWith("content://")){
						 orginalUri = Uri.parse("file://" + getPath(SocketInfoActivity.this, orginalUri));
					 }
					 Log.i(TAG, "uri:" + orginalUri);
		             startPhotoZoom(orginalUri);
				}
			}
		}
	}
	
}
