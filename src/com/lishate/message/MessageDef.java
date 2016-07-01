package com.lishate.message;

public class MessageDef {
	/*
	public static final byte DEVICE_MTYPE_SOCKET = 0x01;
	public static final byte DEVICE_STYPE_SOCKET_WIFI = 0x01;
	
	public static final byte COMMAND_MTYPE_PUBLIC = 0x00;
	public static final byte COMMAND_STYPE_PUBLIC_LOGIN = 0x01;
	public static final byte COMMAND_STYPE_PUBLIC_CHECK_STATU = 0x03;
	public static final byte COMMAND_STYPE_PUBLIC_CHECK_TIME = 0x07;
	
	public static final byte COMMAND_MTYPE_SOCKET = 0x01;
	public static final byte COMMAND_STYPE_SOCKET_OPEN = 0x01;
	public static final byte COMMAND_STYPE_SOCKET_CLOSE = 0x02;
	public static final byte COMMAND_STYPE_SOCKET_GET_STATUE = 0x03;
	public static final byte COMMAND_STYPE_SOCKET_SET_CONFIGUE = 0x04;
	public static final byte COMMAND_STYPE_SOCKET_GET_CONFIGUE_SERVER = 0x05;
	public static final byte COMMAND_STYPE_SOCKET_GET_CONFIGUE_CLIENT = 0x06;
	*/
	public static final byte DIRECT_REQ = 1;
	public static final byte DIRECT_RSP = 2;
	
	public static final byte RSP_OK = 0;
	public static final byte RSP_FAIL = 1;
	
	public static final int MESSAGE_TYPE_LOGIN_REQ = 0;
	public static final int MESSAGE_TYPE_LOGIN_RSP = 1;
	
	public static final int MESSAGE_TYPE_GETSERVER_REQ = 4;
	public static final int MESSAGE_TYPE_GETSERVER_RSP = 5;
	//public static final int MESSAGE_TYPE_CHECK_STATU = 2;
	
	public static final int MESSAGE_TYPE_OPEN_REQ = 6;
	public static final int MESSAGE_TYPE_OPEN_RSP = 7;
	
	public static final int MESSAGE_TYPE_CLOSE_REQ = 8;
	public static final int MESSAGE_TYPE_CLOSE_RSP = 9;
	
	public static final int MESSAGE_TYPE_GET_STATUS_REQ = 10;
	public static final int MESSAGE_TYPE_GET_STATUS_RSP = 11;
	
	public static final int MESSAGE_TYPE_SET_CONFIG_REQ = 12;
	public static final int MESSAGE_TYPE_SET_CONFIG_RSP = 13;
	
	public static final int MESSAGE_TYPE_GET_CONFIG_SERVER_REQ = 14;
	public static final int MESSAGE_TYPE_GET_CONFIG_SERVER_RSP = 15;
	
	
	
	public static final int MESSAGE_TYPE_SET_DELAY_ONOFF_REQ = 18;
	public static final int MESSAGE_TYPE_SET_DELAY_ONOFF_RSP = 19;
	
	public static final int MESSAGE_TYPE_SET_LIGHT_REQ = 20;
	public static final int MESSAGE_TYPE_SET_LIGHT_RSP = 21;
	public static final int MESSAGE_TYPE_SET_LIGHT_DATA_REQ = 22;
	public static final int MESSAGE_TYPE_SET_LIGHT_DATA_RSP = 23;
	public static final int MESSAGE_TYPE_SET_LIGHT_MODE_REQ = 24;
	public static final int MESSAGE_TYPE_SET_LIGHT_MODE_RSP = 25;
	public static final int MESSAGE_TYPE_GET_LIGHT_MODE_COUNT_REQ = 26;
	public static final int MESSAGE_TYPE_GET_LIGHT_MODE_COUNT_RSP = 27;
	public static final int MESSAGE_TYPE_GET_LIGHT_MODE_ITEM_REQ = 28;
	public static final int MESSAGE_TYPE_GET_LIGHT_MODE_ITEM_RSP = 29;
	public static final int MESSAGE_TYPE_CONFIG_LIGHT_NEW_REQ = 30;
	public static final int MESSAGE_TYPE_CONFIG_LIGHT_NEW_RSP = 31;
	public static final int MESSAGE_TYPE_CONFIG_LIGHT_DEL_REQ = 32;
	public static final int MESSAGE_TYPE_CONFIG_LIGHT_DEL_RSP = 33;
	public static final int MESSAGE_TYPE_RFCODE_STUDY_REQ = 34;
	public static final int MESSAGE_TYPE_RFCODE_STUDY_RSP = 35;
	public static final int MESSAGE_TYPE_RFCODE_SEND_REQ = 36;
	public static final int MESSAGE_TYPE_RFCODE_SEND_RSP = 37;
	public static final int MESSAGE_TYPE_RFCODE_FINISH_REQ = 38;
	public static final int MESSAGE_TYPE_RFCODE_FINISH_RSP = 39;
	public static final int MESSAGE_TYPE_LIGHT_GET_REQ	= 40;
	public static final int MESSAGE_TYPE_LIGHT_GET_RSP = 41;
	public static final int MESSAGE_TYPE_TEMPE_GET_REQ = 42;
	public static final int MESSAGE_TYPE_TEMPE_GET_RSP = 43;
	public static final int MESSAGE_TYPE_LIGHT_MODE_GET_REQ = 44;
	public static final int MESSAGE_TYPE_LIGHT_MODE_GET_RSP = 45;
	public static final int MESSAGE_TYPE_LIGHT_MODE_SET_REQ = 46;
	public static final int MESSAGE_TYPE_LIGHT_MODE_SET_RSP = 47;
	public static final int MESSAGE_TYPE_LIGHT_ON_REQ = 48;
	public static final int MESSAGE_TYPE_LIGHT_ON_RSP = 49;
	public static final int MESSAGE_TYPE_LIGHT_OFF_REQ = 50;
	public static final int MESSAGE_TYPE_LIGHT_OFF_RSP = 51;
	public static final int MESSAGE_TYPE_PUBLIC_UPGRADE_REQ = 52;
	public static final int MESSAGE_TYPE_PUBLIC_UPGRADE_RSP = 53;
	public static final int MESSAGE_TYPE_PUBLIC_VERSION_REQ = 54;
	public static final int MESSAGE_TYPE_PUBLIC_VERSION_RSP	= 55;
	public static final int MESSAGE_TYPE_PUBLIC_GOBALTIME_CHECK_REQ = 56;
	public static final int MESSAGE_TYPE_PUBLIC_GOBALTIME_CHECK_RSP = 57;
	
	public static final int MESSAGE_TYPE_LIGHT_GETONOFF_REQ = 58;
	public static final int MESSAGE_TYPE_LIGHT_GETONOFF_RSP = 59;
	public static final int MESSAGE_TYPE_SWITCH_COUNT_REQ = 60;
	public static final int MESSAGE_TYPE_SWITCH_COUNT_RSP = 61;
	public static final int MESSAGE_TYPE_SWITCH_SETONOFF_REQ = 62;
	public static final int MESSAGE_TYPE_SWITCH_SETONOFF_RSP = 63;
	public static final int MESSAGE_TYPE_SWITCH_GETONOFF_REQ = 64;
	public static final int MESSAGE_TYPE_SWITCH_GETONOFF_RSP = 65;
	public static final int MESSAGE_TYPE_MAGNET_REPORT_REQ = 66;
	public static final int MESSAGE_TYPE_MAGNET_REPORT_RSP = 67;
	public static final int MESSAGE_TYPE_MAGNET_GET_STATUE_REQ = 68;
	public static final int MESSAGE_TYPE_MAGNET_GET_STATUE_RSP = 69;
	public static final int MESSAGE_TYPE_LIGHT_RANDOMCHANGE_REQ = 70;
	public static final int MESSAGE_TYPE_LIGHT_RANDOMCHANGE_RSP	= 71;
	public static final int MESSAGE_TYPE_LIGHT_SETTARGETCHANGE_REQ = 72;
	public static final int MESSAGE_TYPE_LIGHT_SETTARGETCHANGE_RSP = 73;
	public static final int MESSAGE_TYPE_LIGHT_GETTARGETCHANGE_REQ = 74;
	public static final int MESSAGE_TYPE_LIGHT_GETTARGETCHANGE_RSP = 75;
	public static final int MESSAGE_TYPE_LIGHT_TARGETCOUNT_REQ = 76;
	public static final int MESSAGE_TYPE_LIGHT_TARGETCOUNT_RSP = 77;
	public static final int MESSAGE_TYPE_PUBLIC_TIMEZONE_SET_REQ	= 78;
	public static final int MESSAGE_TYPE_PUBLIC_TIMEZONE_SET_RSP	= 79;
	
	public static final int BASE_MSG_FT_HUB = 0x02;
    public static final int BASE_MSG_FT_END = 0x03;
    public static final int BASE_MSG_FT_SERVER = 0x01;
    public static final int BASE_MSG_FT_MOBILE = 0x00;
    public static final int BASE_MSG_FT_REQ = 0x01;
    public static final int BASE_MSG_FT_RCV = 0x02;
}