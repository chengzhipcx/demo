package com.langsin.qq.client;

import javax.swing.JFrame;

import com.langsin.qq.client.Chat;

public class windowShake extends Thread {
	int ID;
	windowShake(int id){
		ID = id;
	}
	
	public void run() {
		int[] x = { -3, 3, -3, 3, 3, -3, 3, -3, -3, 3, -3, 3, 3, -3, 3, -3 };
		int[] y = { -3, 3, 3, -3, 3, -3, -3, 3, -3, 3, 3, -3, 3, -3, -3, 3 };
		for (int i = 0; i < 16; i++) {
			JFrame chat_jf = MainInterface.chats.get(ID).getFrame();
			chat_jf.setLocation(chat_jf.getX() + x[i], chat_jf.getY() + y[i]);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}