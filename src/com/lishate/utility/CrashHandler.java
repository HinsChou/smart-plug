package com.lishate.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

	protected static final String TAG = "CrashHandler";
	private static final String filepath = "/crashinfo";
	private static final String filename="/crashinfo/dumpcrash.txt";
	public static CrashHandler Instance = new CrashHandler();
	
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private String mSavePath = Environment.getExternalStorageDirectory() + "/switch/CrashInfos";
	private Context context;
	private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
	
	public void setSavePath(String path){
		mSavePath = path;
	}
	
	private void saveCrashLogToFile(Throwable te) {
		File f = new File(mSavePath + filename);
		File fp = new File(mSavePath + filepath);
		boolean append = true;
		try {
			if(!fp.exists()){
				fp.mkdirs();
			}
			if(!f.exists()){
				f.createNewFile();
				append = false;
			}
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES );
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			sw.append(pi.versionName + "\r\n");
			sw.append(te.getMessage() + "\r\n");
			te.printStackTrace(pw);
			sw.append(pw.toString() + "\r\n");
			sw.append(mSimpleDateFormat.format(new Date()) + "\r\n");
			sw.append("*****************************************\r\n\r\n");
			FileWriter fw = new FileWriter(mSavePath + filename, append);
			fw.append(sw.toString());
			fw.flush();
			fw.close();
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private CrashHandler(){
		
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		Log.e(TAG, ex.getMessage());
		ex.printStackTrace();
		saveCrashLogToFile(ex);
		//if()
		//Log.e(TAG, ex.printStackTrace());
		//if(!handleException(ex))
		try{
			Thread.sleep(1000L);
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
		catch (InterruptedException localInterruptedException){
			localInterruptedException.printStackTrace();
		}
	}
	
	public void Init(Context ct){
		this.context = ct;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

}
