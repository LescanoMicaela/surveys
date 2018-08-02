package com.survey.surveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;
    private String email;
    private String email2;
    private String subject;


    @Autowired
    public NotificationService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendNotifiction(User user, String emailBcc, String emailfrom, String subject, String body) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();

//        currentuser
        mail.setTo(user.getEmail());
        mail.setBcc(emailBcc);
        mail.setFrom(emailfrom);
        mail.setSubject(subject);
        mail.setText(body);

//        send a notification
        javaMailSender.send(mail);

    }



}

