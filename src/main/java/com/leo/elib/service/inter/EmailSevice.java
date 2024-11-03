package com.leo.elib.service.inter;

public interface EmailSevice {
  void sendSimpleEmail(String to, String subject, String content);
  void sendHtmlEmail(String to, String subject, String content);
}