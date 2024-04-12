package pl.darsonn.gui;

import pl.darsonn.Main;
import pl.darsonn.api.AccountApi;
import pl.darsonn.api.TransactionApi;
import pl.darsonn.api.model.Account;
import pl.darsonn.api.model.Card;
import pl.darsonn.api.model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

public class MainScreen {
    public JPanel panel;
    private JTabbedPane tabbedPane1;
    private JLabel welcomeText;
    private JLabel balanceText;
    private JTable transactionTable;
    private JTextField textField1;
    private JButton confirmButton;
    private JButton confirmButton2;
    private JButton logoutButton;
    private JTextField accountNumber;
    private JTextField valueField;
    private JButton transferButton;

    private final Card card;

    public MainScreen(Card card) {
        this.card = card;

        welcomeText.setText("Welcome " + card.getAccount().getClient().getName());
        balanceText.setText("Account balance: " + card.getAccount().getAccountBalance() + " zł");

        transactionTable.setModel(getTransactionTableModel());
        confirmButton.addActionListener(e -> {
            float value;
            try {
                value = Float.parseFloat(textField1.getText());
            } catch (NumberFormatException ignore) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
                textField1.setText("");

                return;
            }

            Account account = card.getAccount();

            if (account.getAccountBalance() - value < 0) {
                JOptionPane.showMessageDialog(null, "Account balance is too low");
                return;
            }

            account.setAccountBalance(account.getAccountBalance() - value);
            card.setAccount(account);

            try {
                new AccountApi().updateAccount(account.getAccountId(), account);

                Transaction transaction = new Transaction();
                transaction.setAccount(card.getAccount());
                transaction.setCard(card);
                transaction.setAmount(BigDecimal.valueOf(value));
                transaction.setTransactionType("WITHDRAWAL");
                transaction.setTransactionDate(Date.from(Instant.now()));

                new TransactionApi().createTransaction(transaction);

                reloadTransactionTable();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Unable to process transaction");
                return;
            }

            reloadBalanceAmount();
            JOptionPane.showMessageDialog(null, "Transaction processed successfully");
        });
        confirmButton2.addActionListener(e -> {
            String value = JOptionPane.showInputDialog(null, "Please enter value to deposit in your account");

            float convertedValue;
            try {
                convertedValue = Float.parseFloat(value);
            } catch (NumberFormatException ignore) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
                return;
            }

            Account account = card.getAccount();

            account.setAccountBalance(account.getAccountBalance() + convertedValue);
            card.setAccount(account);

            try {
                new AccountApi().updateAccount(account.getAccountId(), account);

                Transaction transaction = new Transaction();
                transaction.setAccount(card.getAccount());
                transaction.setCard(card);
                transaction.setAmount(BigDecimal.valueOf(Math.round(convertedValue)));
                transaction.setTransactionType("DEPOSIT");
                transaction.setTransactionDate(Date.from(Instant.now()));

                new TransactionApi().createTransaction(transaction);

                reloadTransactionTable();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Unable to process transaction");
                return;
            }

            reloadBalanceAmount();
            JOptionPane.showMessageDialog(null, "Transaction processed successfully");
        });
        logoutButton.addActionListener(e -> {
            Main.getGuiManager().showLoginScreen();
            JOptionPane.showMessageDialog(null, "Successfully logged out!");
        });
        transferButton.addActionListener(e -> {
            String accountNumberValue = accountNumber.getText();

            try {
                Integer.parseInt(accountNumberValue);
            } catch (NumberFormatException ignore) {
                JOptionPane.showMessageDialog(null, "Insert valid account number");
                return;
            }

            float value;

            try {
                value = Float.parseFloat(valueField.getText());
            } catch (NumberFormatException ignore) {
                JOptionPane.showMessageDialog(null, "Insert valid transfer value");
                return;
            }

            Account primaryAccount = card.getAccount();

            if(primaryAccount.getAccountBalance() < value) {
                JOptionPane.showMessageDialog(null, "Account balance is too low");
                return;
            }

            try {
                Account receiverAccount = new AccountApi().getAccountByAccountNumber(accountNumberValue);

                if(receiverAccount == null) {
                    JOptionPane.showMessageDialog(null, "Account not found");
                    return;
                }

                receiverAccount.setAccountBalance(receiverAccount.getAccountBalance() + value);
                primaryAccount.setAccountBalance(primaryAccount.getAccountBalance() - value);

                new AccountApi().updateAccount(primaryAccount.getAccountId(), primaryAccount);
                new AccountApi().updateAccount(receiverAccount.getAccountId(), receiverAccount);

                Transaction transaction = new Transaction();
                transaction.setAccount(primaryAccount);
                transaction.setCard(card);
                transaction.setAmount(BigDecimal.valueOf(Math.round(value)));
                transaction.setTransactionType("TRANSFER");
                transaction.setTransactionDate(Date.from(Instant.now()));

                new TransactionApi().createTransaction(transaction);

                transaction.setAccount(receiverAccount);
                transaction.setCard(null);

                new TransactionApi().createTransaction(transaction);

                reloadTransactionTable();
            } catch (IOException ignore) {
                JOptionPane.showMessageDialog(null, "Unable to process transaction");
                return;
            }

            reloadBalanceAmount();
            JOptionPane.showMessageDialog(null, "Transaction processed successfully");
        });
    }

    private void reloadBalanceAmount() {
        balanceText.setText("Account balance: " + card.getAccount().getAccountBalance() + " zł");
    }

    private void reloadTransactionTable() {
        transactionTable.removeAll();
        transactionTable.setModel(getTransactionTableModel());
    }

    private DefaultTableModel getTransactionTableModel() {
        List<Transaction> transactions = getTransactions();

        String[] columnNames = {
                "ID",
                "Date",
                "Type",
                "Amount"
        };

        String[][] data;

        if(transactions == null) {
            data = new String[0][0];
        } else {
            data = new String[transactions.size() + 1][4];
        }

        data[0][0] = "ID";
        data[0][1] = "Date";
        data[0][2] = "Type";
        data[0][3] = "Amount";

        for(int i = 1; i < (transactions.size() + 1); i++) {
            data[i][0] = String.valueOf(transactions.get(i - 1).getTransactionId());
            data[i][1] = String.valueOf(transactions.get(i - 1).getTransactionDate());
            data[i][2] = String.valueOf(transactions.get(i - 1).getTransactionType());
            data[i][3] = String.valueOf(transactions.get(i - 1).getAmount());
        }

        return new DefaultTableModel(data, columnNames);
    }

    private List<Transaction> getTransactions() {
        try {
            return new TransactionApi().getAllClientsTransactions((card.getAccount().getAccountId()));
        } catch (IOException e) {
            return null;
        }
    }
}
