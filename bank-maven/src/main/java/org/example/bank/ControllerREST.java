package org.example.bank;

import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;


public class ControllerREST {

    public void startAPI() {
        Javalin app = Javalin.create().start();

        app.post("register", ctx -> {
            String jsonString=ctx.body();
            ObjectMapper objm=new ObjectMapper();
            User user = objm.readValue(jsonString,User.class);

            if (user.getPassword().length() >= 4 && user.getUsername().length() > 0) {
                UserDAO dao = new UserDAO();
                ServiceUser service = new ServiceUser(dao);
                if (service.findUser(user.getUsername()) == null) {
                    User result = service.createUser(user.getFirstName(),user.getLastName(),user.getEmail(), user.getEmail(), user.getPassword());
                    ctx.status(200);
                    ctx.json(result);
                }
                else ctx.status(400);
            } else ctx.status(400);
        });

        app.post("login", ctx -> {
            String jsonString=ctx.body();
            ObjectMapper objm=new ObjectMapper();
            User user = objm.readValue(jsonString,User.class);

            UserDAO dao = new UserDAO();
            ServiceUser service = new ServiceUser(dao);

            User toCheck = service.findUser(user.getUsername());
            if (toCheck != null && toCheck.getPassword().equals(user.getPassword())) {
                ctx.status(200);
                ctx.json(toCheck);
            }
            else ctx.status(401);
        });

        app.post("checking-account", ctx -> {
            String jsonString=ctx.body();
            ObjectMapper objm=new ObjectMapper();
            User user = objm.readValue(jsonString,User.class);

            AccountDAO dao = new AccountDAO();
            AccountService service = new AccountService(dao);

            BankAccount a = new BankAccount(user.userID, BankAccount.AccountType.CHECKING);
            int id = service.createAccount(a);

            a.setID(id);

            ctx.status(200);
            ctx.json(a);

        });

        app.post("credit-account", ctx -> {
            String jsonString=ctx.body();
            ObjectMapper objm=new ObjectMapper();
            User user = objm.readValue(jsonString,User.class);

            AccountDAO dao = new AccountDAO();
            AccountService service = new AccountService(dao);

            BankAccount a = new BankAccount(user.userID, BankAccount.AccountType.CREDIT);
            int id = service.createAccount(a);

            a.setID(id);

            ctx.status(200);
            ctx.json(a);

        });

        app.post("/account/{accountID}/deposit/{amount}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("accountID"));
            int amount = (int)(Float.parseFloat(ctx.pathParam("amount")) * 100);

            AccountDAO dao = new AccountDAO();
            AccountService service = new AccountService(dao);

            BankAccount a = service.findAccount(id);

            a.deposit(amount);

            ctx.status(200);

        });

        app.post("/account/{accountID}/withdraw/{amount}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("accountID"));
            int amount = (int)(Float.parseFloat(ctx.pathParam("amount")) * 100);

            AccountDAO dao = new AccountDAO();
            AccountService service = new AccountService(dao);

            BankAccount a = service.findAccount(id);
            a.withdraw(amount);

            ctx.status(200);

        });

        app.post("/account/{accountID}/transfer/{amount}/{transferToID}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("accountID"));
            int amount = (int)(Float.parseFloat(ctx.pathParam("amount")) * 100);
            int transferToID = Integer.parseInt(ctx.pathParam("transferToID"));

            AccountDAO dao = new AccountDAO();
            AccountService service = new AccountService(dao);

            BankAccount a = service.findAccount(id);
            BankAccount other = service.findAccount(transferToID);
            a.transfer(amount, other);

            ctx.status(200);

        });

        app.get("/account/{accountID}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("accountID"));

            AccountDAO dao = new AccountDAO();
            AccountService service = new AccountService(dao);

            BankAccount a = service.findAccount(id);

            ctx.json(a);

        });

        app.get("/account/{accountID}/history", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("accountID"));

            TransactionDAO dao = new TransactionDAO();
            TransactionService service = new TransactionService(dao);

            List<Transaction> list = service.findTransactionsByAccount(id);

            List<String> toReturn = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                toReturn.add(list.get(i).toString());
            }

            ctx.status(200);
            ctx.json(toReturn);

        });

        app.delete("/account/{accountID}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("accountID"));

            AccountDAO dao = new AccountDAO();
            AccountService service = new AccountService(dao);

            BankAccount a = service.findAccount(id);

            if (a.deleteAccount()) {
                service.deleteAccount(a);
                ctx.status(200);
            }
            else ctx.status(400);

        });

    }

}
