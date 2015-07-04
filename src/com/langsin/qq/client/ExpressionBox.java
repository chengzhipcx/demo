package com.langsin.qq.client;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.langsin.qq.client.Chat;
import com.langsin.qq.client.Chats;
public class ExpressionBox extends Thread {
	int ID2;
	public ExpressionBox(int x,int id2){
		
		if(x==1){
			doc = MainInterface.chats.get(id2).getInputBox()
					.getStyledDocument();
		}else if(x==2){
			doc = Chats.getInputBox()
					.getStyledDocument();
		}
		ID2 = id2;
	}
	private static StyledDocument doc = null;
	public static String[][] imagePath = {
			{ "ExpressionBox/bq01_wx.png", "ExpressionBox/bq02_se.png",
					"ExpressionBox/bq03_dy.png", "ExpressionBox/bq04_ll.png",
					"ExpressionBox/bq05_dk.png", "ExpressionBox/bq06_fn.png" },
			{ "ExpressionBox/bq07_tp.png", "ExpressionBox/bq08_cy.png",
					"ExpressionBox/bq09_zk.png", "ExpressionBox/bq10_tx.png",
					"ExpressionBox/bq11_ka.png", "ExpressionBox/bq12_lh.png" },
			{ "ExpressionBox/bq13_db.png", "ExpressionBox/bq14_yiw.png",
					"ExpressionBox/bq15_shuai.png",
					"ExpressionBox/bq16_qd.png", "ExpressionBox/bq17_zj.png",
					"ExpressionBox/bq18_gz.png" },
			{ "ExpressionBox/bq19_huaix.png", "ExpressionBox/bq20_zhh.png",
					"ExpressionBox/bq21_yhh.png", "ExpressionBox/bq22_bs.png",
					"ExpressionBox/bq23_wq.png", "ExpressionBox/bq24_yl.png" },
			{ "ExpressionBox/bq25_qq.png", "ExpressionBox/bq26_kel.png",
					"ExpressionBox/bq27_yb.png", "ExpressionBox/bq28_hx.png",
					"ExpressionBox/bq29_aiq.png", "ExpressionBox/bq30_fw.png" } };

	public static String[][] imageName = {
			{ "/wx", "/se", "/dy", "/ll", "/dk", "/fn" },
			{ "/tp", "/cy", "/zk", "/tx", "/ka", "/lh" },
			{ "/db", "/yw", "/su", "/qd", "/zj", "/gz" },
			{ "/hu", "/zh", "/yh", "/bs", "/wq", "/yl" },
			{ "/qq", "/kl", "/yb", "/hx", "/aq", "/fw" } };

	final static JFrame expressionBox = new JFrame("表情盒子");

	public void init() {
		expressionBox.setLayout(null);
		expressionBox.setSize(174, 146);
		expressionBox.setUndecorated(true);
		expressionBox.setAlwaysOnTop(true);
		expressionBox.addMouseMotionListener(new MouseMotionListener() {
			Point p1 = null;

			public void mouseMoved(MouseEvent arg0) {
				p1 = arg0.getLocationOnScreen();
			}

			public void mouseDragged(MouseEvent arg0) {
				Point p2 = arg0.getLocationOnScreen();
				Point p3 = expressionBox.getLocationOnScreen();
				expressionBox.setLocation(p3.x + (p2.x - p1.x), p3.y
						+ (p2.y - p1.y));
				p1 = arg0.getLocationOnScreen();
			}
		});

		expressionBox.setVisible(false);
		expressionBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {

				final int x = i;
				final int y = j;
				JButton bqs = new JButton();
				bqs.setBounds(y * 28 + 3, x * 28 + 3, 27, 27);
				bqs.setIcon(new ImageIcon(imagePath[x][y]));
				bqs.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent arg0) {
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						expressionBox.setVisible(false);
						GetXY.bool = false;
						try { // 插入图片
							doc.insertString(doc.getLength(), "BQ"+imageName[x][y]+"BQ", null);							
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
					}

					@Override
					public void mouseClicked(MouseEvent arg0) {
					}
				});

				expressionBox.add(bqs);
				expressionBox.repaint();
			}
		}
	}

	public static JFrame getFrame() {
		return expressionBox;
	}
}