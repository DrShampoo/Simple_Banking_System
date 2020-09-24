package banking.service;

import banking.system.Account;
import banking.system.BankingSystem;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CheckService {

    private static BankingSystem bankingSystem;


    public CheckService(BankingSystem bankingSystem) {
        CheckService.bankingSystem = bankingSystem;
    }

    public void checkAccount(String cardNumber, String pinNumber, Scanner scanner, Statement statement) throws SQLException {
        System.out.println();
        for (Account account : bankingSystem.getAccountList()) {
            if (account.getCardNumber().equals(cardNumber) && account.getAccountPin().equals(pinNumber)) {
                System.out.println("You have successfully logged in!\n");
                WorkService.accountProfile(scanner, account, statement);
                return;
            }
        }
        System.out.println("Wrong card number or PIN!");
    }

    public static Account checkTransferAccount(Account account, String transferCardNumber) {
        Account transferAccount = null;
        if (account.getCardNumber().equals(transferCardNumber)) {
            System.out.println("You can't transfer money to the same account!");
            return null;
        }
        String cardNum = transferCardNumber.substring(0, 15);
        String controlNum = transferCardNumber.substring(15);
        if (!controlNum.equals(CreateService.algorithmLuna(cardNum))) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return null;
        }
        boolean cardExist = false;
        for (Account user : bankingSystem.getAccountList()) {
            if (user.getCardNumber().equals(transferCardNumber)) {
                cardExist = true;
                transferAccount = user;
            }
        }
        if (!cardExist) {
            System.out.println("Such a card does not exist.");
            return null;
        }
        return transferAccount;
    }

    public static void deleteAccount(Account account) {
        bankingSystem.getAccountList().remove(account);
    }

}
