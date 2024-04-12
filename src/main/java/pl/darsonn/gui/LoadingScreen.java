package pl.darsonn.gui;

import pl.darsonn.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class LoadingScreen {
    public JPanel panel;
    private Timer timer;
    private final Random random;
    private JProgressBar progressBar1;
    private JLabel successfullyLoadedLabel;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(progressBar1.getValue() >= 100) {
                successfullyLoadedLabel.setVisible(true);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                if(Main.isRestApiReachable()) {
                    Main.getGuiManager().showLoginScreen();
                    timer.stop();
                    return;
                } else {
                    JOptionPane.showMessageDialog(panel, "Connection with database was unsuccessful! Check your config file");
                }
            }

            progressBar1.setValue(progressBar1.getValue() + random.nextInt(0, 4));
        }
    };

    public LoadingScreen() {
        random = new Random();

        successfullyLoadedLabel.setVisible(false);

        timer = new Timer(50, actionListener);
        timer.start();
    }
}
