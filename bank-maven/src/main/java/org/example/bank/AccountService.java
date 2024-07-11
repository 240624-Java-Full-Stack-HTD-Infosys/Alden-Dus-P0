package org.example.bank;

import java.util.List;

public class AccountService {
    AccountDAO dao;
    public AccountService(AccountDAO dao) {
        this.dao = dao;
    }
    int createAccount(BankAccount a) {
       return dao.createAccount(a);
    }

    BankAccount findAccount(int id) {
        return dao.findAccount(id);
    }

    void deleteAccount(BankAccount a) {
        dao.deleteAccount(a);
    }

    void updateAccount(BankAccount a) {
        dao.updateAccount(a);
    }

    List<BankAccount> findAccountsByOwner(int id) {
        return dao.findAccountByOwwner(id);
    }
    List<BankAccount> findAllAccounts() {
        return dao.findAllAccounts();
    }
}
