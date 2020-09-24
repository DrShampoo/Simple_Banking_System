package banking.service;

import banking.system.Account;
import banking.system.BankingSystem;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class WorkService {
    private static boolean isNotExit = true;

    public static void workSystem(Statement statement) throws SQLException {
        final int CREATE_ACTION = 1;
        final int LOG_IN_ACTION = 2;
        final int EXIT_ACTION = 0;
        Scanner scanner = new Scanner(System.in);
        BankingSystem system = new BankingSystem();
        CheckService service = new CheckService(system);
        while (isNotExit) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            int action = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (action) {
                case CREATE_ACTION:
                    Account newAccount = CreateService.createAccount();
                    system.getAccountList().add(newAccount);
                    DataBaseService.insertIntoDB(statement, newAccount.getCardNumber(), newAccount.getAccountPin());
                    System.out.println("Your card has been created\n" +
                            "Your card number:\n" +
                            newAccount.getCardNumber() + "\n" +
                            "Your card PIN:\n" +
                            newAccount.getAccountPin());
                    System.out.println();
                    break;
                case LOG_IN_ACTION:
                    System.out.println("Enter your card number:");
                    String card = scanner.nextLine();
                    System.out.println("Enter your PIN:");
                    String pin = scanner.nextLine();
                    service.checkAccount(card, pin, scanner, statement);
                    System.out.println();
                    break;
                case EXIT_ACTION:
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Error!");
            }
        }
    }

    public static void accountProfile(Scanner scanner, Account account, Statement statement) throws SQLException {
        final int BALANCE_ACTION = 1;
        final int ADD_MONEY = 2;
        final int TRANSFER = 3;
        final int CLOSE_ACCOUNT = 4;
        final int LOG_OUT_ACTION = 5;
        final int EXIT_ACTION = 0;
        while (true) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            int action = Integer.parseInt(scanner.nextLine());
            System.out.println();
            switch (action) {
                case BALANCE_ACTION:
                    DataBaseService.readBalance(statement, account.getCardNumber());
                    System.out.println();
                    break;
                case ADD_MONEY:
                    System.out.println("Enter income:");
                    int income = Integer.parseInt(scanner.nextLine());
                    DataBaseService.addBalance(statement, account.getCardNumber(), income);
                    account.setBalance(income);
                    System.out.println("Income was added!\n");
                    break;
                case TRANSFER:
                    System.out.println("Transfer");
                    System.out.println("Enter card number:");
                    String transferCardNumber = scanner.nextLine();
                    transferMoney(statement, account, transferCardNumber, scanner);
                    System.out.println();
                    break;
                case CLOSE_ACCOUNT:
                    closeAccount(statement, account);
                    System.out.println("The account has been closed!");
                    return;
                case LOG_OUT_ACTION:
                    System.out.println("You have successfully logged out!");
                    return;
                case EXIT_ACTION:
                    isNotExit = false;
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Error!");
            }
        }
    }

    private static void transferMoney(Statement statement, Account account, String transferCardNumber, Scanner scanner) throws SQLException {
        Account transferAccount = CheckService.checkTransferAccount(account, transferCardNumber);
        if (transferAccount == null){
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int transferSum = Integer.parseInt(scanner.nextLine());
        if (transferSum > account.getBalance()) {
            System.out.println("Not enough money!");
        } else {
            transferAccount.setBalance(transferAccount.getBalance() + transferSum);
            DataBaseService.addBalance(statement, transferCardNumber, transferSum);
            account.setBalance(account.getBalance() - transferSum);
            DataBaseService.deductBalance(statement, account.getCardNumber(), transferSum);
            System.out.println("Success!");
        }

    }

    private static void closeAccount(Statement statement, Account account) throws SQLException {
        DataBaseService.deleteAccount(statement, account.getCardNumber());
        CheckService.deleteAccount(account);
    }
}
