package com.lishate.data.dao;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.lishate.data.model.applicationContent;

public class ApplicationDao extends BaseDaoImpl<applicationContent, String>{
	public ApplicationDao(ConnectionSource connectionSource,
			Class<applicationContent> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}
	
	public ApplicationDao(DatabaseHelper help)
			throws SQLException {
		super(help.getConnectionSource(), applicationContent.class);
	}

	public ApplicationDao(Class<applicationContent> dataClass)
			throws SQLException {
		super(dataClass);
		// TODO Auto-generated constructor stub
	}
}
