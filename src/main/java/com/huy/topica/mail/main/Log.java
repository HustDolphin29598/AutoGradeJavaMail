package com.huy.topica.mail.main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    
    private Log() {
        
    }
    private static final Logger logger;
    
    static {
        logger = Logger.getLogger(Log.class.getName());
    }
    
    public static void info(String message) {
        logger.log(Level.INFO, message);
    }
    
    public static void error(Exception e) {
        logger.log(Level.SEVERE, e.getMessage());
    }
}
