package com.lishate.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.lishate.data.model.DeviceItemModel;

public class DeviceItemDao extends BaseDaoImpl<DeviceItemModel, Integer>{

	public DeviceItemDao(ConnectionSource connectionSource,
			Class<DeviceItemModel> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}
	
	public DeviceItemDao(DatabaseHelper help)
			throws SQLException {
		super(help.getConnectionSource(), DeviceItemModel.class);
	}

	public DeviceItemDao(Class<DeviceItemModel> dataClass)
			throws SQLException {
		super(dataClass);
		// TODO Auto-generated constructor stub
	}
	
	
}
