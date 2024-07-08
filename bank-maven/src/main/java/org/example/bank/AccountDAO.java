package org.example.bank;

import java.util.ArrayList;
import java.util.List;

import org.example.util.ConnectionUtil;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

public class AccountDAO {
    int createAccount(BankAccount A) {
        boolean ischecking = A.isChecking();
        try {
            Connection connection = ConnectionUtil.getConnection();

            //Write SQL logic here
            String sql = "INSERT INTO accounts (account_owner, balance, ischecking) VALUES(?,0,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1,A.getUserID());
            preparedStatement.setBoolean(2,ischecking);

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()) {
                return (int)pkeyResultSet.getLong(1);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return -1;
    }
    BankAccount findAccount(int id) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            //Write SQL logic here
            String sql = "SELECT * FROM accounts WHERE account_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                BankAccount.AccountType t = BankAccount.AccountType.CREDIT;
                if (rs.getBoolean("ischecking")) t = BankAccount.AccountType.CHECKING;
                BankAccount a = new BankAccount(rs.getInt("account_id"),rs.getInt("account_owner"),rs.getInt("balance"), t);
                //TODO: Add Transactions
                a.setTransactionList();

                return a;
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    List<BankAccount> findAccountByOwwner(int id) {
        List<BankAccount> accounts = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();

            //Write SQL logic here
            String sql = "SELECT * FROM accounts WHERE account_owner=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                BankAccount.AccountType t = BankAccount.AccountType.CREDIT;
                if (rs.getBoolean("ischecking")) t = BankAccount.AccountType.CHECKING;
                BankAccount a = new BankAccount(rs.getInt("account_id"),rs.getInt("account_owner"),rs.getInt("balance"), t);
                //TODO: Add Transactions
                a.setTransactionList();

                accounts.add(a);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return accounts;
    }

    void deleteAccount(BankAccount a) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            //Write SQL logic here
            String sql = "DELETE FROM accounts WHERE account_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,a.getAccountID());
            preparedStatement.executeUpdate();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void updateAccount(BankAccount a) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            //Write SQL logic here (account_owner, ischecking) VALUES(?,?)
            String sql = "UPDATE accounts SET balance=? WHERE account_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,a.getBalance());
            preparedStatement.setInt(2,a.getAccountID());

            preparedStatement.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
