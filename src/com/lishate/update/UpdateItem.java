package com.lishate.update;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateItem {

	public final static String UPDATE_ITEM_NAME = "update";
	public final static String UPDATE_ITEM_OS = "os";
	public final static String UPDATE_ITEM_LASTVERSION = "lastversion";
	
	
	private String _ItemName = "";
	public String getItemName(){
		return _ItemName;
	}
	public void setItemName(String name){
		_ItemName = name;
	}
	
	private String _OS = "";
	public String getOS(){
		return _OS;
	}
	public void setOS(String os){
		_OS = os;
	}
	
	private int _LastVersion = android.os.Build.VERSION_CODES.BASE;
	public int getLastVersion(){
		return _LastVersion;
	}
	public void setLastVersion(int lastVersion){
		_LastVersion = lastVersion;
	}
	
	private ArrayList<VersionItem> VersionList = new ArrayList<VersionItem>();
	public ArrayList<VersionItem> getVersionList(){
		return VersionList;
	}
	
	public VersionItem getLastSuitVersion(int version, int code){
		VersionItem vi = null;
		for(int i = 0; i<VersionList.size(); i++){
			VersionItem tempvi = VersionList.get(i);
			if(tempvi.CheckVersionIsOK(version, code) == true){
				if(vi == null){
					vi = tempvi;
				}
				else if(tempvi.getVersionCode() > vi.getVersionCode()){
					vi = tempvi;
				}
			}
		}
		return vi;
	}
	
	public boolean CheckVersion(int version, int code){
		boolean result = false;
		for(int i=0; i< VersionList.size(); i++){
			VersionItem vi = VersionList.get(i);
			if(vi.CheckVersionIsOK(version, code) == true){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public void ParseUpdateItem(Element ele){
		String attrname = "";
		String attrvalue = "";
		
		if(ele.hasAttributes()){
			NamedNodeMap attributes = ele.getAttributes();
			for(int i = 0; i<attributes.getLength(); i++)
			{
				Node tempNode = attributes.item(i);
				attrname = tempNode.getNodeName().toLowerCase().trim();
				if(attrname.equalsIgnoreCase(UPDATE_ITEM_OS))
				{
					setOS(tempNode.getNodeValue());
				}
				if(attrname.equalsIgnoreCase(UPDATE_ITEM_LASTVERSION)){
					try{
					setLastVersion(Integer.parseInt(tempNode.getNodeValue()));
					}
					catch(Throwable te){
						te.printStackTrace();
					}
				}
			}
		}
		
		VersionList.clear();
		
		if(ele.hasChildNodes()){
			NodeList childs = ele.getChildNodes();
			for(int i=0; i<childs.getLength(); i++)
			{
				Node tempNode = childs.item(i);
				attrname = tempNode.getNodeName().toLowerCase();
				if(attrname.equalsIgnoreCase(VersionItem.VERSION_ITEM_NAME))
				{
					if(tempNode.getNodeType() == Node.ELEMENT_NODE)
					{
						VersionItem vi = new VersionItem();
						vi.ParseVersionItem((Element) tempNode);
						VersionList.add(vi);
					}
				}
			}
		}
	}
}
