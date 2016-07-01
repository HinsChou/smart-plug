package com.lishate.update;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ExceptOSItem {

	public final static String OSVERSIONCODE = "osversioncode";
	public final static String EXCEPTITEMNAME = "except";
	
	
	private int _OSVersionCode = android.os.Build.VERSION_CODES.BASE;
	public int getOsVer(){
		return _OSVersionCode;
	}
	public void setOsVer(int _os){
		_OSVersionCode = _os;
	}
	
	public void ParseExceptOsItem(Element ele){
		String attrname = "";
		String attrValue = "";
		
		if(ele.hasAttributes()){
			NamedNodeMap attributes = ele.getAttributes();
			for(int i = 0; i<attributes.getLength(); i++)
			{
				Node tempNode = attributes.item(i);
				attrname = tempNode.getNodeName().toLowerCase().trim();
				if(attrname.equalsIgnoreCase(OSVERSIONCODE))
				{
					try{
						setOsVer(Integer.parseInt(tempNode.getNodeValue()));
					}
					catch(Throwable te){
						te.printStackTrace();
					}
				}
			}
		}
	}
	
	public boolean CheckIsExcept(){
		if(android.os.Build.VERSION.SDK_INT == _OSVersionCode){
			return true;
		}
		return false;
	}
}
