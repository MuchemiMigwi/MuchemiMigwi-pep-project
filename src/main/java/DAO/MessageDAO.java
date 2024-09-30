package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Updated createMessage method to retrieve generated message_id
    public void createMessage(Message message) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // Return generated keys
                                                                                        // (message_id)
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.executeUpdate();

            // Retrieve the generated message_id
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1)); // Set the generated message_id in the Message object
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

    // Method to retrieve all messages
    public List<Message> getAllMessages() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Add each message in the result set to the list
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return messages;
    }

    // Method to retrieve a message by its ID
    public Message getMessageById(int messageId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Message message = null;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            rs = stmt.executeQuery();

            // If a matching message is found, return the message object
            if (rs.next()) {
                message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return message;
    }

    // Method to delete a message by its ID
    public Message deleteMessage(int messageId) throws SQLException {
        Message message = getMessageById(messageId); // Retrieve the message before deleting

        if (message != null) {
            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                conn = ConnectionUtil.getConnection();
                String sql = "DELETE FROM message WHERE message_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, messageId);
                stmt.executeUpdate(); // Delete the message
            } finally {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            }
        }
        return message; // Return the deleted message or null if not found
    }

    // Method to update a message's text by its ID
    public boolean updateMessage(int messageId, String newText) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rowsUpdated;

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newText);
            stmt.setInt(2, messageId);
            rowsUpdated = stmt.executeUpdate(); // Update the message text
        } finally {
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return rowsUpdated > 0; // Return true if the message was updated
    }

    // Method to retrieve all messages by a specific user
    public List<Message> getMessagesByUser(int accountId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();

        try {
            conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            rs = stmt.executeQuery();

            // Add each message to the list
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return messages;
    }
}
