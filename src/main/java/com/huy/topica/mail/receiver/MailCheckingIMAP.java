package com.huy.topica.mail.receiver;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import com.huy.topica.mail.main.Log;
import com.huy.topica.mail.main.WorkerThread;
import com.sun.mail.imap.IMAPFolder;

public class MailCheckingIMAP {
    // default number of threads is 3
    private int numOfThreads = 3;
    private Date fromDate = null;
    private Date toDate = null;
    private String mailName;
    private String password;

    public MailCheckingIMAP(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }

    public MailCheckingIMAP(int numOfThreads, Date fromDate, Date toDate) {
        this.numOfThreads = numOfThreads;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public MailCheckingIMAP() {

    }

    public void setThreads(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }

    public int getThreads() {
        return numOfThreads;
    }

    private void checkDate(Executor executor, Date date, Message msg, String email) {
        if (toDate != null && fromDate != null) {
            if (!date.before(fromDate) && !date.after(toDate)) {
                WorkerThread handler = new WorkerThread(msg, mailName, password, email);
                executor.execute(handler);
            }
        } else if (toDate != null) {
            if (!date.after(toDate)) {
                WorkerThread handler = new WorkerThread(msg, mailName, password, email);
                executor.execute(handler);
            }
        } else if (fromDate != null) {
            if (!date.before(fromDate)) {
                WorkerThread handler = new WorkerThread(msg, mailName, password, email);
                executor.execute(handler);
            }
        } else {
            WorkerThread handler = new WorkerThread(msg, mailName, password, email);
            executor.execute(handler);
        }
    }

    public void check(String host, String storeType, String mailName, String password)
            throws MessagingException, IOException {
        this.mailName = mailName;
        this.password = password;
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        IMAPFolder folder = null;
        Store store = null;
        String subject = null;
        try {
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", storeType);

            Session session = Session.getDefaultInstance(props, null);

            store = session.getStore(storeType);
            store.connect(host, mailName, password);

            folder = (IMAPFolder) store.getFolder("inbox"); // This works for both email account

            if (!folder.isOpen())
                folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            Log.info("No of Messages : " + folder.getMessageCount());
            Log.info("No of Unread Messages : " + folder.getUnreadMessageCount());
            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                subject = msg.getSubject();
                if ("ITLAB-HOMEWORK".equals(subject)) {
                    Address[] froms = msg.getFrom();
                    String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                    Log.info("MESSAGE " + (i + 1) + ":");
                    Log.info("Subject: " + subject);
                    Log.info("From: " + email);
                    Log.info("To: " + msg.getAllRecipients()[0]);
                    Log.info("Date: " + msg.getReceivedDate());
                    Log.info("Size: " + msg.getSize());
                    Log.info("Body: \n" + msg.getContent());
                    Log.info(msg.getContentType());
                    Date date = msg.getReceivedDate();
                    checkDate(executor, date, msg, email);
                }
            }
            executor.shutdown();
        } finally {
            if (folder != null && folder.isOpen()) {
                folder.close(true);
            }
            if (store != null) {
                store.close();
            }
        }
    }
}
