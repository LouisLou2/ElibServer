package com.leo.elib.service.impl;

import com.leo.elib.service.inter.EmailSevice;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class EmailServiceImpl implements EmailSevice {
  @Value("${spring.mail.username}")
  private String sender;
  @Resource
  private JavaMailSender javaMailSender;

  @Override
  @Async("taskExecutor")
  public void sendSimpleEmail(String to, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    javaMailSender.send(message);
  }

  @Override
  @Async("taskExecutor")
  public void sendHtmlEmail(String to, String subject, String content) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try{
      message.setFrom(sender);
      message.addRecipients(MimeMessage.RecipientType.TO, to);
      message.setSubject(subject);
      message.setContent(content, "text/html;charset=UTF-8");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    javaMailSender.send(message);
  }
}