package com.vermeg.bookland.servicesImpl;

import com.vermeg.bookland.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Override
    public void sendEmailUsingThymeleaf(String to, Map<String, Object> templateModel, String subject) throws MessagingException {
        Context contextThymeleaf = new Context();
        contextThymeleaf.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("email/verification",contextThymeleaf);
        sendHtmlMessage(to,htmlBody, subject);
    }

    @Override
    public void sendHtmlMessage(String to, String htmlBody,String subject) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody,true);
        emailSender.send(message);
    }
}
