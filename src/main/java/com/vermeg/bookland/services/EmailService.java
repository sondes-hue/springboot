package com.vermeg.bookland.services;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {
    void sendEmailUsingThymeleaf(String to, Map<String, Object> templateModel, String subject) throws MessagingException;
    void sendHtmlMessage(String to, String htmlBody, String subject) throws MessagingException;
}
