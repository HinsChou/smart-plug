package com.lishate.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lishate.data.model.DeviceItemModel;
import com.lishate.data.model.ServerItemModel;
import com.lishate.data.model.TimeTaskItemModel;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public final static String DATABASE_NAME = "remote.db";
	public final static int DATABASE_VER = 1;
	
	/*
	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
		// TODO Auto-generated constructor stub
	}
	*/
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource conn) {
		
		
			try {
				TableUtils.createTableIfNotExists(conn, ServerItemModel.class);
				TableUtils.createTableIfNotExists(conn, DeviceItemModel.class);
				TableUtils.createTableIfNotExists(conn, TimeTaskItemModel.class);
			} catch (java.sql.SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource conn, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		onCreate(database, conn);
		
	}
	

}
