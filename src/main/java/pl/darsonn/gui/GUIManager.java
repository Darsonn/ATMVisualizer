package pl.darsonn.gui;

import org.slf4j.Logger;
import pl.darsonn.api.model.Card;

import javax.swing.*;
import java.awt.*;

public class GUIManager {
    private final JFrame frame;
    private final Logger logger;

    public GUIManager(Logger logger) {
        frame = new JFrame("ATM");
        frame.setMinimumSize(new Dimension(1000, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        this.logger = logger;
    }

    public void showLoadingScreen() {
        logger.trace("Starting LoadingScreen");

        frame.setContentPane(new LoadingScreen().panel);
        frame.pack();
    }

    public void showLoginScreen() {
        frame.setContentPane(new LoginScreen().panel);
        frame.pack();
    }

    public void showMainScreen(Card card) {
        frame.setContentPane(new MainScreen(card).panel);
        frame.pack();
    }
}
