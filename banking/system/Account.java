package banking.system;

public class Account {
    private final String cardNumber;
    private final String accountPin;
    private double balance;

    public Account(String cardNumber, String accountPin, double balance) {
        this.cardNumber = cardNumber;
        this.accountPin = accountPin;
        this.balance = balance;
    }

    public String getAccountPin() {
        return accountPin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

}
