package com.lishate.update;

import com.lishate.data.GobalDef;

public class Utility {

	public static boolean GetServerVersionInfo(){
		String path = GobalDef.Instance.getCachePath() + "/" + GobalDef.UPDATE_XML_NAME;
		return com.lishate.utility.Utility.HttpDownload(GobalDef.UPDATE_URL, path);
	}
	
	public static boolean CheckServerVersionUpdate(){
		if(GetServerVersionInfo() == true){
			UpdateInfo ui = UpdateInfo.getUpdateInfo();
			if(ui != null){
				return ui.checkIsUpdate();
			}
		}
		return false;
	}
	
	public static VersionItem GetVersionItem(){
		if(GetServerVersionInfo() == true){
			UpdateInfo ui = UpdateInfo.getUpdateInfo();
			if(ui != null){
				return ui.GetLastSuitableVersion();
			}
		}
		return null;
	}
}
