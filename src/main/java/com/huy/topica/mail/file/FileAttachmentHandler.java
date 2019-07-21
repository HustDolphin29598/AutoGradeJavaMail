package com.huy.topica.mail.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;

import com.huy.topica.mail.reply.MailReply;

public class FileAttachmentHandler {
    private static final String FROM_MAIL = "huytopicawork@gmail.com";
    private static final String PASS_TEST = "topica123";
    private static final String SAVE_DIRECTORY = "/home/eie/Downloads/AttachmentFiles";

    private FileAttachmentHandler() {

    }


    private static String getFileExtension(String fileName) {
        int index = 0;
        for (int i = fileName.length() - 1; i >= 0; i--) {
            if (fileName.charAt(i) == '.') {
                index = i;
                break;
            }
        }
        String extension = fileName.substring(index, fileName.length());
        if (extension != null) {
            return extension;
        }
        return "";
    }

    private static boolean checkExtension(MimeBodyPart part, String storePath, List<String> filePathList, String email) throws MessagingException, IOException {
        String fileName = part.getFileName();
        String extension = getFileExtension(fileName);
        if (".zip".equals(extension)) {
            String filePath = storePath + File.separator + fileName;
            part.saveFile(filePath);
            String fileExtractedPath = unzipFile(storePath, filePath);
            extension = getFileExtension(fileExtractedPath);
            if (".java".equals(extension)) {
                filePathList.add(fileExtractedPath);
            } else {
                MailReply.replyJavaFormat(FROM_MAIL, PASS_TEST, email);
            }
            return true;
        }
        return false;
    }
    public static List<String> getAttachment(Message message) throws IOException, MessagingException {
        String contentType = message.getContentType();
        List<String> filePathList = new ArrayList<>();
        Address[] froms = message.getFrom();
        String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
        String storePath = SAVE_DIRECTORY + File.separator + email;
        new File(storePath).mkdirs();
        if (contentType.contains("multipart")) {
            // content may contain attachments
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())&&!checkExtension(part, storePath, filePathList, email)) {
                    MailReply.replyZipFormat(FROM_MAIL, PASS_TEST, email);
                    break;
                }
            }
        }
        return filePathList;
    }

    public static String unzipFile(String dirPath, String filePath) throws IOException {
        File dir = new File(dirPath);
        // create output directory if it doesn't exist
        if (!dir.exists())
            dir.mkdirs();

        ZipEntry ze = null;
        File newFile = null;
        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(filePath); 
             ZipInputStream zis = new ZipInputStream(fis);) 
        {
            ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                newFile = new File(dirPath + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                try (FileOutputStream fos = new FileOutputStream(newFile);) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                // close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        if (newFile != null) {
            return newFile.getAbsolutePath();
        }
        return "";
    }

}
