package com.langsin.qq.client;

import com.langsin.qq.msg.IMsgConstance;
import com.langsin.qq.msg.MsgChat;
import com.langsin.qq.msg.MsgHead;

public interface MyClientMsgListener {
	
	/**
	 * ������յ���һ����Ϣ
	 * @param msg:���յ�����Ϣ����
	 */
	public void dispatchMessage(MsgHead msg);
}
