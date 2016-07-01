package com.lishate.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.lishate.data.model.TimeTaskItemModel;

public class TimeTaskItemDao extends BaseDaoImpl<TimeTaskItemModel, String> {

	public TimeTaskItemDao(ConnectionSource connectionSource,
			Class<TimeTaskItemModel> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}

	public TimeTaskItemDao(DatabaseHelper help) throws SQLException{
		super(help.getConnectionSource(), TimeTaskItemModel.class);
	}
	
	
}
