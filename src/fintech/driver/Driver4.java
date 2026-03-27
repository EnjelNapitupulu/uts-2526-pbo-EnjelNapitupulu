package fintech.driver;

import java.util.Scanner;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import fintech.model.*;

/**
 * @author 12S24056 Enjel Ayuti Napitupulu
 * @author 12S24056 Enjel Ayuti Napitupulu
 */
public class Driver4 {


    public static void performWithdraw(Account acc, double amount) throws NegativeBalanceException {
        if (!acc.withdraw(amount)) {
            throw new NegativeBalanceException("Saldo tidak cukup");
        }
    }

    public static void main(String[] _args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Account> accounts = new LinkedHashMap<>();
        List<Transaction> allTransactions = new ArrayList<>();
        int txId = 1;

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
                String timestamp = parts[3];
                String desc = parts[4];

                Account acc = accounts.get(username);
                if (acc != null) {
                    acc.deposit(amount);
                    allTransactions.add(new DepositTransaction(txId++, username, amount, timestamp, desc));
                }
                
            } else if (command.equals("withdraw")) {
                String username = parts[1];
                double amount = Double.parseDouble(parts[2]);
                String timestamp = parts[3];
                String desc = parts[4];

                Account acc = accounts.get(username);
                if (acc != null) {
                    try {
                        performWithdraw(acc, amount);
                        allTransactions.add(new WithdrawTransaction(txId++, username, amount, timestamp, desc));
                    } catch (NegativeBalanceException e) {

                    }
                }
                
            } else if (command.equals("transfer")) {
                String senderUsername = parts[1];
                String receiverUsername = parts[2];
                double amount = Double.parseDouble(parts[3]);
                String timestamp = parts[4];
                String desc = parts[5];

                Account sender = accounts.get(senderUsername);
                Account receiver = accounts.get(receiverUsername);

                if (sender != null && receiver != null) {
                    try {
                        performWithdraw(sender, amount);
                        receiver.deposit(amount);
                        allTransactions.add(new TransferTransaction(txId++, senderUsername, receiverUsername, amount, timestamp, desc));
                    } catch (NegativeBalanceException e) {

                    }
                }
                
            } else if (command.equals("show-account")) {
                String username = parts[1];
                Account acc = accounts.get(username);
                
                if (acc != null) {
                    System.out.println(acc.getUsername() + "|" + acc.getName() + "|" + acc.getBalance());

                    List<Transaction> accTxs = new ArrayList<>();

                    for (Transaction tx : allTransactions) {
                        if (tx.getUsername().equals(username)) {
                            accTxs.add(tx);
                        } else if (tx instanceof TransferTransaction) {
                            TransferTransaction ttx = (TransferTransaction) tx;
                            if (ttx.getReceiver().equals(username)) {
                                accTxs.add(tx);
                            }
                        }
                    }


                    accTxs.sort(Comparator.comparing(Transaction::getTimestamp));

                    for (Transaction tx : accTxs) {
                        double printAmount = tx.getAmount();
                        

                        if (tx.getType().equals("withdraw")) {
                            printAmount = -tx.getAmount();
                        } else if (tx.getType().equals("transfer") && tx.getUsername().equals(username)) {
                            printAmount = -tx.getAmount();
                        }
                        
                        System.out.println(tx.getId() + "|" + tx.getType() + "|" + printAmount + "|" + tx.getTimestamp() + "|" + tx.getDescription());
                    }
                }
            }
        }

        scanner.close();
    }
}
