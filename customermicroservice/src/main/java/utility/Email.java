package utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class Email {

    @Autowired
    private MailSender crunchifymail; // MailSender interface defines a strategy
    // for sending simple mails

    public void crunchifyReadyToSendEmail(String toAddress, String fromAddress, String subject, String msgBody) {

        SimpleMailMessage crunchifyMsg = new SimpleMailMessage();
        crunchifyMsg.setFrom(fromAddress);
        crunchifyMsg.setTo(toAddress);
        crunchifyMsg.setSubject(subject);
        crunchifyMsg.setText(msgBody);
        crunchifymail.send(crunchifyMsg);
    }
}