package com.bugtracker.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void send (Mail mail){
        String recipent = mail.getRecipient();
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(mail.getRecipient());
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setText(mail.getContent());

            javaMailSender.send(mimeMessage);
            log.info("Wysłano maila na adres :" + recipent);

        }catch (Exception e){
            log.error("Wysłane maila na adres : " + recipent + " nie powiodło się..");
            e.printStackTrace();
        }
    }
}
