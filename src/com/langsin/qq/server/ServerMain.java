package com.langsin.qq.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.langsin.qq.util.LogTools;

public class ServerMain extends Thread {
	
	private int port;// �������˿�

	/**
	 * ��������������ʱ�����봫��˿ں�
	 * 
	 * @param port:���������ڶ˿ں�
	 */
	public ServerMain (int port) {
		this.port = port;
	}

	public void run() {// ���߳�������������
		setupServer();
	}
	
	// ��ָ���Ķ˿�������������
		private void setupServer() {
			try {
				ServerSocket sc = new ServerSocket(this.port);
				LogTools.INFO(this.getClass(), "�����������ɹ�:" + port);
				while (true) {
					Socket client = sc.accept();// �ȴ��������
					String cAdd = client.getRemoteSocketAddress().toString();
					LogTools.INFO(this.getClass(), "��������:" + cAdd);
					ServerThread ct = new ServerThread(client);
//					ct.readFirstMsg();
					ct.start();// ����һ�������̣߳�ȥ��������������...
				}
			} catch (Exception ef) {
				LogTools.ERROR(this.getClass(), "����������ʧ��:" + ef);
			}
		}
		
		public static void main(String args[]) {
			ServerMain sm = new ServerMain(9090);
			sm.start();
		}

}
