package org.example.bank;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
public class User {

    //Accounts
    List<BankAccount> accountList = new ArrayList<>();

    //Username, password, id, and email
    String firstname;
    String lastname;
    String username;
    String password;
    String email;
    int userID;

    User(String firstname,String lastname, String email, String username, String password) {
        //The DB will make this as the primary key... TODO
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User(int id, String firstname, String lastname, String email, String username, String password) {
        this.userID = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public User(int userID) {
        this.userID = userID;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User() {

    }

    public BankAccount makeCheckingAccount() {
        BankAccount newAccount = new BankAccount(userID, BankAccount.AccountType.CHECKING);

        AccountDAO accountDAO = new AccountDAO();
        AccountService accountService = new AccountService(accountDAO);

        int id = accountService.createAccount(newAccount);
        newAccount.setID(id);

        System.out.println("id: " + newAccount.getAccountID());

        accountList.add(newAccount);

        return newAccount;
    }

    public BankAccount makeCreditAccount() {
        BankAccount newAccount = new BankAccount(userID, BankAccount.AccountType.CREDIT);

        AccountDAO accountDAO = new AccountDAO();
        AccountService accountService = new AccountService(accountDAO);

        int id = accountService.createAccount(newAccount);
        newAccount.setID(id);

        accountList.add(newAccount);

        return newAccount;
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

    public String getFirstName() {
        return firstname;
    }

    @JsonProperty("firstName")
    public void setFirstName(String name) {
        this.firstname = name;
    }

    public String getLastName() {
        return lastname;
    }

    @JsonProperty("lastName")
    public void setLastName(String name) {
        this.lastname = name;
    }

    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void deleteAccount(int acc) {
        accountList.get(acc).deleteAccount();
        accountList.remove(acc);
    }
}
