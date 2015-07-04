package com.langsin.qq.client;
import com.langsin.qq.client.Chat;
import com.langsin.qq.client.Chats;

public class GetXY extends Thread {
	
	public GetXY(int x,int id2){
		this.index = x;
		ID2 = id2;
	}
	public static boolean bool = true;
	static int x;
	static int y;
	int index = 0;
	int ID2;
	public void run() {
		while (bool) {
			if(index==1){
				x = MainInterface.chats.get(ID2).getFrame().getX();
				y = MainInterface.chats.get(ID2).getFrame().getY();
			}else if(index == 2){
				x = Chats.getFrame().getX();
				y = Chats.getFrame().getY();
			}
			ExpressionBox.getFrame().setLocation(x + 20, y + 210);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}
}