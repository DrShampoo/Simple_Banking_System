package banking.service;

import banking.system.Account;

import java.util.Random;

public class CreateService {

    public static Account createAccount() {
        Random random = new Random();
        String accountPin = generatePin(random);
        String cardNumber = generateCard(random);
        return new Account(cardNumber, accountPin, 0);
    }

    private static String generatePin(Random random) {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10));
        }
        return pin.toString();
    }

    private static String generateCard(Random random) {
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append(400000);
        for (int i = 0; i < 9; i++) {
            cardNumber.append(random.nextInt(10));
        }
        cardNumber.append(algorithmLuna(cardNumber.toString()));
        return cardNumber.toString();
    }

    public static String algorithmLuna (String line) {
        int sum = 0;
        int nDigits = line.length();
        int parity = nDigits % 2;
        for (int i = nDigits; i > 0; i--) {
            int digit = Character.getNumericValue(line.charAt(i - 1));
            if (i % 2 == parity) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        if ((((sum % 10) - 10) * -1) == 10) {
            return "0";
        } else {
            return String.valueOf(((sum % 10) - 10) * -1);
        }
    }
}
