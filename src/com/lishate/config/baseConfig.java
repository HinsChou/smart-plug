package com.lishate.config;

public class baseConfig {
	
	private configInterface ci = null;
	public configInterface getConfigInterface(){
		return ci;
	}
	public void setConfigInterface(configInterface tci){
		ci = tci;
	}
	
	public boolean startConfig(){
		
		return false;
	}
	
	public boolean stopConfig(){
		return false;
	}
	
	
}
