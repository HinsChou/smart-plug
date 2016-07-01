package com.lishate.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.lishate.data.model.SocketItemModel;

public class SocketItemDao extends BaseDaoImpl<SocketItemModel, String> {

	public SocketItemDao(ConnectionSource connectionSource,
			Class<SocketItemModel> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}

	public SocketItemDao(DatabaseHelper help) throws SQLException{
		super(help.getConnectionSource(), SocketItemModel.class);
	}
}
