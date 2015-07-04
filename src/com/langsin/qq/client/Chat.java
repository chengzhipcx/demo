package com.langsin.qq.client;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.langsin.qq.msg.IMsgConstance;
import com.langsin.qq.msg.MsgFile;
import com.langsin.qq.msg.MsgFileResp;
import com.langsin.qq.msg.MsgHead;

public class Chat {

	/**
	 * һ����Ҫ������������źŵ�Ԥ���� 1.��197��216�У�������Ϣ�� 2.��622�У����ڶ����� 3.��759�У���ͼ�� 4.��858�У����ļ���
	 * 
	 * �������յ����������͵��ź���Ҫ���õķ����� 1.���������յ�������Ϣ������ chulixiaoxi(str, info);������
	 * ��һ������Ϊ���յ����ַ���(String)���ڶ�������Ϊ��2(int).
	 * 
	 * 2.���������յ����ļ������� saveFile(fil);������ ����ΪFile����
	 * 
	 * 3.���������յ����ͽ�ͼ������ getjietu(ima);������ ����ΪimageIcon����
	 * 
	 * 4.���������յ���������Ϣ�������������д��롣 windowShake ws = new windowShake(); ws.start();
	 */
	private int ID1;
	private int ID2;
	private final int HIGHT = 518;
	private final int WIDTH = 540;
	private String headImgSrc;
	private String str_myNickName = "��";
	private String str_nickName;
	private String title;
	private JButton send;
	public JFrame jf;
	private JScrollPane scrollPane;
	public JTextPane sessionsBox = new JTextPane();
	public JTextPane inputBox = new JTextPane();
	private ClientConnection conn = ClientConnection.getIns();
	private JComboBox<String> fontName = null, fontSize = null,
			fontStyle = null, fontColor = null;
	// ��������;�ֺŴ�С;������ʽ;������ɫ;
	private StyledDocument doc = sessionsBox.getStyledDocument();
	private FileInputStream fis;
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

	Chat(int id1, int id2, String nick, String headImg) {
		ID1 = id1;
		ID2 = id2;
		str_nickName = nick;
		headImgSrc = headImg;
	}

	Chat() {
	}

	public void init() {
		initJfram();
		inOutPut();
		addFuctionButton();
		addTransferFiles();
		addCloseButtonAndMinButton();
		addHeadPicture();
		addNickName();
		addRemoteDesktop();
		addExpressionPicture();
		addShake();
		addMsgLog();
		addQQShow();
		addSkin();
		addJfFunction();
	}

	/**
	 * �����������˻Ự������򣬷��Ͱ�ť
	 */

	private void inOutPut() {

		String[] str_name = { "����", "����", "Dialog", "Gulim" };
		String[] str_Size = { "12", "14", "18", "22", "30", "40" };
		String[] str_Style = { "����", "б��", "����" };
		String[] str_Color = { "��ɫ", "��ɫ", "��ɫ", "��ɫ" };
		Dialog d=new Dialog(jf);
		d.dispose();
		fontName = new JComboBox<String>(str_name); // ��������
		fontName.setBounds(13, 489, 60, 22);
		fontSize = new JComboBox<String>(str_Size); // �ֺ�
		fontSize.setBounds(76, 489, 40, 22);
		fontStyle = new JComboBox<String>(str_Style); // ��ʽ
		fontStyle.setBounds(119, 489, 52, 22);
		fontColor = new JComboBox<String>(str_Color); // ��ɫ
		fontColor.setBounds(174, 489, 52, 22);
		d.add(fontName);
		d.add(fontSize);
		d.add(fontStyle);
		d.add(fontColor);
//		jf.add(d);

		/**
		 * ��ӻỰ��
		 */
		scrollPane = new JScrollPane();
		jf.getContentPane().add(scrollPane, null);
		sessionsBox.setBounds(10, 95, 370, 269);
		sessionsBox.setEditable(false);
		scrollPane.setViewportView(sessionsBox);
		scrollPane.setBounds(10, 95, 370, 269);
		jf.getContentPane().add(scrollPane);

		/**
		 * ��������
		 */
		inputBox.setBounds(10, 388, 370, 95);
		JScrollPane scrollPane2 = new JScrollPane(inputBox);
		scrollPane2.setBounds(10, 388, 370, 95);
		jf.getContentPane().add(scrollPane2);
		inputBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyChar() == '\n') {
					String getInput = inputBox.getText();
					char[] gi = getInput.toCharArray();
					boolean check = false;
					for (int i = 0; i < gi.length; i++) {
						if (i % 2 == 0) {
							if (gi[i] != '\r') {
								check = true;
							}
						}
					}
					if (check) {
						chulixiaoxi(getInput, 1);
						inputBox.setText("");
						conn.sendTest(ID1, ID2, getInput);
					}
				}
			}
		});

		/**
		 * ��ӷ��Ͱ�ť
		 */
		send = new JButton();
		send.setBounds(310, 487, 70, 25);
		send.setIcon(new ImageIcon("Icon/buttonSend1.jpg"));
		send.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				String getInput = inputBox.getText();
				chulixiaoxi(getInput, 1);
				inputBox.setText("");
				conn.sendTest(ID1, ID2, getInput);
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				send.setIcon(new ImageIcon("Icon/buttonSend2.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				send.setIcon(new ImageIcon("Icon/buttonSend1.jpg"));
			}
		});
		send.setBorderPainted(false);
		jf.add(send);
	}

	/**
	 * ������Ϣ�ķ���������Ҫ������
	 * 
	 * @param str
	 */
	public void chulixiaoxi(String str, int info) {
		FontAttrib att = new FontAttrib();
		if (info == 3) {
			try {
				doc.insertString(doc.getLength(), "\n", null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			return;
		}
		String str_name = str_myNickName;
		if (info == 2) {
			str_name = str_nickName;
		}
		if (info == 1) {
			str_name = str_myNickName;
		}
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
		String time = format.format(calendar.getTime());
		try {
			doc.insertString(doc.getLength(), str_name + "     " + time + "\n",
					null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		String xiaoxi = str;
		String[] fenjiexiaoxi = xiaoxi.split("BQ");
		for (int i = 0; i < fenjiexiaoxi.length; i++) {
			char[] bq = fenjiexiaoxi[i].toCharArray();
			if (fenjiexiaoxi[i].contains("/") && bq.length == 3 && bq[0] == '/'
					&& bq[1] >= 'a' && bq[1] <= 'z' && bq[2] >= 'a'
					&& bq[2] <= 'z') {
				for (int x = 0; x < 5; x++) {
					for (int y = 0; y < 6; y++) {
						if (fenjiexiaoxi[i].equals(imageName[x][y])) {
							sessionsBox.insertIcon(new ImageIcon(
									imagePath[x][y]));
						}
					}
				}
			} else {
				if (!fenjiexiaoxi[i].equals("")) {
					String get = fenjiexiaoxi[i];
					att.setText(get);
					att.setName((String) fontName.getSelectedItem());
					att.setSize(Integer.parseInt((String) fontSize
							.getSelectedItem()));
					String temp_style = (String) fontStyle.getSelectedItem();
					if (temp_style.equals("����")) {
						att.setStyle(FontAttrib.GENERAL);
					} else if (temp_style.equals("����")) {
						att.setStyle(FontAttrib.BOLD);
					} else if (temp_style.equals("б��")) {
						att.setStyle(FontAttrib.ITALIC);
					}
					String temp_color = (String) fontColor.getSelectedItem();
					if (temp_color.equals("��ɫ")) {
						att.setColor(new Color(0, 0, 0));
					} else if (temp_color.equals("��ɫ")) {
						att.setColor(new Color(255, 0, 0));
					} else if (temp_color.equals("��ɫ")) {
						att.setColor(new Color(0, 0, 255));
					} else if (temp_color.equals("��ɫ")) {
						att.setColor(new Color(255, 255, 0));
					} else if (temp_color.equals("��ɫ")) {
						att.setColor(new Color(0, 255, 0));
					}
					try {
						doc.insertString(doc.getLength(), fenjiexiaoxi[i],
								att.getAttrSet());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			doc.insertString(doc.getLength(), "\n", null);
			sessionsBox.setCaretPosition(doc.getText(0, doc.getLength())
					.length());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	public JTextPane getSessionsBox() {
		return sessionsBox;
	}

	public JTextPane getInputBox() {
		return inputBox;
	}

	public JFrame getFrame() {
		return jf;
	}

	/**
	 * ���ô���λ�á���С�����ֹ�����
	 */
	private void initJfram() {
		title = "�� " + str_nickName + " ������";
		jf = new JFrame(title);
		jf.setLayout(null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		jf.setBounds((int) d.getWidth() / 2 - WIDTH / 2, (int) d.getHeight()
				/ 2 - HIGHT / 2, WIDTH, HIGHT);
		// jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * ��Ӵ��ڰ�ť���رհ�ť��X��
	 */
	private void addCloseButtonAndMinButton() {
		final JButton closee = new JButton();
		closee.setBounds(503, 0, 37, 25);
		closee.setIcon(new ImageIcon("Icon/close1.jpg"));
		closee.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				jf.dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				closee.setIcon(new ImageIcon("Icon/close2.jpg"));
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				closee.setIcon(new ImageIcon("Icon/close1.jpg"));
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
			}
		});
		closee.setBorderPainted(false);
		jf.add(closee);

		/**
		 * ��Ӵ��ڰ�ť����С����ť
		 */
		final JButton minimize = new JButton();
		minimize.setBounds(473, 0, 30, 25);
		minimize.setIcon(new ImageIcon("Icon/minimize1.jpg"));
		minimize.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				jf.setExtendedState(JFrame.ICONIFIED);
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				minimize.setIcon(new ImageIcon("Icon/minimize2.jpg"));
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				minimize.setIcon(new ImageIcon("Icon/minimize1.jpg"));
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
			}
		});
		minimize.setBorderPainted(false);
		jf.add(minimize);

		/**
		 * �رհ�ť
		 */
		final JButton close = new JButton();
		close.setBounds(240, 487, 70, 25);
		close.setIcon(new ImageIcon("Icon/buttonClose1.jpg"));
		close.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				jf.dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				close.setIcon(new ImageIcon("Icon/buttonClose2.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				close.setIcon(new ImageIcon("Icon/buttonClose1.jpg"));
			}
		});
		close.setBorderPainted(false);
		jf.add(close);

	}

	/**
	 * ���ͷ��
	 */
	private void addHeadPicture() {
		JLabel headPicture = new JLabel();
		headPicture.setBounds(10, 10, 40, 40);
		ImageIcon headIcon = new ImageIcon(headImgSrc + ".jpg");
		headPicture.setIcon(headIcon); // Ԥ��
		jf.add(headPicture);
		jf.repaint();
	}

	/**
	 * ����ǳ�
	 */
	private void addNickName() {

		JLabel nickName = new JLabel(str_nickName);
		nickName.setBounds(58, 8, 140, 25);
		nickName.setFont(new Font("Serif", Font.PLAIN, 20));
		jf.add(nickName);
	}

	/**
	 * ���Զ�����水ť
	 */
	private void addRemoteDesktop() {
		final JButton remoteDesktop = new JButton();
		remoteDesktop.setBounds(10, 59, 32, 30);
		remoteDesktop.setIcon(new ImageIcon("Icon/remoteDesktop1.jpg"));
		remoteDesktop.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				// Ԥ��
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				remoteDesktop.setIcon(new ImageIcon("Icon/remoteDesktop2.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				remoteDesktop.setIcon(new ImageIcon("Icon/remoteDesktop1.jpg"));
			}
		});
		remoteDesktop.setBorderPainted(false);
		jf.add(remoteDesktop);
	}

	/**
	 * ��ӹ��ܰ�ť
	 */
	private void addFuctionButton() {
		/**
		 * �����Ƶ��ť
		 */
		JButton shipin = new JButton();
		shipin.setBounds(98, 59, 30, 30);
		shipin.setIcon(new ImageIcon("Icon/shipin.jpg"));
		shipin.setBorderPainted(false);
		jf.add(shipin);

		/**
		 * ���������ť
		 */
		JButton yuyin = new JButton();
		yuyin.setBounds(138, 59, 30, 30);
		yuyin.setIcon(new ImageIcon("Icon/yuyin.jpg"));
		yuyin.setBorderPainted(false);
		jf.add(yuyin);

		/**
		 * ��ӻỰ�鰴ť
		 */
		JButton huihuazu = new JButton();
		huihuazu.setBounds(178, 59, 30, 30);
		huihuazu.setIcon(new ImageIcon("Icon/huihuazu.jpg"));
		huihuazu.setBorderPainted(false);
		jf.add(huihuazu);

		/**
		 * ��Ӹ���Ӧ�ð�ť
		 */
		JButton gengduoyingyong = new JButton();
		gengduoyingyong.setBounds(218, 59, 35, 30);
		gengduoyingyong.setIcon(new ImageIcon("Icon/gengduoyingyong.jpg"));
		gengduoyingyong.setBorderPainted(false);
		jf.add(gengduoyingyong);

	}

	/**
	 * �����Ϣ��¼��ť
	 */
	private void addMsgLog() {
		final JButton messageLogging = new JButton();
		messageLogging.setBounds(300, 365, 75, 22);
		messageLogging.setIcon(new ImageIcon("Icon/messageLogging1.jpg"));
		messageLogging.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				// Ԥ��
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				messageLogging
						.setIcon(new ImageIcon("Icon/messageLogging2.jpg"));
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				messageLogging
						.setIcon(new ImageIcon("Icon/messageLogging1.jpg"));
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				messageLogging
						.setIcon(new ImageIcon("Icon/messageLogging3.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				messageLogging
						.setIcon(new ImageIcon("Icon/messageLogging2.jpg"));
			}
		});
		messageLogging.setBorderPainted(false);
		jf.add(messageLogging);
	}

	/**
	 * ��Ӵ��ڶ�����ť
	 */
	private void addShake() {
		final JButton shake = new JButton();
		shake.setBounds(56, 365, 24, 22);
		shake.setIcon(new ImageIcon("Icon/shake1.jpg"));
		shake.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				MsgHead mh = new MsgHead();
				int totalLen = 4+1+4+4;
				mh.setTotalLen(totalLen);
				mh.setType(IMsgConstance.command_winshake);
				mh.setSrc(ID1);
				mh.setDest(ID2);
				try {
					conn.sendMsg(mh);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				shake.setIcon(new ImageIcon("Icon/shake2.jpg"));
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				shake.setIcon(new ImageIcon("Icon/shake1.jpg"));
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				shake.setIcon(new ImageIcon("Icon/shake3.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				shake.setIcon(new ImageIcon("Icon/shake2.jpg"));
			}
		});
		shake.setBorderPainted(false);
		jf.add(shake);
	}

	/**
	 * ��ӱ��鰴ť
	 */
	private void addExpressionPicture() {
		final ExpressionBox ebox = new ExpressionBox(1, ID2);
		ebox.init();
		final JButton expression = new JButton();
		expression.setBounds(20, 365, 24, 22);
		expression.setIcon(new ImageIcon("Icon/expression1.jpg"));
		expression.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				ExpressionBox.getFrame().setVisible(true);
				GetXY gxy = new GetXY(1, ID2);
				gxy.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				expression.setIcon(new ImageIcon("Icon/expression2.jpg"));
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				expression.setIcon(new ImageIcon("Icon/expression1.jpg"));
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				expression.setIcon(new ImageIcon("Icon/expression3.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				expression.setIcon(new ImageIcon("Icon/expression2.jpg"));
			}
		});
		expression.setBorderPainted(false);

		jf.add(expression);

	}

	private static String path = null;

	public static void setPath(String pa) {
		path = pa;
	}

//	/**
//	 * ��ӽ�����ť
//	 */
//	public JButton yes = null;
//	public JButton no = null;
//
//	public JButton getButtonYes() {
//		return yes;
//	}
//
//	public JButton getButtonNo() {
//		return no;
//	}
//
//	private void addscreenShot() {
//		final JButton screenShot = new JButton();
//		screenShot.setBounds(95, 365, 24, 22);
//		screenShot.setIcon(new ImageIcon("Icon/screenShot1.jpg"));
//		screenShot.addMouseListener(new MouseListener() {
//			@Override
//			public void mouseClicked(MouseEvent e) {// �����
//				// ȫ������
////				RectD rd = new RectD(ID2);
//				GraphicsDevice gd = GraphicsEnvironment
//						.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//				gd.setFullScreenWindow(rd.getJFrame());//
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) {// ������
//				screenShot.setIcon(new ImageIcon("Icon/screenShot2.jpg"));
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) { // ����˳�
//				screenShot.setIcon(new ImageIcon("Icon/screenShot1.jpg"));
//			}
//
//			@Override
//			public void mousePressed(MouseEvent e) { // ��갴��
//				screenShot.setIcon(new ImageIcon("Icon/screenShot3.jpg"));
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent e) { // ����ͷ�
//				screenShot.setIcon(new ImageIcon("Icon/screenShot2.jpg"));
//			}
//		});
//		screenShot.setBorderPainted(false);
////		jf.add(screenShot);-----------------------------------
//
//		/**
//		 * ���ȷ�Ϸ���ͼƬ��ť
//		 */
//		yes = new JButton();
//		yes.setBounds(122, 365, 22, 22);
//		yes.setIcon(new ImageIcon("Icon/yes1.jpg"));
//		yes.setBorderPainted(false);
//		yes.setVisible(false);
//		yes.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent e) {// �����
//
//				ImageIcon jietu = new ImageIcon(path);
//				chulixiaoxi("", 1);
//				sessionsBox.insertIcon(jietu);
//				chulixiaoxi("", 3);
//				conn.sendjietu(ID1, ID2, path);
//				inputBox.setText("");
//				yes.setVisible(false);
//				no.setVisible(false);
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent e) { // ����ͷ�
//				yes.setIcon(new ImageIcon("Icon/yes2.jpg"));
//			}
//
//			@Override
//			public void mousePressed(MouseEvent e) {// ��갴��
//				yes.setIcon(new ImageIcon("Icon/yes3.jpg"));
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) {// ����˳�
//				yes.setIcon(new ImageIcon("Icon/yes1.jpg"));
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) {// ������
//				yes.setIcon(new ImageIcon("Icon/yes2.jpg"));
//			}
//		});
//		jf.add(yes);
//
//		/**
//		 * ���ȡ������ͼƬ��ť
//		 */
//		no = new JButton();
//		no.setBounds(146, 365, 22, 22);
//		no.setIcon(new ImageIcon("Icon/no1.jpg"));
//		no.setBorderPainted(false);
//		no.setVisible(false);
//		no.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent e) {// �����
//				path = null;
//				inputBox.setText("");
//				yes.setVisible(false);
//				no.setVisible(false);
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent e) { // ����ͷ�
//				no.setIcon(new ImageIcon("Icon/no2.jpg"));
//			}
//
//			@Override
//			public void mousePressed(MouseEvent e) {// ��갴��
//				no.setIcon(new ImageIcon("Icon/no3.jpg"));
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) {// ����˳�
//				no.setIcon(new ImageIcon("Icon/no1.jpg"));
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) {// ������
//				no.setIcon(new ImageIcon("Icon/no2.jpg"));
//			}
//		});
//		jf.add(no);
//	}

	/**
	 * ���QQ�㣨������
	 */
	private void addQQShow() {
		JLabel qqShow1 = new JLabel();
		JLabel qqShow2 = new JLabel();
		qqShow1.setBounds(390, 95, 140, 269);
		qqShow1.setIcon(new ImageIcon("Icon/nanqqxiu.jpg")); // Ԥ��
		jf.add(qqShow1);
		qqShow2.setBounds(390, 368, 140, 140);
		qqShow2.setIcon(new ImageIcon("Icon/zijiqqxiu.jpg")); // Ԥ��
		jf.add(qqShow2);
	}

	/**
	 * ��Ӵ��ļ���ť
	 */
	private void addTransferFiles() {
		final JFileChooser fc = new JFileChooser();
		final JButton transferFiles = new JButton();
		transferFiles.setBounds(52, 59, 36, 30);
		transferFiles.setIcon(new ImageIcon("Icon/transferFiles1.jpg"));
		transferFiles.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				int result = fc.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					int len = (int) file.length();
					FileInputStream fiss = null;
					byte[] fileData = null;
					try {
						fiss = new FileInputStream(file);
						fileData = new byte[len];
						fiss.read(fileData);
						conn.sendFile(ID1, ID2, file.getName(), fileData);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					} finally {
						try {
							fiss.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				transferFiles.setIcon(new ImageIcon("Icon/transferFiles2.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				transferFiles.setIcon(new ImageIcon("Icon/transferFiles1.jpg"));
			}
		});
		transferFiles.setBorderPainted(false);
		jf.add(transferFiles);
	}

	/**
	 * ���ļ�Ӧ��
	 */
	public void fileResp(int state) {

		if (state == 0) {
			JOptionPane jop = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "�ļ����ͳɹ����Է��Ѿ����ա�");
			jf.add(jop);
		}else if(state == 1){
			JOptionPane jop = new JOptionPane();
			JOptionPane.showMessageDialog(jf, "�ļ�����ʧ�ܣ��Է��ܾ����ա�");
			jf.add(jop);
		}
	}

	/**
	 * ���洫�������ļ�,���յ������ļ���Ϣ��ʱ�����
	 * 
	 * @throws IOException
	 */
	public void saveFile(MsgFile mf1) {
		MsgFile mf = mf1;
		String fileName = removeSpace(mf.getFileName());
		byte[] fileData = mf.getFileData();
		int i = JOptionPane.showConfirmDialog(null, "�Է����������ļ�:��" + fileName
				+ "�� �Ƿ���գ�", "�����ļ�", JOptionPane.YES_NO_OPTION);
		if (i == 0) {
			final JFileChooser fc = new JFileChooser();
			fc.showSaveDialog(null);
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			File f = fc.getSelectedFile();
			FileOutputStream fous = null;
			BufferedOutputStream bous = null;
			try {
				fous = new FileOutputStream(f);// д�뵽���Ŀ¼��
				bous = new BufferedOutputStream(fous);
				bous.write(fileData);
				MsgFileResp mfr = new MsgFileResp();
				int totaLenResp = 4 + 1 + 4 + 4 + 1;
				mfr.setTotalLen(totaLenResp);
				mfr.setType(IMsgConstance.command_file_resp);
				mfr.setSrc(ID1);
				mfr.setDest(ID2);
				mfr.setState((byte)0);
				conn.sendMsg(mfr);
				JOptionPane jop = new JOptionPane();
				JOptionPane.showMessageDialog(jf, "�ļ�������ϡ�");
				jf.add(jop);

			} catch (IOException ex) {

			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				try {
					fous.close();
				} catch (IOException ex) {
				}
			}
		} else if (i == 1) {
			MsgFileResp mfr = new MsgFileResp();
			int totaLenResp = 4 + 1 + 4 + 4 + 1;
			mfr.setTotalLen(totaLenResp);
			mfr.setType(IMsgConstance.command_file_resp);
			mfr.setSrc(mf.getDest());
			mfr.setDest(mf.getSrc());
			mfr.setState((byte)1);
			try {
				conn.sendMsg(mfr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ���ս�ͼ�ķ�������������������ͼʱ���ã�Ԥ������������
	 */
	public void getjietu(MsgFile ima) {
		MsgFile imageFile = ima;// ���ܷ����������� imageIcon����
		chulixiaoxi("", 2);
		
		String fileName = removeSpace(imageFile.getFileName());
		sessionsBox.insertIcon(new ImageIcon(fileName));
		chulixiaoxi("", 3);
	}

	private void addSkin() {
		PanelImageTest_Chat panel = new PanelImageTest_Chat();
		panel.repaint();
		panel.setBounds(0, 0, WIDTH, HIGHT);
		jf.add(panel);
	}

	/**
	 * �޿��϶�
	 */
	private void addJfFunction() {
		jf.setUndecorated(true);
		jf.addMouseMotionListener(new MouseMotionListener() {
			Point p1 = null;

			public void mouseMoved(MouseEvent arg0) {
				p1 = arg0.getLocationOnScreen();
			}

			public void mouseDragged(MouseEvent arg0) {
				Point p2 = arg0.getLocationOnScreen();
				Point p3 = jf.getLocationOnScreen();
				jf.setLocation(p3.x + (p2.x - p1.x), p3.y + (p2.y - p1.y));
				p1 = arg0.getLocationOnScreen();
			}
		});
		jf.setVisible(true);
	}

	/**
	 * ȥ��String�����\0
	 */
	private static String removeSpace(String str) {
		char[] s = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length; i++) {
			if (s[i] == '\0') {
				break;
			}
			sb.append(s[i]);
		}
		return sb.toString();
	}

}
