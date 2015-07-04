package com.langsin.qq.client;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelImageTest_login extends JPanel implements Runnable {
	public void run() {
		while(true){
			this.repaint();
		}
	}


	private static final long serialVersionUID = 1L;
	static Image image = null;
	public void paint(Graphics g) {
		try {
			image = ImageIO.read(new File("image/login.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(image, 0, 0, null);
	}
	
}
