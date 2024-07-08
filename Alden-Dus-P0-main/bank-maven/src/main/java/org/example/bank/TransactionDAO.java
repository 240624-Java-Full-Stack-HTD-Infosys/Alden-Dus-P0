package org.example.bank;

import org.example.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    //Find all by account ID
    List<Transaction> findTransactionsByAccount(int id) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "SELECT * FROM transactions WHERE account=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Transaction t = new Transaction(rs.getInt("transaction_id"),rs.getInt("account"), rs.getString("history"));
                transactions.add(t);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return transactions;
    }

    //Create Transaction
    void addTransaction(Transaction t) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "INSERT INTO transactions (account,history) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,t.getAccountID());
            preparedStatement.setString(2,t.toString());

            preparedStatement.executeUpdate();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
