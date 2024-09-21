package com.service.cab.util;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class OTPGenerator {
	
	private static final String NUMBERS = "0123456789";

    public String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            otp.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        System.out.println("OTP IS:"+otp.toString());
        return otp.toString();
    }
    
    public void sendOTPByEmail(String email, String otp) throws MessagingException {
    	System.out.println("CALLING SEND EMAIL");
        Properties props = System.getProperties();
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.auth", "true");

        Session session = Session.getInstance(props, null);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress("service.cab.app@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setSubject("Cab Service OTP Verification");
        message.setText("Your OTP for email verification is : " + otp);

        SMTPTransport transport = (SMTPTransport) session.getTransport("smtps");

        transport.connect("smtp.gmail.com", "service.cab.app@gmail.com", "qgrizchibivtlwpb");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

}
