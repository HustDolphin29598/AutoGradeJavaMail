package com.huy.topica.mail.reply;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.huy.topica.mail.main.Log;

public class MailReply {

    private MailReply() {

    }

    private static final String REPLY_ERR_SUBJECT = "Wrong Mail Format";
    private static final String REPLY_RESULT_SUBJECT = "Exam Result";
    private static final String REPLY_ZIP = "Attached file must be in .zip format";
    private static final String REPLY_JAVA = "Attached file must be in .java format";
    private static final String REPLY_RESULT = "Your score is: ";

    public static void replyZipFormat(String fromMail, String password, String toMail) throws MessagingException {
        replyMail(fromMail, password, toMail, REPLY_ERR_SUBJECT, REPLY_ZIP);
    }

    public static void replyJavaFormat(String fromMail, String password, String toMail) throws MessagingException {
        replyMail(fromMail, password, toMail, REPLY_ERR_SUBJECT, REPLY_JAVA);
    }

    public static void replyResult(String fromMail, String password, String toMail, int res) throws MessagingException {
        String result = REPLY_RESULT + res;
        replyMail(fromMail, password, toMail, REPLY_RESULT_SUBJECT, result);
    }

    private static void replyMail(String fromMail, String password, String toMail, String replySubject,
            String replyMessage) throws MessagingException {
        final String mail = fromMail;
        final String pwd = password;
        String host = "smtp.gmail.com";

        // Get the session object
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Setup mail server
        props.put("mail.smtp.host", host);

        // TLS Port
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail, pwd);
            }
        });

        // Compose the message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
        message.setSubject(replySubject);
        message.setText(replyMessage);

        // send the message
        Transport.send(message);

        Log.info("message sent successfully...");

    }
}
