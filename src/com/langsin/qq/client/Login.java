package com.langsin.qq.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.langsin.qq.msg.MsgGroupList;
import com.langsin.qq.msg.MsgLoginResp;
import com.langsin.qq.msg.MsgTeamList;
import com.sun.awt.AWTUtilities;

public class Login {
	
	public static JFrame jf_login= new JFrame("��½����");//��½����
	private  JFormattedTextField jtf_id;
	private  JTextField jtf_pwd;
	private final static int HIGHT = 400;
	private final static int WIDTH = 500;

	private ClientConnection conn=ClientConnection.getIns();
	
	private void initJfram() {
		jf_login.setLayout(null);
	
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		jf_login.setBounds((int) d.getWidth() / 2 - WIDTH / 2, (int) d.getHeight()
				/ 2 - HIGHT / 2, WIDTH, HIGHT);
	}
	
	public void addComponent(){
		initJfram();
		addCloseButtonAndMinButton();
		showLogin();
		addSkin();
		addJfFunction();
	}
	
	/**
	 * ��Ӵ��ڰ�ť���رհ�ť��X������С����ť��-��
	 */
	private void addCloseButtonAndMinButton() {
		//�رհ�ť
		final JButton closee = new JButton();
		closee.setBounds(463, 0, 37, 25);
		closee.setIcon(new ImageIcon("Icon/close1.jpg"));
		closee.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				jf_login.dispose();
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
		jf_login.add(closee);

		//��С����ť
		final JButton minimize = new JButton();
		minimize.setBounds(433, 0, 30, 25);
		minimize.setIcon(new ImageIcon("Icon/minimize1.jpg"));
		minimize.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				jf_login.setExtendedState(JFrame.ICONIFIED);
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
		jf_login.add(minimize);
	}
	
	public void showLogin(){
		
		JLabel jl_versions = new JLabel("");
		jtf_id=new JFormattedTextField( );
		jtf_pwd=new JPasswordField();
		JLabel jl_name=new JLabel("QQ��");
		JLabel jl_pwd=new JLabel("��  ��");
		JLabel jl_reg = new JLabel("ע���˺�");
		JLabel jl_findPwd = new JLabel("�һ�����");
		final JButton jb_login = new JButton("��    ½");
		
		jl_versions.setBounds(10, 0, 80, 28);
		jl_name.setBounds(100, 225, 50, 28);//QQ��lable
		jtf_id.setBounds(160, 225, 180, 28);//QQ�������
		jl_reg.setBounds(350, 225, 80, 28);//ע���˺�
		jl_pwd.setBounds(100, 260, 50, 28);//����lable
		jl_findPwd.setBounds(350, 260, 80, 28);//�һ�����
		jtf_pwd.setBounds(160, 260, 180, 28);//���������
		jb_login.setBounds(160, 305, 178, 35);//��¼
		
		jl_reg.setForeground(Color.BLUE);
		jl_reg.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showRegForm();
			}
		});
		
		jl_findPwd.setForeground(Color.BLUE);
		jb_login.setIcon(new ImageIcon("Icon/login1.jpg"));
		jb_login.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {// ����ͷ�
				jb_login.setIcon(new ImageIcon("Icon/login1.jpg"));
			}
			@Override
			public void mousePressed(MouseEvent arg0) { // ��갴��
				jb_login.setIcon(new ImageIcon("Icon/login3.jpg"));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {// ����˳�
				jb_login.setIcon(new ImageIcon("Icon/login1.jpg"));
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {// ������
				jb_login.setIcon(new ImageIcon("Icon/login2.jpg"));
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {// �����
				loginAction();
			}
		});
		jtf_pwd.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyChar() == '\n') {
					loginAction();
				}
			}
		});
		
		jf_login.add(jl_versions);
		jf_login.add(jl_name);
		jf_login.add(jtf_id);
		jf_login.add(jl_reg);
		jf_login.add(jl_pwd);
		jf_login.add(jtf_pwd);
		jf_login.add(jl_findPwd);
		jf_login.add(jb_login);
		
		javax.swing.JButton bu_login=new javax.swing.JButton("Login");
		bu_login.setActionCommand("login");
		javax.swing.JButton bu_Register=new javax.swing.JButton("Register");
		bu_Register.setActionCommand("reg");
		
	}
	
	/**
	 * ��¼
	 */
	private void loginAction(){
		//1.ȡ�������QQ�ź�����
	 	String getID=jtf_id.getText().trim();
		int qqNum=Integer.parseInt(getID);
		String pwd=jtf_pwd.getText();
		//2.�����Ϸ�����
		if(conn.connToServer()){//����������Ϸ�����
			//3.��½
			MsgLoginResp mlre = conn.loginServer(qqNum, pwd);
			if(mlre.getState()==(byte)0){
				MsgTeamList mtl = conn.getTeamList();//��ú��ѷ��б�
				MsgGroupList mgl = conn.getGroupList();//�����Ⱥ�б�
				//4.��ʾ���������� //��½�ɹ��ˣ�Ҫ�ص���½����
				MainInterface mainUI = new MainInterface(qqNum,mlre,mtl,mgl);
				mainUI.showMainUI();				
				conn.start();//5.���������߳�
				//6.���û����Ӹ��������,��Ϊ��Ϣ������
				conn.addMsgListener(qqNum,mainUI);
				jf_login.dispose();//�رյ�½����
			}else{
				conn.closeMe();
				JOptionPane.showMessageDialog(jf_login, "��½ʧ��,��ȷ���ʺ���ȷ!");
			}
		}else{
			conn.closeMe();
		JOptionPane.showMessageDialog(jf_login, "����ʧ��,��ȷ�Ϸ���������,IP�Ͷ˿���ȷ!");
		}
	}	
	
	
	/**
	 * ע��
	 */
	String headImgSrc = null;
	private void showRegForm(){
		
		final JFrame jf_reg=new JFrame("��ע��:");
		jf_reg.setSize(300,220);
		jf_reg.setLayout(null);
		
		final JTextField jta_regNikeName=new JTextField(16);
		final JTextField jta_regPwd=new JTextField(12);
		JLabel la_regName=new JLabel("�� ��:");
		JLabel la_regPwd=new JLabel("��  ��:");
		JLabel la_head = new JLabel("ѡ��ͷ��:");
		final JButton bu_reg=new JButton("ע���û�");
		JButton head_qie = new JButton();
		JButton head_boy = new JButton();
		JButton head_girl = new JButton();
		
		la_regName.setBounds(30, 20, 50, 28);
		la_regPwd.setBounds(30, 55, 50, 28);
		jta_regNikeName.setBounds(80, 20, 180, 28);
		jta_regPwd.setBounds(80, 55, 180, 28);
		la_head.setBounds(30, 102, 60, 28);
		head_qie.setBounds(100, 90, 40, 40);
		head_boy.setBounds(155, 90, 40, 40);
		head_girl.setBounds(210, 90, 40, 40);
		bu_reg.setBounds(80, 140, 180, 35);
		head_qie.setIcon(new ImageIcon("Icon/headImg_qie.jpg"));
		head_boy.setIcon(new ImageIcon("Icon/headImg_boy.jpg"));
		head_girl.setIcon(new ImageIcon("Icon/headImg_gir.jpg"));
		
		head_qie.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				headImgSrc = "Icon/headImg_qie";
			}
		});
		head_boy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				headImgSrc = "Icon/headImg_boy";
			}
		});
		head_girl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				headImgSrc = "Icon/headImg_gir";
			}
		});
		
		bu_reg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 //ȡ��ע����سƺ�����
				 String nikeName=jta_regNikeName.getText().trim();
				 String pwd=jta_regPwd.getText().trim();
				 String s="����������ʧ��!";
				 if(ClientConnection.getIns().connToServer()){//����������Ϸ�����
					 int qqNum=conn.regServer(nikeName, pwd , headImgSrc);
					 s="ע��ʧ��,��ʶ��:"+qqNum;
					 if(qqNum!=-1){
						 s="ע��ɹ�,���QQ��:"+qqNum;
					 }
				}
				JOptionPane.showMessageDialog(jf_reg, s);
				conn.closeMe();
				jf_reg.dispose();
			}
		});
		
		jf_reg.add(la_regName);
		jf_reg.add(jta_regNikeName);
		jf_reg.add(la_regPwd);
		jf_reg.add(jta_regPwd);
		jf_reg.add(la_head);
		jf_reg.add(head_qie);
		jf_reg.add(head_boy);
		jf_reg.add(head_girl);
		jf_reg.add(bu_reg);
		
		jf_reg.setLocationRelativeTo(null);//����
		jf_reg.setVisible(true);
       
	}
	
	
	/**
	 * ��ӱ���ͼƬ
	 */
	private void addSkin() {
		
		PanelImageTest_login panel = new PanelImageTest_login();
		panel.repaint();
		panel.setBounds(0, 0, WIDTH, HIGHT);
		jf_login.add(panel);
	}
	
	/**
	 * �޿��϶�
	 */
	private void addJfFunction() {
		jf_login.setUndecorated(true);
		AWTUtilities.setWindowOpaque(jf_login, false);
		jf_login.addMouseMotionListener(new MouseMotionListener() {
			Point p1 = null;

			public void mouseMoved(MouseEvent arg0) {
				p1 = arg0.getLocationOnScreen();
			}

			public void mouseDragged(MouseEvent arg0) {
				Point p2 = arg0.getLocationOnScreen();
				Point p3 = jf_login.getLocationOnScreen();
				jf_login.setLocation(p3.x + (p2.x - p1.x), p3.y + (p2.y - p1.y));
				p1 = arg0.getLocationOnScreen();
			}
		});
		jf_login.setVisible(true);
	}
}
