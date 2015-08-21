package com.rimusdesign.messaging.server.connection;


import com.rimusdesign.messaging.core.connection.HandshakeHandler;
import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Responsible for establishing connections as described by protocol.
 * Attempts to read connection message, if connection message is decoded successfully - a connection thread is created.
 * Based on the outcome - 'success' or 'fail' response is sent to the client.
 *
 * @author Rimas Krivickas.
 */
public class ConnectionHandler implements Runnable {


    private Socket socket;
    private LinkedBlockingQueue controlQueue;
    protected HandshakeHandler handshakeHandler;


    public ConnectionHandler (Socket socket, LinkedBlockingQueue controlQueue) {

        this.socket = socket;
        this.controlQueue = controlQueue;
    }


    protected void handleConnection (DataInputStream inputStream, DataOutputStream outputStream) {

        handshakeHandler = new ServerHandshakeHandler();

        if (handshakeHandler.handshakeSuccessful(inputStream, outputStream)) {

            // Start connection thread
            new Thread(new ConnectionThread(
                    new SocketConnectionDetails(socket, handshakeHandler.getConnectionDetails()),
                    new ConnectionContextFactory(),
                    controlQueue)).start();
        } else {

            // TODO Gracefully disconnect client
        }
    }


    public void run () {

        try {

            // Get I/O streams
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // Handle connection
            handleConnection(inputStream, outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
