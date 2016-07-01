package com.lishate.message;

import java.util.ArrayList;



public class SynPCList {
	@SuppressWarnings("rawtypes")
	private ArrayList list = new ArrayList();
	private Object synroot = new Object();

	@SuppressWarnings("unchecked")
	public void addItem(Object o){
		synchronized(synroot){
			list.add(o);
			synroot.notifyAll();
		}
	}
	
	public Object getItem()
	{
		synchronized(synroot){
			while(true){
				try{
					Object result = null;
					if(list.size() > 0){
						result = list.get(0);
						list.remove(0);
						return result;
					}
					else{
						try {
							synroot.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							if(e.getMessage() != null)
							{
								System.out.println("the error is: " + e.getMessage());
							}
							e.printStackTrace();
						}
					}
				}
				catch(Throwable te)
				{
					if(te.getMessage() != null)
					{
						System.out.println("the error is: " + te.getMessage());
					}
					te.printStackTrace();
				}
			}
		}
	}
}
