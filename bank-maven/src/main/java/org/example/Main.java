package org.example;
import org.example.bank.ServiceUser;
import org.example.bank.UserDAO;
import org.example.bank.User;
import org.example.bank.BankAccount;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();
        ServiceUser service = new ServiceUser(userDAO);

        loop(service);
    }

    //This is the main loop, calls other methods
    public static void loop(ServiceUser service) {
        User u = null;
        while (true) {
            //Offer signup/login when not signed in
            if (u == null) u = noLogin(service);
                //get commands
            else u = probeCommand(service, u);
        }

    }

    public static User noLogin(ServiceUser service) {
        System.out.printf("Welcome to Alden Bank, you must signup or log in.");
        Scanner scan = new Scanner(System.in);
        System.out.println("Type 'register' (or 1) or 'login' (or 2)");
        String command = scan.nextLine();

        if (command.equalsIgnoreCase("register") || command.equalsIgnoreCase("1")) return register(service);
        if (command.equalsIgnoreCase("login") || command.equalsIgnoreCase("2")) return login(service);

        return null;
    }

    public static User register(ServiceUser service) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter name");
        String name = scan.nextLine();

        System.out.println("Enter Email");
        String email = scan.nextLine();

        System.out.println("Enter Username");
        String username = scan.nextLine();

        System.out.println("Enter Password");
        String password = scan.nextLine();

        return service.createUser(name, email, username, password);
    }

    public static User login(ServiceUser service) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your Username or email");
        String username = scan.nextLine();

        System.out.println("Enter Password");
        String password = scan.nextLine();

        User u = service.login(username, password);

        if (u == null) System.out.printf("Username or password incorrect");

        return u;
    }

    public static User probeCommand(ServiceUser service, User u) {
        System.out.println("Welcome: " + u.getUsername() + " enter either: ");
        System.out.println("'UPDATE PERSONAL INFORMATION' (or 1)");
        System.out.println("'ADD ACCOUNT' (or 2)");
        System.out.println("'VIEW ACCOUNTS' (or 3)");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();

        if (command.equalsIgnoreCase("UPDATE PERSONAL INFORMATION") || command.equalsIgnoreCase("1")) u = updateInfo(service, u);
        if (command.equalsIgnoreCase("ADD ACCOUNT") || command.equalsIgnoreCase("2")) u = addAccount(service, u);
        if (command.equalsIgnoreCase("VIEW ACCOUNTS") || command.equalsIgnoreCase("3")) u = viewAccounts(service, u);

        return u;
    }

    public static User updateInfo(ServiceUser service, User u) {
        System.out.println("What do you want to update? 'name' (or 1), 'email' (or 2), 'username' (or 3), or 'password' (or 4)");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();

        if (command.equalsIgnoreCase("name") || command.equalsIgnoreCase("1")) {
            System.out.println("Enter you new name: ");
            String name = scan.nextLine();

            u.setName(name);
            service.updateUser(u);
        }
        else if (command.equalsIgnoreCase("email") || command.equalsIgnoreCase("2")) {
            System.out.println("Enter you new email: ");
            String email = scan.nextLine();

            u.setEmail(email);
            service.updateUser(u);
        }
        else if (command.equalsIgnoreCase("username") || command.equalsIgnoreCase("3")) {
            System.out.println("Enter you new username: ");
            String username = scan.nextLine();

            u.setUsername(username);
            service.updateUser(u);
        }
        else if (command.equalsIgnoreCase("password") || command.equalsIgnoreCase("4")) {
            System.out.println("Enter you new password: ");
            String password = scan.nextLine();

            u.setPassword(password);
            service.updateUser(u);
        }

        return u;
    }
    public static User addAccount(ServiceUser service, User u) {
        System.out.println("type 'Checking' (or 1) or 'Credit' (or 2)?");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();

        if (command.equalsIgnoreCase("checking") || command.equalsIgnoreCase("1")) {
            u.makeCheckingAccount();
            service.updateUser(u);
        }
        else if (command.equalsIgnoreCase("credit") || command.equalsIgnoreCase("2")) {
            u.makeCreditAccount();
            service.updateUser(u);
        }

        return u;
    }

    public static User viewAccounts(ServiceUser service, User u) {

        List<BankAccount> accounts = u.getAccountList();

        for (int i = 0; i < accounts.size(); i++) {
            System.out.println("--------------------");
            System.out.println("Press to access: " + i);
            System.out.println(accounts.get(i));
            System.out.println("");
        }

        System.out.printf("Enter what account you want to use: ");
        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();

        int acc = Integer.parseInt(command);
        if (acc >= 0 && acc < accounts.size()) accountAction(service, u, acc);

        return u;
    }

    public static User accountAction(ServiceUser service, User u, int acc) {

        System.out.println("Account Action List please type: 'deposit' (or 1), 'withdraw' (or 2), 'transfer' (or 3), 'history' (or 4), or 'delete' (or 5)");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();

        if (command.equalsIgnoreCase("deposit") || command.equalsIgnoreCase("1")) {
            System.out.println("Enter amount: ");
            String param = scan.nextLine();
            float amount = Float.parseFloat(param);

            int bal = u.getAccountList().get(acc).deposit((int)(amount*100));

            System.out.println("new Balance: " + bal);
            service.updateUser(u);
        }
        else if (command.equalsIgnoreCase("withdraw") || command.equalsIgnoreCase("2")) {
            System.out.println("Enter amount: ");
            String param = scan.nextLine();
            float amount = Float.parseFloat(param);

            int bal = u.getAccountList().get(acc).withdraw((int)(amount*100));

            System.out.println("new Balance: " + bal);
            service.updateUser(u);
        }
        else if (command.equalsIgnoreCase("transfer") || command.equalsIgnoreCase("3")) {
            System.out.println("Enter ID of other account: ");
            String param = scan.nextLine();
            int transfer = Integer.parseInt(param);

            System.out.println("Enter amount: ");
            param = scan.nextLine();
            float amount = Float.parseFloat(param);

            u.getAccountList().get(acc).transfer((int)(amount*100), u.getAccountList().get(transfer));
            service.updateUser(u);
        }
        else if (command.equalsIgnoreCase("history") || command.equalsIgnoreCase("4")) {
            u.getAccountList().get(acc).viewTransactionHistory();
            System.out.println("--------------------");
        }
        else if (command.equalsIgnoreCase("delete") || command.equalsIgnoreCase("5")) {
            u.deleteAccount(acc);
        }

        return u;
    }

}