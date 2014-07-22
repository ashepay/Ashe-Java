package com.ASHE;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public abstract class ASHE_API {

	private static String VERSION = "1";
	private static String BASE_URL = "https://www.ashepay.com";
	private static ArrayList<String> methods, modes;
	
	private static String mode = null;
	private static String merchant_id = null;
	private static String private_key = null;
	
	public static void setMode(String mode) {
		ASHE_API.mode = mode;
	}
	
	public static void setMerchantId(String merchant_id) {
		ASHE_API.merchant_id = merchant_id;
	}
	
	public static void setPrivateKey(String private_key) {
		ASHE_API.private_key = private_key;
	}
	
	public static ASHEResponse postRequest(HashMap<String, String> params, String method) throws ASHEException {
		checkParams(params, method);
		String url = buildUrl(method);
        try {
    		URI uri = new URI(url);
    		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost(uri);
    		httppost = buildHeaders(httppost);
    		params = buildPrivateData(params);
    		String request = new Gson().toJson(params);
            httppost.setEntity(new StringEntity(request, ContentType.create("application/json")));
        	HttpResponse response = httpclient.execute(httppost);
    		HttpEntity entity = response.getEntity();
    		String responseBody = EntityUtils.toString(entity);
    		StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HttpStatus.SC_OK && status.getStatusCode() != HttpStatus.SC_ACCEPTED ) {
            	throw new ASHEException("Could not connect to the server. Please check your internet connection.", "E500");
            }
            ASHEResponse asheresponse = new Gson().fromJson(responseBody, ASHEResponse.class);
            if(asheresponse.containsErrors()) {
            	ASHEResponse.Error error = asheresponse.getErrors().get(0);
            	throw new ASHEException(error.getMsg(), error.getCode());
            }
            httpclient.close();
    		return asheresponse;
        } catch (ClientProtocolException e) {
        	throw new ASHEException("Could not connect to the server. Please check your internet connection.", "E500");
        } catch (IOException e) {
        	throw new ASHEException("Could not connect to the server. Please check your internet connection.", "E500");
        } catch (URISyntaxException e) {
        	throw new ASHEException("Could not connect to the server. Please check your internet connection.", "E500");
		}
	}
	
	private static void checkParams(HashMap<String, String> params, String method) throws ASHEException {
		methods = new ArrayList<String>();
		methods.add("charge");
		methods.add("refund");
		modes = new ArrayList<String>();
		modes.add("sandbox");
		modes.add("production");
		if(!methods.contains(method)) {
			throw new ASHEException("Unknown method.", "");
		}
		if(merchant_id == null) {
			throw new ASHEException("Invalid merchant id.", "E402");
		}
		if(private_key == null) {
			throw new ASHEException("Invalid private key.", "E402");
		}
		if(!modes.contains(mode)) {
			throw new ASHEException("Invalid mode. Please specify either 'production' or 'sandbox'.", "E402");
		}
		if(!params.containsKey("amount")) {
			throw new ASHEException("Invalid amount.", "E401");
		}
		if(method.equals("charge") && !params.containsKey("token")) {
			throw new ASHEException("Invalid token.", "E401");
		}
		if(method.equals("refund") && !params.containsKey("transaction_id")) {
			throw new ASHEException("Invalid transaction id.", "E401");
		}
	}
	
	private static String buildUrl(String method) {
		String url = "";
		if(method.equals("charge")) {
			if(mode.equals("production")) {
				url = BASE_URL + "/api/payment/v1/" + merchant_id + "/";
			}
			else {
				url = BASE_URL + "/api/sandbox/" + merchant_id + "/";
			}
		}
		else if(method.equals("refund")) {
			if(mode.equals("production")) {
				url = BASE_URL + "/api/refund/v1/" + merchant_id + "/";
			}
			else {
				url = BASE_URL + "/api/sandbox/refund/" + merchant_id + "/";
			}
		}
		return url;
	}
	
	private static HttpPost buildHeaders(HttpPost httppost) {
		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("User-Agent", "ASHE Corporation Java Payment API V" + VERSION);
		return httppost;
	}
	
	private static HashMap<String, String> buildPrivateData(HashMap<String, String> params) {
		params.put("merchant_id", merchant_id);
		params.put("private_key", private_key);
		return params;
	}
}
