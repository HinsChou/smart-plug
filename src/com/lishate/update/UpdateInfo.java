package com.lishate.update;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lishate.data.GobalDef;

public class UpdateInfo {

	private ArrayList<UpdateItem> UpdateItemList = new ArrayList<UpdateItem>();
	
	public ArrayList<UpdateItem> getItemList(){
		return UpdateItemList;
	}
	
	public boolean checkIsUpdate(){
		for(int i = 0; i<UpdateItemList.size(); i++){
			UpdateItem ui = UpdateItemList.get(i);
			if(ui.getOS().equalsIgnoreCase("android")){
				return ui.CheckVersion(GobalDef.Instance.GetApkVer(), android.os.Build.VERSION.SDK_INT);
			}
		}
		return false;
	}
	
	public VersionItem GetLastSuitableVersion(){
		for(int i = 0; i<UpdateItemList.size(); i++){
			UpdateItem ui = UpdateItemList.get(i);
			if(ui.getOS().equalsIgnoreCase("android")){
				return ui.getLastSuitVersion(GobalDef.Instance.GetApkVer(), android.os.Build.VERSION.SDK_INT);// ui.CheckVersion(GobalDef.Instance.GetApkVer(), android.os.Build.VERSION.SDK_INT);
			}
		}
		return null;
	}
	
	public static UpdateInfo getUpdateInfo(){
		String path = GobalDef.Instance.getCachePath() + "/" + GobalDef.UPDATE_XML_NAME;
		return getUpdateInfo(path);
	}
	
	public static UpdateInfo getUpdateInfo(String path){
		DocumentBuilderFactory docBuilderFactory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		UpdateInfo info = new UpdateInfo();
		String attrname = "";
		try{
			File f = new File(path);
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(f);
			Element root = doc.getDocumentElement();
			if(root.hasChildNodes()){
				NodeList childs = root.getChildNodes();
				for(int i=0; i<childs.getLength(); i++)
				{
					Node tempNode = childs.item(i);
					attrname = tempNode.getNodeName().toLowerCase();
					if(attrname.equalsIgnoreCase(UpdateItem.UPDATE_ITEM_NAME))
					{
						if(tempNode.getNodeType() == Node.ELEMENT_NODE)
						{
							UpdateItem ui = new UpdateItem();
							ui.ParseUpdateItem((Element)tempNode);
							info.getItemList().add(ui);
						}
					}
				}
			}
		}
		catch(Throwable te){
			if(te != null)
			{
				if(te.getMessage() != null)
				{
					System.out.println(te.getMessage());
				}
				te.printStackTrace();
			}
		}
		finally{
			docBuilderFactory = null;
			docBuilder = null;
			doc = null;
		}
		return info;
	}
	
}
