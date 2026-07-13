package com.identityaccessmanagement.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class EmailService {
	
	@Value("${app.base-url}")
	private String baseUrl;

	@Autowired
    private  JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("OTP Verification");

        message.setText(
                "Dear User,\n\n" +
                "Your OTP is: " + otp +
                "\n\nIt is valid for 5 minutes." +
                "\n\nThank You."
        );

        mailSender.send(message);
    }
    
    public void sendPasswordResetEmail(String toEmail, String token) {

    	String resetLink = baseUrl + "/api/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Password Reset Request");

        message.setText(
                "Dear User,\n\n" +
                "You requested to reset your password.\n\n" +
                "Use the following link to reset your password:\n\n" +
                resetLink +
                "\n\nThis link is valid for 30 minutes." +
                "\n\nIf you didn't request this, please ignore this email."
        );

        mailSender.send(message);
    }
}
