package com.ASHE;

import java.util.List;

public class ASHEResponse {

	private String status;
	private String mode;
	private String message;
	private String amount;
	private List<Error> errors;
	private String transactionId;
	private String refundId;
	
	public static class Error {
		private String code;
		private String msg;
		
		public String getMsg() {
			return msg;
		}
		
		public String getCode() {
			return code;
		}
	}

	public String getStatus() {
		return status;
	}

	public String getMode() {
		return mode;
	}

	public String getMessage() {
		return message;
	}

	public String getAmount() {
		return amount;
	}

	public String getTransactionId() {
		return transactionId;
	}
	
	public String getRefundId() {
		return refundId;
	}
	
	public Boolean containsErrors() {
		return errors != null && errors.size() > 0;
	}
	
	public List<Error> getErrors() {
		return errors;
	}

}
