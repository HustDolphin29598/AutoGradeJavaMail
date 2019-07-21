package com.huy.topica.mail.main;
import java.io.IOException;

import javax.mail.MessagingException;

import com.huy.topica.mail.receiver.MailCheckingIMAP;

public class TestAll {
    public static void main(String[] args) {
        String host = "imap.googlemail.com";
        String storeType = "imaps";
        String mailName = "huytopicawork@gmail.com";
        String passTest = "topica123";
        
        try {
            MailCheckingIMAP mailCheck = new MailCheckingIMAP();
            mailCheck.check(host, storeType, mailName, passTest);
        } catch (MessagingException e) {
            Log.info("Can not send mail");
            Log.error(e);
        } catch (IOException e) {
            Log.info("File not opened");
            Log.error(e);
        }        
    }
}
