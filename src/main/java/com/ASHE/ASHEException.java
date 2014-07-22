package com.ASHE;

public class ASHEException extends Exception {
	private static final long serialVersionUID = 1L;
	private String code;
	
	public ASHEException(String message, String code) {
		super(message, null);
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
}
