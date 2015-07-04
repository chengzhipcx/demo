package com.langsin.qq.client;

import java.awt.Color;
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
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class Chats {
	
/**
 * һ����Ҫ������������źŵ�Ԥ����
 * 		1.��197��216�У�������Ϣ��
 * 		2.��622�У����ڶ�����
 * 		3.��759�У���ͼ��
 * 		4.��858�У����ļ���
 * 
 * �������յ����������͵��ź���Ҫ���õķ�����
 * 		1.���������յ�������Ϣ������	chulixiaoxi(str, info);������
 * 		    ��һ������Ϊ���յ����ַ���(String)���ڶ�������Ϊ��2(int).
 * 
 * 		2.���������յ����ļ�������    saveFile(fil);������
 * 		    ����ΪFile����
 * 
 * 		3.���������յ����ͽ�ͼ������	getjietu(ima);������
 * 		    ����ΪimageIcon����
 * 
 * 		4.���������յ���������Ϣ�������������д��롣
 * 		  windowShake ws = new windowShake();
 * 		  ws.start();
 */
	
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
	private static int ID1;
	private static int ID2;
	private final static int HIGHT = 518;
	private final static int WIDTH = 540;

	ImageIcon HeadIcon = new ImageIcon("Icon/headPicture.jpg");// �ڷ������л�öԷ���ͷ��ͼƬ��Ԥ��������
	static String str_myNickName = "�Լ��ǳ�"; // �ڷ������л���Լ����ǳƣ�Ԥ��������
	static String str_nickName = "Ⱥ�ǳ�"; // �ڷ������л��Ⱥ�ǳƣ�Ԥ��������
	String str_Signature = "�ڷ������л��Ⱥ����ǩ��"; // �ڷ������л�öԷ��ĸ���ǩ����Ԥ��������
	static String title = str_nickName;
	final static JFrame jf = new JFrame(title);
	static JTextPane sessionsBox = new JTextPane();
	static JTextPane inputBox = new JTextPane();
	static JButton send = new JButton();
	private static JComboBox<String> fontName = null, fontSize = null,
			fontStyle = null, fontColor = null;// ��������;�ֺŴ�С;������ʽ;������ɫ;
	private static StyledDocument doc = sessionsBox.getStyledDocument();
	private FileInputStream fis;

	Chats(int ID1, int ID2, String title, ImageIcon HeadIcon, String nickName,
			String Signature) {
		Chats.ID1 = ID1;
		Chats.ID2 = ID2;
	}

	Chats() {
	}

	public static void main(String[] args) {
		Chats c = new Chats();
		c.init();
	}

	private void init() {

		initJfram();
		inOutPut();
		addCloseButtonAndMinButton();
		addHeadPicture();
		addNickName();
		addSignature();
//		addExpressionPicture();
		addadmin();
//		addscreenShot();
		addMsgLog();
		addSkin();
		addJfFunction();

	}

	/**
	 * �����������˻Ự������򣬷��Ͱ�ť
	 */

	public static void inOutPut() {

		String[] str_name = { "����", "����", "Dialog", "Gulim" };
		String[] str_Size = { "12", "14", "18", "22", "30", "40" };
		String[] str_Style = { "����", "б��", "����" };
		String[] str_Color = { "��ɫ", "��ɫ", "��ɫ", "��ɫ" };

		fontName = new JComboBox<String>(str_name); // ��������
		fontName.setBounds(13, 489, 60, 22);
		fontSize = new JComboBox<String>(str_Size); // �ֺ�
		fontSize.setBounds(76, 489, 40, 22);
		fontStyle = new JComboBox<String>(str_Style); // ��ʽ
		fontStyle.setBounds(119, 489, 52, 22);
		fontColor = new JComboBox<String>(str_Color); // ��ɫ
		fontColor.setBounds(174, 489, 52, 22);
//		jf.add(fontName);
//		jf.add(fontSize);
//		jf.add(fontStyle);
//		jf.add(fontColor);

		/**
		 * ��ӻỰ��
		 */
		final JScrollPane scrollPane = new JScrollPane();
		jf.getContentPane().add(scrollPane, null);
		sessionsBox.setBounds(10, 64, 370, 300);
		sessionsBox.setEditable(false);
		scrollPane.setViewportView(sessionsBox);
		scrollPane.setBounds(10, 64, 370, 300);
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
						/**
						 * Ԥ����
						 */
					}
				}
			}
		});

		/**
		 * ��ӷ��Ͱ�ť
		 */
		send.setBounds(310, 487, 70, 25);
		send.setIcon(new ImageIcon("Icon/buttonSend1.jpg"));
		send.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				String getInput = inputBox.getText();
				chulixiaoxi(getInput, 1);
				inputBox.setText("");
				/**
				 * Ԥ����
				 */
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
	 * �ֽ���Ϣ�ķ���������Ҫ������
	 * 
	 * @param str
	 */
	private static void chulixiaoxi(String str, int info) {
		FontAttrib att = new FontAttrib();
		if(info==3){
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
							Chats.getSessionsBox().insertIcon(
									new ImageIcon(imagePath[x][y]));
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
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	public static JTextPane getSessionsBox() {
		return sessionsBox;
	}

	public static JTextPane getInputBox() {
		return inputBox;
	}

	public static JFrame getFrame() {
		return jf;
	}

	/**
	 * ���ô���λ�á���С�����ֹ�����
	 */
	private void initJfram() {
		jf.setLayout(null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		jf.setBounds((int) d.getWidth() / 2 - WIDTH / 2, (int) d.getHeight()
				/ 2 - HIGHT / 2, WIDTH, HIGHT);
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

		headPicture.setIcon(HeadIcon); // Ԥ��
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
	 * ��Ӹ���ǩ��
	 */
	private void addSignature() {
		JLabel signature = new JLabel(str_Signature);
		signature.setBounds(59, 34, 370, 25);
		signature.setFont(new Font("Serif", Font.PLAIN, 12));
		jf.add(signature);
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
	 * ��ӱ��鰴ť
	 */
//	private void addExpressionPicture() {
//		final ExpressionBox ebox = new ExpressionBox(2);
//		ebox.init();
//		final JButton expression = new JButton();
//		expression.setBounds(20, 365, 24, 22);
//		expression.setIcon(new ImageIcon("Icon/expression1.jpg"));
//		expression.addMouseListener(new MouseListener() {
//			@Override
//			public void mouseClicked(MouseEvent e) { // �����
//				ExpressionBox.getFrame().setVisible(true);
//				GetXY gxy = new GetXY(2);
//				gxy.start();
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) { // ������
//				expression.setIcon(new ImageIcon("Icon/expression2.jpg"));
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) { // ����˳�
//				expression.setIcon(new ImageIcon("Icon/expression1.jpg"));
//			}
//
//			@Override
//			public void mousePressed(MouseEvent e) { // ��갴��
//				expression.setIcon(new ImageIcon("Icon/expression3.jpg"));
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent e) { // ����ͷ�
//				expression.setIcon(new ImageIcon("Icon/expression2.jpg"));
//			}
//		});
//		expression.setBorderPainted(false);
//
//		jf.add(expression);
//
//	}
	
	private static String path = null;
	static void setPath(String pa){
		path = pa;
	}
	
	/**
	 * ��ӽ�����ť
	 */
//	static JButton yes = null;
//	static JButton no = null;
//	private void addscreenShot() {
//		final JButton screenShot = new JButton();
//		screenShot.setBounds(95, 365, 24, 22);
//		screenShot.setIcon(new ImageIcon("Icon/screenShot1.jpg"));
//		screenShot.addMouseListener(new MouseListener() {
//			@Override
//			public void mouseClicked(MouseEvent e) {// �����
//				// ȫ������
//				GraphicsDevice gd = GraphicsEnvironment
//						.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//				gd.setFullScreenWindow(rd.getJFrame());
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
//		jf.add(screenShot);
//		
		/**
		 * ���ȷ�Ϸ���ͼƬ��ť
		 */
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
//				chulixiaoxi("",1);
//				sessionsBox.insertIcon(jietu);
//				chulixiaoxi("",3);
//				/**
//				 * ��������jietu���������������
//				 * Ԥ����
//				 */
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
	 *  ���ս�ͼ�ķ�������������������ͼʱ���ã�Ԥ������������
	 */
	public void getjietu(ImageIcon ima){
		ImageIcon image = ima;//���ܷ����������� imageIcon����
		chulixiaoxi("", 2);
		sessionsBox.insertIcon(image);
		chulixiaoxi("",3);
	}
	
	private void addSkin() {
		PanelImageTest_Chat panel = new PanelImageTest_Chat();
		panel.repaint();
		panel.setBounds(0, 0, WIDTH, HIGHT);
		jf.add(panel);
	}
	
	/**
	 * ���Ⱥ��Ա��ť
	 */
	private void addadmin(){
		final JButton addadm = new JButton();
		addadm.setBounds(58, 365, 22, 22);
		addadm.setIcon(new ImageIcon("Icon/add1.jpg"));
		addadm.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {// �����
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {// ������
				addadm.setIcon(new ImageIcon("Icon/add2.jpg"));
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				addadm.setIcon(new ImageIcon("Icon/add1.jpg"));
			}

			@Override
			public void mousePressed(MouseEvent e) { // ��갴��
				addadm.setIcon(new ImageIcon("Icon/add3.jpg"));
			}

			@Override
			public void mouseReleased(MouseEvent e) { // ����ͷ�
				addadm.setIcon(new ImageIcon("Icon/add2.jpg"));
			}
		});
		addadm.setBorderPainted(false);
		jf.add(addadm);
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
}
