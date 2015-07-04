package com.langsin.qq.client;

import com.langsin.qq.msg.IMsgConstance;
import com.langsin.qq.msg.MsgChat;
import com.langsin.qq.msg.MsgHead;

public interface MyClientMsgListener {
	
	/**
	 * 处理接收到的一条消息
	 * @param msg:接收到的消息对象
	 */
	public void dispatchMessage(MsgHead msg);
}
