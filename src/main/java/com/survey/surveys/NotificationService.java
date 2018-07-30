package com.survey.surveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendNotifiction(User user) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();

        //currentuser
//        mail.setTo(user.getEmail());
        mail.setTo("lescano.micaela@gmail.com");
        mail.setBcc("micaela@ubiqum.com");
        mail.setFrom("java.mail.testing.survey@gmail.com");
        mail.setSubject("Testing");
        mail.setText("This is the body of the email");

        //send a notification
        javaMailSender.send(mail);

    }
}
