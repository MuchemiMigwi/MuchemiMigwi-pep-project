package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import java.sql.SQLException;
import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO = new MessageDAO();
    private final AccountDAO accountDAO = new AccountDAO(); // Add AccountDAO reference

    // Method to create a new message
    public boolean createMessage(Message message) throws SQLException {
        // Validate the message text length and content
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()
                || message.getMessage_text().length() > 255) {
            return false; // Validation failed
        }
        // Check if the user exists using AccountDAO
        if (accountDAO.findAccountById(message.getPosted_by()) == null) {
            return false; // User does not exist
        }
        // If validation passes, create the message
        messageDAO.createMessage(message);
        return true;
    }

    // Method to get all messages
    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    // Method to retrieve a message by its ID
    public Message getMessageById(int messageId) throws SQLException {
        return messageDAO.getMessageById(messageId);
    }

    // Method to delete a message by its ID
    public Message deleteMessage(int messageId) throws SQLException {
        return messageDAO.deleteMessage(messageId);
    }

    // Method to update a message's text
    public boolean updateMessage(int messageId, String newText) throws SQLException {
        // Validate that the new message text is not empty and does not exceed 255 characters
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            return false; // Validation failed, return false
        }
        return messageDAO.updateMessage(messageId, newText); // Update the message in the database
    }

    // Method to retrieve all messages by a specific user
    public List<Message> getMessagesByUser(int accountId) throws SQLException {
        return messageDAO.getMessagesByUser(accountId);
    }
}
