package com.lishate.update;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class VersionItem {

	public static final String VERSION_ITEM_NAME = "version";
	public static final String VERSION_ITEM_CODE = "code";
	public static final String VERSION_ITEM_URL = "url";
	public static final String VERSION_ITEM_MARK = "mark";
	public static final String VERSION_ITEM_MIN_OS = "minosver";
	
	private int _VersionCode = 0;
	public int getVersionCode(){
		return _VersionCode;
	}
	public void setVersionCode(int ver){
		_VersionCode = ver;
	}
	
	private String _Url = "";
	public String getUrl(){
		return _Url;
	}
	public void setUrl(String url){
		_Url = url;
	}
	
	private String _mark = "";
	public String getMark(){
		return _mark;
	}
	public void setMark(String mark){
		_mark = mark;
	}
	
	private int _minOsVersion = android.os.Build.VERSION_CODES.BASE;
	public int getMinOsVersion(){
		//android.os.Build.VERSION_CODES.
		return _minOsVersion;
	}
	public void setMinOsVersion(int osVer)
	{
		_minOsVersion = osVer;
	}
	
	private ArrayList<ExceptOSItem> _ExceptOs = new ArrayList<ExceptOSItem>();
	public ArrayList<ExceptOSItem> getExceptOs(){
		return _ExceptOs;
	}
	
	public boolean CheckVersionIsOK(int ver, int Code){
		if(ver < _VersionCode){
			if(Code >= _minOsVersion){
				for(int i = 0; i < _ExceptOs.size(); i++){
					ExceptOSItem eoi = _ExceptOs.get(i);
					if(eoi.CheckIsExcept()){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public void ParseVersionItem(Element ele){
		String attrname = "";
		String attrValue = "";
		
		if(ele.hasAttributes()){
			NamedNodeMap attributes = ele.getAttributes();
			for(int i = 0; i<attributes.getLength(); i++)
			{
				Node tempNode = attributes.item(i);
				attrname = tempNode.getNodeName().toLowerCase().trim();
				if(attrname.equalsIgnoreCase(VERSION_ITEM_CODE))
				{
					try{
						setVersionCode(Integer.parseInt(tempNode.getNodeValue()));
					}
					catch(Throwable te){
						te.printStackTrace();
					}
				}
				else if(attrname.equalsIgnoreCase(VERSION_ITEM_MARK)){
					setMark(tempNode.getNodeValue());
				}
				else if(attrname.equalsIgnoreCase(VERSION_ITEM_MIN_OS)){
					try{
					setMinOsVersion(Integer.parseInt(tempNode.getNodeValue()));
					}
					catch(Throwable te){
						te.printStackTrace();
					}
				}
				else if(attrname.equalsIgnoreCase(VERSION_ITEM_URL)){
					setUrl(tempNode.getNodeValue());
				}
			}
		}
		_ExceptOs.clear();
		if(ele.hasChildNodes()){
			NodeList childs = ele.getChildNodes();
			for(int i=0; i<childs.getLength(); i++)
			{
				Node tempNode = childs.item(i);
				attrname = tempNode.getNodeName().toLowerCase();
				if(attrname.equalsIgnoreCase(ExceptOSItem.EXCEPTITEMNAME))
				{
					if(tempNode.getNodeType() == Node.ELEMENT_NODE)
					{
						ExceptOSItem eosi = new ExceptOSItem();
						eosi.ParseExceptOsItem((Element)tempNode);
						_ExceptOs.add(eosi);
					}
				}
			}
		}
	}
}
