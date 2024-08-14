package com.example.demo.email;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Async
@Service
public class emailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void SendEmail(String to, String username,
            EmailTemplate templateEngine, String confirmationUrl,
            String ActivationCode, String subject) throws MessagingException {

        String template;

        if (templateEngine == null) {
            template = "confirm-Email ";
        } else {
            template = templateEngine.getName();
        }
        // creatiion d un email complet
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // helper va nous aider a manipuler mimeMessage
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
     
        String text =  String.format(
            "Bonjour %s,\n\nVeuillez cliquer sur le lien suivant pour confirmer votre email : %s\nCode d'activation : %s",
            username, confirmationUrl, ActivationCode);
        helper.setFrom("ahmedgarci146@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,false);
        
        mailSender.send(mimeMessage);
    }

}
