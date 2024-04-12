package pl.darsonn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ATMLogger {
    private static Logger logger;

    public static synchronized Logger getLogger() {
        if (logger == null) {
            logger = LoggerFactory.getLogger(ATMLogger.class);
        }
        return logger;
    }
}
