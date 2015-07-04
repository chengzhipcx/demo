package com.langsin.qq.msg;

public interface IMsgConstance {
	
	String serverIP = "localhost";// ������IP
	int serverPort = 9090; // �������˿�
	int Server_QQ_NUMBER = 100000;// ��������QQ��
	
	// ϵͳ�õ�����Ϣ���Ͷ���
	byte command_reg = 0x01;// ע��������Ϣ
	byte command_reg_resp = 0x02;// ע��Ӧ����Ϣ
	byte command_login = 0x03;// ��½������Ϣ
	byte command_login_resp = 0x04;// ��½Ӧ����Ϣ
	byte command_teamList = 0x05;// �������������ߺ����б�
	byte command_onLine = 0x06;// ���������ͺ���������Ϣ*����Ϣ��
	byte command_offLine = 0x07;// ���������ͺ���������Ϣ*����Ϣ��
	byte command_chat = 0x08;// ������Ϣ���������	
	byte command_file = 0x09;// �ļ����ͷ���
	byte command_file_resp = 0x11;//�ļ�����Ӧ��
	byte command_prtsc = 0x12;//���ͽ���ͼƬ������Ϣ
	byte command_winshake = 0x13;//���ڶ�����Ϣ����*����Ϣ��
	byte command_createGroup = 0x14;//����Ⱥ������
	byte command_createGroup_resp = 0x15;//����Ⱥ������Ӧ��*����Ϣ��
	byte command_findUsers = 0x16;// �����û����� *����Ϣ��
	byte command_findUsers_resp = 0x17;// ���Һ�������Ӧ��
	byte command_addFriend = 0x18;// ��Ӻ�������
	byte command_addFriend_Resp = 0x19;// ��Ӻ���Ӧ��
	byte command_groupList = 0x21;//����������Ⱥ���б�
	byte command_findGroup = 0x22;//������Ⱥ
	byte command_findGroup_resp = 0x23;//������ȺӦ��
	byte command_addGroup = 0x24;//�����Ⱥ
	byte command_addGroup_resp = 0x25;//�����ȺӦ��
	byte command_groupChat = 0x26;//Ⱥ����Ϣ���������
//	byte command_remoteControl = 0x27;//Զ��������Ϣ����*����Ϣ��
//	byte command_remoteControl_resp = 0x28;//Զ������Ӧ��*����Ϣ��
	//��26����Ϣ��2��ѡ����
}
