package com.langsin.qq.client;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class IconNodeRenderer extends DefaultTreeCellRenderer// �̳и���
{
	// ��д�÷���
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus); // ���ø���ĸ÷���
		Icon icon = ((IconNode) value).getIcon();// �ӽڵ��ȡͼƬ
		String txt = ((IconNode) value).getText(); // �ӽڵ��ȡ�ı�
		setIcon(icon);// ����ͼƬ
		setText(txt);// �����ı�
		return this;
	}
}
