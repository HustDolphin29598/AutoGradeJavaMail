package com.huy.topica.mail.main;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.huy.topica.mail.data.Test;
import com.huy.topica.mail.file.FileAttachmentHandler;
import com.huy.topica.mail.reply.MailReply;

public class WorkerThread implements Runnable {

    private Message message;
    private String mailName;
    private String password;
    private String email;

    public WorkerThread(Message message, String mailName, String password, String email) {
        this.message = message;
        this.mailName = mailName;
        this.password = password;
        this.email = email;
    }

    @Override
    public void run() {
        ArrayList<String> javaList;
        try {
            javaList = (ArrayList<String>) FileAttachmentHandler.getAttachment(this.message);
            for (String string : javaList) {
                int score = Test.testExam(string);
                MailReply.replyResult(mailName, password, email, score);
            }
        } catch (IOException | MessagingException e) {
            Log.error(e);
        } catch (InterruptedException e) {
            Log.info("Thread interrupted");
            Log.error(e);
            Thread.currentThread().interrupt();
        }

    }
}
