package DAO;

// Import the Account model class
import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    // Method to create a new account
    public void createAccount(Account account) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // Return generated account_id
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.executeUpdate();

            // Retrieve the generated account_id
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                account.setAccount_id(rs.getInt(1)); // Set the generated account_id in the Account object
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Method to find an account by its username
    public Account findAccountByUsername(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Create an Account object using the constructor with all fields
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return null; // Return null if no account is found
    }

    // Method to find an account by its ID (account_id)
    public Account findAccountById(int accountId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId); // Use account_id (int)
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Retrieve data from the ResultSet and map to an Account object
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return null; // Return null if no account is found
    }

    // Method to validate login credentials
    public boolean validateLogin(String username, String password) throws SQLException {
        Account account = findAccountByUsername(username);
        return account != null && account.getPassword().equals(password); // Validate password
    }
}
