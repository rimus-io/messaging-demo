package com.rimusdesign.messaging.client;


import com.rimusdesign.messaging.client.connection.ClientHandshakeHandler;
import com.rimusdesign.messaging.client.topic.ClientTopicFactory;
import com.rimusdesign.messaging.client.topic.Topic;
import com.rimusdesign.messaging.core.connection.HandshakeHandler;
import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;


/**
 * @author Rimas Krivickas.
 */
public class ClientConnection {


    protected Socket socket;

    protected int connectionType;
    private String connectionId;

    protected String hostName;
    protected int port;

    protected boolean isConfigured;
    protected boolean isConnected;

    protected HandshakeHandler handshakeHandler;

    protected SocketConnectionDetails socketConnectionDetails;


    public ClientConnection (int connectionType, String connectionId) {

        // Use default connection details
        this(connectionType, connectionId, "localhost", 7777);
    }


    public ClientConnection (int connectionType, String connectionId, String hostName, int port) {

        this.connectionType = connectionType;
        this.connectionId = connectionId;
        this.hostName = hostName;
        this.port = port;
    }


    public void configure () {

        // Instantiate handshake handler
        handshakeHandler = new ClientHandshakeHandler(connectionType, connectionId);

        // Mark as configured
        isConfigured = true;
    }


    public boolean connect () throws IOException, IllegalStateException {

        // Do not connect if not configured
        if (!isConfigured) {
            throw new IllegalStateException("Connection needs to be configured using 'configure' method before it can connect.");
        }

        // TODO prevent from triggering while connecting
        if (isConnected) return true; // TODO Handle this in some way smarter

        try {
            // Establish connection with server
            socket = new Socket(hostName, port);
        } catch (ConnectException e) {
            // Return if server is not started or refusing connections
            // TODO Add some mechanism to notify application using the connection of this
            System.out.println("Server is down or refusing connections");
            return false;
        }

        // Get I/O streams
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        // Process handshake
        if (handshakeHandler.handshakeSuccessful(inputStream, outputStream)) {

            // If handshake is successful make connection details
            socketConnectionDetails = new SocketConnectionDetails(socket, handshakeHandler.getConnectionDetails());

            // Mark as connected
            isConnected = true;

            return true;
        }

        return false;
    }


    public void disconnect () throws IOException {

        System.out.println("Disconnecting..");
        if (isConnected) {

            socket.shutdownOutput();
            socket.shutdownInput();

            socket.close();

            // Flag as disconnected
            isConnected = false;
            System.out.println("Disconnected");
        }
    }


    public Topic createTopic (String topicName) throws IllegalStateException {

        // Do not connect if not configured
        if (!isConnected) {
            throw new IllegalStateException("Connection needs to be established using 'connect' method before a topic can be created.");
        }

        return ClientTopicFactory.make(socketConnectionDetails, topicName);
    }


}
