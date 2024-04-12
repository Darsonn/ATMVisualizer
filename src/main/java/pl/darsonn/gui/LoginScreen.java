package pl.darsonn.gui;

import pl.darsonn.Main;
import pl.darsonn.api.CardApi;
import pl.darsonn.api.model.Card;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginScreen {
    public JPanel panel;
    private JButton insertCardVirtuallyButton;

    public LoginScreen() {
        insertCardVirtuallyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardNumber = JOptionPane.showInputDialog("Enter Card Number");

                if(cardNumber != null) {
                    if(cardNumber.length() != 16) {
                        JOptionPane.showMessageDialog(null, "Card Number must be 16 characters");
                    } else {
                        Card card;
                        try {
                            card = new CardApi().getCardByNumber(cardNumber);
                        } catch (IOException ignore) {
                            JOptionPane.showMessageDialog(null, "Card not found");
                            return;
                        }

                        if(card != null) {
                            String pin = JOptionPane.showInputDialog(null, "Enter PIN");

                            if(pin.length() != 4) {
                                JOptionPane.showMessageDialog(null, "PIN must be 4 characters");
                            } else {
                                try {
                                    for(int i = 0; i < 3; i++) {
                                        if(card.getPin().equals(Integer.parseInt(pin))) {
                                            Main.getGuiManager().showMainScreen(card);
                                            return;
                                        } else {
                                            pin = JOptionPane.showInputDialog(null, "Invalid PIN!\nEnter PIN");
                                        }
                                    }

                                    JOptionPane.showMessageDialog(null, "The number of incorrect attempts has been exceeded");
                                } catch (NumberFormatException ignore) {
                                    JOptionPane.showMessageDialog(null, "PIN must be a number");
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
