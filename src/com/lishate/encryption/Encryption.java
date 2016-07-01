package com.lishate.encryption;

import com.lishate.message.*;
import com.lishate.message.deviceswitch.SwitchCountReqMessage;
import com.lishate.message.deviceswitch.SwitchCountRspMessage;
import com.lishate.message.deviceswitch.SwitchGetOnOffReqMessage;
import com.lishate.message.deviceswitch.SwitchGetOnOffRspMessage;
import com.lishate.message.deviceswitch.SwitchSetOnOffRspMessage;
import com.lishate.message.deviceswitch.SwitchSetOnoffReqMessage;
import com.lishate.message.light.LightGetOnoffReqMessage;
import com.lishate.message.light.LightGetOnoffRspMessage;
import com.lishate.message.light.LightGetReqMessage;
import com.lishate.message.light.LightGetRspMessage;
import com.lishate.message.light.LightModeGetReqMessage;
import com.lishate.message.light.LightModeGetRspMessage;
import com.lishate.message.light.LightModeSetReqMessage;
import com.lishate.message.light.LightModeSetRspMessage;
import com.lishate.message.light.LightOffReqMessage;
import com.lishate.message.light.LightOffRspMessage;
import com.lishate.message.light.LightOnReqMessage;
import com.lishate.message.light.LightOnRspMessage;
import com.lishate.message.light.LightRandomChangeReqMessage;
import com.lishate.message.light.LightRandomChangeRspMessage;
import com.lishate.message.light.SetLightDataReqMessage;
import com.lishate.message.light.SetLightDataRspMessage;
import com.lishate.message.light.SetLightDelReqMessage;
import com.lishate.message.light.SetLightDelRspMessage;
import com.lishate.message.light.SetLightModeCountReqMessage;
import com.lishate.message.light.SetLightModeCountRspMessage;
import com.lishate.message.light.SetLightModeItemReqMessage;
import com.lishate.message.light.SetLightModeItemRspMessage;
import com.lishate.message.light.SetLightModeReqMessage;
import com.lishate.message.light.SetLightModeRspMessage;
import com.lishate.message.light.SetLightNewReqMessage;
import com.lishate.message.light.SetLightNewRspMessage;
import com.lishate.message.magnet.MagnetGetStatusReqMessage;
import com.lishate.message.magnet.MagnetGetStatusRspMessage;
import com.lishate.message.magnet.MagnetReportReqMessage;
import com.lishate.message.magnet.MagnetReportRspMessage;
import com.lishate.message.rfcode.FinishRFCodeReqMessage;
import com.lishate.message.rfcode.FinishRFCodeRspMessage;
import com.lishate.message.rfcode.SendRFCodeReqMessage;
import com.lishate.message.rfcode.SendRFCodeRspMessage;
import com.lishate.message.rfcode.StudyRFCodeReqMessage;
import com.lishate.message.rfcode.StudyRFCodeRspMessage;
import com.lishate.message.temperature.GetTemperatureReqMessage;
import com.lishate.message.temperature.GetTemperatureRspMessage;

public class Encryption {
	static {
		System.loadLibrary("lishatejni");
	}
	
	//public static byte[] Encryption(baseMessage msg){return null;}
	//public static baseMessage Decryption(byte[] content){return null;}
	
	//public static int CheckIsMsg(byte[] buf){return 0;}
	
	//public static byte[] Encription(LoginReqMessage lrm){return null;}
	
	
	public native static int init();
	private native static int checkIsMsg(byte[] buf);
	
	private native static byte[] EncriptionLoginReq(LoginReqMessage lrm);
	private native static byte[] EncriptionLoginRsp(LoginRspMessage lrm);
	private native static byte[] EncriptionGetServerReq(GetServerReqMessage gsrm);
	private native static byte[] EncriptionGetServerRsp(GetServerRspMessage gsrm);
	private native static byte[] EncriptionOpenReq(OpenReqMessage orm);
	private native static byte[] EncriptionCloseReq(CloseReqMessage crm);
	private native static byte[] EncriptionGetStatueReq(GetStatueReqMessage gsrm);
	private native static byte[] EncriptionSetConfigReq(SetConfigReqMessage scrm);
	private native static byte[] EncriptionGetServerConfigReq(GetServerConfigReqMessage gsrm);
	private native static byte[] EncriptionSocketDelayReq(SocketDelayReqMessage sdrm);
	private native static byte[] EncriptionPublicVersionReq(PublicVersionReqMessage pvrm);
	private native static byte[] EncriptionPublicTimezonesetReq(PublicTimezoneSetReqMessage ptrm);
	private native static int DecryptionLoginRsp(byte[] buf, LoginRspMessage lrm);
	private native static int DecryptionLoginReq(byte[] buf, LoginReqMessage lrm);
	private native static int DecryptionGetServerReq(byte[] buf, GetServerReqMessage gsrm);
	private native static int DecryptionGetServerRsp(byte[] buf, GetServerRspMessage gsrm);
	private native static int DecryptionOpenRsp(byte[] buf, OpenRspMessage orm);
	private native static int DecryptionCloseRsp(byte[] buf, CloseRspMessage crm);
	private native static int DecryptionGetStatueRsp(byte[] buf, GetStatueRspMessage gsrm);
	private native static int DecryptionSetConfigRsp(byte[] buf, SetConfigRspMessage scrm);
	private native static int DecryptionGetServerConfigRsp(byte[] buf, GetServerConfigRspMessage result);
	private native static int DecryptionSocketDelayRsp(byte[] buf, SocketDelayRspMessage result);
	private native static int DecryptionPublicTimezonesetRsp(byte[] buf, PublicTimezoneSetRspMessage result);
	private native static int DecryptionPublicVersionRsp(byte[] buf, PublicVersionRspMessage result);
	
	public native static void MsgTest(messageTest mt);
	
	public static void Test(){
		GetServerReqMessage bm = new GetServerReqMessage();
		bm.Seq = 1;
		bm.FromHID = 0x80000002;
		bm.FromLID = 0x90000012;
		bm.ToHID = 0x72300000;
		bm.ToLID = 0x90000012;
		
		byte[] buf = EncriptionGetServerReq(bm);
		GetServerReqMessage bm1 = new GetServerReqMessage();
		DecryptionGetServerReq(buf, bm1);
		bm.Seq = 2;
	}
	
	public static baseMessage GetMsg(byte[] buf){
		baseMessage result = null;
		int msgtype = checkIsMsg(buf);
		switch(msgtype){
		case MessageDef.MESSAGE_TYPE_LOGIN_REQ:
			result = new LoginReqMessage();
			if(DecryptionLoginReq(buf, (LoginReqMessage)result) < 0)
			{
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_LOGIN_RSP:
			result = new LoginRspMessage();
			if(DecryptionLoginRsp(buf, (LoginRspMessage)result) < 0)
			{
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_GETSERVER_REQ:
			result = new GetServerReqMessage();
			if(DecryptionGetServerReq(buf, (GetServerReqMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_GETSERVER_RSP:
			result = new GetServerRspMessage();
			if(DecryptionGetServerRsp(buf, (GetServerRspMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_OPEN_RSP:
			result = new OpenRspMessage();
			if(DecryptionOpenRsp(buf, (OpenRspMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_CLOSE_RSP:
			result = new CloseRspMessage();
			if(DecryptionCloseRsp(buf, (CloseRspMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_GET_STATUS_RSP:
			result = new GetStatueRspMessage();
			if(DecryptionGetStatueRsp(buf, (GetStatueRspMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_SET_CONFIG_RSP:
			result = new SetConfigRspMessage();
			if(DecryptionSetConfigRsp(buf, (SetConfigRspMessage)result) < 0){
				result = null;
				
			}
			break;
		case MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_RSP:
			
			result = new GetServerConfigRspMessage();
			if(DecryptionGetServerConfigRsp(buf, (GetServerConfigRspMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_SET_DELAY_ONOFF_RSP:
			result = new SocketDelayRspMessage();
			if(DecryptionSocketDelayRsp(buf, (SocketDelayRspMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_PUBLIC_VERSION_RSP:
			result = new PublicVersionRspMessage();
			if(DecryptionPublicVersionRsp(buf, (PublicVersionRspMessage)result) < 0){
				result = null;
			}
			break;
		case MessageDef.MESSAGE_TYPE_PUBLIC_TIMEZONE_SET_RSP:
			result = new PublicTimezoneSetRspMessage();
			if(DecryptionPublicTimezonesetRsp(buf, (PublicTimezoneSetRspMessage)result) < 0){
				result = null;
			}
			break;
		}
		return result;
	}
	
	public static byte[] GetMsg2Buf(baseMessage msg){
		byte[] buf = null;
		int msgtype = msg.MsgType;
		switch(msgtype){
		case MessageDef.MESSAGE_TYPE_LOGIN_REQ:
			buf = EncriptionLoginReq((LoginReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_LOGIN_RSP:
			buf = EncriptionLoginRsp((LoginRspMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_GETSERVER_REQ:
			buf = EncriptionGetServerReq((GetServerReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_GETSERVER_RSP:
			buf = EncriptionGetServerRsp((GetServerRspMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_OPEN_REQ:
			buf = EncriptionOpenReq((OpenReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_CLOSE_REQ:
			buf = EncriptionCloseReq((CloseReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_GET_STATUS_REQ:
			buf = EncriptionGetStatueReq((GetStatueReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_SET_CONFIG_REQ:
			buf = EncriptionSetConfigReq((SetConfigReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_GET_CONFIG_SERVER_REQ:
			buf = EncriptionGetServerConfigReq((GetServerConfigReqMessage)msg);//EncriptionSetConfigRsp((GetServerconfigRspMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_SET_DELAY_ONOFF_REQ:
			buf = EncriptionSocketDelayReq((SocketDelayReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_PUBLIC_VERSION_REQ:
			buf = EncriptionPublicVersionReq((PublicVersionReqMessage)msg);
			break;
		case MessageDef.MESSAGE_TYPE_PUBLIC_TIMEZONE_SET_REQ:
			buf = EncriptionPublicTimezonesetReq((PublicTimezoneSetReqMessage)msg);
			break;
		}
		return buf;
	}
}
