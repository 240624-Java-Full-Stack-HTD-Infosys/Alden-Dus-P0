import org.example.bank.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDriver {
    UserDAO testDAO = new UserDAO();
    ServiceUser service = new ServiceUser(testDAO);

    @BeforeEach
    public void resetState() {
        service.dumpDataBase();
    }

    @Test
    public void test_createUsers() {
        //Arrange
        service.createUser("Test","User1",  "test@revature.com", "I-Test", "Password");
        service.createUser("Test","User2",  "test2@revature.com", "I-Test2", "Password");
        int expected_users = 2;

        //Activate
        int actual_users = service.findAllUsers().size();

        //Assert
        assertEquals(expected_users, actual_users);

    }

    @Test
    public void test_findUsers() {
        //Arrange
        service.createUser("Test","User1",  "test@revature.com", "I-Test", "Password");
        service.createUser("Test","User2",  "test2@revature.com", "I-Test2", "Password");
        String expected_username = "I-Test";
        String expected_password = "Password";

        //Activate
        User u = service.findUser("I-Test");
        String actual_username = u.getUsername();
        String actual_password = u.getPassword();

        //Assert
        assertEquals(expected_username, actual_username);
        assertEquals(expected_password, actual_password);

    }

    @Test
    public void test_createAccounts() {
        //Arrange
        service.createUser("Test","User1", "test@revature.com", "I-Test", "Password");
        int expected_accounts = 3;

        //Activate
        User u = service.findUser("I-Test");
        u.makeCheckingAccount();
        u.makeCheckingAccount();
        u.makeCreditAccount();
        service.updateUser(u);


        User find = service.findUser("I-Test");
        int actual_accounts = find.getAccountList().size();

        //Assert
        assertEquals(expected_accounts, actual_accounts);

    }

    @Test
    public void test_DepositAndWithdraw() {
        //Arrange
        service.createUser("Test","User1", "test@revature.com", "I-Test", "Password");
        int expected_balance = 111;

        //Activate
        User u = service.findUser("I-Test");
        u.makeCheckingAccount();

        float deposit = 5.67f;
        float withdraw = 4.56f;

        //Deposit once $5.67
        u.getAccountList().get(0).deposit((int)(deposit*100));
        //Withdraw $4.56 ($1.11 remains)
        u.getAccountList().get(0).withdraw((int)(withdraw*100));
        //Try to withdraw $4.56 ($1.11 remains still because overdraw)
        u.getAccountList().get(0).withdraw((int)(withdraw*100));

        assertEquals(expected_balance, u.getAccountList().get(0).getBalance());
    }

    @Test
    public void test_transfers() {
        //Arrange
        service.createUser("Test","User1" , "test@revature.com", "I-Test", "Password");
        int expected_balance_acc0 = 544;
        int expected_balance_acc1 = 456;

        //Activate
        User u = service.findUser("I-Test");
        u.makeCheckingAccount();
        u.makeCheckingAccount();
        float deposit = 10f;
        float transfer_amount = 4.56f;

        //Deposit $10 into account 1
        u.getAccountList().get(0).deposit((int)(deposit*100));

        //Transfer $4.56 from account 0 to account 1
        u.getAccountList().get(0).transfer((int)(transfer_amount*100), u.getAccountList().get(1));

        assertEquals(expected_balance_acc0, u.getAccountList().get(0).getBalance());
        assertEquals(expected_balance_acc1, u.getAccountList().get(1).getBalance());

    }
}
