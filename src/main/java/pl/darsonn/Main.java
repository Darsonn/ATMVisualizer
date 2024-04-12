package pl.darsonn;

import lombok.Getter;
import org.slf4j.Logger;
import pl.darsonn.api.ApiConnectionChecker;
import pl.darsonn.gui.GUIManager;

import java.io.IOException;

public class Main {
    static {
        if(System.getProperty("development") == null) {
            System.setProperty("log4j.configurationFile", "log4j2.xml");
        } else {
            System.setProperty("log4j.configurationFile", "src/main/resources/config/log4j2.xml");
        }
    }

    @Getter
    private static GUIManager guiManager;
    @Getter
    private static boolean isRestApiReachable;
    public static void main(String[] args) {
        Logger logger = ATMLogger.getLogger();

        logger.info("Starting ATM service...");

        guiManager = new GUIManager(logger);
        guiManager.showLoadingScreen();

        try {
            if(new ApiConnectionChecker("config.json").checkConnection()) {
                logger.info("Connection with RestApi is ok");
                isRestApiReachable = true;
            } else {
                logger.error("Connection with RestApi failed");
                isRestApiReachable = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}