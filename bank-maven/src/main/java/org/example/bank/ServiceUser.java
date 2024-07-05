package org.example.bank;

import java.util.ArrayList;
import java.util.List;

public class ServiceUser {

    UserDAO dao;
    public ServiceUser(UserDAO dao) {
        this.dao = dao;
    }

    public User login(String info, String password) {
        User u = findUser(info);
        if (u == null) return null;
        if (password.equals(u.getPassword())) return u;

        else return null;
    }

    public User createUser(String name, String email, String username, String password) {
        User newUser = new User(name, email, username, password);
        dao.addUser(newUser);

        return newUser;

    }

    public List<User> findAllUsers() { return dao.findAllUsers();}

    //Find using id
    public User findUser(int id) {
        return dao.findUser(id);
    }
    //Find using email or username
    public User findUser(String info) {
        return dao.findUser(info);
    }

    //Delete user by id
    public void deleteUser(User u) {
        //Delete from DB
        dao.deleteUser(u);
    }

    public void updateUser(User u) {
        //update in DB
        dao.updateUser(u);
    }

    public void dumpDataBase() {
        dao.dump();
    }
}
