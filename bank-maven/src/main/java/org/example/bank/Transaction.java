package org.example.bank;

public class Transaction {

    String history;
    int transactionID;
    int account_id;

    public Transaction(int account_id, String history) {
        this.account_id = account_id;
        this.history = history;
    }
    public Transaction(int id, int account_id, String history) {
        this.transactionID = id;
        this.account_id = account_id;
        this.history = history;
    }

    public String toString() {
        return history;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getAccountID() {
        return account_id;
    }

    public void setAccountID(int accountID) {
        this.account_id = accountID;
    }
}
