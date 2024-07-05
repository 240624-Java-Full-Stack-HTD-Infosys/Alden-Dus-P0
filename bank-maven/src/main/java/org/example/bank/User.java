package org.example.bank;

import java.util.ArrayList;
import java.util.List;
public class User {

    //Accounts
    List<BankAccount> accountList = new ArrayList<>();

    //Username, password, id, and email
    String name;
    String username;
    String password;
    String email;
    int userID;

    User(String name, String email, String username, String password) {
        //The DB will make this as the primary key... TODO
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User(int id, String name, String email, String username, String password) {
        this.userID = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public void makeCheckingAccount() {
        BankAccount newAccount = new BankAccount(userID, BankAccount.AccountType.CHECKING);

        AccountDAO accountDAO = new AccountDAO();
        AccountService accountService = new AccountService(accountDAO);

        int id = accountService.createAccount(newAccount);
        newAccount.setID(id);

        System.out.println("id: " + newAccount.getAccountID());

        accountList.add(newAccount);
    }

    public void makeCreditAccount() {
        BankAccount newAccount = new BankAccount(userID, BankAccount.AccountType.CREDIT);

        AccountDAO accountDAO = new AccountDAO();
        AccountService accountService = new AccountService(accountDAO);

        int id = accountService.createAccount(newAccount);
        newAccount.setID(id);

        accountList.add(newAccount);
    }

    public BankAccount accessAccount(int i) {
        if (i > 0 && i < accountList.size()) return accountList.get(i);

        return null;
    }

    public List<BankAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList() {

        AccountDAO accountDAO = new AccountDAO();
        AccountService accountService = new AccountService(accountDAO);

        List<BankAccount> accList = accountService.findAccountsByOwner(userID);

        this.accountList = accList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
