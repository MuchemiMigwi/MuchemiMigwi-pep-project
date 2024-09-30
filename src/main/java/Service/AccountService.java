package Service;

import DAO.AccountDAO;
import Model.Account;
import java.sql.SQLException;

public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    public boolean registerAccount(Account account) throws SQLException {
        if (account.getUsername() == null || account.getUsername().isEmpty() || account.getPassword().length() < 4) {
            return false; // Validation failed
        }
        if (accountDAO.findAccountByUsername(account.getUsername()) != null) {
            return false; // Username already exists
        }
        accountDAO.createAccount(account);
        return true;
    }

    public boolean login(String username, String password) throws SQLException {
        return accountDAO.validateLogin(username, password);
    }

    public Account getAccountByUsername(String username) throws SQLException {
        return accountDAO.findAccountByUsername(username);
    }
}
