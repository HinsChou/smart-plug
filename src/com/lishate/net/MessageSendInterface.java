package com.lishate.net;

import com.lishate.message.*;

public interface MessageSendInterface {

	public void beforeSendMsg(baseMessage msg);
	public void afterSendMsg(baseMessage msg);
	public void failSendMsg(baseMessage msg);
}
