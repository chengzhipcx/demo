package com.langsin.qq.msg;


public class MsgFile extends MsgHead {

	private String fileName;
	private byte[] fileData;

	public String toString() {
		return super.toString() + " fileName:" + removeSpace(fileName);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

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
