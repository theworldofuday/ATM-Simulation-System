import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cardNumber;
    private String pin;
    private String accountHolderName;
    private double balance;
    private boolean isLocked;
    private int failedAttempts;
    private List<String> transactionHistory;

    private static final int MAX_FAILED_ATTEMPTS = 3;

    public BankAccount(String cardNumber, String pin, String accountHolderName, double initialBalance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.isLocked = false;
        this.failedAttempts = 0;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Account created with initial balance: $" + initialBalance);
    }

    // PIN Verification
    public boolean verifyPin(String enteredPin) {
        if (isLocked) {
            System.out.println("❌ Account is locked due to too many failed attempts.");
            return false;
        }
        if (this.pin.equals(enteredPin)) {
            failedAttempts = 0;
            return true;
        } else {
            failedAttempts++;
            System.out.println("❌ Incorrect PIN. Attempt " + failedAttempts + " of " + MAX_FAILED_ATTEMPTS);
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                isLocked = true;
                System.out.println("🔒 Account has been LOCKED due to too many failed PIN attempts.");
            }
            return false;
        }
    }

    // Check Balance
    public double getBalance() {
        return balance;
    }

    // Deposit
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Deposit amount must be greater than zero.");
            return false;
        }
        balance += amount;
        addTransaction("Deposited: $" + String.format("%.2f", amount) + " | Balance: $" + String.format("%.2f", balance));
        return true;
    }

    // Withdraw
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Withdrawal amount must be greater than zero.");
            return false;
        }
        if (amount > balance) {
            System.out.println("❌ Insufficient funds. Available balance: $" + String.format("%.2f", balance));
            return false;
        }
        balance -= amount;
        addTransaction("Withdrawn: $" + String.format("%.2f", amount) + " | Balance: $" + String.format("%.2f", balance));
        return true;
    }

    // Transfer
    public boolean transfer(BankAccount recipient, double amount) {
        if (amount <= 0) {
            System.out.println("❌ Transfer amount must be greater than zero.");
            return false;
        }
        if (amount > balance) {
            System.out.println("❌ Insufficient funds for transfer. Available: $" + String.format("%.2f", balance));
            return false;
        }
        balance -= amount;
        recipient.balance += amount;
        addTransaction("Transferred: $" + String.format("%.2f", amount) + " to Card [" + recipient.getCardNumber() + "] | Balance: $" + String.format("%.2f", balance));
        recipient.addTransaction("Received: $" + String.format("%.2f", amount) + " from Card [" + this.cardNumber + "] | Balance: $" + String.format("%.2f", recipient.balance));
        return true;
    }

    // Add Transaction
    private void addTransaction(String record) {
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        transactionHistory.add("[" + timestamp + "] " + record);
    }

    // Get Transaction History
    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    // Getters
    public String getCardNumber() { return cardNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public boolean isLocked() { return isLocked; }
    public int getFailedAttempts() { return failedAttempts; }

    // Unlock Account (Admin)
    public void unlockAccount() {
        isLocked = false;
        failedAttempts = 0;
        addTransaction("Account unlocked.");
        System.out.println("✅ Account unlocked successfully.");
    }
}
