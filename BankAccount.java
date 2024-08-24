import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }
}

class User {
    private String cardNumber;
    private String pin;
    private BankAccount account;

    public User(String cardNumber, String pin, double initialBalance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.account = new BankAccount(initialBalance);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public BankAccount getAccount() {
        return account;
    }
}

class ATMSystem {
    private Map<String, User> users = new HashMap<>();

    public void addUser(String cardNumber, String pin, double initialBalance) {
        users.put(cardNumber, new User(cardNumber, pin, initialBalance));
    }

    public User authenticate(String cardNumber, String pin) {
        User user = users.get(cardNumber);
        if (user != null && user.getPin().equals(pin)) {
            return user;
        }
        return null;
    }
}

class LoginScreen extends JFrame implements ActionListener {
    private ATMSystem atmSystem;
    private JTextField cardNumberField;
    private JPasswordField pinField;

    public LoginScreen(ATMSystem atmSystem) {
        this.atmSystem = atmSystem;

        setTitle("ATM Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = new JTextField(10);

        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JPasswordField(10);

        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        loginButton.addActionListener(this);
        createAccountButton.addActionListener(this);

        panel.add(cardNumberLabel);
        panel.add(cardNumberField);
        panel.add(pinLabel);
        panel.add(pinField);
        panel.add(loginButton);
        panel.add(createAccountButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String cardNumber = cardNumberField.getText();
        String pin = new String(pinField.getPassword());

        if (command.equals("Login")) {
            User user = atmSystem.authenticate(cardNumber, pin);
            if (user != null) {
                ATMGUI atmGUI = new ATMGUI(user);
                atmGUI.setVisible(true);
                this.dispose();  // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card number or PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (command.equals("Create Account")) {
            // Prompt for initial balance and create a new account
            String initialBalanceStr = JOptionPane.showInputDialog(this, "Enter initial balance:");
            try {
                double initialBalance = Double.parseDouble(initialBalanceStr);
                atmSystem.addUser(cardNumber, pin, initialBalance);
                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid balance entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        ATMSystem atmSystem = new ATMSystem();
        atmSystem.addUser("123456", "0000", 1000.0);  // Example user

        LoginScreen loginScreen = new LoginScreen(atmSystem);
        loginScreen.setVisible(true);
    }
}

class ATMGUI extends JFrame implements ActionListener {
    private User user;
    private JTextField balanceField;
    private JTextField amountField;
    private JTextArea outputArea;

    public ATMGUI(User user) {
        this.user = user;

        setTitle("ATM Machine");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel balanceLabel = new JLabel("Current Balance:");
        balanceField = new JTextField(10);
        balanceField.setEditable(false);
        balanceField.setText(String.format("%.2f", user.getAccount().getBalance()));

        JLabel amountLabel = new JLabel("Enter Amount:");
        amountField = new JTextField(10);

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");

        outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);

        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);

        panel.add(balanceLabel);
        panel.add(balanceField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(depositButton);
        panel.add(withdrawButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid amount entered.");
            return;
        }

        if (command.equals("Deposit")) {
            user.getAccount().deposit(amount);
            outputArea.setText("Deposited: " + amount);
        } else if (command.equals("Withdraw")) {
            if (user.getAccount().withdraw(amount)) {
                outputArea.setText("Withdrew: " + amount);
            } else {
                outputArea.setText("Insufficient balance for withdrawal.");
            }
        }

        balanceField.setText(String.format("%.2f", user.getAccount().getBalance()));
        amountField.setText("");
    }
}
