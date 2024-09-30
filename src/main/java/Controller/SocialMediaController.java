package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.sql.SQLException;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */

     // Create service objects to handle business logic for accounts and messages
    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();

    // Method to define all API endpoints
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // Register a new user
        app.post("/register", this::registerUserHandler);

        // Login a user
        app.post("/login", this::loginUserHandler);

        // Post a new message
        app.post("/messages", this::createMessageHandler);

        // Get all messages
        app.get("/messages", this::getAllMessagesHandler);

        // Get a message by its ID
        app.get("/messages/{message_id}", this::getMessageByIdHandler); // Updated to use {message_id}

        // Delete a message by its ID
        app.delete("/messages/{message_id}", this::deleteMessageHandler); // Updated to use {message_id}

        // Update a message by its ID
        app.patch("/messages/{message_id}", this::updateMessageHandler); // Updated to use {message_id}

        // Get all messages by a specific user
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler); // Updated to use {account_id}

        return app;
    

    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

     // Handler methods for each user story:

    // Handler for registering a new user
    public void registerUserHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        try {
            if (accountService.registerAccount(account)) {
                ctx.status(200).json(account);
            } else {
                ctx.status(400);
            }
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }

    // Handler for logging in a user
    public void loginUserHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        try {
            if (accountService.login(account.getUsername(), account.getPassword())) {
                ctx.status(200).json(accountService.getAccountByUsername(account.getUsername()));
            } else {
                ctx.status(401);
            }
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }

    // Handler for creating a new message
    public void createMessageHandler(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        try {
            if (messageService.createMessage(message)) {
                ctx.status(200).json(message);
            } else {
                ctx.status(400);
            }
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }

    // Handler to get all messages
    public void getAllMessagesHandler(Context ctx) {
        try {
            ctx.status(200).json(messageService.getAllMessages());
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }

    // Handler to get a message by ID
    public void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        try {
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.status(200).json(message);
            } else {
                ctx.status(200); // Return 200 with empty response if message not found
            }
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }

    // Handler to delete a message by ID
    public void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        try {
            Message message = messageService.deleteMessage(messageId);
            if (message != null) {
                ctx.status(200).json(message); // Return the deleted message
            } else {
                ctx.status(200); // Return 200 with empty response (idempotent delete)
            }
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }

    // Handler to update a message by ID
    public void updateMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newText = ctx.bodyAsClass(Message.class).getMessage_text();
        try {
            if (messageService.updateMessage(messageId, newText)) {
                ctx.status(200).json(messageService.getMessageById(messageId)); // Return updated message
            } else {
                ctx.status(400);
            }
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }

    // Handler to get all messages posted by a specific user
    public void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        try {
            ctx.status(200).json(messageService.getMessagesByUser(accountId));
        } catch (SQLException e) {
            ctx.status(500).result("Error: " + e.getMessage());
        }
    }
}