package com.app_bank.app_bank.services;

import com.app_bank.app_bank.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String senderMail;

    @Override
    public void sendMailAlert(EmailDetails emailDetails) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderMail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMailBody());

            javaMailSender.send(mailMessage);
            System.out.println("Le mail a bien été envoyé à " + emailDetails.getRecipient());

        } catch (RuntimeException e) {
            System.out.println("Error d'envoi de mail");
            throw new RuntimeException(e);
        }
    }
}
