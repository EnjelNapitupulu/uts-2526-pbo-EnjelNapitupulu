package fintech.driver;

import java.util.Scanner;
import java.util.Map;
import java.util.LinkedHashMap;
import fintech.model.Account;

/**
 * @author 12S24056 Enjel Ayuti Napitupulu
 * @author 12S24056 Enjel Ayuti Napitupulu
 */
public class Driver2 {

    public static void main(String[] _args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Account> accounts = new LinkedHashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("---")) break;
            
            String[] parts = line.split("#");
            String command = parts[0];

            if (command.equals("create-account")) {
                String name = parts[1];
                String username = parts[2];
                if (!accounts.containsKey(username)) {
                    accounts.put(username, new Account(name, username));
                }
            } else if (command.equals("deposit")) {
                String username = parts[1];
                double amount = Double.parseDouble(parts[2]);
                
                Account acc = accounts.get(username);
                if (acc != null) {
                    acc.deposit(amount);
                }
            } else if (command.equals("withdraw")) {
                String username = parts[1];
                double amount = Double.parseDouble(parts[2]);
                
                Account acc = accounts.get(username);
                if (acc != null) {
                    acc.withdraw(amount); 
                }
            }
        }

        for (Account acc : accounts.values()) {
            System.out.println(acc.getUsername() + "|" + acc.getName() + "|" + acc.getBalance());
        }

        scanner.close();
    }
}
