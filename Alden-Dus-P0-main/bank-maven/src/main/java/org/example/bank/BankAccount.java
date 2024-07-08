package org.example.bank;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    //Account types
    enum AccountType {
        CREDIT, CHECKING
    }

    //Account info
    //Balance is an int, we display our balance in cents, and will be displayed as a float after
    int balance;
    //Each user will have an ID
    int userID;
    //ID of the account, created by the DB;
    int accountID;

    //Is the account a checking, credit, or savings account
    AccountType accountType;
    //Credit limit: $500 (50000 cents)
    int creditLimit = 50000;

    //Transaction history
    List<Transaction> transactionHistory = new ArrayList<>();

    //Create a bank account from email, first name, and last name
    public BankAccount(int userID, AccountType accountType) {
        this.userID = userID;
        this.accountType = accountType;
    }
    public BankAccount(int accountID, int userID, int balance, AccountType accountType) {
        this.accountID = accountID;
        this.userID = userID;
        this.balance = balance;
        this.accountType = accountType;
    }
    //Given an amount to deposit, withdraw, or transfer, ensure its positive and non zero.
    private boolean validAmount(int amount) {
        return amount > 0;
    }

    //Withdraw, return the amount withdrawn
    public int withdraw(int amount) {
        //Ensure positive/ non zero
        if (validAmount(amount)) {
            //Different accounts have different behaviors
            if (accountType == AccountType.CREDIT) {
                //We can withdraw if we dont dip below our credit limit
                if (amount <= balance + creditLimit) {
                    balance -= amount;

                    addTransaction(new Transaction(accountID, "Withdrew $" + (float)amount/100 + " into account " + accountID));

                    AccountDAO dao = new AccountDAO();
                    AccountService service = new AccountService(dao);

                    service.updateAccount(this);

                    return balance;
                }
                else {
                    System.out.println("Too little money, none withdrawn");
                }
            }
            //Checking accounts must remain in their balance
            else {
                if (amount <= balance) {
                    balance -= amount;

                    addTransaction(new Transaction(accountID, "Withdrew $" + (float)amount/100 + " out of account " + accountID));

                    AccountDAO dao = new AccountDAO();
                    AccountService service = new AccountService(dao);

                    service.updateAccount(this);

                    return balance;
                }
                else {
                    System.out.println("Too little money, none withdrawn.");
                }
            }
        }

        return balance;
    }

    //Deposit money, return accounts balance
    public int deposit(int amount) {
        balance += amount;

        addTransaction(new Transaction(accountID, "Deposited $" + (float)amount/100 + " into account " + accountID));

        AccountDAO dao = new AccountDAO();
        AccountService service = new AccountService(dao);

        service.updateAccount(this);

        return balance;
    }

    public int transfer(int amount, BankAccount other) {
        if (balance >= amount) {
            //Complete transfer
            balance -= amount;
            other.deposit(amount);

            //Log it
            addTransaction(new Transaction(accountID, "Transfered " + (float)amount/100 + " from account: " + accountID + " to " + other.getAccountID()));
        }
        else {
            System.out.printf("Balance too low to transfer");
        }

        AccountDAO dao = new AccountDAO();
        AccountService service = new AccountService(dao);

        service.updateAccount(this);
        service.updateAccount(other);
        return balance;
    }

    public String toString() {
        String type = "CREDIT";
        if (accountType == AccountType.CHECKING) type = "CHECKING";
        return "User: " + userID + ", Account number: " + accountID + "\n Balance: $" + (float)balance/100 + " " + "type: " + type;
    }

    public boolean deleteAccount() {
        if (balance != 0) {
            System.out.println("You can only delete an account with $0 balance! \n You currently have: $" + balance);
            return false;
        }

        AccountDAO dao = new AccountDAO();
        AccountService service = new AccountService(dao);

        service.deleteAccount(this);

        return true;
    }

    //Add a transaction
    public void addTransaction(Transaction t) {

        transactionHistory.add(t);
        //Transaction work.

        TransactionDAO dao = new TransactionDAO();
        TransactionService service = new TransactionService(dao);

        service.addTransaction(t);

    }

    //View transaction history
    public void viewTransactionHistory() {
        System.out.println("TRANSACTION HISTORY: ");
        for (int i = 0; i < transactionHistory.size(); i++) {
            System.out.println(i + ". " + transactionHistory.get(i));
        }
    }

    public int getBalance() {
        return balance;
    }

    public int getUserID() { return userID; }

    public boolean isChecking() {
        return accountType == AccountType.CHECKING;
    }

    public int getAccountID() {
        return accountID;
    }

    public Object[] getTransactionIDs() {
        List<Integer> transactionIDs = new ArrayList<>();

        for (int i = 0; i < transactionHistory.size(); i++) {
            transactionIDs.add(transactionHistory.get(i).getTransactionID());
        }

        return (Object[])transactionIDs.toArray();
    }

    public void setID(int id) {
        System.out.println(id);
        accountID = id;
    }

    public void setTransactionList() {

        TransactionDAO transactionDAO = new TransactionDAO();
        TransactionService service = new TransactionService(transactionDAO);

        List<Transaction> accList = service.findTransactionsByAccount(accountID);

        this.transactionHistory = accList;

    }

}