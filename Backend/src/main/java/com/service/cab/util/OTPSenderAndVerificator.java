package com.service.cab.util;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import lombok.Getter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.service.cab.constants.Constants;
import com.service.cab.constants.StatusConstants;

public class OTPSenderAndVerificator {
	
	@Value("${fast2.sms.api-key}")
	private String API_KEY;
	@Value("${fast2.sms.url}")
	private String API_URL;
	@Value("${fast2.sms.content-type}")
	private String API_CONTENT_TYPE;
	
	private OTPGenerator otpGenerator = new OTPGenerator();
	private static final int OTP_LENGHT = 6;
	@Getter
    private static Map<String, String> emailVsOtp = new HashMap<String, String>();
	@Getter
    private static Map<String, String> mobilenoVsOtp = new HashMap<String, String>();


    public static void setEmailVsOtp(Map<String, String> emailVsOtp) {
		OTPSenderAndVerificator.emailVsOtp = emailVsOtp;
	}


    public static void setMobilenoVsOtp(Map<String, String> mobilenoVsOtp) {
		OTPSenderAndVerificator.mobilenoVsOtp = mobilenoVsOtp;
	}


	public String sendOtpToEmail(String email) {
		
		String otp = otpGenerator.generateOTP(OTP_LENGHT);
		try {
			otpGenerator.sendOTPByEmail(email, otp);
		} 
		catch (MessagingException e) {
			System.out.println(e.getMessage());
			return Constants.OTP_NOT_SENT;
		}
		emailVsOtp.put(email, otp);
		return Constants.OTP_SENT_SUCCESSFULLY;
	}

	
	public boolean verifyOtpEmail(String email, String otp) {
		
		String savedOtp = emailVsOtp.get(email);
		if(savedOtp != null && savedOtp.equals(otp)) {
			emailVsOtp.remove(email);
			StatusConstants.setEMAIL_OTP_STATUS(true);
			return true;
		}
		
		return false;
	}

	
	public String sendOtpToMobile(String mobileNo) {
		try {
	            String otp = otpGenerator.generateOTP(OTP_LENGHT);
	            String requestBody = "variables_values=" + otp + "&route=otp&numbers="+mobileNo;
	            CloseableHttpClient httpClient = HttpClients.createDefault();
	            HttpPost httpPost = new HttpPost(API_URL);
	            httpPost.addHeader("authorization", API_KEY);
	            httpPost.addHeader("Content-Type", API_CONTENT_TYPE);
	
	            StringEntity requestEntity = new StringEntity(requestBody);
	            httpPost.setEntity(requestEntity);
	            HttpResponse response = httpClient.execute(httpPost);
	
	            HttpEntity entity = response.getEntity();
	            String responseString = EntityUtils.toString(entity);
	            JSONObject jsonResponse = new JSONObject(responseString);
	
	            boolean returnValue = jsonResponse.getBoolean("return");
	            JSONArray message = jsonResponse.getJSONArray("message");
	        	System.out.println(responseString);
	
	            if(returnValue)
	            {
	            	httpClient.close();
	            	mobilenoVsOtp.put(mobileNo, otp);
	                return Constants.OTP_SENT_SUCCESSFULLY;	
	            }
	            else
	            {
	                return Constants.OTP_NOT_SENT+", "+message.optString(0);
	            }
	            	     
        } catch (Exception e) {
            return Constants.OTP_NOT_SENT+", "+e.getMessage();
        }
	}

	
	public boolean verifyOtpMobile(String mobileNo, String otp) {
		String savedOtp = mobilenoVsOtp.get(mobileNo);
		if(savedOtp != null && savedOtp.equals(otp)) {
			mobilenoVsOtp.remove(mobileNo);
			StatusConstants.setMOBILE_OTP_STATUS(true);
			return true;
		}
		return false;
	}

}
