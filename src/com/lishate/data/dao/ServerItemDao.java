package com.lishate.data.dao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.lishate.data.model.ServerItemModel;

public class ServerItemDao extends BaseDaoImpl<ServerItemModel, String>
{

	public ServerItemDao(ConnectionSource connectionSource,
			Class<ServerItemModel> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}
	
	public ServerItemDao(DatabaseHelper help)
			throws SQLException {
		super(help.getConnectionSource(), ServerItemModel.class);
	}

	public ServerItemDao(Class<ServerItemModel> dataClass)
			throws SQLException {
		super(dataClass);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public void clearTable() throws SQLException{
		TransactionManager.callInTransaction(this.getConnectionSource(), new Callable(){

			@Override
			public Object call() throws Exception {
				// TODO Auto-generated method stub
				Iterator iterator = ServerItemDao.this.queryForAll().iterator();
				while(iterator.hasNext()){
					ServerItemModel sim = (ServerItemModel)iterator.next();
					delete(sim);
				}
				return null;
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public void InsertTable(final List<ServerItemModel> list) throws SQLException{
		
		TransactionManager.callInTransaction(this.connectionSource, new Callable(){

			@Override
			public Object call() throws Exception {
				// TODO Auto-generated method stub
				//Iterator it = this.
				Iterator iterator = list.iterator();
				while(iterator.hasNext()){
					ServerItemModel sim = (ServerItemModel)iterator.next();
					create(sim);
				}
				return null;
			}

			
			
		});
	}

}
