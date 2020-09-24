package banking.service;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DataBaseService {

    public static void createTable(Statement statement) throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                "id INTEGER PRIMARY KEY," +
                "number TEXT NOT NULL," +
                "pin TEXT NOT NULL," +
                "balance INTEGER DEFAULT 0);");
    }

    public static void insertIntoDB(Statement statement, String cardNumber, String pinNumber) throws SQLException {
        statement.executeUpdate(String.format("INSERT INTO card (number, pin) VALUES ('%s','%s');", cardNumber, pinNumber));
    }

    public static void readBalance(Statement statement, String cardNumber) throws SQLException {
        try(ResultSet resultSet = statement.executeQuery(String.format(
                "SELECT balance FROM card WHERE number='%s';", cardNumber))){
            while (resultSet.next()){
                int balance = resultSet.getInt("balance");
                System.out.println("Balance: " + balance);
            }
        }
    }

    public static void addBalance(Statement statement, String cardNumber, int income) throws SQLException {
        statement.executeUpdate(String.format(
                "UPDATE card SET balance=balance+%d WHERE number='%s';", income, cardNumber));
    }

    public static void deductBalance(Statement statement, String cardNumber, int expense) throws SQLException {
        statement.executeUpdate(String.format(
                "UPDATE card SET balance=balance-%d WHERE number='%s';", expense, cardNumber));
    }

    public static void deleteAccount(Statement statement, String cardNumber) throws SQLException {
        statement.executeUpdate(String.format("DELETE FROM card WHERE number='%s';", cardNumber));
    }
}
