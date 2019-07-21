package com.huy.topica.mail.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

    private Test() {

    }

    public static int testExam(String filePath) throws InterruptedException, IOException {
        int count = 0;
        Process process = null;
        String compilePath = "javac -cp src " + filePath;

        try {
            process = Runtime.getRuntime().exec(compilePath);
            process.waitFor();
            for (int month = 1; month <= 12; month++) {
                String runPath = "java -cp " + getDirectory(filePath) + " " + getFileName(filePath) + " " + month;
                process = Runtime.getRuntime().exec(runPath);
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String res;
                if ((res = in.readLine()) != null) {
                    if (TestData.check(month, Integer.parseInt(res)))
                        count++;
                }
            }
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return count;
    }

    private static String getFileName(String filePath) {
        for (int i = filePath.length() - 1; i > -1; i--) {
            if (filePath.charAt(i) == '/')
                return filePath.substring(i + 1, filePath.length() - 5);
        }
        return "0";
    }

    private static String getDirectory(String filePath) {
        for (int i = filePath.length() - 1; i > -1; i--) {
            if (filePath.charAt(i) == '/')
                return filePath.substring(0, i + 1);
        }
        return "0";
    }
}
