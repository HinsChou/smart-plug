package com.lishate.message.light;

import com.lishate.message.MessageDef;
import com.lishate.message.baseReqMessage;

public class SetLightModeItemReqMessage extends baseReqMessage{
	public SetLightModeItemReqMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_GET_LIGHT_MODE_ITEM_REQ;
	}
	
	private byte itemid = 0;

	public byte getItemid() {
		return itemid;
	}

	public void setItemid(byte itemid) {
		this.itemid = itemid;
	}
	
}
