package com.langsin.qq.client;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

class IconNode extends DefaultMutableTreeNode {
	protected Icon icon;
	protected String txt;

	// ֻ�����ı��Ľڵ㹹��
	public IconNode(String txt) {
		super();
		this.txt = txt;
	}

	// �����ı���ͼƬ�Ľڵ㹹��
	public IconNode(Icon icon, String txt) {
		super();
		this.icon = icon;
		this.txt = txt;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setText(String txt) {
		this.txt = txt;
	}

	public String getText() {
		return txt;
	}
}