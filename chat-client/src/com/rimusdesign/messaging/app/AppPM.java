package com.rimusdesign.messaging.app;


import com.rimusdesign.messaging.app.controller.ChatViewController;
import com.rimusdesign.messaging.app.controller.LoginViewController;
import com.rimusdesign.messaging.client.ClientConnection;
import com.rimusdesign.messaging.client.topic.MessageConsumer;
import com.rimusdesign.messaging.client.topic.MessageProducer;
import com.rimusdesign.messaging.core.codecs.MessageFrameCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.sun.deploy.util.StringUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;


/**
 * @author Rimas Krivickas.
 */
public class AppPM implements MessageConsumer {


    public static final int LOGIN_VIEW = 0;
    public static final int CHAT_VIEW = 1;

    public static final int MAX_MESSAGES_DISPLAYED = 200;
    public static String NEW_LINE = System.getProperty("line.separator");

    private LoginViewController loginViewController;
    private ChatViewController chatViewController;
    private Scene loginScene;
    private Scene chatScene;
    private Stage primaryStage;

    private ClientConnection pubConnection;
    private ClientConnection subConnection;
    private MessageProducer messageProducer;

    public StringProperty username = new SimpleStringProperty("");
    public StringProperty chatGroup = new SimpleStringProperty("");
    public StringProperty messages = new SimpleStringProperty("");
    private String tempMessages = "";

    private int messageCount;

    private boolean isConnecting;
    private boolean isConnected;


    public AppPM (Stage primaryStage) {

        this.primaryStage = primaryStage;
    }


    public void onCreationComplete () throws Exception {

        // Set app title
        primaryStage.setTitle("SimpleChat");

        // Instantiate login view
        loginScene = instLoginView();

        // Instantiate chat view
        chatScene = instChatView();

        // Set initial view
        showView(LOGIN_VIEW);

        // Display view
        primaryStage.show();

    }


    public void exit () {

        //System.out.println("System.EXIT");
        disconnect();
    }


    // TODO Add timeout
    public void login (String username, String chatGroup) {

        if (!isConnecting && !isConnected) {

            // Mark as connecting
            isConnecting = true;

            // Reset message count
            messageCount = 0;

            // Connect sockets
            if (establishConnections(username)) {

                try {

                    // If connections succeed set topics
                    setTopics(chatGroup);
                } catch (UnsupportedOperationException e) {

                    // If setting topics failed set error message in the view and return
                    // TODO Set error message in the view

                    // Unset 'isConnecting' flag so another attempt could be made
                    isConnecting = false;
                    return;
                } catch (IOException e) {
                    e.printStackTrace();

                    // Unset 'isConnecting' flag so another attempt could be made
                    isConnecting = false;
                    return;
                }

                // Login successful, change view
                showView(CHAT_VIEW);

                // Set flags
                isConnecting = false;
                isConnected = true;
            } else {
                // Un-mark as connecting so login could be retried
                isConnecting = false;
            }
        }
    }


    private boolean establishConnections (String username) {

        try {

            // Instantiate pubConnection
            pubConnection = new ClientConnection(ConnectionType.PUB, username);
            pubConnection.configure();

            // Connect. If connecting fails return
            if (!pubConnection.connect()) return false;

            // Instantiate subConnection
            subConnection = new ClientConnection(ConnectionType.SUB, username);
            subConnection.configure();

            // Connect. If connecting fails return
            if (!subConnection.connect()) return false;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    private void setTopics (String topicName) throws UnsupportedOperationException, IOException {

        // Set topic topic and instantiate message producer
        messageProducer = pubConnection.createTopic(topicName).getMessageProducer();

        // Subscribe to topic and set massage consumer
        subConnection.createTopic(topicName).setMessageConsumer(this);
    }


    public void sendMessage (String text) {

        // Do not allow sending messages when not connected
        if (!isConnected) return;

        try {
            messageProducer.sendTextMessage(username.getValue() + ": " + text);
        } catch (IOException e) {
            // TODO Add some logging here?
            disconnect();
        }
    }


    public void disconnect () {

        // Do not proceed if not connected
        if (!isConnected) return;

        try {
            pubConnection.disconnect();
            subConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.chatGroup.setValue("");
        this.username.setValue(null);

        //System.out.println("disconnect...");
        showView(LOGIN_VIEW);

        // Mark as disconnected
        isConnected = false;
    }


    public void onMessage (ByteBuffer message) {

        updateMessagesList(MessageFrameCodec.getTextBody(message));
    }


    public void onError (String message) {

        messages.setValue(message
                + NEW_LINE + NEW_LINE
                + "Please disconnect and try again later...");
    }


    private void updateMessagesList (String message) {

        List tempMessagesList;


        if (messageCount > MAX_MESSAGES_DISPLAYED) {
            // remove line at the top and ad one at the bottom
            tempMessagesList = Arrays.asList(tempMessages.split(NEW_LINE)).subList(0, MAX_MESSAGES_DISPLAYED - 1);

            tempMessages = message + NEW_LINE + StringUtils.join(tempMessagesList, NEW_LINE);
        } else {
            // Increment message count
            tempMessages = message + NEW_LINE + messages.getValue();
            messageCount++;
        }


        messages.setValue(tempMessages);
    }


    private void showView (int view) {

        switch (view) {
            case LOGIN_VIEW:
                doShowView(loginScene);
                break;
            case CHAT_VIEW:
                doShowView(chatScene);
                break;
        }
    }


    private void doShowView (Scene scene) {

        primaryStage.setScene(scene);
        scene.getRoot().requestFocus(); // Removes focus from control components
    }


    private Scene instLoginView () throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Parent root = fxmlLoader.load();
        loginViewController = fxmlLoader.getController();
        loginViewController.setPresentationModel(this);

        return new Scene(root);
    }


    private Scene instChatView () throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
        Parent root = fxmlLoader.load();
        chatViewController = fxmlLoader.getController();
        chatViewController.setPresentationModel(this);

        return new Scene(root);
    }


}
