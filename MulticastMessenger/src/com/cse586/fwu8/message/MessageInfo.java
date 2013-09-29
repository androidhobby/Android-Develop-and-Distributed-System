package com.cse586.fwu8.message;

import java.io.Serializable;

public class MessageInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NORMAL_MSG = 0;
	public static final int PRONUM_MSG = 1;
	public static final int CASTED_MSG = 2;
	public static final int AGRNUM_MSG = 3;
	
	private boolean agreed;
	private int messageType = -1;
	private String Msg = null;
	private int propNum = 0;
	private int agreeNum = 0;
	private String uid = null;
	private int msgFrom = 0;
	public int[] timestampVector;
	
	public void setMessage(String Msg) {
		this.Msg = Msg;
	}
		
	public void setPropNum(int propNum) {
		this.propNum = propNum;
	}
	
	public void setAgreeNum(int agreeNum) {
		this.agreeNum = agreeNum;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void setMsgType(int messageType) {
		this.messageType = messageType;
	}
	
	public void setMsgFrom(int messageFrom) {
		this.msgFrom = messageFrom;
	}
	
	public void setTimestampVector(int[] vector) {
		this.timestampVector = vector;
	}
	
	public void setAgreed(boolean agree) {
		this.agreed = agree;
	}
	
	public String getMsg() {
		return Msg;
	}
		
	public int getPropNum() {
		return propNum;
	}
	
	public int getAgreeNum() {
		return agreeNum;
	}
	
	public String getUid() {
		return uid;
	}
	
	public int getMessageType() {
		return messageType;
	}
	
	public int getMessageFrom() {
		return msgFrom;
	}
	
	public int[] getTimestamp() {
		return timestampVector;
	}
	
	public boolean getAgreed() {
		return agreed;
	}
}
