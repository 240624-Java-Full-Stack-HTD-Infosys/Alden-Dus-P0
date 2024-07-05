package org.example.bank;

import java.util.List;

public class TransactionService {
    TransactionDAO dao;
    public TransactionService(TransactionDAO dao) {
        this.dao = dao;
    }
    List<Transaction> findTransactionsByAccount(int id) {
        return dao.findTransactionsByAccount(id);
    }

    void addTransaction(Transaction t) {
        dao.addTransaction(t);
    }

}
