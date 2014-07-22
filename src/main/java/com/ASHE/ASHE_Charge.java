package com.ASHE;

import java.util.HashMap;

public class ASHE_Charge {

	public static ASHEResponse charge(HashMap<String, String> params) throws ASHEException {
		ASHEResponse response = ASHE_API.postRequest(params, "charge");
		return response;
	}

	public static ASHEResponse refund(HashMap<String, String> params) throws ASHEException {
		ASHEResponse response = ASHE_API.postRequest(params, "refund");
		return response;
	}
}
