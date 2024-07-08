package org.example.bank;

import java.util.ArrayList;
import java.util.List;

import org.example.util.ConnectionUtil;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

public class UserDAO {
    void addUser(User u) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "INSERT INTO users (firstname, lastname, username, email, pass) VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,u.getFirstName());
            preparedStatement.setString(2,u.getLastName());
            preparedStatement.setString(3,u.getUsername());
            preparedStatement.setString(4,u.getEmail());
            preparedStatement.setString(5,u.getPassword());

            preparedStatement.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    User findUser(int id) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "SELECT * FROM users WHERE user_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                User u = new User(rs.getInt("user_id"),rs.getString("firstname"),rs.getString("lastname"),rs.getString("email"), rs.getString("username"), rs.getString("pass"));
                //set accounts
                u.setAccountList();

                return u;
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    User findUser(String info) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,info);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                User u = new User(rs.getInt("user_id"),rs.getString("firstname"),rs.getString("lastname"),rs.getString("email"), rs.getString("username"), rs.getString("pass"));
                //set accounts
                u.setAccountList();

                return u;
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "SELECT * FROM users WHERE email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,info);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                User u = new User(rs.getInt("user_id"),rs.getString("firstname"),rs.getString("lastname"),rs.getString("email"), rs.getString("username"), rs.getString("pass"));
                //set accounts
                u.setAccountList();

                return u;
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    void deleteUser(User u) {
        try  {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "DELETE FROM users WHERE user_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,u.getUserID());
            preparedStatement.executeUpdate();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void updateUser(User u) {
        try  {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "UPDATE users SET firstname=?,lastname=?, username=?, email=?, pass=? WHERE user_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,u.getFirstName());
            preparedStatement.setString(2,u.getLastName());
            preparedStatement.setString(3,u.getUsername());
            preparedStatement.setString(4,u.getEmail());
            preparedStatement.setString(5,u.getPassword());
            preparedStatement.setInt(6,u.getUserID());

            preparedStatement.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void dump() {
        try  {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "TRUNCATE users";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<User> findAllUsers() {

        List<User> userList = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();
            //Write SQL logic here
            String sql = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                User u = new User(rs.getInt("user_id"),rs.getString("firstname"),rs.getString("lastname"),rs.getString("email"), rs.getString("username"), rs.getString("pass"));
                //set accounts
                u.setAccountList();

                userList.add(u);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return userList;
    }

}
