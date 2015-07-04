package com.langsin.qq.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.langsin.qq.model.GroupInfo;
import com.langsin.qq.model.UserInfo;
import com.langsin.qq.msg.IMsgConstance;
import com.langsin.qq.msg.MsgAddFriend;
import com.langsin.qq.msg.MsgAddFriendResp;
import com.langsin.qq.msg.MsgChat;
import com.langsin.qq.msg.MsgCreateGroupResp;
import com.langsin.qq.msg.MsgFile;
import com.langsin.qq.msg.MsgFileResp;
import com.langsin.qq.msg.MsgFindGroupResp;
import com.langsin.qq.msg.MsgFindUsersResp;
import com.langsin.qq.msg.MsgGroupList;
import com.langsin.qq.msg.MsgHead;
import com.langsin.qq.msg.MsgLoginResp;
import com.langsin.qq.msg.MsgTeamList;

public class MainInterface extends JPanel implements MyClientMsgListener {

	private static final long serialVersionUID = 1L;

	public int ID;
	public MsgLoginResp mlr;
	public MsgGroupList mgl;
	private String gName;
	MsgTeamList mtl;
	String[] possibleValues;

	MainInterface(int ID, MsgLoginResp mlre, MsgTeamList mtl, MsgGroupList mgl) {
		this.ID = ID;
		this.mlr = mlre;
		this.mtl = mtl;
		this.mgl = mgl;
	}

	public static JFrame jf_main;// ������
	JPanel friend = new JPanel(); // �����б�panel
	JPanel groupPanel = new JPanel();
	private final static int HIGHT = 675;
	private final static int WIDTH = 280;
	public static Map<Integer, Chat> chats = new HashMap<Integer, Chat>();
	Map<String, UserInfo> fMapByName = new HashMap<String, UserInfo>();
	Map<Integer, UserInfo> fMapByID = new HashMap<Integer, UserInfo>();
	Map<String, ArrayList<UserInfo>> gMapByName = new HashMap<String, ArrayList<UserInfo>>();
	private ClientConnection conn = ClientConnection.getIns();

	public void showMainUI() {
		initJfram();
		addCloseButtonAndMinButton();

		mainUI();
		addFindFriendButton();
//		addFindGroupButton();
		addSkin();
		addJfFunction();
	}

	private void initJfram() {
		jf_main = new JFrame(removeSpace(mlr.getNikeName()));
		jf_main.setLayout(null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		jf_main.setBounds((int) d.getWidth() - WIDTH - 30, (int) d.getHeight()
				/ 2 - HIGHT / 2 - 10, WIDTH, HIGHT);
		jf_main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * ��Ӵ��ڰ�ť���رհ�ť��X������С����ť��-��
	 */
	private void addCloseButtonAndMinButton() {
		// �رհ�ť
		final JButton closee = new JButton();
		closee.setBounds(243, 0, 37, 25);
		closee.setIcon(new ImageIcon("Icon/close1.jpg"));
		closee.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				jf_main.dispose();
				System.exit(0);
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
		jf_main.add(closee);

		// ��С����ť
		final JButton minimize = new JButton();
		minimize.setBounds(213, 0, 30, 25);
		minimize.setIcon(new ImageIcon("Icon/minimize1.jpg"));
		minimize.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // �����
				jf_main.setExtendedState(JFrame.ICONIFIED);
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
		jf_main.add(minimize);
	}

	/**
	 * ��ʾ������
	 */
	private void mainUI() {
		JButton bu_healImg = new JButton();
		JLabel la_nikeName = new JLabel(removeSpace(mlr.getNikeName()));

		bu_healImg.setBounds(15, 50, 90, 90);
		bu_healImg.setIcon(new ImageIcon(removeSpace(mlr.getHeadImgSrc())
				+ "1.jpg"));
		la_nikeName.setBounds(115, 100, 180, 30);
		la_nikeName.setFont(new Font("����", Font.BOLD, 24));

		JButton jb_friend = new JButton();
		JButton jb_group = new JButton();
		jb_friend.setIcon(new ImageIcon("Icon/myFriends.jpg"));
		jb_group.setIcon(new ImageIcon("Icon/myGroups.jpg"));
		jb_friend.setBounds(15, 155, 125, 25);
		jb_group.setBounds(140, 155, 125, 25);

		jb_friend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				groupPanel.setVisible(false);
				friend.setVisible(true);
				jf_main.repaint();
			}
		});
		jb_group.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				friend.setVisible(false);
				groupPanel.setVisible(true);
				jf_main.repaint();
			}
		});
		friendPanl();
		groupPanl();

		jf_main.add(bu_healImg);
		jf_main.add(la_nikeName);
		jf_main.add(jb_friend);
		jf_main.add(jb_group);

	}

	/**
	 * ���Ⱥ��panel
	 */
	IconNode root2;
	JTree jtree2;

	private void groupPanl() {
		groupPanel.setBounds(15, 180, 250, 460);
		groupPanel.setLayout(null);
		root2 = new IconNode(null, "Ⱥ���б�");
		/**
		 * ����JTree
		 */
		for (int i = 0; i < mgl.getGroupList().size(); i++) {
			IconNode roots = new IconNode(null, removeSpace(mgl.getGroupList()
					.get(i).getGName()));
			gMapByName.put(removeSpace(mgl.getGroupList().get(i).getGName()),
					mgl.getGroupList().get(i).getGroupsUsers());
			root2.add(roots);
		}
		JScrollPane scrollPane = new JScrollPane();
		jtree2 = new JTree(root2);
		jtree2.setEditable(false);
		jtree2.setCellRenderer(new IconNodeRenderer()); // ���õ�Ԫ������
		jtree2.setToggleClickCount(1);// ���õ�������չ�����ڵ�
		jtree2.setFont(new Font("����", Font.BOLD, 18));
		scrollPane.setViewportView(jtree2);
		scrollPane.setBounds(0, 0, 250, 460);
		groupPanel.add(scrollPane);
		jtree2.setRowHeight(30);// ����ÿ����Ԫ��ĸ߶�
		jtree2.setOpaque(false);
		DefaultTreeCellRenderer cellRender = (DefaultTreeCellRenderer) jtree2
				.getCellRenderer();
		cellRender.setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
		jtree2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)// ˫���ڵ�
				{
					TreePath path = jtree2.getSelectionPath();// ��ȡѡ�нڵ�·��
					IconNode node = (IconNode) path.getLastPathComponent();// ͨ��·����ָ��ָ��ýڵ�
					if (node.isLeaf())// ����ýڵ���Ҷ�ӽڵ�
					{
//						 int ID2 = fMapByName.get(node.getText()).getID();
//						System.out.println("MainInterface 216 :"
//								+ node.getText());
//						 if (chats.get(ID2) == null) {
//						 Chat c = new Chat(ID, ID2, removeSpace(node
//						 .getText()), removeSpace(fMapByName.get(
//						node.getText()).getHeadImgSrc()));
//						 chats.put(ID2, c);
//						 c.init();
//						 jtree.repaint();// �ػ������
//						 } else {
//						 chats.get(ID2).getFrame().setVisible(true);
//						}
					} else {// ����Ҷ�ӽڵ�
					}

				}
			}
		});
		jf_main.add(groupPanel);
	}

	IconNode root;
	Map<String, IconNode> rootMap = new HashMap<String, IconNode>();
	JTree jtree;

	private void friendPanl() {

		friend.setBounds(15, 180, 250, 460);
		friend.setLayout(null);
		root = new IconNode(null, "�����б�");
		/**
		 * ����JTree
		 */
		possibleValues = new String[mtl.getTeamLists().size() + 1];
		possibleValues[0] = "�½�����";
		for (int i = 0; i < mtl.getTeamLists().size(); i++) {
			IconNode roots = new IconNode(null, removeSpace(mtl.getTeamLists()
					.get(i).getTName()));
			possibleValues[i + 1] = removeSpace(mtl.getTeamLists().get(i)
					.getTName());
			for (int j = 0; j < mtl.getTeamLists().get(i).getTeamUsers().size(); j++) {
				int getID = mtl.getTeamLists().get(i).getTeamUsers().get(j)
						.getID();
				String getName = removeSpace(mtl.getTeamLists().get(i)
						.getTeamUsers().get(j).getNikeName());
				String getHeadImg = removeSpace(mtl.getTeamLists().get(i)
						.getTeamUsers().get(j).getHeadImgSrc());

				IconNode rootss = new IconNode(new ImageIcon(getHeadImg
						+ "2.jpg"), getName);
				roots.add(rootss);
				UserInfo user = new UserInfo(getName, getID);
				user.setHeadImgSrc(getHeadImg);
				fMapByName.put(getName, user);
				fMapByID.put(getID, user);
			}
			root.add(roots);
			rootMap.put(removeSpace(mtl.getTeamLists().get(i).getTName()),
					roots);
		}
		JScrollPane scrollPane = new JScrollPane();
		jtree = new JTree(root);
		jtree.setEditable(false);
		jtree.setCellRenderer(new IconNodeRenderer()); // ���õ�Ԫ������
		jtree.setToggleClickCount(1);// ���õ�������չ�����ڵ�
		jtree.setFont(new Font("����", Font.BOLD, 18));
		scrollPane.setViewportView(jtree);
		scrollPane.setBounds(0, 0, 250, 460);
		friend.add(scrollPane);
		jtree.setRowHeight(30);// ����ÿ����Ԫ��ĸ߶�
		jtree.setOpaque(false);
		DefaultTreeCellRenderer cellRender = (DefaultTreeCellRenderer) jtree
				.getCellRenderer();
		cellRender.setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
		jtree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)// ˫���ڵ�
				{
					TreePath path = jtree.getSelectionPath();// ��ȡѡ�нڵ�·��
					IconNode node = (IconNode) path.getLastPathComponent();// ͨ��·����ָ��ָ��ýڵ�
					if (node.isLeaf())// ����ýڵ���Ҷ�ӽڵ�
					{
						int ID2 = fMapByName.get(node.getText()).getID();
						if (chats.get(ID2) == null) {
							Chat c = new Chat(ID, ID2, removeSpace(node
									.getText()), removeSpace(fMapByName.get(
									node.getText()).getHeadImgSrc()));
							chats.put(ID2, c);
							c.init();
							jtree.repaint();// �ػ������
						} else {
							chats.get(ID2).getFrame().setVisible(true);
						}

					} else {// ����Ҷ�ӽڵ�
					}
				}
			}
		});

		friend.setVisible(true);
		jf_main.add(friend);
	}

	UserInfo heUi = null;
	String tName = null;

	public void dispatchMessage(MsgHead m) {
		if (m.getType() == IMsgConstance.command_findUsers_resp) {// ����ǲ��Һ��ѷ�����Ϣ
			addFriendFrame(m);
		} else if (m.getType() == IMsgConstance.command_addFriend) {// ����Ǳ���Ӻ�����Ϣ

			MsgAddFriend maf = (MsgAddFriend) m;
			JOptionPane.showMessageDialog(this, "������: " + maf.getFID()
					+ " ��Ϊ����,�����ú��ѷ���");

			String selectedValue = (String) JOptionPane.showInputDialog(null,
					"�����������", "SelectTeamName",
					JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
					possibleValues[0]);
			if ("�½�����".equals(selectedValue)) {
				selectedValue = JOptionPane.showInputDialog("�������½��ķ�����");
			}
			heUi = maf.getUi();
			tName = selectedValue;
			UserInfo iUi = new UserInfo(removeSpace(mlr.getNikeName()), ID);
			iUi.setHeadImgSrc(removeSpace(mlr.getHeadImgSrc()));
			conn.addUser(maf.getDest(), maf.getFID(), iUi, selectedValue, 1);
			resetJtree();
		} else if (m.getType() == IMsgConstance.command_addFriend_Resp) {// �������Ӻ���Ӧ��
			MsgAddFriendResp mafr = (MsgAddFriendResp) m;
			heUi = mafr.getUi();
			resetJtree();

		} else if (m.getType() == IMsgConstance.command_chat) {// �����������Ϣ
			MsgChat mt = (MsgChat) m;
			int src = mt.getSrc();
			String xiaoxi = mt.getMsgContent();
			if (chats.get(src) == null) {
				// ����һ��Chat����
				Chat c = new Chat(ID, src, removeSpace(fMapByID.get(src)
						.getNikeName()), removeSpace(fMapByID.get(src)
						.getHeadImgSrc()));
				chats.put(src, c);
				c.init();
				c.chulixiaoxi(xiaoxi, 2);

			} else {
				chats.get(mt.getSrc()).getFrame().setVisible(true);
				chats.get(mt.getSrc()).chulixiaoxi(xiaoxi, 2);
			}

		} else if (m.getType() == IMsgConstance.command_file) {
			MsgFile mf = (MsgFile) m;
			int src = mf.getSrc();
			if (chats.get(src) == null) {
				// ����һ��Chat����
				Chat c = new Chat(ID, src, removeSpace(fMapByID.get(src)
						.getNikeName()), removeSpace(fMapByID.get(src)
						.getHeadImgSrc()));
				chats.put(src, c);
				c.init();
				c.saveFile(mf);

			} else {
				chats.get(mf.getSrc()).getFrame().setVisible(true);
				chats.get(mf.getSrc()).saveFile(mf);
			}

		} else if (m.getType() == IMsgConstance.command_file_resp) {
			MsgFileResp mfr = (MsgFileResp) m;
			int src = mfr.getSrc();
			chats.get(src).getFrame().setVisible(true);
			chats.get(src).fileResp(mfr.getState());
		} else if (m.getType() == IMsgConstance.command_prtsc) {
			MsgFile mf = (MsgFile) m;
			int src = mf.getSrc();
			if (chats.get(src) == null) {
				// ����һ��Chat����
				Chat c = new Chat(ID, src, removeSpace(fMapByID.get(src)
						.getNikeName()), removeSpace(fMapByID.get(src)
						.getHeadImgSrc()));
				chats.put(src, c);
				c.init();
				c.saveFile(mf);

			} else {
				chats.get(mf.getSrc()).getFrame().setVisible(true);
				chats.get(mf.getSrc()).getjietu(mf);
			}

		} else if (m.getType() == IMsgConstance.command_winshake) {
			int src = m.getSrc();
			if (chats.get(src) == null) {
				// ����һ��Chat����
				Chat c = new Chat(ID, src, removeSpace(fMapByID.get(src)
						.getNikeName()), removeSpace(fMapByID.get(src)
						.getHeadImgSrc()));
				chats.put(src, c);
				c.init();
				windowShake ws = new windowShake(src);
				ws.start();

			} else {
				chats.get(m.getSrc()).getFrame().setVisible(true);
				windowShake ws = new windowShake(src);
				ws.start();
			}

		} else if (m.getType() == IMsgConstance.command_findGroup_resp) {
			MsgFindGroupResp mfgr = (MsgFindGroupResp) m;
			addGroupFrame(mfgr);
		} else if (m.getType() == IMsgConstance.command_createGroup_resp) {// ����Ǵ�����ȺӦ��
			MsgCreateGroupResp mcgr = (MsgCreateGroupResp) m;

			UserInfo i = new UserInfo(ID);
			i.setNikeName(removeSpace(mlr.getNikeName()));
			i.setHeadImgSrc(removeSpace(mlr.getHeadImgSrc()));

			ArrayList<UserInfo> li = new ArrayList<UserInfo>();
			li.add(i);

			GroupInfo newgi = new GroupInfo();
			newgi.setGID(mcgr.getGID());
			newgi.setGName(gName);
			newgi.setGMaster(ID);
			newgi.setGroupsUsers(li);
			// ///////////////////////////////////////////////////////////////////////////////////////////a

		} else { // ����Ϣ�����װΪ�½��Ľڵ�
			javax.swing.JOptionPane.showMessageDialog(this,
					"ʲô��Ϣ?����:" + m.getType());
		}
		javax.swing.SwingUtilities.updateComponentTreeUI(this);// ˢ�½���
	}

	private void resetJtree() {
		System.out.println("MainInterface328: ( " + ID + " )   tName=" + tName
				+ "   heUi=" + heUi);
		IconNode roots = null;
		if (rootMap.get(removeSpace(tName)) != null) {
			roots = rootMap.get(tName);
			rootMap.remove(removeSpace(tName));
			root.remove(roots);
		} else if (rootMap.get(tName) == null) {
			roots = new IconNode(null, removeSpace(tName));
			// String �����һ��Ԫ��
			possibleValues = copyStringArray(possibleValues, removeSpace(tName));
			System.out.println("MainInterface 341:   ������һ�������ڵ�: "
					+ roots.getText());
		}
		roots.add(new IconNode(new ImageIcon(removeSpace(heUi.getHeadImgSrc())
				+ "2.jpg"), removeSpace(heUi.getNikeName())));
		rootMap.put(removeSpace(tName), roots);
		root.add(roots);
		fMapByID.put(heUi.getID(), heUi);
		fMapByName.put(removeSpace(heUi.getNikeName()), heUi);
		jtree.updateUI();
		jf_main.repaint();
	}

	private String[] copyStringArray(String[] s1, String str) {
		String[] s = new String[s1.length + 1];
		for (int i = 0; i < s1.length; i++) {
			s[i] = s1[i];
		}
		s[s1.length] = str;
		return s;
	}

	/**
	 * ��Ӳ��Һ��Ѱ�ť
	 */
	private void addFindFriendButton() {

		final JButton addFriend = new JButton();
		addFriend.setBounds(15, 645, 55, 20);
		addFriend.setIcon(new ImageIcon("Icon/findFriend1.jpg"));
		addFriend.setBorderPainted(false);
		addFriend.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				addFriend.setIcon(new ImageIcon("Icon/findFriend2.jpg"));
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				addFriend.setIcon(new ImageIcon("Icon/findFriend3.jpg"));
				conn.findUser(ID);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				addFriend.setIcon(new ImageIcon("Icon/findFriend1.jpg"));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				addFriend.setIcon(new ImageIcon("Icon/findFriend2.jpg"));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		jf_main.add(addFriend);
	}

	/**
	 * ���Ѱ��Ⱥ�鰴ť
	 */
	private void addFindGroupButton() {

		final JButton addGroup = new JButton();
		addGroup.setBounds(80, 645, 55, 20);
		addGroup.setIcon(new ImageIcon("Icon/findFrien"));
		addGroup.setBorderPainted(false);
		addGroup.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				addGroup.setIcon(new ImageIcon("Icon/findFrien"));
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				addGroup.setIcon(new ImageIcon("Icon/findFrien"));
				MsgHead mh = new MsgHead();
				int totalLen = 4 + 1 + 4 + 4;
				mh.setTotalLen(totalLen);
				mh.setType(IMsgConstance.command_findGroup);
				mh.setSrc(ID);
				mh.setDest(IMsgConstance.Server_QQ_NUMBER);
				try {
					conn.sendMsg(mh);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				addGroup.setIcon(new ImageIcon("Icon/findFrie.jpg"));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				addGroup.setIcon(new ImageIcon("Icon/findFrie.jpg"));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		jf_main.add(addGroup);
	}

	private List<UserInfo> findusers;

	private void addFriendFrame(MsgHead msg) {

		int width = 275;
		int hight = 240;
		final JFrame jf_addfri = new JFrame("����/��������û�");
		jf_addfri.setLayout(null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		jf_addfri.setBounds((int) d.getWidth() / 2 - width / 2,
				(int) d.getHeight() / 2 - hight / 2, width, hight);

		MsgFindUsersResp mfur = (MsgFindUsersResp) msg;
		findusers = mfur.getUsers();
		String[][] users = new String[findusers.size()][2];
		for (int i = 0; i < findusers.size(); i++) {
			users[i][0] = String.valueOf(findusers.get(i).getID());
			users[i][1] = removeSpace(findusers.get(i).getNikeName());
		}
		String[] title = { "ID", "NikeName" };
		final JTable table = new JTable(users, title);
		table.setBounds(0, 0, 260, 200);
		table.setRowHeight(20);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (table.getValueAt(table.getSelectedRow(), 0) != null) {
					String s = (String) table.getValueAt(
							table.getSelectedRow(), 0); // ��ȡ��ѡ�е�λ������ָ��������е�1��
					int qqNum = Integer.parseInt(s);

					String selectedValue = (String) JOptionPane
							.showInputDialog(null, "�����������", "Input",
									JOptionPane.INFORMATION_MESSAGE, null,
									possibleValues, possibleValues[0]);
					if ("�½�����".equals(selectedValue)) {
						selectedValue = JOptionPane
								.showInputDialog("�������½��ķ�����");
					}
					UserInfo iUi = new UserInfo(removeSpace(mlr.getNikeName()),
							ID);
					iUi.setHeadImgSrc(removeSpace(mlr.getHeadImgSrc()));
					tName = selectedValue;
					conn.addUser(ID, qqNum, iUi, selectedValue, 0);
					jf_addfri.dispose();
				}
				repaint();
			}
		});

		jf_addfri.add(table);
		jf_addfri.setVisible(true);

	}

	List<GroupInfo> findGroups;

	private void addGroupFrame(MsgFindGroupResp msg) {

		int width = 275;
		int hight = 287;
		final JFrame jf_addgro = new JFrame("����/���/������Ⱥ");
		jf_addgro.setLayout(null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		jf_addgro.setBounds((int) d.getWidth() / 2 - width / 2,
				(int) d.getHeight() / 2 - hight / 2, width, hight);

		MsgFindGroupResp mfgr = (MsgFindGroupResp) msg;
		findGroups = mfgr.getGroups();
		String[][] groups = new String[findGroups.size()][3];
		for (int i = 0; i < findGroups.size(); i++) {
			groups[i][0] = String.valueOf(findGroups.get(i).getGID());
			groups[i][1] = removeSpace(findGroups.get(i).getGName());
			groups[i][2] = String.valueOf(findGroups.get(i).getGMaster());
		}
		String[] title = { "GID", "GroupName", "GroupMaster" };
		JLabel jl1 = new JLabel("ȺID");
		JLabel jl2 = new JLabel("Ⱥ����");
		JLabel jl3 = new JLabel("Ⱥ��ID");
		jl1.setBounds(1, 0, 80, 20);
		jl2.setBounds(88, 0, 80, 20);
		jl3.setBounds(175, 0, 80, 20);
		jf_addgro.add(jl1);
		jf_addgro.add(jl2);
		jf_addgro.add(jl3);
		final JTable table = new JTable(groups, title);
		table.setBounds(0, 20, 260, 200);
		table.setRowHeight(20);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (table.getValueAt(table.getSelectedRow(), 0) != null) {
					String s = (String) table.getValueAt(
							table.getSelectedRow(), 0); // ��ȡ��ѡ�е�λ������ָ��������е�1��
					int gid = Integer.parseInt(s);
					JOptionPane jop = new JOptionPane();
					int i = JOptionPane.showConfirmDialog(null, "�Ƿ�����Ⱥ��",
							"���Ⱥ", JOptionPane.YES_NO_OPTION);
					jf_addgro.add(jop);

					if (i == 0) {// ��

					} else if (i == 1) {// ��

					}
					// GroupInfo igi = new GroupInfo(removeSpace())
					// UserInfo iUi = new
					// UserInfo(removeSpace(mlr.getNikeName()),
					// ID);
					// iUi.setHeadImgSrc(removeSpace(mlr.getHeadImgSrc()));
					// tName = selectedValue;
					// conn.addUser(ID, qqNum, iUi, selectedValue, 0);
					// jf_addfri.dispose();
				}
				repaint();
			}
		});

		final JTextField jtf = new JTextField("�����봴����Ⱥ����");
		jtf.setBounds(0, 220, 150, 30);
		JButton jb = new JButton("������Ⱥ");
		jb.setBounds(150, 220, 110, 30);
		jb.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				gName = jtf.getText();
				conn.createGroup(ID, gName);
			}// /////////////////////////////////////////////////////////////////////////////////a
		});

		jf_addgro.add(jtf);
		jf_addgro.add(jb);
		jf_addgro.add(table);
		jf_addgro.setVisible(true);

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

	/**
	 * ��ӱ���ͼƬ
	 */
	private void addSkin() {
		PanelImageTest_MainUI panel = new PanelImageTest_MainUI();
		panel.repaint();
		panel.setBounds(0, 0, WIDTH, HIGHT);
		jf_main.add(panel);
	}

	/**
	 * �޿��϶�
	 */
	private void addJfFunction() {
		jf_main.setUndecorated(true);
		jf_main.addMouseMotionListener(new MouseMotionListener() {
			Point p1 = null;

			public void mouseMoved(MouseEvent arg0) {
				p1 = arg0.getLocationOnScreen();
			}

			public void mouseDragged(MouseEvent arg0) {
				Point p2 = arg0.getLocationOnScreen();
				Point p3 = jf_main.getLocationOnScreen();
				jf_main.setLocation(p3.x + (p2.x - p1.x), p3.y + (p2.y - p1.y));
				p1 = arg0.getLocationOnScreen();
			}
		});
		jf_main.setVisible(true);
	}

}
