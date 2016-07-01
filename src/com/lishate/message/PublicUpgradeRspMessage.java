package com.lishate.message;

public class PublicUpgradeRspMessage extends baseRspMessage {
	public PublicUpgradeRspMessage(){
		super();
		MsgType = MessageDef.MESSAGE_TYPE_PUBLIC_UPGRADE_RSP;
	}
}
